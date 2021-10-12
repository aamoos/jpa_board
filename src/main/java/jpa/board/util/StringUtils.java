package jpa.board.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * 자주 사용하는 {@link String} 유틸리티 모음 클래스.
 * <p>이 클래스는 코어 자바에서 제공하는 {@link String} 과 {@link StringBuilder}
 * 클래스에서 제공하는 기능들을 사용하기 쉽게 재정의 하였습니다.
 * @version 1.0.1
 */
@Slf4j
public abstract class StringUtils {
    public static String encodeFileNm(String fileName, String browser) {

        String encodedFilename = null;
        if ("MSIE".equals(browser)) {
            try {
                encodedFilename = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        } else if ("Firefox".equals(browser)) {
            try {
                encodedFilename =

                        "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        } else if ("Opera".equals(browser)) {
            try {
                encodedFilename =

                        "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        } else if ("Chrome".equals(browser)) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (c > '~') {
                    try {
                        sb.append(URLEncoder.encode("" + c, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage());
                    }
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else if ("Safari".equals(browser)) {
            try {
                encodedFilename =

                        "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        } else {
            try {
                encodedFilename = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
        }

        return encodedFilename;
    }

    //널 빈값 체크
    public static boolean isStringEmpty(String str) {
        return str == null || str.isEmpty();
    }
}
