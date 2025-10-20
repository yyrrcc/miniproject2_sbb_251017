package com.mycompany.mini2.movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.mini2.member.Member;
import com.mycompany.mini2.member.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private MovieCommentRepository movieCommentRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	// 한줄평 등록 (영화 존재 여부 -> 권한 확인 -> 유효성 검증 -> 저장)
    @PostMapping("/{movieId}/comments")
    public ResponseEntity<?> addComment(@PathVariable("movieId") Long movieId,
    		@Valid @RequestBody MovieCommentDto movieCommentDto,
    		BindingResult result,  Authentication auth) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(
        		() -> new EntityNotFoundException("해당 영화를 찾을 수 없음"));
        if (auth == null) {
			return ResponseEntity.status(403).body("한줄평 등록 권한이 없습니다.");
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
        MovieComment movieComment = new MovieComment();
        movieComment.setContent(movieCommentDto.getContent());
        movieComment.setMovie(movie);
        movieComment.setWriter(member);
        movieCommentRepository.save(movieComment);
        return ResponseEntity.ok(movieComment);
    }

    // 모든 한줄평 불러오기
    @GetMapping("/comments")
    public ResponseEntity<?> getMovieComments() {
        List<MovieComment> movieComments = movieCommentRepository.findAll();
        return ResponseEntity.ok(movieComments);
    }
    
    // 1개 영화에 모든 한줄평 목록
//    @GetMapping("/{movieId}/comments")
//    public ResponseEntity<?> getMovieComments(@PathVariable("movieId") Long movieId) {
//    	Movie movie = movieRepository.findById(movieId).orElseThrow(
//         		() -> new EntityNotFoundException("해당 영화를 찾을 수 없음"));
//        List<MovieComment> movieComments = movieCommentRepository.findByMovie(movie);
//        return ResponseEntity.ok(movieComments);
//    }

    // 찜(좋아요)
    @PostMapping("/{movieId}/like")
    public ResponseEntity<?> likeMovie(@PathVariable("movieId") Long movieId) {
    	Movie movie = movieRepository.findById(movieId).orElseThrow(
         		() -> new EntityNotFoundException("해당 영화를 찾을 수 없음"));
        movie.setLikeCount(movie.getLikeCount() + 1);
        movieRepository.save(movie);
        return ResponseEntity.ok(null);
    }

}
