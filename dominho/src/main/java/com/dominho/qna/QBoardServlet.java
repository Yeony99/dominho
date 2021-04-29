package com.dominho.qna;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dominho.member.SessionInfo;
import com.util.MyUtil;

@WebServlet("/qna/*")
public class QBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info==null) {
			forward(req, resp, "/WEB-INF/views/member/login.jsp");
			return;
		}
		String uri = req.getRequestURI();
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("created.do") != -1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("reply.do") != -1) {
			replyForm(req, resp);
		} else if(uri.indexOf("reply_ok.do") != -1) {
			replySubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MyUtil util = new MyUtil();
		QBoardDAO dao = new QBoardDAO();
		String cp = req.getContextPath();
		
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page !=null) { //여기서 오류 numberformatException For input string: "null"
			current_page = Integer.parseInt(page);
		}
		
		//검색
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "all";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		//데이터 개수
		int dataCount;
		if(keyword.length() == 0) {
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		
		//전체 페이지 수
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		//게시글 가져오기
		int offset = (current_page - 1) * rows;
		if(offset <0) offset = 0;
		
		List<QBoardDTO> list;
		if(keyword.length() == 0) list = dao.listBoard(offset, rows);
		else list = dao.listBoard(offset, rows, condition, keyword);
		
		//리스트 글 번호
		int listNum, n = 0;
		for(QBoardDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query ="";
		if(keyword.length()!=0) {
			query = "condition="+condition+"&keyword"+URLEncoder.encode(keyword, "utf-8");
		}
		
		//페이징
		String listUrl = cp + "/qna/list.do";
		String articleUrl = cp+"/qna/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl+="?"+query;
			articleUrl+="&"+query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("current_page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/qna/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/qna/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		QBoardDAO dao = new QBoardDAO();
		
		try {
			QBoardDTO dto = new QBoardDTO();
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			
			dao.insertQBoard(dto, "created");
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		QBoardDAO dao = new QBoardDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int qBoardNum = Integer.parseInt(req.getParameter("qBoardNum"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF_8");
			}
			dao.updateHitCount(qBoardNum);
			QBoardDTO dto = dao.readBoard(qBoardNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}

			MyUtil util = new MyUtil();
			dto.setContent(util.htmlSymbols(dto.getContent()));
			QBoardDTO preReadDto = dao.preReadBoard(dto.getGroupNum(), dto.getOrderNo(), condition, keyword);
			QBoardDTO nextReadDto = dao.nextReadBoard(dto.getGroupNum(), dto.getOrderNo(), condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			forward(req, resp, "/WEB-INF/views/qna/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		
		QBoardDAO dao = new QBoardDAO();
		
		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition ="subject";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			int boardNum = Integer.parseInt(req.getParameter("qBoardNum"));
			QBoardDTO dto = dao.readBoard(boardNum);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			
			//게시글 올린 사람 아닐때
			if(!dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/views/qna/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		
		QBoardDAO dao = new QBoardDAO();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition!=null) { 
				query +="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
			}
			
			QBoardDTO dto = new QBoardDTO();
			dto.setqBoardNum(Integer.parseInt(req.getParameter("qBoardNum")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dao.updateBoard(dto, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
	protected void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		QBoardDAO dao = new QBoardDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int boardNum = Integer.parseInt(req.getParameter("qBoardNum"));
			
			QBoardDTO dto =dao.readBoard(boardNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/qna/list.do?page="+page);
				return;
			}
			String s = "["+dto.getSubject()+"]의 답변입니다.\n";
			dto.setContent(s);
			
			req.setAttribute("mode", "reply");
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/qna/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?page="+page);
	}
	
	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qna/list.do");
			return;
		}
		String page = req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		QBoardDAO dao = new QBoardDAO();
		
		try {
			QBoardDTO dto = new QBoardDTO();
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
			dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
			dto.setDepth(Integer.parseInt(req.getParameter("depth")));
			dto.setParent(Integer.parseInt(req.getParameter("parent")));
			
			dto.setUserId(info.getUserId());
			
			dao.insertQBoard(dto, "reply");
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		QBoardDAO dao = new QBoardDAO();
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition ="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if(keyword.length()!=0) {
				query +="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			int boardNum = Integer.parseInt(req.getParameter("qBoardNum"));
			QBoardDTO dto= dao.readBoard(boardNum);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			
			if(!dto.getUserId().equals(info.getUserId()) && ! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/qna/list.do?"+query);
				return;
			}
			
			dao.deleteBoard(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/qna/list.do?"+query);
	}
	
	
}
