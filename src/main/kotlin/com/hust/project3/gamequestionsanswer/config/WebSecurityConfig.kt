package com.hust.project3.gamequestionsanswer.config

import com.hust.project3.gamequestionsanswer.security.TokenAuthenticationEntryPoint
import com.hust.project3.gamequestionsanswer.security.TokenAuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        return TokenAuthenticationFilter()
    }

    @Value("\${websocket.stomp.endpoint}")
    private lateinit var stompEndpointRegistry: String

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/",
                        "/account/register",
                        "/account/login**",
                        "/app-version/check",
                        "/v2/api-docs",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources",
                        "/swagger-resources/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        stompEndpointRegistry,
                        "$stompEndpointRegistry/**").permitAll()
                .anyRequest().authenticated()

        httpSecurity
                .exceptionHandling().authenticationEntryPoint(TokenAuthenticationEntryPoint()).and()
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        httpSecurity.headers().cacheControl()
    }

}