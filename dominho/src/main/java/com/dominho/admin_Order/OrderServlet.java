package com.dominho.admin_Order;

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
@WebServlet("/admin_Order/*")
public class OrderServlet extends MyUploadServlet {

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

		if (uri.indexOf("orderList") == -1 && info == null) {
			resp.sendRedirect(cp + "/member/login.do");
			return;
		}
		
		if(uri.indexOf("orderList")!=-1) {
			orderList(req,resp);
		}
		
	}

	private void orderList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		OrderDAO dao = new OrderDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page =1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		
		if(condition==null) {
			condition = "orderNum";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword,"utf-8");
		}
		
		int dataCount;
		if(keyword.length()!=0) {
			dataCount = dao.dataCount(condition, keyword);
		}else
			dataCount = dao.dataCount();
		
		int rows =10;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		// 정보 가져오기
		int offset = (current_page - 1) *rows;
		if(offset < 0) offset = 0;
		
		List<OrderDTO> list;
		if(keyword.length()==0)
			list = dao.listOrder(offset, rows);
		else 
			list = dao.listOrder(offset, rows, condition, keyword);
		
		// 리스트 글 번호
		int listNum, n =0;
		for(OrderDTO dto : list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length()!=0) {
			query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
		}
		
		// 페이징 처리
		String listUrl = cp+"/admin_Order/orderList";
		
		if(query.length()!=0) {
			listUrl += "?"+query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/admin_Order/orderList.jsp");

	}

}
