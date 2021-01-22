package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


public class ManageFrame extends BaseFrame{
	private JTextField codeTxt,birthTxt,telTxt,regPriceTxt,monthPriceTxt;
	private JComboBox<String> nameBox, insuaranceBox,adminBox;
	private DefaultTableModel model;
	private JTable table;
	private Object getTableData[] = new Object[6];
	ManageFrame() {
		super(800, 700, "고객 관리");
		setLayout(new BorderLayout());
		
		JPanel gridPanel[] = new JPanel[2];
		for(int i=0;i<gridPanel.length;i++) {
			gridPanel[i] = new JPanel();
		}
		JPanel northPanel = new JPanel();
		northPanel.setLayout(null);
		northPanel.setPreferredSize(new Dimension(800,150));
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout());
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		southPanel.setSize(800,500);
		
		model = new DefaultTableModel(null,"customerCode,contractName,regPrice,regDate,monthPrice,adminName".split(","));
		table = new JTable(model);;
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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

		scrollPane.setSize(800,1000);
		JLabel label = new JLabel("< 고객 보험 계약현황 >");
		label.setHorizontalAlignment(JLabel.CENTER);
		
		gridPanel[0].setLayout(new GridLayout(4,2,-80,0));
		gridPanel[1].setLayout(new GridLayout(3,2,-80,0));
		
		nameBox = boxSetting(nameBox, "SELECT name FROM customer");
		adminBox = boxSetting(adminBox, "SELECT name FROM admin");
		insuaranceBox = boxSetting(insuaranceBox, "무배당암보험,변액연금보험,여성건강보험,연금보험,의료실비보험,종신보험".split(","));
		
		nameBox.addActionListener(e->nameBoxAct(nameBox));
		
		codeTxt = new JTextField(10);
		birthTxt = new JTextField(10);
		telTxt = new JTextField(10);
		regPriceTxt = new JTextField(10);
		monthPriceTxt = new JTextField(10);
		
		defaultSetting();
		tableSetting();
		
		gridPanel[0].add(new JLabel("고객코드:"));
		gridPanel[0].add(codeTxt);
		gridPanel[0].add(new JLabel("고 객 명:"));
		gridPanel[0].add(nameBox);
		gridPanel[0].add(new JLabel("생년월일:"));
		gridPanel[0].add(birthTxt);
		gridPanel[0].add(new JLabel("연 락 처:"));
		gridPanel[0].add(telTxt);
		gridPanel[0] = createComponent(gridPanel[0], 160, 15, 200, 100);
		
		gridPanel[1].add(new JLabel("보험상품:"));
		gridPanel[1].add(insuaranceBox);
		gridPanel[1].add(new JLabel("가입금액:"));
		gridPanel[1].add(regPriceTxt);
		gridPanel[1].add(new JLabel("월보험료:"));
		gridPanel[1].add(monthPriceTxt);
		gridPanel[1] = createComponent(gridPanel[1], 400, 15, 200, 100);

		northPanel.add(gridPanel[0]);
		northPanel.add(gridPanel[1]);
		
		centerPanel.add(new JLabel("담당자:"));
		centerPanel.add(adminBox);
		centerPanel.add(createButton("가입", e->registAct()));
		centerPanel.add(createButton("삭제", e->deleteAct()));
		centerPanel.add(createButton("파일로저장", e->saveAsFile()));
		centerPanel.add(createButton("닫기", e->setVisible(false)));

		southPanel.add(label,BorderLayout.NORTH);
		southPanel.add(scrollPane,BorderLayout.CENTER);
		
		add(northPanel,BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
	}
	
	
	private JComboBox<String> boxSetting(JComboBox<String> box,String sql){
		box = new JComboBox<>();
		try (ResultSet rs = getSqlResults(sql)){
			while(rs.next()) {
				box.addItem(rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
		
		return box;
	}
	private JComboBox<String> boxSetting(JComboBox<String> box, String...items) {
		box = new JComboBox<>();
		for(int i=0;i<items.length;i++) {
			box.addItem(items[i]);
		}
		return box;
	}
	
	private void saveAsFile() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("텍스트 파일로 저장하기");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int check = fileChooser.showSaveDialog(new JFrame());
		if(check == JFileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile(),"");
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file));
				
				try (ResultSet rs = getSqlResults("SELECT cus.name,cus.code FROM customer AS cus LEFT JOIN contract AS con ON cus.code = con.customerCode WHERE con.customerCode=?", getTableData[0] )){
					while(rs.next()) {
						bw.write("고객명  :  "+rs.getString("name")+"("+rs.getString("code")+")\n\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				bw.write("담당자명 :"+getTableData[5]+"\n\n");
				bw.write("보험상품\t가입금액\t가입일\t월보험료\n");
				try (ResultSet rs = getSqlResults("SELECT contractName, regPrice, regDate, monthPrice FROM contract WHERE customerCode=?", getTableData[0])){
					while(rs.next()) {
						bw.write(rs.getString("contractName")+"\t"+rs.getString("regPrice")+"\t"+rs.getString("regDate")+"\t"+rs.getString("monthPrice")+"\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				 bw.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(bw != null) {
						bw.close();
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	private void nameBoxAct(JComboBox<String> box) {
		try (ResultSet rs = getSqlResults("SELECT code, birth, tel FROM customer WHERE name=?", box.getSelectedItem().toString())){
			while(rs.next()) {
				codeTxt.setText(rs.getString("code"));
				birthTxt.setText(rs.getString("birth"));
				telTxt.setText(rs.getString("tel"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteAct() {
		int check = ok_cancelDialog(getTableData[0]+"("+getTableData[1]+")을 삭제하시겠습니까?", "계약정보 삭제");
		if(check == 0) {
			withoutSqlResult("DELETE FROM contract WHERE customerCode=? AND contractName=?", getTableData[0],getTableData[1]);
			tableSetting();
		}
	}
	
	private void registAct() {
		Date date = new Date(System.currentTimeMillis());
		withoutSqlResult("INSERT INTO contract VALUES (?,?,?,?,?,?)",codeTxt.getText(),nameBox.getSelectedItem().toString(),regPriceTxt.getText(),date,monthPriceTxt.getText(),adminBox.getSelectedItem().toString());
		tableSetting();
	}
	
	private void defaultSetting() {
		try (ResultSet rs = getSqlResults("SELECT code,birth,tel FROM customer LIMIT 1")){
			while(rs.next()) {
				codeTxt.setText(rs.getString("code"));
				birthTxt.setText(rs.getString("birth"));
				telTxt.setText(rs.getString("tel"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
	}
	
	private void tableSetting() {
		model.setNumRows(0);
		try(ResultSet rs = getSqlResults("SELECT * FROM contract ORDER BY regDate DESC	")){
			while(rs.next()) {
				model = setTable(model,rs,"customerCode,contractName,regPrice,regDate,monthPrice,adminName".split(","));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
	}
}
