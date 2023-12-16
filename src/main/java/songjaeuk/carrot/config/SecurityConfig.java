package songjaeuk.carrot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import songjaeuk.carrot.config.auth.PrincipalDetailsOAuth2Service;
import songjaeuk.carrot.config.auth.PrincipalDetailsService;
import songjaeuk.carrot.config.auth.exceptionHandler.CustomAccessDeniedHandler;
import songjaeuk.carrot.config.auth.exceptionHandler.CustomAuthenticationEntryPoint;
import songjaeuk.carrot.config.auth.loginHandler.CustomAuthenticationFailureHandler;
import songjaeuk.carrot.config.auth.loginHandler.CustomLoginSuccessHandler;
import songjaeuk.carrot.config.auth.logoutHandler.CustomLogoutHandler;
import songjaeuk.carrot.config.auth.logoutHandler.CustomLogoutSuccessHandler;
import songjaeuk.carrot.config.auth.logoutHandler.OAuthLogoutHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private PrincipalDetailsService principalDetailsService;

    @Autowired
    private PrincipalDetailsOAuth2Service principalDetailsOAuth2Service;

    //----------------------------------------------------------------
    // 웹 요청 처리
    //----------------------------------------------------------------
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.csrf().disable();

        http
                .authorizeRequests()
                .antMatchers("/css/**","/js/**","/images/**","/post/**").permitAll()
                .antMatchers("/","/user/login","/user/join","/user/logout").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/post/**").permitAll()
                .antMatchers("/mypage/**").permitAll()
                .antMatchers("/post/**","/post/add","/post/delete","/post/update").permitAll()


                .anyRequest().authenticated()									//나머지 URL은 모두 인증작업이 완료된 이후 접근가능
                .and()

                //로그인
                .formLogin()
                .loginPage("/user/login")
                .successHandler(new CustomLoginSuccessHandler())					//ROLE_USER -> user페이지 / ROLE_MEMBER -> member페이지
                .failureHandler(new CustomAuthenticationFailureHandler())

                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .addLogoutHandler(new CustomLogoutHandler()) // 세션 초기화
                .addLogoutHandler(new OAuthLogoutHandler())


                .and()
                //예외처리
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())		//인증이 필요한 자원에 접근 예외처리
                .accessDeniedHandler(new CustomAccessDeniedHandler())				//권한 실패 예외처리

                //REMEMBER ME
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .tokenValiditySeconds(60*60)
                .alwaysRemember(false)
                .tokenRepository(tokenRepository())
                .userDetailsService(principalDetailsOAuth2Service)

                .and()
                //OAUTH2
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalDetailsOAuth2Service);


    }

    //----------------------------------------------------------------
    // 인증처리 함수
    //----------------------------------------------------------------
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {


        auth.userDetailsService(principalDetailsOAuth2Service)
                .passwordEncoder(bCryptPasswordEncoder());

    }


    //----------------------------------------------------------------
    //BEANS
    //----------------------------------------------------------------

    // BCryptPasswordEncoder Bean 등록
    // 패스워드 검증에 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //REMEMBER ME BEAN 추가

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        //repo.setCreateTableOnStartup(true);
        return repo;
    }
}
