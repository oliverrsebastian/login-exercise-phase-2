package com.blibli.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private CustomAuthenticationFilter customAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable().cors().configurationSource(corsConfigurationSource()).and().httpBasic().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/library/login").permitAll()
                .antMatchers(HttpMethod.GET, "/library/member/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/library/member/**").permitAll()
                .antMatchers(HttpMethod.GET).hasAnyRole("ADMIN", "MEMBER")
                .antMatchers(HttpMethod.POST, "/library/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/library/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/library/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        .and().sessionManagement().sessionCreationPolicy(STATELESS)
        .and().addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost", "*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Access-Control-Allow-Origin", "X-Auth-Token",
                "Access-Control-Allow-Headers", "Authorization",
                "X-Requested-With", "requestId", "Correlation-Id"
        ));
        configuration.setExposedHeaders(Arrays.asList("Content-Type", "Access-Control-Allow-Origin", "X-Auth-Token",
                "Access-Control-Allow-Headers", "Authorization",
                "X-Requested-With", "requestId", "Correlation-Id"
        ));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(customAuthenticationProvider);
    }
}
