package com.mycompany.mini2.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieReviewDto {
	
	// 양방향 순환 에러 때문에 엔티티의 값을 dto로 변환해서 필요한 데이터만 가져오도록 하는 것 
//	private Long id;
//	private Long movieId; // 문제가 됐던 Movie의 id(기본키)
//	private String writerId;
	
	@NotBlank(message = "한줄평을 입력해 주세요")
	@Size(min = 2, message = "한줄평은 최소 2글자 이상이어야 합니다")
	private String content;

}
