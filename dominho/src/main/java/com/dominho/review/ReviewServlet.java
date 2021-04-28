package com.dominho.review;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dominho.member.SessionInfo;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/review/*")
public class ReviewServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri=req.getRequestURI();
		
		if(uri.indexOf("review.do")!=-1) {
			review(req, resp);
		} else if(uri.indexOf("review.do")!=-1) {
			reviewSubmit(req, resp);
		} else if(uri.indexOf("review.do")!=-1) {
			reviewDelete(req, resp);
		}
	}
	
	private void review(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 방명록 리스트
		String cp=req.getContextPath();
		
		ReviewDAO dao=new ReviewDAO();
		MyUtil util=new MyUtil();
		
		// 넘어온 페이지
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null && page.length()!=0)
			current_page=Integer.parseInt(page);
		
		// 전체 데이터 개수
		int dataCount=dao.dataCount();
		
		// 전체페이지수 구하기
		int rows=5;
		int total_page=util.pageCount(rows, dataCount);
		
		// 전체페이지보다 표시할 페이지가 큰경우
		if(total_page<current_page) {
			current_page=total_page;
		}
		
		int offset = (current_page-1) * rows;
		if(offset < 0) offset = 0;
		
		// 데이터 가져오기
		List<ReviewDTO> list=dao.listReview(offset, rows);
		
		for(ReviewDTO dto : list) {
			dto.setContent(dto.getContent().replaceAll(">", "&gt;"));
			dto.setContent(dto.getContent().replaceAll("<", "&lt;"));
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		}
		
		// 페이징처리
		String strUrl=cp+"/review/review.do";
		String paging=util.paging(current_page, total_page, strUrl);
		
		// guest.jsp에 넘겨줄 데이터
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		
		forward(req, resp, "/WEB-INF/views/review/review.jsp");
	}
	
	private void reviewSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 방명록 저장
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();

		if(info==null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		ReviewDAO dao=new ReviewDAO();
		
		try {
			ReviewDTO dto=new ReviewDTO();
			
			dto.setReviewId(info.getUserId());
			dto.setContent(req.getParameter("content"));
			
			dao.insertReview(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/review.do");
	}
	
	private void reviewDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 방명록 삭제
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		String page=req.getParameter("page");
		
		if(info==null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		ReviewDAO dao=new ReviewDAO();
		
		try {
			int orderNum=Integer.parseInt(req.getParameter("orderNum"));

			dao.deleteGuest(orderNum, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/review.do?page="+page);
	}
}
