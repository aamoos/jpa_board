package jpa.board.common;

/** 서비스에 사용되는 공통변수 */

public final class Constants {
	
	//프로젝트 패키지 이름
	public final static String APP_DEFAULT_PACKAGE_NAME = "jpa.board";

	/** 정적 리소스 종류 */
	private final static String[] STATIC_RES = {
			 "/js"
			,"/css"
			,"/images"
			,"/favicon"
			,"/template"
			,"/font"
			,"/robots.txt"
			,"/favicon.ico"
	};
	
	/** 정적 리소스 매핑 URL 패턴 (위에꺼랑 순서 맞아야함) */
	public final static String[] STATIC_RESOURCES_URL_PATTERS = {
			 STATIC_RES[0]+"/**"
			,STATIC_RES[1]+"/**"
			,STATIC_RES[2]+"/**"
			,STATIC_RES[3]+"/**"
			,STATIC_RES[4]+"/**"
			,STATIC_RES[5]+"/**"
			,STATIC_RES[6]
			,STATIC_RES[7]+"/**"
	};
	
}