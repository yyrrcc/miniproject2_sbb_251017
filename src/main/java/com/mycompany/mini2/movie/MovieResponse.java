package com.mycompany.mini2.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// JSON 반환용 Response DTO
public class MovieResponse {
	
    private Long id;  // React의 MovieList id와 동일하게 관리 (1~5)
    
    private String title; // 영화 제목

    private Long likeCount = 0L;  // 찜(좋아요) 수
    
    public MovieResponse(Movie movie) {
    	this.id = movie.getId();
    	this.title = movie.getTitle();
    	this.likeCount = movie.getLikeCount();
    }

}
