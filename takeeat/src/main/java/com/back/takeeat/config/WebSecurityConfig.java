package com.back.takeeat.config;

import com.back.takeeat.security.CustomOAuth2UserService;
import com.back.takeeat.security.UserDetailsServiceImpl;
import com.back.takeeat.security.handler.CustomAccessDeniedHandler;
import com.back.takeeat.security.handler.CustomAuthenticationEntryPoint;
import com.back.takeeat.security.handler.LoginFailureHandler;
import com.back.takeeat.security.handler.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/market/**").hasAuthority("ROLE_OWNER")
                        .requestMatchers("/my/**").hasAnyAuthority("ROLE_MEMBER", "ROLE_OWNER", "ROLE_ADMIN")
                        .requestMatchers("/error/**").permitAll()
                        .requestMatchers("/payment/result/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin // form기반의 로그인인 경우
                        .loginPage("/member/login") //인증이 필요한 URL에 접근하면 /member/login으로 이동
                        .usernameParameter("memberLoginId") // 로그인 시 form에서 가져올 값(id, email 등이 해당)
                        .passwordParameter("password") // 로그인 시 form에서 가져올 값
                        .loginProcessingUrl("/login") // 로그인을 처리할 URL (form에서 요청보내는 URL)
                        .defaultSuccessUrl("/") // 로그인 성공하면 "/" 으로 이동
                        .failureHandler(loginFailureHandler) // 로그인이 실패할 때 처리할 Handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout") // 로그아웃 처리 URL
                        .logoutSuccessUrl("/") //로그아웃 성공 후 이동페이지
                )
                .httpBasic(AbstractHttpConfigurer::disable) //Spring Security가 기본적으로 제공해주는 httpBasic 로그인 방식 disable
                .oauth2Login((oauth2) -> oauth2 // OAuth2기반의 로그인인 경우
                        .loginPage("/member/login") //인증이 필요한 URL에 접근하면 /member/login으로 이동
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)) //인증절차를 진행하는 메서드
                        .successHandler(oAuth2LoginSuccessHandler)) //인증 성공시에 싱행되는 메서드
                .sessionManagement((session) -> session //Spring Security가 제공하는 세션방식 설정
                        .maximumSessions(1) //최대 세션 수 1개로 제한
                        .maxSessionsPreventsLogin(false) // 동시 로그인 차단, false인 경우 기존 세션 만료하고 로그인 진행
                        .expiredUrl("/member/login") // 세션이 만료된 경우 이동 할 페이지
                        .sessionRegistry(sessionRegistry()))
                 .exceptionHandling(handler -> handler.authenticationEntryPoint(new CustomAuthenticationEntryPoint())) //위 authorizeHttpRequests에 대한 인증(로그인 여부) 실패 시 실행되는 메서드
                .exceptionHandling((handler) -> handler.accessDeniedHandler(new CustomAccessDeniedHandler())); //위 authorizeHttpRequests에 대한 인가(ROLE 권한) 실패 시 실행되는 메서드
        return http.build();
    }

    // logout 후 login할 때 정상동작을 위함
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * resources의 static 경로는 Spring Security Filter 대상에서 제외
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * formLogin 방식에서 비밀번호 검증에서 사용되는 암호화 추가
     */
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
        return auth.build();
    }

}
