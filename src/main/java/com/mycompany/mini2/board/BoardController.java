package com.mycompany.mini2.board;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.mini2.member.Member;
import com.mycompany.mini2.member.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	// 전체 글 목록 가져오기 + 페이징(RequestParam으로 page, size 값 받아오기)
	@GetMapping
	public ResponseEntity<?> listPageBoards(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		// page와 size 유효성 검증
		if (page < 0) {
			page = 0;
		}
		if (size < 1 || size > 100) {
			size = 10;
		}
		// Pageable 이용해서 page, size, sort 정한 후 Page<Board> 이용해서 글 가져오기
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<Board> boardPage = boardRepository.findAll(pageable);
		// url로 들어온 page의 값 유효성 체크 **
		if (page >= boardPage.getTotalPages()) {
			page = 0;
		}
		Map<String, Object> pagingResponse = new HashMap<>();
		pagingResponse.put("totalItems", boardPage.getTotalElements()); // 총 글 수
		pagingResponse.put("totalPages", boardPage.getTotalPages()); // 총 페이지 수
		pagingResponse.put("currentPage", boardPage.getNumber()); // 현재 페이지 번호
		pagingResponse.put("posts", boardPage.getContent()); // 페이징된 현재 페이지에 해당하는 게시글 리스트
		return ResponseEntity.ok(pagingResponse);
		
	}
	
	// 글 작성
	@PostMapping("/new")
	public ResponseEntity<?> createBoard(@Valid @RequestBody BoardDto boardDto,
			BindingResult result, Authentication auth) {
		// 글 작성 권한 확인
		if (auth == null) {
			return ResponseEntity.badRequest().body("로그인 후 글쓰기 가능합니다.");
		} 
		// 유효성 검사
		if (result.hasErrors()) {
			Map<String, Object> errors = new HashMap<>();
			result.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.badRequest().body(errors);
		}
		// 로그인 한 유저의 정보 가져오기
		Member member =  memberRepository.findByUsername(auth.getName()).orElseThrow(
				() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));
		// 문제 없을 시 엔티티 선언 후 값 넣어주기
		Board board = new Board();
		board.setWriter(member);
		board.setTitle(boardDto.getTitle());
		board.setContent(boardDto.getContent());
		boardRepository.save(board);
		return ResponseEntity.ok(board);
	}
	
	// id로 특정 게시글 가져오기
	@GetMapping("/{id}")
	public ResponseEntity<?> getBoardById(@PathVariable("id") Long id) {
		Board board = boardRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("해당 글을 찾을 수 없음"));
		return ResponseEntity.ok(board);
	}
	
	// id로 특정 게시글 수정하기 (해당 글 존재 여부 -> 수정 권한 -> 수정 완료)
	@PutMapping("/{id}")
	public ResponseEntity<?> updateBoard(@PathVariable("id") Long id, 
			@RequestBody BoardDto boardDto, Authentication auth) {
		Board board = boardRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("해당 글을 찾을 수 없음"));
		if (auth == null || !board.getWriter().getUsername().equals(auth.getName())) {
			return ResponseEntity.status(403).body("해당 글의 수정 권한이 없습니다.");
		}
		board.setTitle(boardDto.getTitle());
		board.setContent(boardDto.getContent());
		boardRepository.save(board);		
		return ResponseEntity.ok(board);
	}
	
	// id로 특정 게시글 삭제하기 (해당 글 존재 여부 -> 삭제 권한 -> 삭제 완료)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteBoardById(@PathVariable("id") Long id, Authentication auth) {
		Board board = boardRepository.findById(id).orElseThrow(
				() -> new EntityNotFoundException("해당 글을 찾을 수 없음"));
		if (auth == null || !board.getWriter().getUsername().equals(auth.getName())) {
			return ResponseEntity.status(403).body("해당 글의 삭제 권한이 없습니다.");
		}
		boardRepository.deleteById(id);
		return ResponseEntity.ok("글 삭제 성공");
		
	}

}
