package login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class LoginPage extends JFrame implements ActionListener {
    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginButton;
    
    Connection con = null;
	Statement stmt = null;
	String url = "jdbc:mysql://localhost/3306?serverTimezone=Asia/Seoul";	//dbstudy 스키마
	String user = "root";
	String passwd = "imjiwoo68?";		//본인이 설정한 root 계정의 비밀번호를 입력하면 된다.
	
	LoginPage() {	//데이터베이스에 연결한다.
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, user, passwd);
			stmt = con.createStatement();
			System.out.println("MySQL 서버 연동 성공");
		} catch(Exception e) {
			System.out.println("MySQL 서버 연동 실패 > " + e.toString());
		}
	}

	/* 로그인 정보를 확인 */
	boolean logincheck(String _i, String _p) {
		boolean flag = false;
		
		String id = _i;
		String pw = _p;
		
		try {
			String checkingStr = "SELECT password FROM member WHERE id='" + id + "'";
			ResultSet result = stmt.executeQuery(checkingStr);
			
			int count = 0;
			while(result.next()) {
				if(pw.equals(result.getString("password"))) {
					flag = true;
					System.out.println("로그인 성공");
				}
				
				else {
					flag = false;
					System.out.println("로그인 실패");
				}
				count++;
			}
		} catch(Exception e) {
			flag = false;
			System.out.println("로그인 실패 > " + e.toString());
		}
		
		return flag;
	}

    public LoginPage() {
        setTitle("DB2024Team02 Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new GridBagLayout()); // Use GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        // 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());

        // 제목 추가
        JLabel systemName = new JLabel("DB2024Team02's Database");
        systemName.setFont(new Font("Arial", Font.BOLD, 27));
        systemName.setForeground(Color.black);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(systemName, gbc);

        // 아이콘 추가
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\dci06\\Pictures\\Screenshots\\hospital_logo.png");
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel hospitalImage = new JLabel(scaledIcon);
        gbc.gridy = 1;
        mainPanel.add(hospitalImage, gbc);

        // ID 입력 필드 추가
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Arial", Font.BOLD, 25));
        idLabel.setForeground(Color.black);
        gbc.gridy = 2;
        mainPanel.add(idLabel, gbc);

        idField = new JTextField(15);
        idField.setBackground(new Color(230, 230, 230));
        gbc.gridy = 3;
        mainPanel.add(idField, gbc);

        // PW 입력 필드 추가
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setFont(new Font("Arial", Font.BOLD, 25));
        pwLabel.setForeground(Color.black);
        gbc.gridy = 4;
        mainPanel.add(pwLabel, gbc);

        pwField = new JPasswordField(15);
        pwField.setBackground(new Color(230, 230, 230));
        gbc.gridy = 5;
        mainPanel.add(pwField, gbc);

        // 로그인 버튼 추가
        loginButton = new JButton("로그인");
        loginButton.setBounds(320, 150, 130, 35);
        loginButton.addActionListener(this);
        gbc.gridy = 6;
        mainPanel.add(loginButton, gbc);

        // 컨텐트팬에 패널 붙이기
        contentPane.add(mainPanel);

        setSize(800, 500);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Implement login logic here
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}
