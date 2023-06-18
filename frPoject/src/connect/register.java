package connect;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class register extends ProductVO  {
	static Scanner scn = new Scanner(System.in);
	static productDao dao = new productDao();
	static String menuErrMsg = "잘못된 값을 입력했습니다.";
	static ProductVO prd = new ProductVO();
	static productDao pda = new productDao();
	
	Connection conn;
	PreparedStatement psmt;
	ResultSet rs;
	String sql;
	 void close() {
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
			sql = "select * from register where user_id=? and user_pw=?";
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
			sql = "insert into tbl_register (user_id, user_pw) "
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
		
		
		
		
		
//		
		
		
		//상세보기
		public  List<ProductVO> mine() {
			List<ProductVO> list = new ArrayList<>();
			sql ="select tbl_product.product_name 상품명, tbl_product.price 가격 ,register.name 등록자"
					+ "from  tbl_product"
					+ "    left join register on tbl_product.user_id = register.user_id"
					+ "    where register.user_id = ?"
					;
			conn = Dao.getConnect();

			try {
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, user_id);
				rs = psmt.executeQuery();
				if (rs.next()) {
					ProductVO prd = new ProductVO();
					prd.setProduct_nu(rs.getInt(1));
					prd.setProduct_id(rs.getString(2));
					prd.setProduct_name(rs.getString(3));
					prd.setPrice(rs.getInt(4));
					prd.setName(rs.getString("register.name"));
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

		public static void myList() {

			List<ProductVO> list = dao.list();
			if (list.size() == 0) {
				System.out.println("등록된 상품이 없습니다.");
			} else {
				for (int i = 0; i < list.size(); i++) {
					if(list.get(i).getUser_id().equals(pda.loginId)) {
						System.out.println(prd);
						
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
