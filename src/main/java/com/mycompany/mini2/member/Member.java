package com.mycompany.mini2.member;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 기본키
	
	@Column(unique = true, nullable = false)
	private String username; // 아이디
	
	@Column(nullable = false)
	@JsonIgnore // 비밀번호는 굳이 가져올 필요가 없으니 추가
	private String password; // 비밀번호
	
	@Column(nullable = false)
	private String name; // 이름
	
	@Column(unique = true, nullable = false)
	private String email; // 이메일

}
