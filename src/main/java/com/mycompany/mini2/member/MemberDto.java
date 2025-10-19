package com.mycompany.mini2.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
	
	@NotBlank(message = "아이디는 필수 항목입니다.")
	@Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하입니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문자, 숫자만 사용 가능합니다.")
	private String username; // 아이디
	
	@NotBlank(message = "비밀번호 필수 항목입니다.")
	@Size(min = 3, message = "비밀번호는 최소 3자 이상이어야 합니다.")
	private String password; // 비밀번호
	
	@NotBlank(message = "이름은 필수 항목입니다.")
	@Pattern(regexp = "^[가-힣]{2,10}$", message = "이름은 2-10자의 한글로만 입력해주세요.")
	private String name; // 이름
	
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email; // 이메일
}
