package MEDICAL;

import java.awt.*; // java.awt.BorderLayout, java.awt.Color, java.awt.event.ActionListener, java.awt.event.ActionEvent ���� ����
import java.awt.event.*; // ������ �ߺ��� ActionListener�� ActionEvent�� ����
import javax.swing.*; // JFrame, JPanel, JTextArea, JButton, JTextField, JLabel, JEditorPane ���� ����
import java.sql.*; // SQL ���� ��� Ŭ������ ����
import java.io.File; // ���� ó���� ���� Ŭ����

public class MyPage {


    JFrame frame;
    private JLabel userInfoLabel;
    private String userType; // "Doctor" or "Nurse"
    private JFrame doctorpatient; // doctor�� ȯ������ �ν��Ͻ��� ������ ����
    private JFrame nursepatient; // nurse ȯ������ �ν��Ͻ��� ������ ����
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
   
    private static int currentUserId; // �ǻ� ID�� ����
    private JTextField textField;
   
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame doctorpatient = new Mypatient(currentUserId); // MainScreen Ŭ������ �ν��Ͻ��� �����ؾ� �մϴ�.
                    JFrame nursepatient = new MypatientNurse(currentUserId); // MainScreen Ŭ������ �ν��Ͻ��� �����ؾ� �մϴ�.
                    MyPage window = new MyPage(doctorpatient, nursepatient, currentUserId);
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyPage(JFrame doctorpatient, JFrame nursepatient,int currentUserId) {
        MyPage.currentUserId = currentUserId;
        this.doctorpatient = doctorpatient; //  doctor�� ȯ������ �ν��Ͻ��� ������ ����
        this.nursepatient = nursepatient; // nurse ȯ������ �ν��Ͻ��� ������ ����
        userType = determineUserType(currentUserId);
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 914, 651);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
       
        //�α����� �ǻ� ����
        JPanel Doctorinfo = new JPanel();
        Doctorinfo.setBackground(new Color(255, 255, 255));
        Doctorinfo.setBounds(0, 0, 273, 234);
        frame.getContentPane().add(Doctorinfo);
        Doctorinfo.setLayout(null);
       
        //�����ʻ���
        String imagePath = "images/" + currentUserId + ".png";
       
        // �̹��� ������ ������ �����ϴ��� Ȯ���մϴ�.
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            // ImageIcon�� ����� �̹����� �ҷ��ɴϴ�.
            ImageIcon imageIcon = new ImageIcon(imagePath);
            // �̹����� Image ��ü�� ��ȯ�մϴ�.
            Image image = imageIcon.getImage();
           
            // �̹����� ���ϴ� ũ��� �����մϴ�.
            Image scaledImage = image.getScaledInstance(273, 234, Image.SCALE_SMOOTH);
           
            // ������ �̹����� ���ο� ImageIcon���� ��ȯ�մϴ�.
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
               
