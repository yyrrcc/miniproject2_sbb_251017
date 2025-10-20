package com.mycompany.mini2.movie;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.mini2.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
// Jackson이 객체를 직렬화할 때, 이미 직렬화했던 객체라면 다시 직렬화하는 대신 ID 값만 참조하도록 합니다.
public class MovieReview {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 영화 한줄평 기본키

	// 한줄평:영화=n:1 (JsonIgnore 애노테이션을 이용해서 모든 한줄평 가져올 때 영화 정보는 무시할 것)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Movie movie; // 영화 정보 가져오기

    // 한줄평:유저=n:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member writer; // 유저 정보

    private String content; // 한줄평


}
