package com.dominho.admin_Member;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dominho.member.SessionInfo;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/admin_Member/*")
public class MemberServlet extends MyUploadServlet{

	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);

	}

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(uri.indexOf("boardList")== -1 && info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		if(uri.indexOf("memberList")!=-1) {
			memberList(req,resp);
		}else if(uri.indexOf("memberDetail")!=-1) {
			memberDetail(req,resp);
		}  else if(uri.indexOf("member_delete")!=-1) {
			delete(req,resp);
		}else if(uri.indexOf("deleteList")!=-1) {
			deleteList(req,resp);
		}
	}

	private void memberList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 고객 리스트
		MemberDAO dao = new MemberDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		// 파라미터로 넘어 온 페이지 번호
		String page = req.getParameter("page");
		int current_page =1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		// 이름, 아이디 검색
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		
		if(condition==null) {
			condition="userId";
			keyword="";
		}
		
		// GET방식은 디코딩 필요
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword,"utf-8");
		}
		
		// 데이터 개수
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount();
		}else
			dataCount = dao.dataCount();
		
		// 전체 페이지 수
		int rows =10;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		// 정보 가져오기
		int offset = (current_page - 1) *rows;
		if(offset < 0) offset = 0;
		
		List<MemberDTO> list;
		if(keyword.length()==0)
			list = dao.listMember(offset, rows);
		else 
			list = dao.listMember(offset, rows, condition, keyword);
		
		// 리스트 글 번호
		int listNum, n =0;
		for(MemberDTO dto : list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length()!=0) {
			query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
		}
		
		// 페이징 처리
		String listUrl = cp+"/admin/memberList";
		String detailUrl = cp+"/admin/memberDetail?page="+current_page;
		
		if(query.length()!=0) {
			listUrl += "?"+query;
			detailUrl += "&"+query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("detailUrl", detailUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/admin_Member/memberList.jsp");
	}

	private void memberDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		if(page==null) page="1";
		
		String query ="page="+page;
		
		try {
			String userId = req.getParameter("userId");
			String condition= req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition="userId";
				keyword="";
			}
			
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}

			// 정보 가져오기
			MemberDTO dto = dao.readMember(userId);
			if(dto==null) {
				resp.sendRedirect(cp+"/admin_Member/memberList?"+query);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			forward(req,resp, "/WEB-INF/views/admin_Member/memberDetail.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/admin_Member/memberList?"+query);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query ="page="+page;
		
		try {
			String userId = req.getParameter("usrId");
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query+= "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			MemberDTO dto = dao.readMember(userId);
			if(dto==null) {
				resp.sendRedirect(cp+"/admin_Member/memberList"+query);
				return;
			}
			
			dao.deleteMember(userId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		resp.sendRedirect(cp+"/admin_Member/memberList?"+query);
	}

	private void deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String cp = req.getContextPath();
		
		String page =req.getParameter("page");
		String query = "page="+page;
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		
		try {
			if(keyword!=null && keyword.length()!=0) {
				query+="condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			String nn[] = req.getParameterValues("userIds");
			String userIds[] = null;
			userIds = new String[nn.length];
			
			for(int i = 0; i<nn.length; i++) {
				userIds[i] =nn[i];
			}
			
			MemberDAO dao = new MemberDAO();
			
			dao.deleteMemberList(userIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/WEB-INF/memberList"+query);
	}
}
