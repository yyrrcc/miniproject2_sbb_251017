package com.mycompany.mini2.board;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본키
	
	private String title; // 제목
	private String content; // 내용
	
	@CreationTimestamp // 자동으로 현재 날짜와 시간 삽입. 수정 할 땐 @UpdateTimestamp 사용
	private LocalDateTime createdAt;
	

}
