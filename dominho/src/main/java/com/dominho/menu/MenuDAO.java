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
			
			sql="INSERT INTO menu(menuNum, menuName, menuExplain, menuPrice, imageFilename, menuType) "
					+ " VALUES(menu_seq.NEXTVAL, ?, ?, ?, ?, ?)";
			
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
			sql = "SELECT COUNT(*) FROM menu";
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
			
			sb.append(" SELECT menuNum, menuName, menuPrice, imageFileName, menuType ");
			sb.append(" FROM menu ");
			sb.append(" WHERE active='yes' ");
			sb.append(" ORDER BY menuNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			/*
			sb.append(" SELECT m.menuNum, menuName, menuPrice, imageFileName, menuType, md.storeNum ");
			sb.append(" FROM  menu m ");
			sb.append(" LEFT OUTER JOIN menudetail md ON m.menunum = md.menunum ");
			sb.append(" ORDER BY storeNum, m.menuNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			*/
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MenuDTO dto = new MenuDTO();
				dto.setMenuNum(rs.getInt("menuNum"));
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
			sql = "SELECT m.menuNum, m.menuName, m.menuPrice, m.imageFileName, m.menuType, c.memberId "
					+ " FROM menu m "
					+ " JOIN cart c ON m.menuNum=c.menuNum ";
			if(condition.equals("all")) {
				sql += " WHERE INSTR(menuName, ?) >= 1 OR INSTR(menuType, ?) >= 1 ";
			} else if(condition.equals("menuType")) {
				sql += " WHERE(menuType, ?) >= 1";
			} else {
				sql += " WHERE INSTR("+condition+", ?) >= 1 ";
			}
			sql += " ORDER BY menuNum DESC "
					+ " OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			
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
		//MenuDetailDTO mddto = new MenuDetailDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			/*
			sql = "SELECT m.menuNum, menuName, menuExplain, menuPrice, imageFileName, count "
					+ " FROM menu m "
					+ " JOIN menuDetail md ON m.menuNum = md.menuNum "
					+ " WHERE m.menuNum=?";
			*/
			sql = "SELECT menuNum, menuName, menuExplain, menuPrice, imageFilename "
					+ " FROM menu "
					+ " WHERE menuNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MenuDTO();
				//mddto = new MenuDetailDTO();
				
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuName(rs.getString("menuName"));
				dto.setMenuExplain(rs.getString("menuExplain"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setImageFilename(rs.getString("imageFilename"));
				//dto.setMenuNum(rs.getInt("menuNum"));
				//mddto.setCount(rs.getInt("count"));
				
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
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE menu SET menuName=?, menuExplain=?, menuPrice=?, imageFilename=?, menuType=?, active=? "
					+ " WHERE menuNum=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getMenuName());
			pstmt.setString(2, dto.getMenuExplain());
			pstmt.setInt(3, dto.getMenuPrice());
			pstmt.setString(4, dto.getImageFilename());
			pstmt.setString(5, dto.getMenuType());
			pstmt.setString(6, dto.getActive());
			pstmt.setInt(7, dto.getMenuNum());
			
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
	public int deleteMenu(int menuNum) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "DELETE FROM cart WHERE menuNum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			result = pstmt.executeUpdate();
			pstmt.close();
			
			sql = "DELETE FROM menu WHERE menuNum=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
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
					+ " FROM menu m JOIN menuDetail md "
					+ " ON m.menuNum = md.menuNum WHERE menuNum=?";
			
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
	//모든 가게 storenum가져오기
	public List<String> allStoreNum(){
		List<String> list = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {			
			
			sb.append(" SELECT storeNum ");
			sb.append(" FROM store ");
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				System.out.println(rs.getString("storeNum"));
				list.add(rs.getString("storeNum"));
				
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
	//menudetail 등록
	public int insertMenuDetail(int store, int menu) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			
			sql="INSERT INTO menudetail(menudetailId, storenum, menunum, count) "
					+ " VALUES(menudetail_seq.NEXTVAL, ?, ?, 30)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, store);
			pstmt.setInt(2, menu);
			
			
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
	//가장 최근에 등록한 메뉴의 메뉴번호가져오기
		public String recentMenu(){
			String result=null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			StringBuilder sb = new StringBuilder();
			
			try {			
				
				sb.append(" SELECT menunum from menu where menunum=(select max(menunum) from menu)");
				pstmt = conn.prepareStatement(sb.toString());
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					result=rs.getString(1);
					
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
			return result;
		}
}
