package com.example.demo.pack

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MyController {

    @GetMapping("/api/a")
    fun a() = 2
}


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {


     override fun configure(web: WebSecurity) {
        web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
                .antMatchers("/api/**");
    }

    override fun configure(http: HttpSecurity) {

//        http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest()
//                .hasRole("USER").and()
                // Possibly more configuration ...
//                .formLogin() // enable form based log in
                // set permitAll for all URLs associated with Form Login
//                .permitAll();
    }
}