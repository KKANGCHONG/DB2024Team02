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
    private String UserId; // �α��� ���� �� ����� ID�� ������ ����
    private int currentUserId;

    public LoginPage() {
        setTitle("DB2024Team02 Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new GridBagLayout()); // Use GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        // �г� ����
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.insets = new Insets(10, 10, 10, 10);
        mainGbc.fill = GridBagConstraints.HORIZONTAL;

        // ���� �߰�
        JLabel systemName = new JLabel("DB2024Team02's Database");
        systemName.setFont(new Font("Arial", Font.BOLD, 27));
        systemName.setForeground(Color.black);
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.gridwidth = 2;
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(systemName, mainGbc);

        // ������ �߰�
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\dci06\\Pictures\\Screenshots\\hospital_logo.png");
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel hospitalImage = new JLabel(scaledIcon);
        mainGbc.gridy = 1;
        mainPanel.add(hospitalImage, mainGbc);

        // ID �� �� �Է� �ʵ� �߰�
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

        // PW �� �� �Է� �ʵ� �߰�
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

        // �α��� ��ư �߰�
        loginButton = new JButton("�α���");
        loginButton.addActionListener(this);
        mainGbc.gridy = 4;
        mainGbc.gridx = 0;
        mainGbc.gridwidth = 2;
        mainGbc.insets = new Insets(20, 10, 10, 10);
        mainGbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, mainGbc);

        // ����Ʈ�ҿ� �г� ���̱�
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
                    UserId = id; // �α��ο� ������, ����� ID �� ����
                    currentUserId = Integer.parseInt(UserId); //ID �� int�� ��ȯ
                    JOptionPane.showMessageDialog(this, "Login Success", "�α��� ����", JOptionPane.INFORMATION_MESSAGE);
                   
                    new MainPage(currentUserId); // ���ο� MainPage â�� ����
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Login Failed", "�α��� ����", JOptionPane.ERROR_MESSAGE);
                }

                stmt.close();
                conn.close();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Login Failed", "�α��� ����", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
   
    public int getCurrentUserId() { // ����� ID ���� ��ȯ�ϴ� getter �޼ҵ�
        return currentUserId;
    }

    //DB�� ����
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