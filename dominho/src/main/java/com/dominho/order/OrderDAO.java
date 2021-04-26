package com.dominho.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dominho.store.StoreDTO;
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
	
	public List<StoreDTO> allStore() {
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select storenum, storename, storetel, storeaddress, openinghours,closinghours,totalsales from store ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				StoreDTO dto = new StoreDTO();
				dto.setStoreNum(rs.getInt("storenum"));
				dto.setStoreName(rs.getString("storename"));
				dto.setStoreTel(rs.getString("storetel"));
				dto.setStoreAddress(rs.getString("storeaddress"));
				dto.setOpeningHours(rs.getString("openinghours"));
				dto.setClosingHours(rs.getString("closinghours"));
				dto.setTotalSales(rs.getInt("totalsales"));
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
	public int insertOrder(String userId, int storeNum, String isDelivery, double totalPrice, String creditCard) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "insert into myorder(ordernum, userid, storenum, orderdate, isdelivery, totalprice, creditcardnum) values(myorder_seq.nextval,?,?,sysdate,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,userId );
			pstmt.setInt(2,storeNum );
			pstmt.setString(3,isDelivery );
			pstmt.setDouble(4,totalPrice );
			pstmt.setString(5,creditCard );
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {

				}
			}
		}
		return result;
	}
	public int insertOrderDetail( int menuNum, int orderPrice, int orderCount) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "insert into orderdetail(ORDERDETAILID, ORDERNUM, MENUNUM, ORDERPRICE, ORDERCOUNT) values(orderd_seq.nextval,myorder_seq.currval,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1,menuNum );
			pstmt.setInt(2,orderPrice );
			pstmt.setInt(3,orderCount );
			
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {

				}
			}
		}
		return result;
	}
	public List<AllOrderInfoDTO> allMyOrder() {
		List<AllOrderInfoDTO> list = new ArrayList<AllOrderInfoDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select m.ordernum, userid, s.storename, orderdate, isdelivery, totalprice, nvl(creditcardnum,'만나서결제') creditcardnum, menu.menuname, o.ordercount, o.orderprice from myorder m join store s on m.storenum=s.storenum join orderdetail o on m.ordernum=o.ordernum join menu on o.menunum=menu.menunum"
					+ " order by m.ordernum ";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AllOrderInfoDTO dto = new AllOrderInfoDTO();
				dto.setOrderNum(rs.getInt("ordernum"));
				dto.setUserId(rs.getString("userId"));
				dto.setStoreName(rs.getString("storeName"));
				dto.setOrderDate(rs.getString("orderDate"));
				dto.setIsDelivery(rs.getString("isDelivery"));
				dto.setTotalPrice(rs.getInt("totalPrice"));
				dto.setCardNum(rs.getString("creditcardnum"));
				dto.setMenuName(rs.getString("menuname"));
				dto.setOrderCount(rs.getInt("orderCount"));
				dto.setOrderPrice(rs.getInt("orderprice"));
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
}
