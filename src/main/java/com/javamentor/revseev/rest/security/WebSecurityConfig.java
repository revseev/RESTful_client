package com.javamentor.revseev.rest.security;

import com.javamentor.revseev.rest.model.Role;
import com.javamentor.revseev.rest.model.User;
import com.javamentor.revseev.rest.service.RoleService;
import com.javamentor.revseev.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableOAuth2Sso
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("myUserDetailsService")
    UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("noOpPasswordEncoder")
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*http.authorizeRequests()
//                .antMatchers("/login", "/oauth2")
//                .permitAll()
                .antMatchers("/").authenticated()
//                .hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/list","/new","/save","/delete","/edit","/api/**")
                .hasAuthority("ADMIN");

        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(myAuthenticationSuccessHandler())
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("username").passwordParameter("password")
                // даем доступ к форме логина всем
                .permitAll();


        http.oauth2Login()
                .loginPage("/login")
                .permitAll();

        http.csrf()
                .ignoringAntMatchers("/api/**");

        http.logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/");

        http.exceptionHandling()
                .accessDeniedPage("/access-denied");
*/
        // =========================
        http.authorizeRequests()
                .anyRequest().permitAll();

//                .antMatchers("/login")
//                .permitAll()
//                .antMatchers("/").authenticated()
//                .antMatchers("/list","/new","/save","/delete","/edit","/api/**")
//                .hasAuthority("ADMIN");

        http.formLogin()
                // указываем страницу с формой логина
                .loginPage("/login")
                //указываем логику обработки при логине
                .successHandler(myAuthenticationSuccessHandler())
                // указываем action с формы логина
                .loginProcessingUrl("/login")
                // Указываем параметры логина и пароля с формы логина
                .usernameParameter("username").passwordParameter("password")
                // даем доступ к форме логина всем
                .permitAll();

//        http.oauth2Login()
//                .loginPage("/login")
//                .permitAll();


        http.logout()
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");

        http.exceptionHandling()
                .accessDeniedPage("/access-denied");

        http.csrf()
                .disable();
    }


    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return map -> {
            String principalName = map.get("email").toString();
            User user = userService.findByUsername(principalName);

            if (user == null) {
                String principalPassword = map.get("at_hash").toString();
                long principalMoney = Long.parseLong(map.get("sub").toString());

                user = new User(principalName, principalPassword, principalMoney);
                user.setRoles(Collections.singleton(new Role("USER")));

                userService.saveUser(user);
            }
            return user;
        };
    }

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }*/

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new LoginSuccessHandler();
    }


/*     //для разработки
   @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity.ignoring()
                .antMatchers("/api/**");
    }*/

/*
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS);
    }*/

/* @Bean // CORS Override
    public FilterRegistrationBean processCorsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("'");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);


        final FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }*/
}

