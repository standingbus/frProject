package connect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {
	private int product_nu;
	private String product_id;
	private String product_name;
	private int	price;
	private String content;
	private int stock = 0;
	private String user_id;
	private String user_pw;
	@Override
	public String toString() {
		return  "상품번호 = " + product_nu +  "상품아이디 = " + product_id + ", 상품명 = " + product_name + ", 가격 = " + price + ", 내용 = "
				+ content + ", 재고 = " + stock + ", 사용자명 = " + user_id ;
	}
	
	
	

	
	
	
	
}
