package com.proyecto.idat.security;

import com.proyecto.idat.security.filters.JwtAuthenticationFilter;
import com.proyecto.idat.security.filters.JwtAuthorizationFilter;
import com.proyecto.idat.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtAuthorizationFilter authorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        return httpSecurity
                .csrf(config -> config.disable())
                .authorizeHttpRequests(auth -> {
                    //auth.requestMatchers("/empleados/**", "/roles/**", "/productos/**", "/categorias/**", "/pedidos/**", "fondo-inicial/**", "/fondo-arqueo/**", "/cab_pedidos/**").hasRole("INVITADO");
                    auth.requestMatchers("/mesas/**", "/clientes").hasAnyRole("ADMINISTRADOR", "CAJERO", "MOZO", "INVITADO");
                    auth.requestMatchers("/roles/**", "/empleados/**").hasAnyRole("ADMINISTRADOR", "INVITADO", "CAJERO", "MOZO");
                    auth.requestMatchers("/productos/**", "/categorias/**").hasAnyRole("ADMINISTRADOR", "INVITADO", "MOZO");
                    auth.requestMatchers("/clientes/**", "/cab_pedidos/**").hasAnyRole("CAJERO", "MOZO", "INVITADO");
                    auth.requestMatchers("/fondo-inicial/**", "/fondo-arqueo/**").hasAnyRole("CAJERO", "INVITADO", "MOZO");
                    auth.requestMatchers("/reservacion/**").hasAnyRole("CAJERO", "INVITADO");
                    auth.requestMatchers("/reportes/**").hasAnyRole("ADMINISTRADOR", "INVITADO");
                    auth.anyRequest().authenticated();
                	//auth.anyRequest().permitAll();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors()
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("santiago")
//                .password("1234")
//                .roles()
//                .build());
//
//        return manager;
//    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
   }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }
}