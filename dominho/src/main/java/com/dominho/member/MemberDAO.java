package com.dominho.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn = DBConn.getConnection();

	public MemberDTO readMember(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";

		try {
			sql = "SELECT userid, userpwd, username, email, TO_CHAR(birth, 'YYYY-MM-DD') birth, tel, zip, address1, address2, TO_CHAR(joinedDate, 'YYYY-MM-DD') joinedDate FROM member WHERE userid = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setUserId(rs.getString("userId"));
				dto.setUserPwd(rs.getString("userPwd"));
				dto.setUserName(rs.getString("userName"));
				dto.setEmail(rs.getString("email"));
				dto.setBirth(rs.getString("birth"));
				dto.setTel(rs.getString("tel"));
				dto.setZip(rs.getString("zip"));
				dto.setAddr1(rs.getString("address1"));
				dto.setAddr2(rs.getString("address2"));
				dto.setJoinedDate(rs.getString("joinedDate"));
				

			}
		} catch (SQLException e) {
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

		return dto;
	}

	public int insertMember(MemberDTO dto) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "INSERT INTO member(userid, userpwd, username, email, birth, tel, zip, address1, address2, joinedDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getUserName());
			pstmt.setString(4, dto.getEmail());
			pstmt.setString(5, dto.getBirth());
			pstmt.setString(6, dto.getTel());
			pstmt.setString(7, dto.getZip());
			pstmt.setString(8, dto.getAddr1());
			pstmt.setString(9, dto.getAddr2());
			
			result += pstmt.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e2) {
			}
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
			}
		}

		return result;
	}

	public int updateMember(MemberDTO dto) throws SQLException {
		int result = 0;

		return result;
	}

	public int deleteMember(String userId) throws SQLException {
		int result = 0;

		return result;
	}

}
