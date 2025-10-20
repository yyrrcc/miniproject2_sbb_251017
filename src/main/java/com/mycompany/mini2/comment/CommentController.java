package com.mycompany.mini2.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.mini2.board.Board;
import com.mycompany.mini2.board.BoardRepository;
import com.mycompany.mini2.member.Member;
import com.mycompany.mini2.member.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	// 댓글 작성 (해당 게시글 존재 여부 -> 로그인 및 권한 여부 -> 댓글 유효성 검증 -> 저장
	@PostMapping("/{boardId}")
	public ResponseEntity<?> createComment(@PathVariable("boardId") Long boardId,
			@Valid @RequestBody CommentDto commentDto,
			BindingResult result,
			Authentication auth) {
		Board board = boardRepository.findById(boardId).orElseThrow(
				() -> new EntityNotFoundException("해당 글을 찾을 수 없음"));
		if (auth == null) {
			return ResponseEntity.status(403).body("댓글 등록 권한이 없습니다.");
		}
		Member member =  memberRepository.findByUsername(auth.getName()).orElseThrow(
				() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));
		if (result.hasErrors()) {
			Map<String, Object> errors = new HashMap<>();
			result.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.badRequest().body(errors);
		}
		Comment comment = new Comment();
		comment.setContent(commentDto.getContent());
		comment.setBoard(board);
		comment.setWriter(member);
		commentRepository.save(comment);
		return ResponseEntity.ok(comment);
	}
	
	// 1개의 게시글에 존재하는 모든 댓글 불러오기 (게시글 존재 여부 후 가져오기)
	@GetMapping("/{boardId}")
	public ResponseEntity<?> getComments(@PathVariable("boardId") Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(
				() -> new EntityNotFoundException("해당 글을 찾을 수 없음"));
		List<Comment> comments = commentRepository.findByBoard(board);
		return ResponseEntity.ok(comments);
	}
	
	// 댓글 수정 (해당 댓글 존재 여부 -> 수정 권한 -> 수정 완료)
	@PutMapping("/{commentId}")
	public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId, 
			@Valid @RequestBody CommentDto commentDto,
			BindingResult result,
			Authentication auth) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new EntityNotFoundException("해당 댓글을 찾을 수 없음"));
		if (auth == null || !comment.getWriter().getUsername().equals(auth.getName())) {
			return ResponseEntity.status(403).body("댓글 삭제 권한이 없습니다.");
		}
		if (result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			result.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());					
				}
			);
			return ResponseEntity.badRequest().body(errors);
		}
		comment.setContent(commentDto.getContent());
		commentRepository.save(comment);
		return ResponseEntity.ok(comment);
	}

	
	// 댓글 삭제 (해당 댓글 존재 여부 -> 삭제 권한 -> 삭제 완료)
	@DeleteMapping("/{commentId}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId, Authentication auth) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(
				() -> new EntityNotFoundException("해당 댓글을 찾을 수 없음"));
		if (auth == null || !comment.getWriter().getUsername().equals(auth.getName())) {
			return ResponseEntity.status(403).body("댓글 삭제 권한이 없습니다.");
		}
		commentRepository.deleteById(commentId);
		return ResponseEntity.ok("댓글 삭제 완료");
	}

}
