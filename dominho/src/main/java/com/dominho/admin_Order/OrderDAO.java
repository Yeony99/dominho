package com.dominho.admin_Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class OrderDAO {
	private Connection conn = DBConn.getConnection();
	
	public int dataCount() { // 데이터 개수
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		String sql;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM myorder";
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
	
	public int dataCount(String condition, String keyword) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		
		try {
			sql = "SELECT NVL(COUNT(*),0) FROM myorder WHERE INSTR("+condition+",?)>=1";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			rs= pstmt.executeQuery();
			
			if(rs.next()) {
				result = rs.getInt(1);
			}
		}catch (Exception e) {
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
	
	public List<OrderDTO> listOrder(int offset, int rows){
		List<OrderDTO> list = new ArrayList<OrderDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs= null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT orderNum, orderDate, storeName, userId, isDelivery, totalPrice FROM myorder m JOIN store s ON m.storeNum=s.storeNum ");
			sb.append(" ORDER BY orderDate DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OrderDTO dto = new OrderDTO();
				
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setOrderDate(rs.getString("orderDate"));
				dto.setStoreName(rs.getString("storeName"));
				dto.setUserId(rs.getString("userId"));
				dto.setIsDelivery(rs.getString("isDelivery"));
				dto.setTotalPrice(rs.getInt("totalPrice"));
				
				list.add(dto);
			}
		}catch (SQLException e) {
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
	
	public List<OrderDTO> listOrder(int offset, int rows, String condition, String keyword){
		List<OrderDTO> list = new ArrayList<OrderDTO>();
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		PreparedStatement pstmt=null;
		
		try {
			sb.append("SELECT orderNum, orderDate, storeName, userId, isDelivery, totalPrice FROM myorder m JOIN store s ON m.storeNum=s.storeNum ");
			sb.append(" WHERE INSTR("+condition+", ?) >= 1");
			sb.append(" ORDER BY orderDate DESC OFFSET ? ROWS FETCH FIRST ? ROWS ONLY");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, keyword);
			pstmt.setInt(2, offset);
			pstmt.setInt(3, rows);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				OrderDTO dto = new OrderDTO();
				
				dto.setOrderNum(rs.getInt("orderNum"));
				dto.setOrderDate(rs.getString("orderDate"));
				dto.setStoreName(rs.getString("storeName"));
				dto.setUserId(rs.getString("userId"));
				dto.setIsDelivery(rs.getString("isDelivery"));
				dto.setTotalPrice(rs.getInt("totalPrice"));
				
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
}
