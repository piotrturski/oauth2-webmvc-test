package com.example.demo.pack

import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.DependsOn
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.web.bind.annotation.PutMapping
import java.security.Principal


@RestController
class MyController {

    @GetMapping("/api/a")
    fun a(principal: Principal): String = """"hello ${principal.name}""""

    @PutMapping("/api/a")
    fun b(principal: Principal): String = """"hello ${principal.name}""""
}


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder){
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("admin")
                .roles("USER")
                ;
    }

     override fun configure(web: WebSecurity) {
//        web.ignoring()
                // Spring Security should completely ignore URLs starting with /resources/
//                .antMatchers("/api/**")
//                .antMatchers("/**")
    }

    override fun configure(http: HttpSecurity) {

//        http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest()
//                .hasRole("USER").and()
                // Possibly more configuration ...
//                .formLogin() // enable form based log in
                // set permitAll for all URLs associated with Form Login
//                .permitAll();
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
//        return BCryptPasswordEncoder()
    }
}

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConf : AuthorizationServerConfigurerAdapter() {

    @Autowired lateinit var authenticationManager: AuthenticationManager

    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
    }

    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("my-cliend-id")
                .secret("frontendClientSecret")
                .authorizedGrantTypes("password","authorization_code", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600*24)
                .scopes("read")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(inMemoryTokenStore()).authenticationManager(authenticationManager)
    }

    @Bean
    fun inMemoryTokenStore(): TokenStore = InMemoryTokenStore()
}

@Configuration @EnableResourceServer
public class Oauth2ResourceServerConfig : ResourceServerConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.requestMatchers()
                .antMatchers("/api/**")
                .and()
                .authorizeRequests() .anyRequest() .authenticated()
    }
}