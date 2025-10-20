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
public class MovieCommentDto {
	
	@NotBlank(message = "한줄평을 입력해 주세요")
	@Size(min = 2, message = "한줄평은 최소 2글자 이상이어야 합니다")
	private String content;

}
