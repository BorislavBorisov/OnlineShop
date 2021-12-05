package project.onlinestore.configs;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    public ApplicationSecurityConfiguration(DataSource dataSource, UserDetailsService userDetailsService) {
        this.dataSource = dataSource;
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/users/login");

        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/js/**", "/css/**").permitAll()
                .antMatchers("/", "/users/register", "/users/login").permitAll()
                .antMatchers("/shop/**").permitAll()
                .antMatchers("/question").permitAll()
                .antMatchers("/search").permitAll()
                .antMatchers("/moderator/**").hasAnyRole("MODERATOR", "ROOT")
                .antMatchers("/api").hasAnyRole("MODERATOR", "ROOT", "ADMIN")
                .antMatchers("/admin/users").hasAnyRole( "ROOT")
                .antMatchers("/stats").hasAnyRole( "ROOT")
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "ROOT")
                .anyRequest().authenticated()
                .and()
                .rememberMe().rememberMeParameter("remember-me")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/shop/categories", true)
                .failureForwardUrl("/users/login-error")
                .and()
                .logout()
                .logoutSuccessUrl("/users/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }

}