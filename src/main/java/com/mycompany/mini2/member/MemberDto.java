package com.mycompany.mini2.member;

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
public class MemberDto {
	
	@NotBlank(message = "아이디를 입력해 주세요.")
	@Size(min = 3, message = "아이디는 최소 3글자 이상입니다.")
	private String username; // 아이디
	
	@NotBlank(message = "비밀번호를 입력해 주세요.")
	@Size(min = 3, message = "비밀번호는 최소 3글자 이상입니다.")
	private String password; // 비밀번호
	
	@NotBlank(message = "이름을 입력해 주세요.")
	private String name; // 이름
}
