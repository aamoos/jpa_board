package jpa.board.util;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class ResponseUtil {
    public static void alertAndGo(HttpServletResponse response, String msg, String url)
            throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().printf("<script>alert('%s', function(){location.href='%s';});</script>",
                msg, url);
    }

    public static void alertAndBack(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().printf("<script>alert('%s', function(){history.go(-1);});</script>", msg);
    }

    public static void write(HttpServletResponse response, String contents) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(contents);
    }
    public static void writeJson(HttpServletResponse response, String contents) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(contents);
    }

    public static void addCookie(HttpServletResponse response, String key, String value)
            throws IOException {
        Cookie cookie = new Cookie(key, value);
        cookie.setSecure(true);
        cookie.setHttpOnly(true); //HttpOnly flag
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void wrapHtml(HttpServletResponse response, String contents) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<body>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />");
        sb.append("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\" />");
        sb.append(
                "<link href=\"/hpms/fonts/nanumbarungothic.css\" rel=\"stylesheet\" type=\"text/css\" />");
        sb.append(
                "<style>*{ font-family: 'Nanum Barun Gothic','돋움', '굴림', Dotum, Gulim, sans-serif;  font-size:13px; }</style>");
        sb.append("<script src=\"/hpms/js/jquery-2.0.2.min.js\"></script>");
        sb.append("<script>$(window).load(function(){parent.resizeContents();});</script>");
        sb.append("</head>");
        sb.append(contents);
        sb.append("</body>");
        sb.append("</html>");
        response.getWriter().print(sb.toString());
    }
}
