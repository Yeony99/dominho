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
	
	public int insertReview(ReviewDTO rdto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO review(reviewNum, orderNum, content, created) "
					+ "VALUES(review_seq.NEXTVAL, ?, ?, SYSDATE) ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, rdto.getOrderNum());
			pstmt.setString(2, rdto.getContent());
			
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
				} catch (Exception e) {
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {
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
			
			sb.append("SELECT orderNum, content, created ");
			sb.append(" FROM review ");
			sb.append(" ORDER BY reviewNum DESC ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO rdto = new ReviewDTO();
				rdto.setOrderNum(rs.getInt("orderNum"));
				rdto.setContent(rs.getString("content"));
				rdto.setCreated(rs.getString("created"));
				
				list.add(rdto);
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
	
	public int deleteReview(int reviewNum) throws SQLException {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM review WHERE reviewNum=?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, reviewNum);
			
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
