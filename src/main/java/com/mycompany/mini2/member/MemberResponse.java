package com.mycompany.mini2.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// JSON 반환용 Response DTO
public class MemberResponse {
	
	private Long id; // 기본키
	
	private String username; // 아이디
		
	private String name; // 이름

	public MemberResponse(Member member) {
		this.id = member.getId();
		this.username = member.getUsername();
		this.name = member.getName();
	}
	
	

}
