package com.mycompany.mini2.movie;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
	
	@Id
    private Long id;  // React의 MovieList id와 동일하게 관리 (1~5)
    
    private String title; // 영화 제목

    private Long likeCount = 0L;  // 찜(좋아요) 수

    // 영화:한줄평=1:n
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieComment> comments = new ArrayList<>();

}
