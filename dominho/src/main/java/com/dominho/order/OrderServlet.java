package com.dominho.order;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dominho.member.SessionInfo;
import com.util.MyServlet;

@WebServlet("/order/*")
public class OrderServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
 
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();
		// 로그인이 되지 않은 상태이면 로그인 페이지로
				HttpSession session = req.getSession();
				SessionInfo info = (SessionInfo) session.getAttribute("member");
				if (info == null) {
					String cp = req.getContextPath();
					resp.sendRedirect(cp + "/member/login.do");
					return;
				}
		if (uri.indexOf("order_ok.do") != -1) {
			orderSubmit(req, resp);
		} else if (uri.indexOf("order.do") != -1) {
			order(req, resp);
		} else if (uri.indexOf("cart_add.do") != -1) {
			cartAdd(req, resp);
		} else if (uri.indexOf("cart_delete.do") != -1) {
			cartDelete(req, resp);
		} else if (uri.indexOf("cart.do") != -1) {
			cartList(req, resp);
		}
	}

	private void orderSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/views/order/orderResult.jsp";
		forward(req, resp, path);
	}

	private void order(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/views/order/order.jsp";
		forward(req, resp, path);
	}

	private void cartAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	private void cartDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		OrderDAO dao=new OrderDAO();
		try {
			dao.deleteCart(Integer.parseInt(req.getParameter("num")), info.getUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String cp=req.getContextPath();
		resp.sendRedirect(cp+"/order/cart.do");
		
	}

	private void cartList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		OrderDAO dao=new OrderDAO();
		List<CartDTO> list=null;
		list=dao.listBoard(info.getUserId());
		
		int dataCount;
		dataCount = dao.dataCount(info.getUserId());
		 
		req.setAttribute("cartlist", list);
		req.setAttribute("dataCount", dataCount);

		String path = "/WEB-INF/views/order/cart.jsp";
		forward(req, resp, path);
	}

}
