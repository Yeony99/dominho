package com.dominho.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class BoardDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertBoard(BoardDTO dto) throws SQLException{
		int result= 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql = "SELECT board_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			seq = 0;
			if(rs.next()) {
				seq= rs.getInt(1);
			}
			dto.setPostNum(seq);
			
			rs.close();
			pstmt.close();
			rs = null;
			pstmt=null;
			
			sql ="INSERT INTO postBoard(postNum,memberid,subject,content,hitCount,created) VALUES(?,?,?,?,0,SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getPostNum());
			pstmt.setString(2, dto.getMemberid());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getSaveFiles()!=null) { 
				sql = "INSERT INTO boardFile(fileNum, num, saveFilename, originalFilename) VALUES (boardFile_seq.NEXTVAL,?,?,?)";
				pstmt=conn.prepareStatement(sql);
				
				for(int i =0; i<dto.getSaveFiles().length; i++) {
					pstmt.setInt(1, dto.getPostNum());
					pstmt.setString(2, dto.getSaveFiles()[i]);
					pstmt.setString(3, dto.getOriginalFiles()[i]);
					pstmt.executeUpdate();
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
		
	}
	
	public int dataCount() { // 데이터 개수
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM postBoard";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return result;
	}

	public int dataCount(String condition,String keyword) { // 검색(제목+내용/ 제목/ 내용)에서의 전체 개수
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs =null;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM postBoard JOIN member ON memberid = userId ";
			if(condition.equals("all")) {
				sql+=" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ";
			}else {
				sql+= " WHERE INSTR(" +condition+ ",?) >=1";
			}
			
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			if(condition.equals("all")) {
				pstmt.setString(2, keyword);
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			
		}
		return result;
	}
	
	public List<BoardDTO> listBoard(int offset, int rows){ // 게시글 리스트
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		PreparedStatement pstmt =null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT postNum, memberid, subject, hitCount, created ");
			sb.append(" FROM postBoard JOIN member ON memberid = userId ");
			sb.append(" ORDER BY postNum DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setPostNum(rs.getInt("postNum"));
				dto.setMemberid(rs.getString("memberid"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
	}
	
	public List<BoardDTO> listBoard (int offset, int rows, String condition, String keyword){
		List<BoardDTO> list = new ArrayList<BoardDTO>();
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		
		try {
			sb.append("SELECT postNum, memberid, subject, hitCount, created ");
			sb.append(" FROM postBoard JOIN member ON memberid = userId ");
			
			if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else {
				sb.append(" WHERE INSTR("+condition+", ?) >= 1");
			}
			
			sb.append(" ORDER BY postNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt=conn.prepareStatement(sb.toString());
			
			if(condition.equals("all")) {
				pstmt.setString(1, keyword);
				pstmt.setString(2, keyword);
				pstmt.setInt(3, offset);
				pstmt.setInt(4, offset);
			}else {
				pstmt.setString(1, keyword);
				pstmt.setInt(2, offset);
				pstmt.setInt(3, rows);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setPostNum(rs.getInt("postNum"));
				dto.setMemberid(rs.getString("memberid"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
		
	}
	
	
	public BoardDTO readBoard(int postNum) {
		BoardDTO dto = new BoardDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		sql = "SELECT postNum, memberid, subject, content, hitCount, created FROM postBoard JOIN member ON memberid = userId WHERE postNum=?";
		
		try {
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, postNum);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				
				dto.setPostNum(rs.getInt("postNum"));
				dto.setMemberid(rs.getString("memberid"));
				dto.setSubject(rs.getString("subject"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return dto;
	}
	
	public BoardDTO preRead(int postNum, String condition, String keyword) { // 이전 글
		BoardDTO dto = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword.length()!=0) {
				sb.append("SELECT postNum, subject FROM postBoard JOIN member ON memberid = userId ");
				if(condition.equals("all")) {
					sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?)>=1 ) ");
				}else {
					sb.append(" WHERE ( INSTR("+condition+", ?) >= 1) ");
				}
				sb.append(" AND (postNum > ?) ");
				sb.append(" ORDER BY postNum ASC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt =conn.prepareStatement(sb.toString());
				if(condition.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, postNum);
				}
				else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, postNum);
				}
			}else {
				sb.append("SELECT postNum, subject FROM postBoard WHERE postNum > ?");
				sb.append(" ORDER BY postNum ASC FETCH FIRST 1 ROWS ONLY");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, postNum);				
			}
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setPostNum(rs.getInt("postNum"));
				dto.setSubject(rs.getString("subject"));
			}
		}catch (SQLException e) {
            e.printStackTrace();
        } finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return dto;
	}
	
	public BoardDTO nextRead(int postNum, String condition, String keyword) { // 다음 글
		BoardDTO dto = null;
		
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			if(keyword.length()!=0) {
				sb.append("SELECT postNum, subject FROM postBoard JOIN member ON memberid=userId ");
				if(condition.equals("all")) {
					sb.append(" WHERE ( INSTR(subject,?) >= 1 OR INSTR(content, ?) >=1 ");					
				} else {
					sb.append(" WHERE ( INSTR("+condition+", ?) >=1 )");
				}
				sb.append(" AND (postNum <?) ");
				sb.append(" ORDER BY postNum DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				if(condition.equals("all")) {
					pstmt.setString(1, keyword);
					pstmt.setString(2, keyword);
					pstmt.setInt(3, postNum);
				}else {
					pstmt.setString(1, keyword);
					pstmt.setInt(2, postNum);
				}
			} else {
				sb.append("SELECT postNum, subject FROM postBoard WHERE postNum < ? ");
				sb.append(" ORDER BY postNum DESC ");
				sb.append(" FETCH FIRST 1 ROWS ONLY ");
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setInt(1, postNum);
			}
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				dto.setPostNum(rs.getInt("postNum"));
				dto.setSubject(rs.getString("subject"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e2) {
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return dto;
	}
	
	public int updateHitCount(int postNum) throws SQLException{ // 조회 수
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql ="UPDATE postBoard SET hitCount=hitCount+1 WHERE postNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postNum);
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public int updateBoard(BoardDTO dto) throws SQLException{ // 수정
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="UPDATE postBoard SET subject=?, content=? WHERE postNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getPostNum());
			
			result = pstmt.executeUpdate();
			pstmt.close();
			pstmt = null;
			
			if(dto.getSaveFiles()!=null){
				sql = "INSERT INTO boardFile(fileNum, num, saveFilename, originalFilename) VALUES (boardFile_seq.NEXTVAL, ?,?,?)";
				pstmt = conn.prepareStatement(sql);
				
				for(int i =0; i<dto.getSaveFiles().length; i++) {
					pstmt.setInt(1, dto.getPostNum());
					pstmt.setString(2, dto.getSaveFiles()[i]);
					pstmt.setString(3, dto.getOriginalFiles()[i]);
					
					pstmt.executeUpdate();					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int deleteBoard(int postNum) throws SQLException{ // 삭제
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM postBoard WHERE postNum = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postNum);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}
	
	public int deleteBoardList(int[] postNums) throws SQLException{
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql = "DELETE FROM postBoard WHERE postNum IN (";
			for(int i=0; i<postNums.length; i++) {
				sql+="?,";
			}
			sql = sql.substring(0, sql.length()-1)+ ")";
			
			pstmt = conn.prepareStatement(sql);
			for(int i =0; i<postNums.length; i++) {
				pstmt.setInt(i+1, postNums[i]);
			}
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return result;
	}
	
	public List<BoardDTO> listBoardFile(int postNum){
		List<BoardDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		String sql;
		
		try {
			sql = "SELECT fileNum, num, saveFilename, originalFilename FROM boardFile WHERE num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postNum);
			rs =pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setPostNum(rs.getInt("postNum"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return list;
	}
	
	public BoardDTO readBoardFile(int fileNum) {
		BoardDTO dto = null;
		String sql;
		ResultSet rs = null;
		PreparedStatement pstmt =null;
		
		try {
			sql = "SELECT fileNum, num, saveFilename, originalFilename FROM boardFile WHERE fileNum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, fileNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new BoardDTO();
				
				dto.setFileNum(rs.getInt("fileNum"));
				dto.setPostNum(rs.getInt("postNum"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}		
		return dto;
	}
	
	public int deleteBoardFile(String mode, int postNum) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			if(mode.equals("all")) {
				sql ="DELETE FROM boardFile WHERE num=?";
			} else {
				sql = "DELETE FROM boardFile WHERE fileNum=?";
			}
			pstmt= conn.prepareStatement(sql);
			pstmt.setInt(1, postNum);
			
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}	
		return result;
	}
}
