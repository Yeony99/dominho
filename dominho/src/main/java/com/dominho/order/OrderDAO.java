package com.dominho.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dominho.menu.MenuDTO;
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
			sql = "select cartId, c.menuNum, memberId, quantity, created, m.menuName, m.menuprice*quantity price, m.imageFileName from cart c join menu m on c.menuNum=m.menuNum"
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
				dto.setImageFileName(rs.getString("imageFileName"));
		
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
	public int insertOrder(String userId, int storeNum, String isDelivery, double totalPrice, String creditCard,String tel,String address,String request) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "insert into myorder(ordernum, userid, storenum, orderdate, isdelivery, totalprice, creditcardnum,tel,address,request) values(myorder_seq.nextval,?,?,sysdate,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,userId );
			pstmt.setInt(2,storeNum );
			pstmt.setString(3,isDelivery );
			pstmt.setDouble(4,totalPrice );
			pstmt.setString(5,creditCard );
			pstmt.setString(6,tel);
			pstmt.setString(7,address);
			pstmt.setString(8,request);
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
	public List<AllOrderInfoDTO> recentMyOrder(String userId) {
		List<AllOrderInfoDTO> list = new ArrayList<AllOrderInfoDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select ordernum, userId, storename, orderdate, isdelivery, totalprice, creditcardnum,request from(select m.ordernum, userid, s.storename, orderdate, isdelivery, totalprice, nvl(creditcardnum,'만나서결제') creditcardnum,request from myorder m join store s on m.storenum=s.storenum "
					+ " where userId=?"
					+ "	 order by m.ordernum desc) where rownum=1 ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
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
				dto.setRequest(rs.getString("request"));
				
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
	
	public List<OrderDetailDTO> recentMyOrderDetail() {
		List<OrderDetailDTO> list = new ArrayList<OrderDetailDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select ordernum, m.menuname, ordercount, orderprice from orderdetail o join menu m on o.menunum=m.menunum where ordernum=(select max(ordernum) from orderdetail)";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				OrderDetailDTO dto = new OrderDetailDTO();
				dto.setOrderNum(rs.getInt("ordernum"));
				dto.setMenuName(rs.getString("menuname"));
				dto.setCount(rs.getInt("ordercount"));
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
	public List<StoreDTO> deliveryStore(int menuNum, int quantity) {
		List<StoreDTO> list = new ArrayList<StoreDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			sql = "select s.storenum, storename, storetel, storeaddress, openinghours,closinghours,totalsales "
					+ " from store s join menudetail m on s.storenum=m.storenum "
					+ "	where m.menunum=? and m.count>=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			pstmt.setInt(2, quantity);
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
	public int insertCart(int menuNum, String memberId, int count) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "insert into cart(cartId, menuNum, memberId, quantity, created) values(cart_seq.nextval,?,?,?,sysdate)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			pstmt.setString(2, memberId);
			pstmt.setInt(3, count);
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
	public MenuDTO readMenu(int menuNum) {
		MenuDTO dto = new MenuDTO();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT m.menuNum, menuName, menuExplain, menuPrice, imageFileName "
					+ " FROM menu m "
					+ " WHERE m.menuNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, menuNum);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MenuDTO();				
				dto.setMenuNum(rs.getInt("menuNum"));
				dto.setMenuName(rs.getString("menuName"));
				dto.setMenuExplain(rs.getString("menuExplain"));
				dto.setMenuPrice(rs.getInt("menuPrice"));
				dto.setImageFilename(rs.getString("imageFileName"));
				
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
	public int updateStore(int storeNum, int count, int menuNum, double totalPrice) throws SQLException {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE menudetail SET count=count-? WHERE menuNum=? and storenum = ? ";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, count);
			pstmt.setInt(2, menuNum);		
			pstmt.setInt(3, storeNum);		
			result = pstmt.executeUpdate();
			pstmt.close();
			
			sql="UPDATE store set totalsales=totalsales+? where storenum=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setDouble(1, totalPrice);
			pstmt.setInt(2, storeNum);
			result+=pstmt.executeUpdate();
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
}
