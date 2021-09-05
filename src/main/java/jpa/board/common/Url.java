package jpa.board.common;

/* api url 정의 */
public final class Url {
	public static final String TILES_AJAX = "/tiles/ajax";
	
	/* 로그인 */
	public static final class AUTH {
		
		/* 로그인 url */
		public static final String LOGIN = "/auth/login";
		
		/* 로그인 jsp */
		public static final String LOGIN_HTML = "/auth/login";
		
		/* 회원가입 url */
		public static final String JOIN = "/auth/join";
		
		/* 회원가입 jsp */
		public static final String JOIN_HTML = "/auth/join";
	  
		/* 사용자 등록 */
		public static final String INSERT_USER = "/auth/insertUser";
		 
		/* 로그인 인증 요청 */
		public static final String LOGIN_PROC = "/auth/login-proc";
		  
		/* 로그아웃 요청 */
		public static final String LOGOUT_PROC = "/auth/logout-proc";
	}
	
	/** 공통 */
	public static final class COMMON {
		
		/* 파일 업로드 */
		public static final String FILE_UPLOAD = "/file-upload";
		
		/** 파일 다운로드 */
		public static final String FILE_DOWNLOAD = "/file-download";
		
	}
	
	/* 메인 화면 */
	public static final class MAIN {

		/* 메인 url */
		public static final String MAIN = "/";
		
		/* 메인 jsp */
		public static final String MAIN_HTML = "/main/list";
		
		/* 메인 리스트 ajax */
		public static final String MAIN_LIST_AJAX = "/list-view";
		
		/* 메인 글쓰기 */
		public static final String MAIN_WRITE = "/board/write";
		
		/* 메인 글쓰기 jsp */
		public static final String MAIN_WRITE_JSP = "/main/write";
		
		/* 메인 수정화면 */
		public static final String MAIN_UPDATE = "/board/update";
		
		/* 메인 글쓰기 jsp */
		public static final String MAIN_UPDATE_JSP = "/main/update";
		
		/** 게시판 삭제 */
		public static final String MAIN_DELETE = "/board/delete";
		
	}
	
}