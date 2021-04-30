package com.dominho.store;

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

@WebServlet("/store/*")
public class StoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		process(req, resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path)
			throws ServletException, IOException {
		// 포워딩...
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		// cp부터 끝까지 주소...
		String uri = req.getRequestURI();
		if (uri.indexOf("list.do") != -1) { // 매장 리스트
			list(req, resp);
		} else if (uri.indexOf("create.do") != -1) { // 매장 등록
			createdForm(req, resp);
		} else if (uri.indexOf("create_ok.do") != -1) { // 등록 완료
// ok_create로 해야되는거 아닌지??
			createdSubmit(req, resp);
		} else if (uri.indexOf("detail.do") != -1) { // 매장 보기
			detail(req, resp);
		} else if (uri.indexOf("update.do") != -1) { // 매장 수정
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) { // 수정 완료
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) { // 삭제
			delete(req, resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 점포 리스트
		StoreDAO dao = new StoreDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();

		// 파라미터로 넘어온 페이지 번호 ...
		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}

		// 검색
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if (condition == null) {
			condition = "all";
			keyword = "";

		}
		if (req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}

		// 데이터 개수 ..
		int dataCount;
		if (keyword.length() == 0) {
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * rows;
		if (offset < 0)
			offset = 0;

		// 게시글 가져오기..
		List<StoreDTO> list = null;
		if (keyword.length() == 0) {
			list = dao.listStore(offset, rows);
		} else {
			list = dao.listStore(offset, rows, condition, keyword);
		}

		// 리스트 글번호 만들기
		int listNum, n = 0;
		for (StoreDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}

		String query = "";
		if (keyword.length() != 0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}

		String listUrl = cp + "/store/list.do";
		String detailUrl = cp + "/store/detail.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			detailUrl += "&" + query;
		}
		String paging = util.paging(current_page, total_page, listUrl);

		// list.jsp에 넘겨줄 데이터
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("detailUrl", detailUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);

		forward(req, resp, "/WEB-INF/views/store/list.jsp");
	}

	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 점포 등록 폼
		req.setAttribute("mode", "create");
		forward(req, resp, "/WEB-INF/views/store/create.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 점포 저장
		StoreDAO dao = new StoreDAO();
		StoreDTO dto = new StoreDTO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		try {
			dto.setStoreName(req.getParameter("storeName"));
			dto.setStoreTel(req.getParameter("storeTel"));
			dto.setStoreAddress(req.getParameter("storeAddress"));
			dto.setOpeningHours(req.getParameter("openingHours"));
			dto.setClosingHours(req.getParameter("closingHours"));

			dao.insertStore(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/store/list.do");
	}

	protected void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 점포 조회
		StoreDAO dao = new StoreDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page=" + page;

		try {
			int storeNum = Integer.parseInt(req.getParameter("storeNum"));

			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if (condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");

			if (keyword.length() != 0) {
				query += "&codition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");

			}

			// 게시글 가져오기
			StoreDTO dto = dao.readStore(storeNum);
			if (dto == null) {
				resp.sendRedirect(cp + "/store/list.do?" + query);
				return;
			}
			dto.setStoreName(dto.getStoreName().replaceAll("\n", "<br>"));

			StoreDTO preReadDto = dao.preReadStore(storeNum, condition, keyword);
			StoreDTO nextReadDto = dao.nextReadStore(storeNum, condition, keyword);

			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);

			forward(req, resp, "WEB/INF/views/store/detail.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp + "/store/list.do?" + query);
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 점포 수정 폼
		StoreDAO dao = new StoreDAO();
		//HttpSession session = req.getSession();
		//SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page = req.getParameter("page");

		try {
			int storeNum = Integer.parseInt(req.getParameter("storeNum"));
			StoreDTO dto = dao.readStore(storeNum);
			
			if(dto!=null) {
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("mode", "update");
				forward(req, resp, "/WEB-INF/views/store/create.jsp");
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/store/list.do?page=" + page);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 점포 수정 완료
		StoreDAO dao = new StoreDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String page = req.getParameter("page");

		try {
			StoreDTO dto = new StoreDTO();
			dto.setStoreNum(Integer.parseInt(req.getParameter("storeNum")));
			dto.setStoreName(req.getParameter("storeName"));
			dto.setStoreTel(req.getParameter("storeTel"));
			dto.setStoreAddress(req.getParameter("storeAddress"));
			dto.setOpeningHours(req.getParameter("openingHours"));
			dto.setClosingHours(req.getParameter("closingHours"));
			dto.setTotalSales(0);

			dao.updateStore(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp + "/store/list.do?page=" + page);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 점포 삭제
		StoreDAO dao = new StoreDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int storenum = Integer.parseInt(req.getParameter("storeNum"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition== null) {
				condition = "all";
				keyword= "";
			}
			
			keyword = URLDecoder.decode(keyword, "UTF-8");
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="
				+ URLDecoder.decode(keyword, "utf-8");
			}
			dao.deleteStore(storenum, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/store/list.do?"+query);
	}
	
	

}
