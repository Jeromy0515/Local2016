package frame;

import javax.swing.JLabel;

public class RegistFrame extends BaseFrame{
	
	private RegistUpdateFrame ruf;

	RegistFrame(){
		
		ruf = new RegistUpdateFrame("고객 등록","추가","닫기",e -> regAct(),new JLabel("고객코드:"),new JLabel("*고 객 명:"),new JLabel("*생년월일(YYYY-MM-DD):"),
		  new JLabel("*연 락 처:"),new JLabel("주   소:"),new JLabel("회   사:"));
		ruf.setVisible(true);
	}
	
	private void regAct() {
		if(ruf.nameTxt.getText().equals("") || ruf.birthTxt.getText().equals("") || ruf.telTxt.getText().equals("")) {
			errorMessage("필수항목(*)을 모두 입력하세요", "고객등록 에러");
		}else {
			informationMessage("고객추가가 완료되었습니다.", "메세지");
			withoutSqlResult("INSERT INTO `customer` VALUES(?,?,?,?,?,?)", ruf.codeTxt.getText(),ruf.nameTxt.getText(),ruf.birthTxt.getText(),ruf.telTxt.getText(),ruf.addressTxt.getText(),ruf.companyTxt.getText());
		}
	}
	
}
