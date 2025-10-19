package com.mycompany.mini2.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletResponse;

@Configuration // 환경 설정 파일
@EnableWebSecurity // 모든 요청 url이 스프링 시큐리티의 제어를 받도록 하는 annotation
public class Securityconfig {

		@Bean
		SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http
			.csrf(csrt -> csrt.disable()) // csrf 인증을 비활성화 (프론트엔드 + 백엔드 구조이기에 불필요)
			.cors(Customizer.withDefaults()) // CORS를 활성화
			.authorizeHttpRequests(auth
					-> auth
					.requestMatchers(
		                      "/", 
		                      "/index.html", 
		                      "/login", 
		                      "/signup", 
		                      "/board/**", 
		                      "/static/**").permitAll() 
					.requestMatchers(
							"/api/auth/**", 
							"/api/auth/signup", 
							"/api/auth/login", 
							"/api/board", 
							"/api/board/**", 
							"/api/comments", 
							"/api/comments/**").permitAll()
					.requestMatchers("/api/auth/me").authenticated()
					.anyRequest().authenticated())
			.formLogin(login -> login
					.loginPage("/login").permitAll()
					.loginProcessingUrl("/api/auth/login") // 로그인 요청 url
					.usernameParameter("username") // 아이디 input name="username"일 때
			        .passwordParameter("password") // password input name="password"일 때
			        // 값들이 SecurityService 클래스로 넘어와서 db에 있는지 없는지 확인
			        .successHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
			        .failureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED))) 
			.logout(logout -> logout
					.logoutUrl("/api/auth/logout")
					.invalidateHttpSession(true)  // 세션 무효화
	                .clearAuthentication(true)    // 인증 정보 완전 삭제
	                .deleteCookies("JSESSIONID", "remember-me")  // 쿠키 삭제
					.logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
					);
			return http.build();
		}
		
		@Bean 
		PasswordEncoder passwordEncoder() { // 비밀번호 암호화
			return new BCryptPasswordEncoder();
		}
		
	   @Bean
	    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        return config.getAuthenticationManager();
	    }
		
		@Bean
	    CorsConfigurationSource corsConfigurationSource() { // 프론트엔드 리액트에서 요청하는 주소 허용
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowedOrigins(List.of("http://localhost:3000")); // React 개발 서버
	        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	        config.setAllowedHeaders(List.of("*"));
	        config.setAllowCredentials(true); // 쿠키, 세션 허용 시 필요

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", config);
	        return source;
	    }
		

	}

