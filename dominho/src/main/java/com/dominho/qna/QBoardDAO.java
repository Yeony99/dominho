package com.dominho.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class QBoardDAO {
	private Connection conn = DBConn.getConnection();

	public int insertQBoard(QBoardDTO dto, String mode) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		int seq;
		
		try {
			sql = "SELECT qna_seq.NEXTVAL FROM dual";
			pstmt = conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			seq=0;
			if(rs.next()) {
				seq = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
			rs = null;
			pstmt = null;
			
			dto.setqBoardNum(seq);
			if(mode.equals("created")) {
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);			
			} else if(mode.equals("reply")) {
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				dto.setDepth(dto.getDepth()+1);
				dto.setOrderNo(dto.getOrderNo()+1);
			}
			
			sql = "INSERT INTO qna(qboardNum, userId, subject, content, groupNum, depth, orderNo, parent, hitCount, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, SYSDATE)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getqBoardNum());
			pstmt.setString(2, dto.getUserId());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());
			
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

	public int updateOrderNo(int groupNum, int orderNo) throws SQLException {
		int result = 0;

		return result;
	}

	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM qna";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				result=rs.getInt(1);
			}
		} catch (SQLException e) {
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
		
		return result;
	}

	public int dataCount(String condition, String keyword) {
		int result = 0;

		return result;
	}

	public List<QBoardDTO> listBoard(int offset, int rows) {
		List<QBoardDTO> list=new ArrayList<QBoardDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			
			sb.append("SELECT qboardNum, q.userId, userName, ");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append(" FROM qna q");
			sb.append(" JOIN member m ON q.userId=m.userId  ");
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, offset);
			pstmt.setInt(2, rows);

			rs=pstmt.executeQuery();
			
			while (rs.next()) {
				QBoardDTO dto=new QBoardDTO();
				dto.setqBoardNum(rs.getInt("qboardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
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

	public List<QBoardDTO> listBoard(int offset, int rows, String condition, String keyword) {
		List<QBoardDTO> list = new ArrayList<QBoardDTO>();
		PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT qboardNum, q.userId, userName,  ");
			sb.append("       subject, groupNum, orderNo, depth, hitCount,  ");
			sb.append("       TO_CHAR(created, 'YYYY-MM-DD') created  ");
			sb.append(" FROM qna q  ");
			sb.append(" JOIN member m ON q.userId=m.userId  ");
			if(condition.equals("created")) {
				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
				sb.append(" WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ");
			} else if(condition.equals("all")) {
				sb.append(" WHERE INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ");
			} else {
				sb.append(" WHERE INSTR(" + condition + ", ?) >= 1  ");
			}
			
			sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
			sb.append(" OFFSET ? ROWS FETCH FIRST ? ROWS ONLY ");
            
			pstmt=conn.prepareStatement(sb.toString());
            if(condition.equals("all")) {
    			pstmt.setString(1, keyword);
    			pstmt.setString(2, keyword);
    			pstmt.setInt(3, offset);
    			pstmt.setInt(4, rows);
            } else {
    			pstmt.setString(1, keyword);
    			pstmt.setInt(2, offset);
    			pstmt.setInt(3, rows);
            }
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
                QBoardDTO dto=new QBoardDTO();
				dto.setqBoardNum(rs.getInt("qboardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
            
                list.add(dto);
            }

        } catch (SQLException e) {
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

	public QBoardDTO readBoard(int boardNum) {
		QBoardDTO dto = null;

		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT qboardNum, q.userId, userName, subject, ");
			sb.append("    content, created, hitCount, groupNum, depth, orderNo, parent  ");
			sb.append(" FROM qna q  ");
			sb.append(" JOIN member m ON q.userId=m.userId  ");
			sb.append(" WHERE qboardNum=?  ");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boardNum);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new QBoardDTO();
				dto.setqBoardNum(rs.getInt("qboardNum"));
				dto.setUserId(rs.getString("userId"));
				dto.setUserName(rs.getString("userName"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
			}
					
		} catch (SQLException e) {
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

	public QBoardDTO preReadBoard(int groupNum, int orderNo, String condition, String keyword) {
		QBoardDTO dto = null;
		
		 PreparedStatement pstmt=null;
	        ResultSet rs=null;
	        StringBuffer sb = new StringBuffer();

	        try {
	            if(keyword!=null && keyword.length() != 0) {
	                sb.append("SELECT qboardNum, subject  ");
	    			sb.append(" FROM qna q  ");
	    			sb.append(" JOIN member m ON q.userId=m.userId  ");
	    			if(condition.equals("created")) {
	    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
	    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND  ");
	    			} else if(condition.equals("all")) {
	    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  ");
	    			} else {
	    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1 ) AND  ");
	            	}
	                sb.append("         (( groupNum = ? AND orderNo < ?) OR (groupNum > ? ))  ");
	                sb.append(" ORDER BY groupNum ASC, orderNo DESC ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

	                pstmt=conn.prepareStatement(sb.toString());
	                
	                if(condition.equals("all")) {
	                    pstmt.setString(1, keyword);
	                    pstmt.setString(2, keyword);
	                    pstmt.setInt(3, groupNum);
	                    pstmt.setInt(4, orderNo);
	                    pstmt.setInt(5, groupNum);
	                } else {
	                    pstmt.setString(1, keyword);
	                    pstmt.setInt(2, groupNum);
	                    pstmt.setInt(3, orderNo);
	                    pstmt.setInt(4, groupNum);
	                }
				} else {
	                sb.append("SELECT qboardNum, subject FROM qna q JOIN member m ON q.userId=m.userId  ");                
	                sb.append(" WHERE (groupNum = ? AND orderNo < ?) OR (groupNum > ? )  ");
	                sb.append(" ORDER BY groupNum ASC, orderNo DESC  ");
	                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

	                pstmt=conn.prepareStatement(sb.toString());
	                pstmt.setInt(1, groupNum);
	                pstmt.setInt(2, orderNo);
	                pstmt.setInt(3, groupNum);
				}

	            rs=pstmt.executeQuery();

	            if(rs.next()) {
	                dto=new QBoardDTO();
	                dto.setqBoardNum(rs.getInt("qboardNum"));
	                dto.setSubject(rs.getString("subject"));
	            }

	        } catch (SQLException e) {
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

	public QBoardDTO nextReadBoard(int groupNum, int orderNo, String condition, String keyword) {
		QBoardDTO dto = null;
		
		PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(keyword!=null && keyword.length() != 0) {
                sb.append("SELECT qboardNum, subject  ");
    			sb.append(" FROM qna q  ");
    			sb.append(" JOIN member m ON q.userId=m.userId  ");
    			if(condition.equals("created")) {
    				keyword = keyword.replaceAll("(\\-|\\/|\\.)", "");
    				sb.append(" WHERE (TO_CHAR(created, 'YYYYMMDD') = ? ) AND  ");
    			} else if(condition.equals("all")) {
    				sb.append(" WHERE ( INSTR(subject, ?) >= 1 OR INSTR(content, ?) >= 1 ) AND  ");
    			} else {
    				sb.append(" WHERE (INSTR(" + condition + ", ?) >= 1) AND  ");
    			}
                sb.append("          (( groupNum = ? AND orderNo > ?) OR (groupNum < ? ))  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                if(condition.equals("all")) {
                    pstmt.setString(1, keyword);
                    pstmt.setString(2, keyword);
                    pstmt.setInt(3, groupNum);
                    pstmt.setInt(4, orderNo);
                    pstmt.setInt(5, groupNum);
                } else {
                    pstmt.setString(1, keyword);
                    pstmt.setInt(2, groupNum);
                    pstmt.setInt(3, orderNo);
                    pstmt.setInt(4, groupNum);
                }

			} else {
                sb.append("SELECT qboardNum, subject FROM qna q JOIN member m ON q.userId=m.userId  ");
                sb.append(" WHERE (groupNum = ? AND orderNo > ?) OR (groupNum < ? )  ");
                sb.append(" ORDER BY groupNum DESC, orderNo ASC  ");
                sb.append(" FETCH  FIRST  1  ROWS  ONLY");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, groupNum);
                pstmt.setInt(2, orderNo);
                pstmt.setInt(3, groupNum);
            }

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new QBoardDTO();
                dto.setqBoardNum(rs.getInt("qboardNum"));
                dto.setSubject(rs.getString("subject"));
            }

        } catch (SQLException e) {
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
	
	public int updateHitCount(int qboardNum) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		sql = "UPDATE qna SET hitCount=hitCount+1 WHERE qboardNum=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qboardNum);
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
	
	public int updateBoard(QBoardDTO dto, String userId) throws SQLException {
		int result = 0;
		
		PreparedStatement pstmt = null;
		String sql;
		
		sql="UPDATE qna SET subject=?, content=? WHERE qboardNum=? AND userId=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getSubject());
			pstmt.setString(2, dto.getContent());
			pstmt.setInt(3, dto.getqBoardNum());
			pstmt.setString(4, userId);
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
	
	public int deleteBoard(int qboardNum) throws SQLException {
		int result=0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql="DELETE FROM qna WHERE qboardNum IN (SELECT qboardNum FROM qna START WITH  qboardNum = ? CONNECT BY PRIOR qBoardNum = parent)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, qboardNum);
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
}
