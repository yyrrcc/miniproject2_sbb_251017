package com.mycompany.mini2.board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.mini2.comment.Comment;
import com.mycompany.mini2.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본키
	
	private String title; // 제목
	private String content; // 내용
	
	@CreationTimestamp // 자동으로 현재 날짜와 시간 삽입.
	private LocalDateTime createdAt;
	
	@UpdateTimestamp // 수정 할 땐 @UpdateTimestamp 사용
	private LocalDateTime updatedAt;
	
	// 조회수 (초기값 0으로 지정)
//	@ColumnDefault("0")
	private Long views = 0L;
	
	// 게시글:유저 = n:1, 지연로딩 통해서 불필요한 값 제외 (성능 최적화)
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Member writer;
	
	// 게시글:댓글 = 1:n
	// mappedBy는 양방향 연관관계에서 누가 외래키를 관리할 지 정하는 것(보통 many쪽이 주인 - 데이터베이스에서 외래키를 실제로 들고 있는 엔티티 쪽)
	// 댓글이 있는 게시글 지울 때 cascade 해줘야 함!
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
	

}
