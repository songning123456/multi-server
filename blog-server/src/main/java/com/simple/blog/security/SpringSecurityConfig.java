package com.simple.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author songning
 * @date 2019/9/18
 * description
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String[] noLoginUrls = new String[]{"/hello", "/history/insert", "/register/all", "/image/original", "/label/getLabelConfig", "/image/save", "/users/exist", "/login", "/loginInfo", "/logoutSuccess", "/thirdPart/gitHub"};
        /** JWT拦截器*/
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = new JwtAuthenticationTokenFilter();
        /** 将JWT拦截器添加到UsernamePasswordAuthenticationFilter之前*/
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.formLogin()
                .loginPage("/loginInfo")
                .loginProcessingUrl("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler);
        httpSecurity.authorizeRequests()
                // 此处的角色不需要 ROLE_ 前缀,实现UserDetailsService设置角色时需要 ROLE_ 前缀
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasRole("USER")
                .antMatchers(noLoginUrls)
                .permitAll()
                .anyRequest()
                .authenticated();
        // 访问 /logout 表示用户注销，并清空session
        httpSecurity.logout().logoutSuccessUrl("/logoutSuccess");
        // 关闭csrf
        httpSecurity.csrf().disable();
        httpSecurity.cors();
        // AccessDeniedHandler处理器 拒绝访问处理器
        httpSecurity.exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler);
    }
}
