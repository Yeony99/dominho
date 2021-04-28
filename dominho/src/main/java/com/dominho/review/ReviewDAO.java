package com.dominho.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReviewDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertReview(ReviewDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO review(reviewId, orderNum, content) "
					+ "VALUES (?, ?, ?) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getReviewId());
			pstmt.setString(2, dto.getContent());
			
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
			sql = "SELECT NVL(COUNT(*), 0) FROM review";
			pstmt = conn.prepareStatement(sql);
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
	
	public List<ReviewDTO> listReview(int offset, int rows){
		List<ReviewDTO> list = new ArrayList<ReviewDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			sb.append("SELECT reviewId, orderNum, content ");
			sb.append(" FROM review ");
			sb.append("OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				dto.setReviewId(rs.getString("reviewId"));
				dto.setContent(rs.getString("content"));
				dto.setOrderNum(rs.getInt("orderNum"));
				
				list.add(dto);
			}
		} catch (Exception e) {
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
	
	public int deleteGuest(int num, String userId) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM review WHERE orderNum=?";
			if(! userId.equals("admin")) {
				sql+=" AND userId = ?";
			}
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			if(! userId.equals("admin")) {
				pstmt.setString(2, userId);
			}
			
			result=pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
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
}
