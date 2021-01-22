package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegistUpdateFrame extends BaseFrame{
	JTextField codeTxt,nameTxt,birthTxt,telTxt,addressTxt,companyTxt;
	RegistUpdateFrame(String title,String btnTxt1,String btnTxt2,ActionListener act,JLabel...labels) {
		super(450, 330, title);
		setLayout(new BorderLayout());
		JPanel eastPanel = new JPanel();
		eastPanel.setLayout(new GridLayout(6,1,0,0));
		
		codeTxt = createComponent(new JTextField(),200,20);
		nameTxt = createComponent(new JTextField(),200,20);
		birthTxt = createComponent(new JTextField(),200,20);
		telTxt = createComponent(new JTextField(),200,20);
		addressTxt = createComponent(new JTextField(),200,20);
		companyTxt = createComponent(new JTextField(),200,20);
		
		codeTxt.setEnabled(false);
		
		birthTxt.addActionListener(e->birthAct());
		
		eastPanel.add(codeTxt);
		eastPanel.add(nameTxt);
		eastPanel.add(birthTxt);
		eastPanel.add(telTxt);
		eastPanel.add(addressTxt);
		eastPanel.add(companyTxt);	
		
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new GridLayout(6,1,0,0));
		for(int i=0;i<labels.length;i++) {
			westPanel.add(labels[i]);
		}
		
		
		JPanel southPanel = new JPanel();
		
		southPanel.add(createButton(btnTxt1, act));
		southPanel.add(createButton(btnTxt2, e-> setVisible(false)));
		
		add(eastPanel, BorderLayout.EAST);
		add(westPanel, BorderLayout.WEST);
		add(southPanel,BorderLayout.SOUTH);
		
		
	}
	
	private void birthAct() {
		String str[] = birthTxt.getText().split("-");
		int sum = 0;
		for(int i=0;i<str.length;i++) {
			sum += Integer.parseInt(str[i]);
		}
		codeTxt.setText("S"+(Calendar.getInstance().get(Calendar.YEAR)%100)+sum);
	}
}
