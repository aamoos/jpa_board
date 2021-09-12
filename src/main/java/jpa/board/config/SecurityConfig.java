package jpa.board.config;

import jpa.board.common.Constants;
import jpa.board.common.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CharacterEncodingFilter;

//java 설정파일
//시큐리티 설정 파일임을 나타냄
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginSucessHandler loginSucessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    /* configure */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AdminAuthenticationProvider());
        super.configure(auth);
    }

    /* http 요청 검사 */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/board/**").permitAll()
                .antMatchers("/auth/**").permitAll()					//로그인, 회원가입 접속허용
                .antMatchers("/resource/**/images/**").permitAll()		//이미지
                .anyRequest().authenticated()
                .and()

                //로그인 화면 설정
                .formLogin()
                .permitAll()
                .loginPage(Url.AUTH.LOGIN)
                .loginProcessingUrl(Url.AUTH.LOGIN_PROC)
                .successHandler(loginSucessHandler)
                .failureHandler(loginFailureHandler)
                .usernameParameter(USERNAME_PARAM)
                .passwordParameter(PASSWORD_PARAM)
                .and()
                .logout()
                .logoutUrl(Url.AUTH.LOGOUT_PROC)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .and()

                //세션관리
                .sessionManagement()
                .maximumSessions(200) 				//세션 허용 갯수
                .expiredUrl(Url.AUTH.LOGIN)		 	//세션 만료시 이동할 페이지
                .sessionRegistry(sesionRegistry())
                .maxSessionsPreventsLogin(true);	//동시 로그인 차단, false인 경우 기존 세션 만료
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        web
                .ignoring()
                .antMatchers(Constants.STATIC_RESOURCES_URL_PATTERS)
                .antMatchers(HttpMethod.GET, "/exception/**");
        super.configure(web);
    }

    //패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sesionRegistry() {
        return new SpringSecuritySessionRegistImpl();
    }

    /* 관리자 아이디 파라미터 이름 */
    public static final String USERNAME_PARAM = "un";

    /* 관리자 비밀번호 파라미터 이름 */
    public static final String PASSWORD_PARAM = "up";
}
