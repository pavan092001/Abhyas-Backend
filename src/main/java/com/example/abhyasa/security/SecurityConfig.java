package com.example.abhyasa.security;


import com.example.abhyasa.jwt.AuthEntryPointJwt;
import com.example.abhyasa.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {


    @Value("${frontend.url}")
    String frontendUrl;



    @Autowired
    LogoutHandler logoutHandler;


    @Autowired
    AuthEntryPointJwt authenticationEntryPoint;


    @Bean
    public AuthTokenFilter getTokenFilter(){
        return new AuthTokenFilter();
    }



    @Bean
    AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();

    }


    @Bean
    public SecurityFilterChain getChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf->csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/auth/csrf-token")
                .ignoringRequestMatchers("/api/**")
                .ignoringRequestMatchers("/email_bg.png")
        );

        httpSecurity.exceptionHandling(exp->exp.authenticationEntryPoint(authenticationEntryPoint));
        httpSecurity.cors(c->c.configurationSource(corsConfigurationSource()));
        httpSecurity.addFilterBefore(getTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        httpSecurity.logout(l->l.logoutUrl("/api/auth/logout").addLogoutHandler(logoutHandler).logoutSuccessHandler(
                ((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
        ));

        httpSecurity.authorizeHttpRequests(
                (http)->http.requestMatchers("/api/auth/csrf-token").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/email_bg.png").permitAll()
                .anyRequest().authenticated());

        httpSecurity.httpBasic(Customizer.withDefaults());
        return  httpSecurity.build();
    }



    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(frontendUrl));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        // configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Origin", "Accept", "X-Requested-With", "*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
