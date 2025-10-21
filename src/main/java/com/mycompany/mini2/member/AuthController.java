package com.mycompany.mini2.member;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
// 인증 전용 컨트롤러
public class AuthController {
	
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 회원가입 + 유효성 체크
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody MemberDto memberDto, BindingResult result) {
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			// getFieldErrors()로 에러 꺼내서 forEach로 돌려준다
			result.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.badRequest().body(errors); // badRequest은 400 에러
		}
		Member member = new Member();
		member.setUsername(memberDto.getUsername());
		if (memberRepository.findByUsername(memberDto.getUsername()).isPresent()) {
			Map<String, String> error = new HashMap<>();
			error.put("idError", "이미 존재하는 아이디입니다.");
			return ResponseEntity.badRequest().body(error);
		}
		member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
		member.setName(memberDto.getName());
		member.setEmail(memberDto.getEmail());
		memberRepository.save(member);
		return ResponseEntity.ok("회원가입 성공"); // ok는 200 성공 응답
	}
	
	
	// 로그인 (관례적으로 me 사용, Session이나 Principal 같은 것이라 생각하기)
	@GetMapping("/me")
	public ResponseEntity<?> me(Authentication auth){
		auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth == null || !auth.isAuthenticated()
//				|| "anonymousUser".equals(auth.getName())) {
//	        return ResponseEntity.status(401).body("로그인이 필요합니다");
//	    }	    
		return ResponseEntity.ok(Map.of("username", auth.getName())); // username으로 로그인한 사용자 정보를 넣어줌
	}
	
	// 회원 정보 가져오기
	@GetMapping("/mypage")
	public ResponseEntity<?> getMember(Authentication auth) {
		Member member = memberRepository.findByUsername(auth.getName()).orElseThrow(
				() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));
		return ResponseEntity.ok(member);
	}
	
	// 회원 정보 수정
	@PostMapping("/mypage")
	public ResponseEntity<?> updateMember(@Valid @RequestBody MemberDto memberDto,
			BindingResult result, Authentication auth) {
		if (auth == null || !auth.isAuthenticated()) {
			return ResponseEntity.badRequest().body("권한이 없습니다.");
		}
		if(result.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			result.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.badRequest().body(errors); // badRequest은 400 에러
		}
		Member member = memberRepository.findByUsername(auth.getName()).orElseThrow(
				() -> new UsernameNotFoundException("사용자를 찾을 수 없음"));
		member.setName(memberDto.getName());
		member.setPassword(passwordEncoder.encode(memberDto.getPassword()));
		member.setEmail(memberDto.getEmail());
		memberRepository.save(member);
		return ResponseEntity.ok(member);
	}

}
