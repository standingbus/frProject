package connect;

import java.util.List;
import java.util.Scanner;

public class Product extends productDao {
	public static void main(String[] args) {
		int menu = 0;
		while (true) {
			System.out.println("1.로그인 2.회원가입");
			int num = Integer.parseInt(scn.nextLine());
			if (num == 1) {
				pda.loginCheck();
				boolean run = true;
//				List<ProductVO> list = pda.list();
//				for(int i = 0; i < list.size(); i++ ) {
					if(prd.getVariety().equals("r")) {
					
					while (run) {
						System.out.println("1.상품등록 2.상품목록 3.나의상품 4.상품수정 5.상품삭제 6. 입/출고 관리 7. 로그아웃");
						menu = Integer.parseInt(scn.nextLine());
						if (menu == 1) {
	
							register();
						} else if (menu == 2) {
							inventory();
						} else if (menu == 3) {
							myList();
						} else if (menu == 4) {
							update();
	
						} else if (menu == 5) {
							delecte();
						} else if (menu == 6) {
							inOutput();
						} else if (menu == 7) {
							run = false;
							System.out.println("로그아웃 되셨습니다.");
						}
					}
//						}
					} //end of while
					
				
			} else if (num == 2) {
				member();
			}

		}

	}

}
