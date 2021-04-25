package com.dominho.member;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.MyServlet;

@WebServlet("/member/*")
public class MemberServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();

		// uri에 따른 작업 구분
		if (uri.indexOf("login.do") != -1) {
			loginForm(req, resp);
		} else if (uri.indexOf("login_ok.do") != -1) {
			loginSubmit(req, resp);
		} else if (uri.indexOf("logout.do") != -1) {
			logout(req, resp);
		} else if (uri.indexOf("member.do") != -1) {
			memberForm(req, resp);
		} else if (uri.indexOf("member_ok.do") != -1) {
			memberSubmit(req, resp);
		} else if (uri.indexOf("pwd.do") != -1) {
			pwdForm(req, resp);
		} else if (uri.indexOf("pwd_ok.do") != -1) {
			pwdSubmit(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("userIdCheck.do") != -1) {
			userIdCheck(req, resp);
		}
	}

	private void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/views/member/login.jsp";
		forward(req, resp, path);
	}

	private void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		String userId=req.getParameter("userId");
		String userPwd=req.getParameter("userPwd");
		
		MemberDTO dto=dao.readMember(userId);
		if(dto!=null) {
			if(userPwd.equals(dto.getUserPwd())) {
				session.setMaxInactiveInterval(20*60); //단위 : sec
				
				SessionInfo info  = new SessionInfo();
				info.setUserId(dto.getUserId());
				info.setUserName(dto.getUserName());
				
				session.setAttribute("member", info);
				
				resp.sendRedirect(cp);
				return;
			}
		}	
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}

	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String cp = req.getContextPath();
		
		session.removeAttribute("member");;
		session.invalidate();
		
		resp.sendRedirect(cp);
	}

	private void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("title", "회원 가입");
		req.setAttribute("mode", "member");
		
		forward(req, resp, "/WEB-INF/views/member/join.jsp");
	}

	private void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO();
		String msg ="";
		
		try {
			MemberDTO dto = new MemberDTO();
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setEmail(req.getParameter("userEmail"));
			String birth=req.getParameter("birth").replaceAll("(\\.|\\-|\\/)", "");
			dto.setBirth(birth);
			dto.setTel(req.getParameter("tel"));
			dto.setZip(req.getParameter("zip"));
			dto.setAddr1(req.getParameter("address1"));
			dto.setAddr2(req.getParameter("address2"));
			
			dao.insertMember(dto);
			String cp = req.getContextPath();
			resp.sendRedirect(cp);
			return;
			
		} catch (SQLException e) {
			if(e.getErrorCode()==1)
				msg = "이미 가입된 아이디입니다.";
			else if(e.getErrorCode()==1400)
				msg = "필수 사항을 입력하지 않았습니다.";
			else if(e.getErrorCode()==1861)
				msg = "날짜 형식이 일치하지 않습니다.";
			else
				msg = "회원 가입에 실패하였습니다.";
		} catch (Exception e) {
			msg = "회원 가입에 실패하였습니다.";
			e.printStackTrace();
		}
		req.setAttribute("msg", msg);
		forward(req, resp, "/WEB-INF/views/member/join.jsp");
	}

	private void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void userIdCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 아이디 중복 검사

	}
}