            JLabel lblNewLabel = new JLabel();
            lblNewLabel.setIcon(scaledIcon); // JLabel�� �̹��� ������ ����
            lblNewLabel.setBounds(0, 0, 273, 234);
            Doctorinfo.add(lblNewLabel);
        } else {
            // �̹��� ������ �������� �ʴ� ���, ���� �޽����� ǥ���մϴ�.
            System.err.println("Image file not found: " + imagePath);
        }
               
        JPanel menu = new JPanel();
        menu.setBackground(new Color(0, 64, 128));
        menu.setBounds(0, 232, 273, 382);
        frame.getContentPane().add(menu);
        menu.setLayout(null);
       
        //����ٽð�
        JButton ����ٽð� = new JButton("����� �Է�");
        ����ٽð�.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WorkHour(userType, currentUserId);
            }
        });
        ����ٽð�.setBounds(23, 46, 226, 33);
        menu.add(����ٽð�);
       
        //ȯ�� ��ȸ
        JButton ȯ����ȸ = new JButton("ȯ�� ��ȸ");
        ȯ����ȸ.setBounds(23, 127, 226, 33);
        menu.add(ȯ����ȸ);
       
        ȯ����ȸ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userRole = determineUserType(currentUserId); // �α����� ������� ���� Ȯ��
                try {
                    if (userRole.equals("Doctor")) {
                        doctorpatient.setVisible(true); // ���� ȭ���� ���̰� ��
                       
                    } else if (userRole.equals("Nurse")) {
                        nursepatient.setVisible(true); // ���� ȭ���� ���̰� ��
                    } else {
                        JOptionPane.showMessageDialog(null, "�� �� ���� ����� �����Դϴ�.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "������ �߻��߽��ϴ�: " + ex.getMessage());
                }
            }
        });
       
        //������������
        JButton ������������ = new JButton("�������� ����");
        ������������.setBounds(23, 212, 226, 33);
        menu.add(������������);
       
        //�α׾ƿ�
        JButton �α׾ƿ� = new JButton("�α׾ƿ�");
        �α׾ƿ�.setBounds(23, 304, 226, 33);
        menu.add(�α׾ƿ�);
       
        �α׾ƿ�.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ���� â�� �ݰ�
                frame.dispose();
                // �α��� �������� ǥ��
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                     new LoginPage(); // LoginPage Ŭ������ �ν��Ͻ��� �����Ͽ� �α��� ȭ���� ǥ��
                    }
                });
            }
        });
       
        JButton backButton = new JButton("�ڷΰ���");
        backButton.setBounds(770, 575, 118, 29);
        frame.getContentPane().add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // JFrame �ݱ�
                // ���� ������ ����
                MainPage mainPage = new MainPage(currentUserId);
                mainPage.setVisible(true);
            }
        });
       
        JPanel ��� = new JPanel();
        ���.setBounds(272, 0, 628, 171);
        frame.getContentPane().add(���);
        ���.setLayout(null);
       
        //�����ΰ�
        JPanel logo = new JPanel();
        logo.setBounds(544, 10, 72, 67);
        ���.add(logo);
     
        // �̹��� ���� �ε�
        ImageIcon imageIcon = new ImageIcon("images/logo.png");
        Image image = imageIcon.getImage().getScaledInstance(72, 67, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);

        // �̹����� JLabel�� �����ϰ� �ΰ� �гο� �߰�
        JLabel logoLabel = new JLabel(imageIcon);
        logo.add(logoLabel);
       
        textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("����", Font.PLAIN, 20));
        textField.setText("�������� ����");
        textField.setBackground(new Color(255, 255, 255));
        textField.setBounds(349, 87, 267, 51);
        ���.add(textField);
        textField.setColumns(10);
       
        //�α������ִ� ����� ����
        userInfoLabel = new JLabel();
        userInfoLabel.setBounds(272, 169, 628, 445);
        userInfoLabel.setOpaque(true); // ������ ���̵��� ����
        frame.getContentPane().add(userInfoLabel);
        userInfoLabel.setForeground(new Color(0, 0, 0));
        userInfoLabel.setBackground(new Color(255, 255, 255));
       
        //���� �ε�
        loadUserInfo();

        frame.setVisible(true);
    }

    //�α����� ����� �ǻ����� ��ȣ������ �Ǵ�
    private String determineUserType(int userId) {
        String userIdString = String.valueOf(userId);
        if (userIdString.startsWith("91")) {
            return "Doctor";
        } else if (userIdString.startsWith("21")) {
            return "Nurse";
        } else {
            return "Unknown";
        }
    }

    //�α����� ����� ������ ���� ������ ǥ��
    private void loadUserInfo() {
        if ("Doctor".equals(userType)) {
            loadDoctorInfo();
        } else if ("Nurse".equals(userType)) {
            loadNurseInfo();
        } else {
            userInfoLabel.setText("<html>�� �� ���� ����� �����Դϴ�.</html>");
        }
    }

    //�α����� �ǻ����� ǥ��
    private void loadDoctorInfo() {
        String query = "SELECT DoctorID, DoctorName, DoctorDepartment, ContactNumber, StartTime, EndTime FROM DB2024_Doctor WHERE DoctorID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
            	String ID = rs.getString("DoctorID");
                String name = rs.getString("DoctorName");
                String department = rs.getString("DoctorDepartment");
                String contact = rs.getString("ContactNumber");
                String startTime = rs.getTime("StartTime").toString();
                String endTime = rs.getTime("EndTime").toString();

                userInfoLabel.setText("<html><div style='padding-left: 20px;'>   �ǻ�: " + name + "<br/>   �μ�: " + department + "<br/>   ����ó: " + contact + "<br/>   �ٹ� ���� �ð�: " + startTime + "<br/>   �ٹ� ���� �ð�: " + endTime + "</div></html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //�α����� ��ȣ�� ���� ǥ��
    private void loadNurseInfo() {
        String query = "SELECT NurseID, NurseName, NurseDepartment, ContactNumber ,StartTime, EndTime FROM DB2024_Nurse WHERE NurseID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
            	String ID = rs.getString("NurseID");
                String name = rs.getString("NurseName");
                String department = rs.getString("NurseDepartment");
                String contact = rs.getString("ContactNumber");
                String startTime = rs.getTime("StartTime").toString();
                String endTime = rs.getTime("EndTime").toString();

                userInfoLabel.setText("<html><div style='padding-left: 20px;'> ��ȣ��: " + name + "<br/>   �μ�: " + department + "<br/>   ����ó: " + contact + "<br/>   �ٹ� ���� �ð�: " + startTime + "<br/>   �ٹ� ���� �ð�: " + endTime + "</div></html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}