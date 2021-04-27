package com.dominho.admin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import com.dominho.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/admin/*")
public class BoardServlet extends MyUploadServlet {

	private static final long serialVersionUID = 1L;
	private String pathname;
	
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
		
		// 파일 저장 경로
		String root = session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"admin";
		
		// uri에 따른 구분
		if(uri.indexOf("boardList")!=-1) {
			boardList(req,resp);
		} else if(uri.indexOf("board_create")!=-1) {
			createdForm(req,resp);
		}else if(uri.indexOf("board_ok_create")!=-1) {
			createdsubmit(req,resp);
		} else if(uri.indexOf("articleBoard")!=-1) {
			board(req,resp);
		} else if(uri.indexOf("board_update")!=-1) {
			updateForm(req,resp);
		} else if(uri.indexOf("board_ok_update")!=-1) {
			updateSubmit(req,resp);
		} else if(uri.indexOf("deleteFile")!=-1) {
			deleteFile(req,resp);
		} else if(uri.indexOf("board_delete")!=-1) {
			delete(req,resp);
		} else if(uri.indexOf("download")!=-1) {
			download(req,resp);
		} else if(uri.indexOf("deleteList")!=-1) {
			deleteList(req,resp);
		}
	}

	private void boardList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 게시물 리스트
		
		BoardDAO dao = new BoardDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		// 파라미터로 넘어온 페이지 번호
		String page = req.getParameter("page");
		int current_page=1;
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		
		//검색
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		
		if(condition==null) {
			condition="all";
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
		}else {
			dataCount = dao.dataCount(condition,keyword);
		}
		
		// 전체 페이지 수
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		// 게시물 가져오기
		int offset = (current_page - 1) * rows;
		if(offset < 0) offset = 0;
		
		List<BoardDTO> list;
		if(keyword.length()==0)
			list = dao.listBoard(offset, rows);
		else
			list = dao.listBoard(offset, rows, condition, keyword);
			
		// 리스트 글 번호
		int listNum, n = 0;
		for(BoardDTO dto : list) {
			listNum = dataCount-(offset+n);
			dto.setListNum(listNum);
			n++;
		}
			
		String query = "";
		if(keyword.length()!=0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}
		
		// 페이징 처리
		String listUrl = cp+"/admin/boardList";
		String articleBoardUrl = cp+"/admin/articleBoard?page="+current_page;
		
		if(query.length()!=0) {
			listUrl += "?" + query;
			articleBoardUrl += "&" +query;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleBoardUrl", articleBoardUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/admin/boardList.jsp");
		
	}

	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 글 작성폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		// id가 admin일 경우에만 등록가능
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/admin/boardList");
			return;
		}
		
		req.setAttribute("mode", "create");
		forward(req, resp, "/WEB-INF/views/admin/boardCreate.jsp");
		
	}

	private void createdsubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 게시글 저장
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		// id가 admin일 경우에만 등록가능
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/admin/boardList");
			return;
		}
		
		try {
			BoardDTO dto = new BoardDTO();
			dto.setMemberid(info.getUserId());
			dto.setContent(req.getParameter("content"));
			dto.setSubject(req.getParameter("subject"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map!= null) {
				String []saveFiles = map.get("saveFilenames");
				String []originalFiles = map.get("originalFilenames");
				dto.setSaveFiles(saveFiles);
				dto.setOriginalFiles(originalFiles);
			}
			
			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/admin/boardList");
	}

	private void board(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 게시글 보기
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		String page =req.getParameter("page");
		if(page==null) page="1";
		
		String query = "page="+page;
		
		try {
			int postNum = Integer.parseInt(req.getParameter("postNum"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			// 조회 수
			dao.updateHitCount(postNum);
			
			// 게시글 가져오기
			BoardDTO dto = dao.readBoard(postNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/admin/boardList?"+query);
				return;
			}
						
			// 이전글 다음글
			BoardDTO preReadDTO = dao.preRead(dto.getPostNum(), condition, keyword);
			BoardDTO nextReadDTO = dao.nextRead(dto.getPostNum(), condition, keyword);
			
			// 파일
			List<BoardDTO> listFile = dao.listBoardFile(postNum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDTO);
			req.setAttribute("nextReadDto", nextReadDTO);
			req.setAttribute("listFile", listFile);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			forward(req,resp, "/WEB-INF/views/admin/board.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/admin/boardList?"+query);
	}

	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 수정 폼
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		
		try {
			int postNum = Integer.parseInt(req.getParameter("postNum"));
			
			BoardDTO dto = dao.readBoard(postNum);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/admin/boardList?page="+page);
				return;
			}
			
			// 글 작성자만 수정가능
			if(! info.getUserId().equals(dto.getMemberid())) {
				resp.sendRedirect(cp+"/admin/boardList?page="+page);
				return;
			}
			
			// 파일
			List<BoardDTO> listFile = dao.listBoardFile(postNum);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			
			req.setAttribute("mode", "update");
			forward(req, resp, "/WEB-INF/views/admin/boardCreate.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/admin/boardList?page="+page);
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 수정완료
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		BoardDTO dto = new BoardDTO();
		
		String page = req.getParameter("page");
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/admin/boardList?page="+page);
			return;
		}
		
		try {
			int postNum = Integer.parseInt(req.getParameter("postNum"));
			dto.setPostNum(postNum);
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map!=null) {
				String []saveFiles = map.get("saveFilenames");
				String []originalFiles = map.get("originalFilenames");
				dto.setSaveFiles(saveFiles);
				dto.setOriginalFiles(originalFiles);
			}
			
			dao.updateBoard(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/admin/boardList?page="+page);
	}

	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 수정 시 파일만 삭제
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		
		try {
			int postNum = Integer.parseInt(req.getParameter("postNum"));
			int fileNum = Integer.parseInt(req.getParameter("fileNum"));
			BoardDTO dto = dao.readBoard(postNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/admin/boardList?page="+page);
				return;
			}
			if(! info.getUserId().equals(dto.getMemberid())) {
				resp.sendRedirect(cp+"/admin/boardList?page="+page);
				return;
			}
			
			List<BoardDTO> listFile= dao.listBoardFile(postNum);
			
			for(BoardDTO vo: listFile) {
				if(vo.getFileNum()==fileNum) {
					FileManager.doFiledelete(pathname, vo.getSaveFilename());
					dao.deleteBoardFile("one", fileNum);
					listFile.remove(vo);
					break;
				}
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/views/admin/boardCreate.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/admin/boardList?page="+page);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { // 삭제
		HttpSession session=req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		BoardDAO dao = new BoardDAO();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String query ="page="+page;
		
		try {
			int postNum= Integer.parseInt(req.getParameter("postNum"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword,"utf-8");
			
			if(keyword.length()!=0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			BoardDTO dto = dao.readBoard(postNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/admin/boardList"+query);
				return;
			}
			
			// 관리자만 삭제 가능
			if(!info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/admin/boardList"+query);
				return;
			}
			
			// 파일 삭제
			List<BoardDTO> listFile = dao.listBoardFile(postNum);
			for(BoardDTO vo : listFile) {
				FileManager.doFiledelete(pathname,vo.getSaveFilename());
			}
			dao.deleteBoardFile("all", postNum);
			
			// 게시글 삭제
			dao.deleteBoard(postNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/admin/boardList?"+query);
	}

	private void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		BoardDAO dao = new BoardDAO();
		boolean b = false;
		
		try {
			int fileNum = Integer.parseInt(req.getParameter("fileNum"));
			
			BoardDTO dto = dao.readBoardFile(fileNum);
			if(dto!=null) {
				b = FileManager.doFiledownload(dto.getSaveFilename(), dto.getOriginalFilename(), pathname, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!b) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out = resp.getWriter();
			out.print("<script>alert('파일 다운로드에 실패했습니다.');history.back();</script>");
		}
	}

	private void deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		if(!info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/admin/boardList");
			return;
		}
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		
		try {
			if(keyword!=null && keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			String nn[] = req.getParameterValues("nums");
			int nums[] = null;
			nums = new int[nn.length];
			
			for(int i =0; i<nn.length; i++) {
				nums[i]=Integer.parseInt(nn[i]);
			}
			
			BoardDAO dao = new BoardDAO();
			
			// 파일 삭제
			for(int i=0; i<nums.length; i++) {
				List<BoardDTO> listFile = dao.listBoardFile(nums[i]);
				for(BoardDTO vo : listFile) {
					FileManager.doFiledelete(pathname, vo.getSaveFilename());
				}
				dao.deleteBoardFile("all", nums[i]);
			}
			
			// 게시글 삭제
			dao.deleteBoardList(nums);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/WEB-INF/boardList"+query);
	}

}
