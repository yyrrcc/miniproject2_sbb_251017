package com.mycompany.mini2.movie;

import com.mycompany.mini2.member.MemberResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// JSON 반환용 Response DTO
public class MovieReviewResponse {
	
	private Long id; // 영화 한줄평 기본키
    private String content; // 한줄평
    private MovieResponse movie; // 영화 정보 가져오기
    private MemberResponse writer; // 유저 정보
    
	public MovieReviewResponse(MovieReview movieReview) {
		this.id = movieReview.getId();
		this.content = movieReview.getContent();
		this.movie = new MovieResponse(movieReview.getMovie());
		this.writer = new MemberResponse(movieReview.getWriter());
	}
    
    



}
