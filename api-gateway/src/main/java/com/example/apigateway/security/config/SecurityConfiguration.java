package com.example.apigateway.security.config;

import com.example.apigateway.security.handler.SuccessJWTHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    private final SuccessJWTHandler successJWTHandler;

    public SecurityConfiguration(SuccessJWTHandler successJWTHandler) {
        this.successJWTHandler = successJWTHandler;
    }

    // 사용자 인증 매니저
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(MapReactiveUserDetailsService userDetailsService) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }

    // SecurityWebFilterChain 설정
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         ReactiveAuthenticationManager authManager,
                                                         ReactiveJwtDecoder jwtDecoder) {

        http
                // SecurityContext를 세션에 저장하지 않고 stateless 처리
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())

                // 접근 권한 설정
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/login").permitAll()      // 로그인 폼 접근 허용
                        .anyExchange().authenticated()           // 나머지는 JWT 필요
                )
                // 기본 폼 로그인 사용
                .formLogin(form -> form
                        .authenticationSuccessHandler(successJWTHandler) // 로그인 성공 시 JWT 반환
                )
                // JWT 검증 활성화
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder))
                );
                // CSRF 비활성화 (API 환경에서 사용)
                //.csrf().disable();

        return http.build();
    }

    // 사용자 정의 계정 등록
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
}
