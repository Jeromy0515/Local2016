package frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginFrame extends BaseFrame {

	JTextField tfId = createComponent(new JTextField(), 120, 20, 120, 20);
	JPasswordField tfPw = createComponent(new JPasswordField(), 120, 50, 120, 20);

	public LoginFrame() {
		super(320, 190, "로그인");
		setLayout(new BorderLayout());
		add(createLabel(new JLabel("관리자 로그인", JLabel.CENTER), new Font("맑은고딕", 1, 24)), BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(null);
		centerPanel.add(createComponent(new JLabel("이름"), 50, 20, 60, 20));
		centerPanel.add(tfId);
		centerPanel.add(createComponent(new JLabel("비밀번호"), 50, 50, 60, 20));
		centerPanel.add(tfPw);

		JPanel southPanel = new JPanel(new FlowLayout());

		southPanel.add(createButton("확인", e -> hasLogin()));
		southPanel.add(createButton("종료", e -> System.exit(EXIT_ON_CLOSE)));

		add(centerPanel);
		add(southPanel, BorderLayout.SOUTH);
	}

	public static void main(String[] args) {
		new LoginFrame().setVisible(true);
	}

	private void hasLogin() { 
		try {
			if(getSqlResults("SELECT * FROM admin WHERE name=? AND passwd=?",tfId.getText(),tfPw.getText()).next()) {
				setVisible(false);
				new MenuFrame().setVisible(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
		
	}
}
