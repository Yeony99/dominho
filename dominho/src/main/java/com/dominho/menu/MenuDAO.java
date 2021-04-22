package com.dominho.menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class MenuDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertMenu(MenuDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="INSERT INTO menu(menuNum, menuName, menuExplain, menuPrice, imageFilename, menuType, count "
					+ "VALUES(menu_seq.NEXTVAL, ?, ?, ?, ?, ?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMenuName());
			pstmt.setString(2, dto.getMenuExplain());
			pstmt.setInt(3, dto.getMenuPrice());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setString(5, dto.getMenuType());
			pstmt.setInt(5, dto.getCount());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public int dataCount() {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*), 0) FROM menu";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
	}
	
	public List<MenuDTO> listMenu(int offset, int rows){
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT menuNum, menuName, menuPrice, imageFilename, menuType ");
			sb.append( "FROM menu");
			sb.append(" ORDER BY menuNum DESC");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				dto.setMenuNum(rs.getInt("num"));
				dto.setMenuName(rs.getString("menuName"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setMenuType(rs.getString("menuType"));
				
				list.add(dto);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return list;
	}
	
	public MenuDTO readMenu(int menuNum) {
		MenuDTO dto = new MenuDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT menuNum, menuName, menuExplain, menuPrice, menuType, count "
					+ " FROM menu "
					+ " WHERE menuNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MenuDTO();
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuExplain(rs.getString("menuExplain"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setMenuType(rs.getString("menuType"));
				dto.setMenuName(rs.getString("menuName"));
				dto.setCount(rs.getInt("count"));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return dto;
	}

}
