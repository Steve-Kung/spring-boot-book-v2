package cn.stevekung.security;

import cn.stevekung.service.impl.CustomUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public CustomUserService customUserService(){
        return new CustomUserService();
    }

    // 登入成功跳转URL 失败URL 默认登录页面
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //路由策略和访问权限的简单配置
        http
                .authorizeRequests()
                //要求有管理员的权限
                .antMatchers("/shutdown").access("hasRole('ADMIN')")
                .antMatchers("/**").permitAll()
                .and()

                .formLogin()                      //启用默认登陆页面
                .failureUrl("/login?error")     //登陆失败返回URL:/login?error
                .defaultSuccessUrl("/ayUser/test")  //登陆成功跳转URL，这里调整到用户首页
                .permitAll();                    //登陆页面全部权限可访问
        super.configure(http);
    }

    // 分配权限 配置内存用户
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserService())  // 判断角色权限
                .passwordEncoder(new MyPasswordEncoder()); // 判断密码正确与否

//            .inMemoryAuthentication()
//            .passwordEncoder(new MyPasswordEncoder())
//            .withUser("steve").password("123456").roles("ADMIN")
//            .and()
//            .withUser("gj").password("123456").roles("USER");
    }
}
