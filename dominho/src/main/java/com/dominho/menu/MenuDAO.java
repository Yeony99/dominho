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
	
	// 메뉴 등록
	public int insertMenu(MenuDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql="INSERT INTO menu(menuNum, menuName, menuExplain, menuPrice, imageFilename, menuType "
					+ "VALUES(menu_seq.NEXTVAL, ?, ?, ?, ?, ?, )";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMenuName());
			pstmt.setString(2, dto.getMenuExplain());
			pstmt.setInt(3, dto.getMenuPrice());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setString(5, dto.getMenuType());
			
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
	
	// 데이터 개수 구하기
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
	
	// 메뉴 리스트
	public List<MenuDTO> listMenu(int offset, int rows){
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT m.menuNum, menuName, menuPrice, imageFilename, menuType, count ");
			sb.append( "FROM menu m JOIN menuDetail md ON m.menuNum = md.menuNum ");
			sb.append(" ORDER BY menuNum DESC");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				MenuDetailDTO mddto = new MenuDetailDTO();
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuName(rs.getString("menuName"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setMenuType(rs.getString("menuType"));
				mddto.setCount(rs.getInt("count"));
				
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
	
	// 검색일 때 데이터 개수 구하기
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(condition.equalsIgnoreCase("menuName")) {
				sql = "SELECT COUNT(*) FROM menu WHERE INSTR(menuName, ?) >= 1";
			} else {
				sql = "SELECT COUNT(*) FROM menu WHERE INSTR(menuType, ?) >= 1";
			}
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
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
		
		return result;
	}
	
	// 검색일 때 메뉴 리스트
	public List<MenuDTO> listMenu(int offset, int rows, String condition, String keyword) {
		List<MenuDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m.menuNum, menuName, menuPrice, imageFilename, menuType, count "
					+ " FROM menu m "
					+ " JOIN menuDetail md ON m.menuNum = md.menuNum ";
			if(condition.equals("menuName")) {
				sql += " WHERE INSTR(menuName, ?) >= 1";
			} else if(condition.equals("menuType")) {
				sql += "WHERE(menuType, ?) >= 1";
			}
			sql += " ORDER BY menuNum DESC "
					+ "OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				MenuDetailDTO mddto = new MenuDetailDTO();
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuName(rs.getString("menuName"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setImageFilename(rs.getString("imageFilename"));
				dto.setMenuType(rs.getString("menuType"));
				mddto.setCount(rs.getInt("count"));
				
				list.add(dto);
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
		return list;
	}
	
	public MenuDTO readMenu(int menuNum) {
		MenuDTO dto = new MenuDTO();
		MenuDetailDTO mddto = new MenuDetailDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m.menuNum, menuName, menuExplain, menuPrice, imageFileName, count "
					+ " FROM menu m "
					+ " JOIN menuDetail md ON m.menuNum = md.menuNum "
					+ " WHERE menuNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MenuDTO();
				mddto = new MenuDetailDTO();
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuExplain(rs.getString("menuExplain"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setImageFilename(rs.getString("imageFileName"));
				//dto.setMenuType(rs.getString("menuType"));
				dto.setMenuName(rs.getString("menuName"));
				mddto.setCount(rs.getInt("count"));
				
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
	
	// 메뉴 수정하기
	public int updateMenu(MenuDTO dto) throws SQLException {
		MenuDetailDTO mddto = new MenuDetailDTO();
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE menu SET menuName=?, menuExplain=?, menuPrice=? "
					+ " , imageFileName=?, menuType=?, count=? "
					+ " FROM cart c JOIN menuDetail md ON c.menuNum = md.menuNum "
					+ " WHERE menuNum=?, userId = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMenuName());
			pstmt.setString(2, dto.getMenuExplain());
			pstmt.setInt(3, dto.getMenuPrice());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setString(5, dto.getMenuType());
			pstmt.setInt(6, mddto.getCount());
			pstmt.setInt(7, dto.getMenuNum());
			pstmt.setString(8, dto.getUserId());
			
			result = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
	
	// 메뉴 삭제하기
	public int deleteMenu(int menuNum, String userId) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM menu "
					+ "FROM menu m INNER JOIN cart c "
					+ "ON c.menuNum = m.menuNum "
					+ "WHERE menuNum=? AND c.userId='admin'";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			pstmt.setString(2, userId);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
	
	public int updateCount(MenuDetailDTO mddto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE menu SET md.menuNum = m.menuNum, count=? "
					+ "FROM menu m JOIN menuDetail md "
					+ "ON m.menuNum = md.menuNum WHERE menuNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, mddto.getCount());
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

}
