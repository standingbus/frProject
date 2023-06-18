package connect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
	 int product_nu;
	 String product_id;
	 String product_name;
	 int	price;
	 String content;
	 int stock = 0;
	 String user_id;
	 String user_pw;
	 String loginId;
	 String name;
	 String tel;
	
	
	
	@Override
	public String toString() {
		return  "상품번호 = " + product_nu +  "상품아이디 = " + product_id + ", 상품명 = " + product_name + ", 가격 = " + price + ", 내용 = "
				+ content + ", 재고 = " + stock + ", 사용자명 = " + user_id ;
	}
	
	
	

	
	
	
	
}
