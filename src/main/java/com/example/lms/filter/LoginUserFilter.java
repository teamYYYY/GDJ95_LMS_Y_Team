package com.example.lms.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Filter implementation class LoginUserFilter
 */
@WebFilter("/*") // 모든 요청에 대해 필터가 동작하도록 설정
public class LoginUserFilter extends HttpFilter implements Filter {
       
		// 필터 초기화 메서드 (필요시 사용)
		public void init(FilterConfig fConfig) throws ServletException {
			System.out.println("LoginUserFilter 초기화됨.");
		}

	    // 필터 종료 시 호출되는 메서드
		public void destroy() {
			// 자원 해제 등이 필요하면 여기에 구현
		}

		/**
		 * 필터의 핵심 로직: 요청/응답을 가로채서 처리합니다.
		 */
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
		        throws IOException, ServletException {
		    
			// 1. HTTP 관련 기능을 사용하기 위해 HttpServletRequest와 HttpServletResponse로 캐스팅
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			
			// 2. 요청 URI 경로를 가져옵니다. (Context Path를 제외한 부분)
			String contextPath = httpRequest.getContextPath();
			String requestURI = httpRequest.getRequestURI();
			String path = requestURI.substring(contextPath.length());

			// 3. 필터링에서 제외할 경로(Exclude Path) 설정
			// 로그인 페이지 자체와 정적 리소스(CSS, JS 등)는 필터링에서 제외해야 무한 루프를 방지할 수 있습니다.
			if (path.startsWith("/login") || // 로그인 페이지
			    path.startsWith("/resources/") || // 정적 리소스 폴더 (예: CSS, JS, 이미지)
			    path.equals("/") || // 루트 경로 (필요에 따라 설정)
			    path.startsWith("/api/public/")) { // 인증이 필요 없는 공개 API 경로 등
			    
				// 제외 경로이므로 다음 필터나 서블릿으로 바로 전달
				chain.doFilter(request, response);
				return;
			}

			// 4. 세션에서 로그인 사용자 정보를 확인
			// false를 인자로 넘겨서 기존 세션이 없으면 새로 생성하지 않도록 합니다.
			HttpSession session = httpRequest.getSession(false);
			
			// 'loggedInUser'는 실제 로그인 시 세션에 저장하는 키 이름으로 변경해야 합니다.
			boolean loggedIn = (session != null && session.getAttribute("loginUser") != null);

			if (!loggedIn) {
				// 5. 로그인되지 않은 경우: /login 페이지로 리다이렉트
				System.out.println("[LoginUserFilter] 인증되지 않은 접근: " + path + " -> /login으로 리다이렉트");
				// Context Path를 포함하여 절대 경로로 리다이렉트해야 정확합니다.
				httpResponse.sendRedirect(contextPath + "/login");
				return;
			} else {
				// 6. 로그인된 경우: 요청을 필터 체인의 다음 단계로 전달
				chain.doFilter(request, response);
			}
		}
}
