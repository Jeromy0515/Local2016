package frame;

import java.awt.FlowLayout;

import javax.swing.JLabel;

public class MenuFrame extends BaseFrame{
	 MenuFrame() {
		 super(600, 450, "보험계약 관리화면");
		 setLayout(new FlowLayout());
		 add(createButton("고객 등록", e-> new RegistFrame()));
		 add(createButton("고객 조회", e->new InquiryFrame().setVisible(true)));
		 add(createButton("계약 관리", e->new ManageFrame().setVisible(true)));
		 add(createButton("종   료", e-> System.exit(EXIT_ON_CLOSE)));
		 add(new JLabel(getImage("src/image/img.jpg")));
	 }
}
