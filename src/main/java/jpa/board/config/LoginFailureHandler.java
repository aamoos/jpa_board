package jpa.board.config;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jpa.board.common.CoTopComponent;
import jpa.board.exception.UserAuthException;
import jpa.board.exception.UserIdException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그인 실패시 타는 핸들러
 */
@Slf4j
@Component
public class LoginFailureHandler extends CoTopComponent implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		writeResponse(response, parseException(request.getParameter("un"), exception));
	}
	
	private JsonObject parseException(String userName, AuthenticationException exception) {
		String errCode = "99";
		String errMsg = exception.getMessage();
		//존재하지 않는 아이디
		if( exception instanceof UserIdException) {
			errMsg = "존재하지않는 아이디입니다.";
		}
		
		//권한이 없을경우
		else if( exception instanceof UserAuthException) {
			errMsg = "권한이 없는 아이디입니다.";
		}
		
		JsonObject result = new JsonObject();
		result.addProperty("resultCode", errCode);
		result.addProperty("resultMessage", errMsg);
		return result;
	}
}
