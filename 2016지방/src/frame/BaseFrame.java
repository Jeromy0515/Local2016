package frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;


public class BaseFrame extends JFrame {
	
	static Connection con = null;
	static Statement st = null;
	PreparedStatement pst = null;
	
	static {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/companysetting?serverTimezone=Asia/Seoul","user","1234");
			st = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public BaseFrame(int width, int height, String title) {
		setSize(width, height);
		setLocationRelativeTo(null);
		setTitle(title);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public BaseFrame() {
		
	}
	
	public static int ok_cancelDialog(String caption, String txt) {
		return JOptionPane.showConfirmDialog(null, caption,txt,JOptionPane.OK_CANCEL_OPTION);
	}

	public static void errorMessage(String caption, String txt) {
		JOptionPane.showInternalMessageDialog(null, caption, txt, JOptionPane.ERROR_MESSAGE);
	}

	public static void informationMessage(String caption, String txt) {
		JOptionPane.showInternalMessageDialog(null, caption, txt, JOptionPane.INFORMATION_MESSAGE);
	}

	public static JLabel createLabel(JLabel label, Font font) {
		label.setFont(font);
		return label;
	}

	public static <T extends JComponent> T createComponent(T comp, int x, int y, int width, int height) {
		comp.setBounds(x, y, width, height);
		return comp;
	}

	public static <T extends JComponent> T createComponent(T comp, int width, int height) {
		comp.setPreferredSize(new Dimension(width, height));
		return comp;
	}
	
	public static JButton createButton(String text, ActionListener actionListener) {
		JButton button = new JButton(text);
		button.addActionListener(actionListener);
		return button;
	}
	
	public static JButton createButtonWithoutMargin(String text, ActionListener actionListener) {
		JButton button = new JButton(text);
		button.addActionListener(actionListener);
		button.setMargin(new Insets(0, 0, 0, 0));
		return button;
	}
	
	public static ImageIcon getImage(String path) {
		return new ImageIcon(path);
	}
	
	public static DefaultTableModel setTable(DefaultTableModel setModel,ResultSet rs,String...strs) {
		try {
			Object rowData[] = new Object[strs.length];
			for(int i=0;i<strs.length;i++) {
				rowData[i] = rs.getString(strs[i]);
			}
			setModel.addRow(rowData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return setModel;
	}
	
	
	public ResultSet getSqlResults(String sql, Object... objects) { 
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				pst.setObject(i + 1, objects[i]);
			}
			rs = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}



	
	public ResultSet withoutResult(String sql) {
		ResultSet rs = null;
		try {
			rs = st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void stClose() {
		try {
			if(st != null)
				st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pstClose() {
		try {
			if(pst != null)
				pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void withoutSqlResult(String sql, Object... objects) { 
		try {
			pst = con.prepareStatement(sql);
			for (int i = 0; i < objects.length; i++) {
				pst.setObject(i + 1, objects[i]);
			}
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
	}

}
