package connect;

import java.util.List;
import java.util.Scanner;

public class Product extends productDao {
	public static void main(String[] args) {

		Scanner scn = new Scanner(System.in);
		int menu = 0;

		while (true) {
			System.out.println("1.상품등록 2.상품목록 3.상품상세정보 4.상품수정 5. 입/출고 관리 6. 종료");
			menu = Integer.parseInt(scn.nextLine());
			if (menu == 1) {
				register();
			} else if (menu == 2) {
				inventory();
			} else if (menu == 3) {
				detail();
			} else if (menu == 4) {
				update();

			} else if (menu == 5) {
				inOutpud();
			} else if (menu == 6) {

			}

		}

	}

}
