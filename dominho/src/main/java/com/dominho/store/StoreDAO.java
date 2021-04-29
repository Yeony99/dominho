package com.dominho.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class StoreDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertStore(StoreDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		try {
			sql = "INSERT INTO store(storeNum, storeName, storeTel, storeAddress, openingHours, closingHours, totalSales) "
				+ " VALUES(store_seq.NEXTVAL, ?, ?, ?, ?, ?, 0)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getStoreName());
			pstmt.setString(2, dto.getStoreTel());
			pstmt.setString(3, dto.getStoreAddress());
			pstmt.setString(4, dto.getOpeningHours());
			pstmt.setString(5, dto.getClosingHours());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(pstmt!=null) {
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
			sql = "SELECT COUNT(*) FROM store";
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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

	public List<StoreDTO> listStore(int offset, int rows){
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT storeNum, storeName, storeTel, storeAddress, openingHours, closingHours, totalSales "
					+ "  FROM  store "
					+ "  ORDER BY storeNum DESC "
					+ "  OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, offset);
				pstmt.setInt(2, rows);
				
				rs = pstmt.executeQuery();
				while(rs.next()) {
					StoreDTO dto = new StoreDTO();
					
					dto.setStoreNum(rs.getInt("storeNum"));
					dto.setStoreName(rs.getString("storeName"));
					dto.setStoreTel(rs.getString("storeTel"));
					dto.setStoreAddress(rs.getString("storeAddress"));
					dto.setOpeningHours(rs.getString("openingHours"));
					dto.setClosingHours(rs.getString("closingHours"));
					dto.setTotalSales(rs.getInt("totalSales"));
					list.add(dto); // ArrayList에 저장
				}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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

	//  검색에서 검색된 전체 데이터 개수 구하기
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			if(condition.equalsIgnoreCase("create")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql = "SELECT COUNT(*) FROM store WHERE TO_CHAR(create, 'YYYYMMDD') = ?";
	
			}else if(condition.equalsIgnoreCase("name")) {
				sql = "SELECT COUNT(*) FROM store WHERE INSTR(name, ?) = 1";
			}else {
				sql = "SELECT COUNT(*) FROM store WHERE INSTR("+condition+", ?) >= 1";
			}
			pstmt = conn.prepareStatement(sql);
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
				// TODO: handle exception
			}
		}
	}
		return result;
	}

	

	 //검색된 리스트..
	public List<StoreDTO> listStore(int offset, int rows, String condition, String keyword){
		List<StoreDTO> list = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT storeName, storeTel, storeAddress, openingHours, closingHours, totalSales "
					+ "      TO_CHAR(create, 'YYYY-MM-DD') create "
					+ "  FROM  store ";
			
			if(condition.equalsIgnoreCase("create")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sql += "  WHERE TO_CHAR(create, 'YYYYMMDD') = ? ";
			} else if(condition.equalsIgnoreCase("name")) {
				sql += "  WHERE INSTR(name, ?) = 1 ";
			} else {
				sql += "  WHERE INSTR("+condition+", ?) >= 1";
			}
			sql += "  ORDER BY num DESC "
				 + "  OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				StoreDTO dto = new StoreDTO();
				
				dto.setStoreNum(rs.getInt("storeNum"));
				dto.setStoreName(rs.getString("storeName"));
				dto.setStoreTel(rs.getString("storeTel"));
				dto.setStoreAddress(rs.getString("storeAddress"));
				dto.setOpeningHours(rs.getString("openingHours"));
				dto.setClosingHours(rs.getString("closingHours"));
				
				list.add(dto); // ArrayList에 저장...
			}	
		} catch (Exception e) {
			e.printStackTrace();
	} finally {
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
	
	public StoreDTO readStore(int num) {
		StoreDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT storeNum, storeName, storeTel, storeAddress, openingHours, closingHours, totalSales"
				+ " FROM store b"
				+ " JOIN store1 s ON b.storeName = s.storeName "
				+ " WHERE storeNum = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				dto = new StoreDTO();
				dto.setStoreNum(rs.getInt("storeNum"));
				dto.setStoreName(rs.getString("storeName"));
				dto.setStoreTel(rs.getString("storeTel"));
				dto.setStoreAddress(rs.getString("storeAddress"));
				dto.setOpeningHours(rs.getString("openingHours"));
				dto.setClosingHours(rs.getString("closingHours"));
				dto.setTotalSales(rs.getInt("totalSales"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		return dto;
	}
	
	// 이전 점포 조회
	public StoreDTO preReadStore(int num, String condition, String keyword) {
		StoreDTO dto = null;
		
	    PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuilder sb = new StringBuilder();

        try {
        	if(keyword.length() != 0) {
                sb.append("SELECT storeNum, storeName FROM store  ");
                if(condition.equals("name")) {
                    sb.append(" WHERE ( INSTR(name, ?) = 1)  ");
                } else if(condition.equals("create")) {
                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
                    sb.append(" WHERE (TO_CHAR(create, 'YYYYMMDD') = ?) ");
                } else {
                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
                }
                sb.append("            AND (num > ? ) ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, keyword);
               	pstmt.setInt(2, num);
        	}else {
                sb.append("SELECT storeNum, storeName FROM store ");
                sb.append(" WHERE storeNum > ? ");
                sb.append(" ORDER BY num ASC ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, num);
            }
        	
            rs=pstmt.executeQuery();

        	if(rs.next()) {
        		dto=new StoreDTO();
        		dto.setStoreNum(rs.getInt("storeNum"));
        	    dto.setStoreName(rs.getString("storeName"));
        	         
        	}
		} catch (SQLException e) {
			e.printStackTrace();
	}finally {
		if(rs!=null) {
			try	{
				rs.close();
			}catch (SQLException e2){
			}
		}
		if(pstmt!=null) {
			try	{
				pstmt.close();
			}catch (SQLException e2){
			}
		}
	}
	return dto;
	}
	
	// 다음 점포 조회
	  public StoreDTO nextReadStore(int num, String condition, String keyword) {
		  StoreDTO dto=null;

	        PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuilder sb = new StringBuilder();

	        try {
	        	if(keyword.length() != 0) {
	                sb.append("SELECT storeNum, storeName FROM store ");
	                if(condition.equals("name")) {
	                    sb.append(" WHERE ( INSTR(name, ?) = 1) ");
	                } else if(condition.equals("create")) {
	                	keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
	                    sb.append(" WHERE (TO_CHAR(create, 'YYYYMMDD') = ?) ");
	                } else {
	                    sb.append(" WHERE ( INSTR("+condition+", ?) > 0) ");
	                }
	                sb.append("          AND (storeNum < ? ) ");
	                sb.append(" ORDER BY num DESC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setString(1, keyword);
	               	pstmt.setInt(2, num);
	            } else {
	                sb.append("SELECT num, subject FROM store ");
	                sb.append(" WHERE num < ? ");
	                sb.append(" ORDER BY num DESC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY ");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, num);
	            }

	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new StoreDTO();
	                dto.setStoreNum(rs.getInt("storeNum"));
	                dto.setStoreName(rs.getString("storeName"));
	            }
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if(rs!=null) {
					try	{
						rs.close();
					}catch (SQLException e2){
					}
				}
				if(pstmt!=null) {
					try	{
						pstmt.close();
					}catch (SQLException e2){
					}
				}
			}

	        return dto;
	    }
/*
	  public int updateStore(StoreDTO dto) throws SQLException {
	    	int result = 0;
	    	PreparedStatement pstmt = null;
	    	String sql;
	    	
	    	try {
				sql = "UPDATE store SET storeNum=?, storeName=?, storeTel=?, storeAddress=?, "
						+ "openingHours=?, closingHours=?, totalSales WHERE num=?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, dto.getStoreNum());
				pstmt.setString(2, dto.getStoreName());
				pstmt.setString(3, dto.getStoreTel());
				pstmt.setString(4, dto.getStoreAddress());
				pstmt.setString(5, dto.getOpeningHours());
				pstmt.setString(6, dto.getClosingHours());				
				
				result = pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(pstmt!=null) {
					try {
						pstmt.close();
					} catch (Exception e2) {
					}
				}
			}
	    	
	    	return result;
	    }

/* 
	  public int deleteStore(int num) throws SQLException {
	    	int result = 0;
	    	PreparedStatement pstmt = null;
	    	String sql;
	    	
	    	try {
				sql = "DELETE FROM store WHERE num = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				result=pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;
			} finally {
				if(pstmt!=null) {
					try {
						pstmt.close();
					} catch (Exception e2) {
					}
				}
			}
	    	
	    	return result;
	    }
*/
}