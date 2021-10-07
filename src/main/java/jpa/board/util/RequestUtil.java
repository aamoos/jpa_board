package jpa.board.util;

import java.net.URI;
import java.util.Enumeration;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;
import com.google.common.collect.Maps;

public class RequestUtil {

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookieName != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");

        if (header == null) {
            return "";
        }

        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        }

        if (header.indexOf("Chrome") > -1 && !(header.indexOf("Firefox") > -1) && !(header.indexOf("Edge") > -1) && !(header.indexOf("OPR") > -1)) {
            return "Chrome";
        }

        if (header.indexOf("OPR") > -1) {
            return "Opera";
        }

        if (header.indexOf("Firefox") > -1) {
            return "Firefox";
        }

        if (header.indexOf("Edge") > -1) {
            return "Edge";
        }

        if (header.indexOf("Safari") > -1) {
            return "Safari";
        }



        else {
            return header;
        }
    }

    public static String getRequestURI(HttpServletRequest request) {
        Object forwardRequestUri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (forwardRequestUri != null) {
            return forwardRequestUri + "(" + request.getRequestURI() + ")";
        }
        return request.getRequestURI();
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getRequestBaseUrl(HttpServletRequest request) {
        String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        return path;
    }

    public static String getHeaderString(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return null;
        }
        Map<String, String> headerMap = Maps.newLinkedHashMap();
        try {
            String headerName = null;
            while ((headerName = headerNames.nextElement()) != null) {
                headerMap.put(headerName, request.getHeader(headerName));
            }
        } catch (NoSuchElementException e) {
            return headerMap.toString();
        }

        return headerMap.toString();
    }

    public static String removeParameterFromURI(URI uri, String param) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri);
        return uriBuilder.replaceQueryParam(param, (Object[]) null).toUriString();
    }

    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        //기기 구분
        if(userAgent.indexOf("iPad") != -1 || userAgent.indexOf("iPhone") != -1) {
            return "IOS";
        }

        else if(userAgent.indexOf("Android") != -1) {
            return "ANDROID";
        }

        else {
            return "PC";
        }

    }

}
