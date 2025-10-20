package com.mycompany.mini2.comment;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mini2.board.Board;
import com.mycompany.mini2.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본키
	
	private String content; // 내용
	
	@CreationTimestamp // 자동으로 현재 날짜와 시간 삽입. 수정 할 땐 @UpdateTimestamp 사용
	private LocalDateTime createdAt;
	
	// 댓글:유저=n:1
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})	
	private Member writer;
	
	// 댓글:게시판=n:1
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private Board board;
	

}
