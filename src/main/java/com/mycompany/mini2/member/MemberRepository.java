package com.mycompany.mini2.member;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>{
	
	// 유저 아이디(username)으로 찾기
	Optional<Member> findByUsername(String username);

}
