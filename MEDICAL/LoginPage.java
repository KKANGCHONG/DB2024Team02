package MEDICAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage extends JFrame implements ActionListener {
    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginButton;
    private String UserId; // 로그인 성공 시 사용자 ID를 저장할 변수
    private int currentUserId;

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
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(10, 10, 10, 10);
        mainGbc.fill = GridBagConstraints.HORIZONTAL;

        // 제목 추가
        JLabel systemName = new JLabel("DB2024Team02's Database");
        systemName.setFont(new Font("Arial", Font.BOLD, 27));
        systemName.setForeground(Color.black);
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.gridwidth = 2;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(systemName, mainGbc);

        // 아이콘 추가
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\dci06\\Pictures\\Screenshots\\hospital_logo.png");
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel hospitalImage = new JLabel(scaledIcon);
        mainGbc.gridy = 1;
        mainPanel.add(hospitalImage, mainGbc);

        // ID 라벨 및 입력 필드 추가
        JLabel idLabel = new JLabel("ID");
        idLabel.setFont(new Font("Arial", Font.BOLD, 25));
        idLabel.setForeground(Color.black);
        mainGbc.gridy = 2;
        mainGbc.gridx = 0;
        mainGbc.gridwidth = 1;
        mainGbc.insets = new Insets(10, 10, 0, 5); // reduce right inset
        mainGbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(idLabel, mainGbc);

        idField = new JTextField(15);
        idField.setBackground(new Color(230, 230, 230));
        mainGbc.gridx = 1;
        mainGbc.insets = new Insets(10, 5, 0, 10); // reduce left inset
        mainGbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(idField, mainGbc);

        // PW 라벨 및 입력 필드 추가
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setFont(new Font("Arial", Font.BOLD, 25));
        pwLabel.setForeground(Color.black);
        mainGbc.gridy = 3;
        mainGbc.gridx = 0;
        mainGbc.insets = new Insets(10, 10, 0, 5); // reduce right inset
        mainGbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(pwLabel, mainGbc);

        pwField = new JPasswordField(15);
        pwField.setBackground(new Color(230, 230, 230));
        mainGbc.gridx = 1;
        mainGbc.insets = new Insets(10, 5, 0, 10); // reduce left inset
        mainGbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(pwField, mainGbc);

        // 로그인 버튼 추가
        loginButton = new JButton("로그인");
        loginButton.addActionListener(this);
        mainGbc.gridy = 4;
        mainGbc.gridx = 0;
        mainGbc.gridwidth = 2;
        mainGbc.insets = new Insets(20, 10, 10, 10);
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, mainGbc);

        // 컨텐트팬에 패널 붙이기
        contentPane.add(mainPanel);

        setSize(800, 500);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String id = idField.getText();
            String pass = new String(pwField.getPassword());
            try {
                String sql_query = String.format("SELECT Password FROM DB2024_LoginDB WHERE ID = '%s' AND Password ='%s'",
                        id, pass);
                Connection conn = getConnection();
                Statement stmt = conn.createStatement();

                ResultSet rset = stmt.executeQuery(sql_query);

                if (rset.next() && pass.equals(rset.getString(1))) {
                    UserId = id; // 로그인에 성공시, 사용자 ID 값 저장
                    currentUserId = Integer.parseInt(UserId); //ID 값 int로 변환
                    JOptionPane.showMessageDialog(this, "Login Success", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
                   
                    new MainPage(currentUserId); // 새로운 MainPage 창을 열기
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login Failed", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                }

                stmt.close();
                conn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Login Failed", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
   
    public int getCurrentUserId() { // 사용자 ID 값을 반환하는 getter 메소드
        return currentUserId;
    }

    //DB와 연결
    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/DB2024Team02";
        String username = "root";
        String password = "root";
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        new LoginPage();
    }
}