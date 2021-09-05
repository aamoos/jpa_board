package jpa.board.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jpa.board.common.CoTopComponent;
import jpa.board.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;

/* 로그인 성공시 타는 handler */
@Component
public class LoginSucessHandler extends CoTopComponent implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
		
		//default 성공
		String resultCode = "00";
		
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(60 * 60 * 3);
		
		session.setAttribute("sessUserInfo",((Users) auth.getPrincipal()));
	    session.setAttribute("authority",((Users) auth.getPrincipal()).getAuthority());
	    session.setAttribute("authorityNm",((Users) auth.getPrincipal()).getAuthorityNm());
	    session.setAttribute("auth",((Users) auth.getPrincipal()).getAuth());
	    
	    //Response 결과 값을 넣어줌
	    JsonObject loginResult = new JsonObject();
	    loginResult.addProperty("resultCode", resultCode);
	    loginResult.addProperty("targetUrl", request.getContextPath()+"/");
	    
	    //응답 전송
	    writeResponse(response, loginResult);
	}
	
}
