package com.mycompany.mini2.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberSecurityService implements UserDetailsService{
	
	@Autowired
	private MemberRepository memberRepository;

	@Override
	// 유저에게 받은 username과 암호화된 password를 조회해서 권한을 준다
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Member member = memberRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("사용자 없음"));
		return org.springframework.security.core.userdetails.User
				.withUsername(member.getUsername())
				.password(member.getPassword())
				.authorities("USER")
				.build();
	}

}
