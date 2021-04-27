package com.dominho.order;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dominho.member.SessionInfo;
import com.dominho.menu.MenuDAO;
import com.dominho.menu.MenuDTO;
import com.dominho.store.StoreDTO;
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
		} else if (uri.indexOf("orderComplete.do") != -1) {
			orderComplete(req, resp);
		}
	}

	private void orderSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		OrderDAO mdao = new OrderDAO();
		double totalPrice = 0;
		String creditCard = req.getParameter("cardNum");
		String isDelivery = req.getParameter("customRadioInline1");
		String tel = req.getParameter("tel");
		String address = req.getParameter("zip");
		address += req.getParameter("addr1");
		address += req.getParameter("addr2");
		String request = req.getParameter("requests");
		int storeNum;
		System.out.println("배달여부" + isDelivery);
		if (isDelivery.equals("배달")) {
			storeNum = Integer.parseInt(req.getParameter("store2"));
			totalPrice = Integer.parseInt(req.getParameter("totalPrice"));
		} else {
			storeNum = Integer.parseInt(req.getParameter("store1"));
			totalPrice = Integer.parseInt(req.getParameter("totalPrice")) * 0.8;

		}

		// 주문 생성
		try {
			mdao.insertOrder(info.getUserId(), storeNum, isDelivery, totalPrice, creditCard, tel, address, request);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 주문상세
		String[] menus = req.getParameterValues("menus");
		for (String m : menus) {
			try {
				mdao.insertOrderDetail(Integer.parseInt(m.split(",")[0]), Integer.parseInt(m.split(",")[2]),
						Integer.parseInt(m.split(",")[1]));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/order/orderComplete.do");

	}

	private void order(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<CartDTO> list = new ArrayList<CartDTO>();
		MenuDAO dao = new MenuDAO();
		OrderDAO mdao = new OrderDAO();
		String[] checkedMenu = req.getParameterValues("cbox");
		int totalPrice = 0;
		List<StoreDTO> allList = mdao.allStore();
		List<StoreDTO> allList2 = mdao.allStore();
		List<String> alladdress = new ArrayList<String>();// 모든 매장의 주소들
		for (StoreDTO s : allList2) {
			alladdress.add(s.getStoreAddress());
		}
		Iterator<StoreDTO> it = allList2.iterator();// ConcurrentModificationException막기위함

		for (String m : checkedMenu) {
			CartDTO cart = new CartDTO();
			cart.setMenuNum(Integer.parseInt(m.split(",")[1]));
			cart.setQuantity(Integer.parseInt(m.split(",")[2]));
			MenuDTO menu = dao.readMenu(Integer.parseInt(m.split(",")[1]));
			cart.setMenuName(menu.getMenuName());
			cart.setPrice(menu.getMenuPrice() * Integer.parseInt(m.split(",")[2]));
			list.add(cart);
			totalPrice += menu.getMenuPrice() * Integer.parseInt(m.split(",")[2]);
			// 배달가능한 매장 리스트
			List<StoreDTO> deliveryList = mdao.deliveryStore(cart.getMenuNum(), cart.getQuantity());
			// 배달가능한 매장 주소들
			List<String> storeaddress = new ArrayList<String>();
			for (StoreDTO s : deliveryList) {
				storeaddress.add(s.getStoreAddress());
			}

			for (String s : alladdress) {
				if (!storeaddress.contains(s)) {// 모든 매장중에 배달 가능한 매장이 아니면 지우기
					while (it.hasNext()) {// 값으로 넘겨야하는 배달가능한 모든 매장
						StoreDTO dto = it.next();
						if (dto.getStoreAddress().equals(s)) { // s는 배달가능한 매장에 포함 안된 매장주소
							it.remove();
							allList2.remove(dto);
						}
					}
				}
			}
		}
		req.setAttribute("allstorelist", allList);
		req.setAttribute("cartlist", list);
		req.setAttribute("totalPrice", totalPrice);
		req.setAttribute("storelist", allList2);

		String path = "/WEB-INF/views/order/order.jsp";
		forward(req, resp, path);
	}

	private void orderComplete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 모든 주문 다 보여주기 시간순으로
		OrderDAO mdao=new OrderDAO();
		List<AllOrderInfoDTO> myorders = mdao.allMyOrder();
		req.setAttribute("AllOrders", myorders);
		String path = "/WEB-INF/views/order/orderResult.jsp";
		forward(req, resp, path);

	}

	private void cartAdd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	private void cartDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		OrderDAO dao = new OrderDAO();
		try {
			dao.deleteCart(Integer.parseInt(req.getParameter("num")), info.getUserId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/order/cart.do");

	}

	private void cartList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		OrderDAO dao = new OrderDAO();
		List<CartDTO> list = null;
		list = dao.listBoard(info.getUserId());

		int dataCount;
		dataCount = dao.dataCount(info.getUserId());

		req.setAttribute("cartlist", list);
		req.setAttribute("dataCount", dataCount);

		String path = "/WEB-INF/views/order/cart.jsp";
		forward(req, resp, path);
	}

}
