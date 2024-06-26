package com.spring.iot.configuration;

import com.spring.iot.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Autowired
    private JwtRequestFilter requestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers( "/Callback?**","/authenticate", "/sign-up","/hello","/checkusername/",
                        "/ws/**","/expirationOfToken/**","/refreshtoken/**","/generateOtp"
                        ,"/token-sign-up","/generateOtp","/validateOtp","/admin-authenticate","/api/user/test"
                ,"/sensor-value/export/excel", "/test", "/swagger-ui/**", "/v3/api-docs/**","/export" ,"/api/sensor/dataByMonthAndWeek/{idStation}", "/api/sensor/dataByWeek/{idStation}").permitAll()
                .and()
                .authorizeHttpRequests().requestMatchers("/api/**","/data")
                .authenticated()
                .and()
                .authorizeHttpRequests().requestMatchers("/api/user/**", "/api/all-min-max/{stationId}","/api/all-station-and-sensor", "/api/station/in-active/{stationId}"
                ,"/api/station/active/{idStation}", "/api/sensor/in-avtive/{idSensor}", "/api/sensor/avtive/{idSensor}","/api/sensor/average/**","/api/notification/**"
                ,"/api/sensor/schedule-inactive","/api/sensor/cancel-schedule/{idSensor}/{taskID}","/api/sensor/edit-schedule-inactive/{taskID}","/api/sensor/get-index").hasAnyAuthority("ADMIN")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
