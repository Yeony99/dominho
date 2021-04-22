package com.dominho.order;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.MyServlet;

@WebServlet("/order/*")
public class OrderServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String uri = req.getRequestURI();

		if (uri.indexOf("order_ok") != -1) {
			orderSubmit(req, resp);
		} else if (uri.indexOf("order") != -1) {
			order(req, resp);
		} else if (uri.indexOf("cart_add") != -1) {
			cartAdd(req, resp);
		} else if (uri.indexOf("cart_delete") != -1) {
			cartDelete(req, resp);
		} else if (uri.indexOf("cart") != -1) {
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
		String path = "/WEB-INF/views/order/cart.jsp";
		forward(req, resp, path);
	}

	private void cartList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/views/order/cart.jsp";
		forward(req, resp, path);
	}

}
