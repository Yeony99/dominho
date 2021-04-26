package com.dominho.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;



public class OrderDAO {
	private Connection conn = DBConn.getConnection();
	
	public int dataCount(String memberId) {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select count(*) from cart where memberid=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}

		}
		return result;
	}
	public List<CartDTO> listBoard(String userId) {
		List<CartDTO> list = new ArrayList<CartDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select cartId, c.menuNum, memberId, quantity, created, m.menuName, m.menuprice*quantity price from cart c join menu m on c.menuNum=m.menuNum"
					+ " where memberId=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CartDTO dto = new CartDTO();
				dto.setCartId(rs.getInt("cartId"));
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuName(rs.getString("menuname"));
				dto.setMemberId(rs.getString("memberId"));
				dto.setQuantity(rs.getInt("quantity"));
				dto.setCreated(rs.getString("created"));
				dto.setPrice(rs.getInt("price"));
				list.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}

		}
		return list;
	}
	public int deleteCart(int num,String userId) throws SQLException {
		int result=0;
    	PreparedStatement pstmt=null;
    	String sql;
    	try {
			sql="delete from cart where cartId=? and memberId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, userId);
			result=pstmt.executeUpdate();
		} catch (SQLException e) {
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

}
