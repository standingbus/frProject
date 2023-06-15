package connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class productDao {
	static Scanner scn = new Scanner(System.in);
	static productDao dao = new productDao();
	private static String menuErrMsg = "잘못된 값을 입력했습니다.";
	
	Connection conn;
	PreparedStatement psmt;
	ResultSet rs;
	String sql;

	private void close() {
		try {
			if (conn != null) {
				conn.close();
			}
			if (psmt != null) {
				psmt.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean input(ProductVO prd) {
		sql = "insert into tbl_product (productNa, price, content, stock) " + "values(?,?,?,?) ";
		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, prd.getProductNa());
			psmt.setInt(2, prd.getPrice());
			psmt.setString(3, prd.getContent());
			psmt.setInt(4, prd.getStock());

			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public static void register() {

		System.out.println("상품명>");
		String name = scn.nextLine();
		System.out.println("가격>");
		int pr = Integer.parseInt(scn.nextLine());
		System.out.println("내용>");
		String cont = scn.nextLine();
		System.out.println("재고수량>");
		int stk = Integer.parseInt(scn.nextLine());

		ProductVO prd = new ProductVO();

		prd.setProductNa(name);
		prd.setPrice(pr);
		prd.setContent(cont);
		prd.setStock(stk);

		if (dao.input(prd)) {
			System.out.println("처리성공");
		} else {
			System.out.println("처리실패");
		}
	}

	public List<ProductVO> list() {
		List<ProductVO> list = new ArrayList<>();

		sql = "select * from tbl_product";
		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				ProductVO prd = new ProductVO();
				prd.setProductNa(rs.getString("productNa"));

				list.add(prd);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void inventory() {

		List<ProductVO> list = dao.list();

		if (list.size() == 0) {
			System.out.println("등록된 상품이 없습니다.");
		} else {
			for (ProductVO prd : list) {
				System.out.println(prd.toString());
			}
		}
	}

	public ProductVO search(String productNa) {
		sql = "select * from tbl_product where productNa = ? ";
		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, productNa);
			rs = psmt.executeQuery();
			if (rs.next()) {
				ProductVO prd = new ProductVO();
				prd.setProductNa(rs.getString("productNa"));
				prd.setPrice(rs.getInt("price"));
				prd.setContent(rs.getString("content"));
				prd.setStock(rs.getInt("stock"));
				return prd;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	public static void detail() {
		System.out.println("상품명을 입력해주세요");
		String name = scn.nextLine();

		ProductVO prd = dao.search(name);
		if (prd == null) {
			System.out.println("상품이 없습니다.");
		} else {
			System.out.println(prd.toString());
		}

	}

	public boolean modify(ProductVO prd) {
		sql = " update tbl_product " + "set  price = nvl(?, price)," + " 	content = nvl(?, content),"
				+ "		stock = nvl(?, stock)" + " where productNa = ?";

		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, prd.getPrice());
			psmt.setString(2, prd.getContent());
			psmt.setInt(3, prd.getStock());
			psmt.setString(4, prd.getProductNa());

			int r = psmt.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	public static void update() {
		List<ProductVO> list = dao.list();
		ProductVO prd = new ProductVO();
		System.out.println("수정할 상품을 입력해주세요");
		String name = scn.nextLine();
		prd.setProductNa(name);
		boolean istru = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProductNa().equals(name) && istru) {
				System.out.println("무엇을 변경할까요?");
				System.out.println("1.가격 2.내용 3.재고수량");
				int menu = Integer.parseInt(scn.nextLine());
				if (menu == 1) {
					System.out.println("가격>");
					int pr = Integer.parseInt(scn.nextLine());
					prd.setPrice(pr);
				} else if (menu == 2) {
					System.out.println("내용>");
					String cont = scn.nextLine();
					prd.setContent(cont);
				} else if (menu == 3) {
					System.out.println("재고수량>");
					int stk = Integer.parseInt(scn.nextLine());
					prd.setStock(stk);
				} else {
					istru = false;
					System.out.println("제대로 고르셈");
				}

//				prd.setProductNa(nam);

				if (dao.modify(prd)) {
					System.out.println("정상수정");
				} else {
					System.out.println("상품이 없습니다.");
				}

			}
		}

	}

	public void inOut(ProductVO prd) {
		sql = " update tbl_product " + "set stock = ? + stock" + "where productNa = ?";

		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setInt(1, prd.getStock());
			psmt.setString(2, prd.getProductNa());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
	}

	public static void inOutpud() {
		List<ProductVO> list = dao.list();
		ProductVO prd = new ProductVO();
		System.out.println("입/출고할 상품을 입력해주세요");
		String name = scn.nextLine();
		prd.setProductNa(name);
		boolean istru = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProductNa().equals(name) && istru) {
				System.out.println("1.입고 2.출고");
				int menu = Integer.parseInt(scn.nextLine());
				if (menu == 1) {
					System.out.println("입고 개수를 입력해주세요");
					int cnt = Integer.parseInt(scn.nextLine());
					prd.setStock(cnt + prd.getStock());
				} else if (menu == 2) {
					System.out.println("출고 개수를 입력해주세요");
					int cnt = Integer.parseInt(scn.nextLine());
					int cnt1 = prd.getStock() - cnt;
					prd.setStock(cnt1);
				}

				
				
			}
		}
	}
	//
	// 메세지와 int반환값.
		private int promptInt(String msg) {
			int result = 0;
			while (true) {
				try {
					System.out.print(msg + "> ");
					result = scn.nextInt();
					scn.nextLine();
					break;
				} catch (Exception e) {
					System.out.println(menuErrMsg);
					scn.nextLine();
				}
			}
			return result;
		}
	
	
	
}
