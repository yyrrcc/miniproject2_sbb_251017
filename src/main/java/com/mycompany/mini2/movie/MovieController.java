package com.mycompany.mini2.movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private MovieReviewRepository movieReviewRepository;
	@Autowired
	private MemberRepository memberRepository;
	
	// 영화 정보 가져오기
	@GetMapping
	public ResponseEntity<?> listMovies() {
		List<Movie> movies = movieRepository.findAll();
		return ResponseEntity.ok(movies);
	}
	
	// 모든 한줄평 불러오기 (새로운 방식 - 이미 생성자를 만들어 놓고 불러오기만 한 것)
    @GetMapping("/reviews")
    public ResponseEntity<?> getMovieReviews() {
        List<MovieReviewResponse> movieReviews = movieReviewRepository.findAll().stream()
        		.map(MovieReviewResponse::new)
        		.collect(Collectors.toList()); // movieReview 모두 불러온 값
        return ResponseEntity.ok(movieReviews);
    }
    
    // 한줄평 등록 (영화 존재 여부 -> 권한 확인 -> 유효성 검증 -> 저장)   
    @PostMapping("/{movieId}/reviews")
	public ResponseEntity<?> addReview(@PathVariable("movieId") Long movieId,
			@Valid @RequestBody MovieReviewDto movieReviewDto,
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
    	MovieReview movieReview = new MovieReview();
    	movieReview.setContent(movieReviewDto.getContent());
    	movieReview.setMovie(movie);
    	movieReview.setWriter(member);
    	movieReviewRepository.save(movieReview);
    	return ResponseEntity.ok(movieReview);
//    	MovieReview savedReview = movieReviewRepository.save(movieReview);
//        MovieReviewResponse response = new MovieReviewResponse(savedReview); // 응답용 DTO 생성하기
//    	return ResponseEntity.ok(response);
	}

    // 모든 한줄평 불러오기 (컨트롤러에서 엔티티를 DTO로 변환)
//    @GetMapping("/reviews")
//    public ResponseEntity<?> getMovieReviews() {
//        List<MovieReview> movieReviews = movieReviewRepository.findAll(); // movieReview 모두 불러온 값
//        // dto로 변환해주기 .. 이 코드 잘 모르겠음.. 순서도 중요한 것 같은데 .. **
//        List<MovieReviewDto> reviewDtos = movieReviews.stream()
//                .map(review -> new MovieReviewDto(
//                    review.getId(),
//                    review.getMovie().getId(),   // movie_id 명시적으로 전달
//                    review.getWriter().getUsername(),
//                    review.getContent()
//                ))
//                .toList();
//        return ResponseEntity.ok(reviewDtos);
//    }
    
	// 한줄평 등록 (영화 존재 여부 -> 권한 확인 -> 유효성 검증 -> 저장)
//  @PostMapping("/{movieId}/comments")
//  public ResponseEntity<?> addComment(@PathVariable("movieId") Long movieId,
//  		@Valid @RequestBody MovieCommentDto movieCommentDto,
//  		BindingResult result,  Authentication auth) {
//      Movie movie = movieRepository.findById(movieId).orElseThrow(
//      		() -> new EntityNotFoundException("해당 영화를 찾을 수 없음"));
//      if (auth == null) {
//			return ResponseEntity.status(403).body("한줄평 등록 권한이 없습니다.");
//		}
//      Member member =  memberRepository.findByUsername(auth.getName()).orElseThrow(
//				() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));
//      if (result.hasErrors()) {
//			Map<String, Object> errors = new HashMap<>();
//			result.getFieldErrors().forEach(
//				err -> {
//					errors.put(err.getField(), err.getDefaultMessage());
//				}
//			);
//			return ResponseEntity.badRequest().body(errors);
//		}
//      MovieComment movieComment = new MovieComment();
//      movieComment.setContent(movieCommentDto.getContent());
//      movieComment.setMovie(movie);
//      movieComment.setWriter(member);
//      movieCommentRepository.save(movieComment);
//      return ResponseEntity.ok(movieComment);
//  }
    
//    // 찜(좋아요)
//    @PostMapping("/{movieId}/like")
//    public ResponseEntity<?> likeMovie(@PathVariable("movieId") Long movieId) {
//    	Movie movie = movieRepository.findById(movieId).orElseThrow(
//         		() -> new EntityNotFoundException("해당 영화를 찾을 수 없음"));
//        movie.setLikeCount(movie.getLikeCount() + 1);
//        movieRepository.save(movie);
//        return ResponseEntity.ok(movie);
//    }
    

    
    // 1개 영화에 모든 한줄평 목록
//    @GetMapping("/{movieId}/comments")
//    public ResponseEntity<?> getMovieComments(@PathVariable("movieId") Long movieId) {
//    	Movie movie = movieRepository.findById(movieId).orElseThrow(
//         		() -> new EntityNotFoundException("해당 영화를 찾을 수 없음"));
//        List<MovieComment> movieComments = movieCommentRepository.findByMovie(movie);
//        return ResponseEntity.ok(movieComments);
//    }



}
