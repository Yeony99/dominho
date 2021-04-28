package com.dominho.admin_Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();
	
	public int dataCount() { // 데이터 개수
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql ="SELECT NVL(COUNT(*),0) FROM member";
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
	
	public int dataCount(String condition, String keyword) { // 검색 조건에 따른 전체 개수
		int result = 0;
		PreparedStatement pstmt= null;
		String  sql;
		ResultSet rs = null;
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM member WHERE INSTR(\" +condition+ \",?) >=1";
			
			pstmt =conn.prepareStatement(sql);
			pstmt.setString(1, keyword);

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
	
	public List<MemberDTO> listMember(int offset, int rows){
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT userId, userName, tel, email FROM member ");
			sb.append(" ORDER BY userName ASC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				
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
	
	public List<MemberDTO> listMember (int offset, int rows, String condition, String keyword){
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt = null;
		
		try {
			sb.append("SELECT userId, userName, tel, email ");
			sb.append(" FROM member WHERE INSTR(\"+condition+\", ?) >= 1");
			sb.append(" ORDER BY userName DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MemberDTO dto = new MemberDTO();
				
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				
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
	
	public MemberDTO readMember(String userId) {
		MemberDTO dto = new MemberDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		sql="SELECT userName, userId, userPwd, tel, email, birth, zip, addr1, addr2 joinedDate FROM member WHERE userId=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setUserName(rs.getString("userName"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setTel(rs.getString("tel"));
				dto.setEmail(rs.getString("email"));
				dto.setBirth(rs.getString("birth"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("addr1"));
				dto.setAddr2(rs.getString("addr2"));
				dto.setJoinedDate(rs.getString("joinedDate"));
				
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
	
	public int deleteMember(String userId) throws SQLException{ // 탈퇴
		int result = 0;
		PreparedStatement pstmt= null;
		String sql;
		
		try {
			sql = "DELETE FROM member WHERE  userId= ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
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
	
	public int deleteMemberList(String []userIds) throws SQLException{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql ="DELETE FROM member WHERE userId IN (";
			for(int i=0; i<userIds.length; i++) {
				sql+="?,";
			}
			sql = sql.substring(0, sql.length()-1)+ ")";
			pstmt = conn.prepareStatement(sql);
			
			for(int i=0; i<userIds.length; i++) {
				pstmt.setString(i+1, userIds[i]);
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
}
