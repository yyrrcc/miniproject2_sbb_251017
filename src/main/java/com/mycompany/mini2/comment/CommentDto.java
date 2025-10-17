package com.mycompany.mini2.comment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
	
	private Long id; // 기본키
	private String content; // 내용
	private LocalDateTime createdAt;
	

}
