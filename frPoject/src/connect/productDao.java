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
	private String loginId;
	
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
	
	// login check. id & pass => 로그인정상.
		public boolean login(String id, String pw) {
			sql = "select * from tbl_users where user_id=? and user_pw=?";
			conn = Dao.getConnect();
			try {
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, id);
				psmt.setString(2, pw);

				rs = psmt.executeQuery();
				if (rs.next()) {
					return true; // id & pw 가 맞는 회원이 있다는 으미.
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return false;
		}
	
		public void loginCheck() {
			while (true) {
				String id = promptString("아이디를 입력하세요");
				String pw = promptString("비밀번호를 입력하세요");

				if (dao.login(id, pw)) {
					loginId = id;
					return;
				}
				System.out.println("입력정보를 확인하세요.");
			}
		}
		
		
		public boolean join(String id, String pw) {
			sql = "insert into tbl_users (user_id, user_pw) "
					+ "values(?,?) ";
			conn = Dao.getConnect();
			
			try {
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, id);
				psmt.setString(2, pw);
				
				int r = psmt.executeUpdate();
				
				if(r>0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				close();
			}
			return false;
		}
	
	
		public static void member() {
			System.out.println("사용할 아이디를 입력하세요");
			String id = scn.nextLine();
			System.out.println("사용할 비밀번호를 입력하세요");
			String pw = scn.nextLine();
			
			ProductVO prd = new ProductVO();
			
			prd.setUser_id(id);
			prd.setUser_pw(pw);
			
			if(dao.join(id, pw)) {
				System.out.println("회원가입이 완료되었습니다.!");
			} else {
				System.out.println("다시입력해주세요");
			}
		}
	
	
	
	
	
	
	
	// 등록
	public boolean input(ProductVO prd) {
		sql = "insert into tbl_product (product_nu, product_id, product_name, price, content, stock, user_id) " 
				+ "values(prd_seq.nextval,?,?,?,?,?,?) ";
		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, prd.getProduct_id());
			psmt.setString(2, prd.getProduct_name());
			psmt.setInt(3, prd.getPrice());
			psmt.setString(4, prd.getContent());
			psmt.setInt(5, prd.getStock());
			psmt.setString(6, dao.loginId);

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
		System.out.println("상품id");
		String id = scn.nextLine();
		System.out.println("상품명>");
		String name = scn.nextLine();
		System.out.println("가격>");
		int pr = Integer.parseInt(scn.nextLine());
		System.out.println("내용>");
		String cont = scn.nextLine();
		System.out.println("재고수량>");
		int stk = Integer.parseInt(scn.nextLine());

		ProductVO prd = new ProductVO();

		prd.setProduct_id(id);
		prd.setProduct_name(name);
		prd.setPrice(pr);
		prd.setContent(cont);
		prd.setStock(stk);
		prd.setUser_id(dao.loginId);
		if (dao.input(prd)) {
			System.out.println("처리성공");
		} else {
			System.out.println("처리실패");
		}
	}

	//조회
	
	public List<ProductVO> list() {
		List<ProductVO> list = new ArrayList<>();

		sql = "select * from tbl_product";
		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();

			while (rs.next()) {
				ProductVO prd = new ProductVO();
				prd.setProduct_nu(rs.getInt(1));
				prd.setProduct_id(rs.getString(2));
				prd.setProduct_name(rs.getString(3));
				prd.setPrice(rs.getInt(4));
				prd.setContent(rs.getString(5));
				prd.setStock(rs.getInt(6));
				prd.setUser_id(rs.getString(7));
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
	//상세보기
	public ProductVO search(String product_name) {
		sql = "select * from tbl_product where product_name = ? ";
		conn = Dao.getConnect();

		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, product_name);
			rs = psmt.executeQuery();
			if (rs.next()) {
				ProductVO prd = new ProductVO();
				prd.setProduct_nu(rs.getInt(1));
				prd.setProduct_id(rs.getString(2));
				prd.setProduct_name(rs.getString(3));
				prd.setPrice(rs.getInt(4));
				prd.setContent(rs.getString(5));
				prd.setStock(rs.getInt(6));
				prd.setUser_id(rs.getString(7));
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
			System.out.println(prd);
		}

	}

	// 수정
	public boolean modify(ProductVO prd) {
		sql = " update tbl_product " 
				+ "set  product_id = nvl(? , product_name),"
				+ "		price = nvl(?, price)," 
				+ " 	content = nvl(?, content),"
				+ "		stock = nvl(?, stock)," 
				+ " 	user_id = nvl(?,user_id)"					
				+ " where product_name = ?"
				;

		conn = Dao.getConnect();
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, prd.getProduct_id());
			psmt.setInt(2, prd.getPrice());
			psmt.setString(3, prd.getContent());
			psmt.setInt(4, prd.getStock());
			psmt.setString(5, prd.getUser_id());
			psmt.setString(6, prd.getProduct_name());
			
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
		prd.setProduct_name(name);
		boolean istru = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getProduct_name().equals(name) && istru) {

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
					System.out.println("1,2,3 중에 골라주세요");
				}
				if (dao.modify(prd)) {
					System.out.println("정상수정");
				} else {
					System.out.println("상품이 없습니다.");
				}
			}
		}
	}

	
	public boolean cut(ProductVO prd) {
		sql = "delete from tbl_product"
				+ " where product_name = ?"
				;
		
		conn = Dao.getConnect();
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, prd.getProduct_name());
			
			int r = psmt.executeUpdate();
			if(r>0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}
	
	public static void delecte() {
		List<ProductVO> list = dao.list();
		ProductVO prd = new ProductVO();
		System.out.println("삭제할 상품을 입력해주세요");
		String name = scn.nextLine();
		prd.setProduct_name(name);
		
		if(dao.cut(prd)) {
			System.out.println("정상수정");
		}else {
			System.out.println("상품이없습니다.");
		}
		
	}
	
	// 재고관리
		public boolean inPut(ProductVO prd) {
			sql = " update tbl_product " 
					+ "set stock = stock + ? " 
					+ "where product_name = ?";

			conn = Dao.getConnect();

			try {
				psmt = conn.prepareStatement(sql);
				psmt.setInt(1, prd.getStock());
				psmt.setString(2, prd.getProduct_name());
				
				int r = psmt.executeUpdate();
				if(r >0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return false;
		}
		
		public boolean outPut(ProductVO prd) {
			sql = " update tbl_product " 
					+ "set stock =  ? " 
					+ "where product_name = ?";

			conn = Dao.getConnect();
		
			try {
				psmt = conn.prepareStatement(sql);
				psmt.setInt(1, prd.getStock());
				psmt.setString(2, prd.getProduct_name());
				
				int r = psmt.executeUpdate();
				if(r >0) {
					return true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				close();
			}
			return false;
		}

		public static void inOutput() {
			List<ProductVO> list = dao.list();
			ProductVO prd = new ProductVO();
			System.out.println("입/출고할 상품을 입력해주세요");
			String name = scn.nextLine();
			prd.setProduct_name(name);
			boolean istru = true;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getProduct_name().equals(name) && istru) {
					System.out.println("1.입고 2.출고");
					int menu = Integer.parseInt(scn.nextLine());
					if (menu == 1) {
						System.out.println("입고 개수를 입력해주세요");
						int cnt = Integer.parseInt(scn.nextLine());
						prd.setStock(prd.getStock()+ cnt);
						if(dao.inPut(prd)) {
							System.out.println("정상수정");
						} else {
							System.out.println("상품이 없습니다.");
						}
					} else if (menu == 2) {
						System.out.println("출고 개수를 입력해주세요");
						int cnt = Integer.parseInt(scn.nextLine());
						
						if(list.get(i).getStock() - cnt < 0) {
							System.out.println("ss");
							continue;
						}else {
							prd.setStock(list.get(i).getStock() - cnt );
	
						}
						if(dao.outPut(prd)) {
							System.out.println("정상수정");
						} else {
							System.out.println("상품이 없습니다.");
						}
					} else {
						System.out.println("1,2 중에 골라주세요");
						istru = false;
					}

				}
			}
		}

		// 메세지와 문자열 반환값.
		private String promptString(String msg) {
			System.out.print(msg + "> ");
			return scn.nextLine();
		}

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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	