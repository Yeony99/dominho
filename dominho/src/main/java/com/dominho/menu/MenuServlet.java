package com.dominho.menu;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.dominho.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/menu/*")
public class MenuServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		String cp = req.getContextPath();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "menu";
		
		if(uri.indexOf("menuList.do") != -1) {
			menuList(req, resp);
		} else if(uri.indexOf("menuCreated.do") != -1) {
			menuCreatedForm(req, resp);
		} else if(uri.indexOf("menuCreated_ok.do") != -1) {
			menuCreatedSubmit(req, resp);
		} else if(uri.indexOf("menuArticle.do") != -1) {
			menuArticle(req, resp);
		} else if(uri.indexOf("menuUpdate.do") != -1) {
			menuUpdateForm(req, resp);
		} else if(uri.indexOf("menuUpdate_ok.do") != -1) {
			menuUpdateSubmit(req, resp);
		} else if(uri.indexOf("menuDelete.do") != -1) {
			menuDelete(req, resp);
		}
	}
	
	protected void menuList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MenuDAO dao = new MenuDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "all";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		int dataCount;
		if(keyword.length() == 0) {
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		int offset = (current_page - 1) * rows;
		if(offset < 0)offset = 0;
		
		List<MenuDTO> list = null;
		if(keyword.length() == 0) {
			list = dao.listMenu(offset, rows);
		} else {
			list = dao.listMenu(offset, rows, condition, keyword);
		}
		
		String query="";
		if(keyword.length() != 0) {
			query = "condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		String listUrl = cp+"/menu/menuList.do";
		String articleUrl = cp+"/menu/menuArticle.do?page="+current_page;
		if(query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/menu/menuList.jsp");
	}
	
	protected void menuCreatedForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "menuCreated");
		forward(req, resp, "/WEB-INF/views/menu/menuCreated.jsp");
	}

	protected void menuCreatedSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		MenuDAO dao = new MenuDAO();
		
		try {
			MenuDTO dto = new MenuDTO();
			
			dto.setMenuName(req.getParameter("menuName"));
			dto.setMenuExplain(req.getParameter("menuExplain"));
			dto.setMenuPrice(Integer.parseInt(req.getParameter("menuPrice")));
			dto.setImageFilename(req.getParameter("imageFilename"));
			dto.setMenuType(req.getParameter("menuType"));
			dto.setmemberId(info.getUserId());
			
			String filename = null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) {
				filename = map.get("saveFilename");
			}
			if(filename != null) {
				dto.setImageFilename(filename);
				dao.insertMenu(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/menu/menuList.do");
	}
	
	protected void menuArticle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MenuDAO dao = new MenuDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int menuNum = Integer.parseInt(req.getParameter("menuNum"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			MenuDTO dto = dao.readMenu(menuNum);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/menu/menuList.do?"+query);
				return;
			}
			
			dto.setMenuExplain(dto.getMenuExplain().replaceAll("\n", "<br>"));
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/menu/menuArticle.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void menuUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		//HttpSession session = req.getSession();
		//SessionInfo info = (SessionInfo)session.getAttribute("member");
		MenuDAO dao = new MenuDAO();
		
		String page = req.getParameter("page");
		
		try {
			int menuNum = Integer.parseInt(req.getParameter("menuNum"));
			MenuDTO dto = dao.readMenu(menuNum);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/menu/menuList.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "menuUpdate");
			forward(req, resp, "/WEB-INF/views/menu/menuCreated.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/menu/menuList.do?page="+page);
	}
	
	protected void menuUpdateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		MenuDAO dao = new MenuDAO();
		MenuDTO dto = new MenuDTO();
		String page = req.getParameter("page");
		
		try {
			dto.setMenuNum(Integer.parseInt(req.getParameter("menuNum")));
			dto.setMenuName(req.getParameter("menuName"));
			dto.setMenuExplain(req.getParameter("menuExplain"));
			dto.setMenuPrice(Integer.parseInt(req.getParameter("menuPrice")));
			dto.setMenuType(req.getParameter("menuType"));
			//mddto.setCount(Integer.parseInt(req.getParameter("count")));
			String imageFilename = req.getParameter("imageFilename");
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) {
				FileManager.doFiledelete(pathname, imageFilename);
				String filename = map.get("saveFilename");
				dto.setImageFilename(filename);
			} else {
				dto.setImageFilename(imageFilename);
			}
			dto.setActive(req.getParameter("active"));
			
			dao.updateMenu(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/menu/menuList.do?page="+page);
	}
	
	protected void menuDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		MenuDAO dao = new MenuDAO();
		//HttpSession session = req.getSession();
		//SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			int menuNum = Integer.parseInt(req.getParameter("menuNum"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword = "";
			}
			
			keyword = URLDecoder.decode(keyword, "utf-8");
			if(keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			dao.deleteMenu(menuNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/menu/menuList.do?"+query);
	}
}
