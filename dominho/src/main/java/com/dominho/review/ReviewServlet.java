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
		} else if(uri.indexOf("review_ok.do")!=-1) {
			reviewSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
	}
	
	private void review(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		
		ReviewDAO rdao=new ReviewDAO();
		MyUtil util=new MyUtil();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null && page.length()!=0)
			current_page=Integer.parseInt(page);

		int dataCount=rdao.dataCount();
		
		int rows=5;
		int total_page=util.pageCount(rows, dataCount);
		
		if(total_page<current_page) {
			current_page=total_page;
		}
		
		int offset = (current_page-1) * rows;
		if(offset < 0) offset = 0;
		
		// 데이터 가져오기
		List<ReviewDTO> list=rdao.listReview(offset, rows);
		
		for(ReviewDTO rdto : list) {
			rdto.setContent(rdto.getContent().replaceAll(">", "&gt;"));
			rdto.setContent(rdto.getContent().replaceAll("<", "&lt;"));
			rdto.setContent(rdto.getContent().replaceAll("\n", "<br>"));
		}
		
		String strUrl=cp+"/review/review.do";
		String paging=util.paging(current_page, total_page, strUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("dataCount", dataCount);
		
		forward(req, resp, "/WEB-INF/views/review/review.jsp");
	}
	
	private void reviewSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();

		if(info==null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		ReviewDAO rdao=new ReviewDAO();
		
		try {
			ReviewDTO rdto=new ReviewDTO();
			
			rdto.setOrderNum(Integer.parseInt(req.getParameter("orderNum")));
			rdto.setContent(req.getParameter("content"));
			
			rdao.insertReview(rdto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/review.do");
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		String page=req.getParameter("page");
		
		if(info==null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		ReviewDAO rdao=new ReviewDAO();
		
		try {
			int reviewNum=Integer.parseInt(req.getParameter("reviewNum"));

			rdao.deleteReview(reviewNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/review/review.do?page="+page);
	}
}
