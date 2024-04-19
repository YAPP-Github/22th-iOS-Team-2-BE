package com.pyonsnalcolor.config;

import com.pyonsnalcolor.member.security.AuthUserDetailsService;
import com.pyonsnalcolor.member.security.JwtAuthenticationFilter;
import com.pyonsnalcolor.handler.JwtAccessDeniedHandler;
import com.pyonsnalcolor.handler.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity//(debug = true) // Spring Security 활성화
public class SecurityConfig {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
                        "/resources/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/health-check",
                        "/",
                        "/apple-app-site-association",
                        "/.well-known/apple-app-site-association"
                );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/promotions/**", "/fcm/**", "/manage/**").permitAll()
                .antMatchers("/products/pb-products", "/products/event-products").authenticated()       // 전체 조회
                .antMatchers("/products/pb-products/**/reviews/**"
                                        , "/products/event-products/**/reviews/**").hasRole("USER")     // 리뷰 작성/좋아요/싫어요
                .antMatchers("/products/pb-products/**", "/products/event-products/**").authenticated() // 단건 조회
                .antMatchers("/products/**").authenticated()                                    // 검색, 메타 데이터 등
                .antMatchers(HttpMethod.POST, "/favorites").hasRole("USER")                             // 찜하기 등록
                .antMatchers(HttpMethod.DELETE, "/favorites").hasRole("USER")                           // 찜하기 삭제
                .antMatchers(HttpMethod.PATCH, "/member/profile", "/member/nickname").hasRole("USER")   // 프로필 수정
                .antMatchers("/member/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthUserDetailsService();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
