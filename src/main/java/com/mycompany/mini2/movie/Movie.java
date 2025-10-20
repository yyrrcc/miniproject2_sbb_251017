package com.mycompany.mini2.movie;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//Jackson이 객체를 직렬화할 때, 이미 직렬화했던 객체라면 다시 직렬화하는 대신 ID 값만 참조하도록 합니다.
public class Movie {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // React의 MovieList id와 동일하게 관리 (1~5)
    
    private String title; // 영화 제목

    private Long likeCount = 0L;  // 찜(좋아요) 수

    // 영화:한줄평=1:n
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieReview> reviews = new ArrayList<>();

}
