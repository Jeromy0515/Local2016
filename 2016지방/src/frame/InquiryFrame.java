package frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class InquiryFrame extends BaseFrame {
	private JTextField nameField = new  JTextField(12);
	private JTable table;
	private DefaultTableModel model;
	private Object getTableData[] = new Object[6];
	private RegistUpdateFrame ruf;
	
	InquiryFrame(){
		super(900, 800, "고객 조회");
		setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		model = new DefaultTableModel(null,"code,name,birth,tel,address,company".split(","));
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		viewAll();
		table.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {
			}
			@Override
			public void mouseExited(MouseEvent e) {
			}
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int row  = table.getSelectedRow();
				for(int i =0;i<table.getColumnCount();i++) {
					getTableData[i] = table.getModel().getValueAt(row, i);
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		panel.add(new JLabel("성명"));
		panel.add(nameField);
		panel.add(createButton("조회", e->lookUp()));
		
		panel.add(createButton("전체보기", e->viewAll()));
		
		panel.add(createButton("수정", e->update()));
		
		panel.add(createButton("삭제", e->delete()));
		
		panel.add(createButton("닫기", e->setVisible(false)));
			
			add(panel,BorderLayout.NORTH);
			add(scrollPane,BorderLayout.CENTER);
		
	}
	private void delete() {
		int check = ok_cancelDialog(getTableData[1]+"님을 정말 삭제하시겠습니까?","고객정보 삭제");
		if(check == 0) {
			withoutSqlResult("delete from customer where name=?", getTableData[1]);
			model.setNumRows(0);
			viewAll();
		}
	}
	
	private void update() {
		ruf = new RegistUpdateFrame("고객 수정","수정","취소",e->updateAct(),new JLabel("고객코드:"),new JLabel("고 객 명:"),new JLabel("생년월일:"),
				new JLabel("연 락 처:"),new JLabel("주   소:"),new JLabel("회 사 명:"));
		ruf.setVisible(true);
		ruf.codeTxt.setText((String)getTableData[0]);
		ruf.nameTxt.setText((String)getTableData[1]);
		ruf.birthTxt.setText((String)getTableData[2]);
		ruf.telTxt.setText((String)getTableData[3]);
		ruf.addressTxt.setText((String)getTableData[4]);
		ruf.companyTxt.setText((String)getTableData[5]);
		
		ruf.nameTxt.setEnabled(false);
		
		ruf.setVisible(true);

	}
	
	private void updateAct() {
		if(ruf.birthTxt.getText().equals("") || ruf.telTxt.getText().equals("")) {
			errorMessage("입력을 확인해주세요", "고객수정 에러");
		}else {
			informationMessage("고객수정이 완료되었습니다.", "메시지");
			withoutSqlResult("UPDATE `customer` SET code=?,name=?,birth=?,tel=?,address=?,company=? WHERE code=?",
					ruf.codeTxt.getText(),ruf.nameTxt.getText(),ruf.birthTxt.getText(),ruf.telTxt.getText(),
					ruf.addressTxt.getText(),ruf.companyTxt.getText(),getTableData[0]);
			viewAll();
		}

	}
	
	private void viewAll() {
		model.setNumRows(0);
		try (ResultSet rs = getSqlResults("SELECT * FROM customer;")){
			while(rs.next()) {
				model = setTable(model,rs, "code,name,birth,tel,address,company".split(","));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
		
	}
	
	private void lookUp() {
		model.setNumRows(0);
		try (ResultSet rs = getSqlResults("SELECT * FROM customer WHERE name LIKE ?", nameField.getText()+"%")){
			while(rs.next()) {
				model = setTable(model,rs, "code,name,birth,tel,address,company".split(","));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
	}
	
	
	
}
