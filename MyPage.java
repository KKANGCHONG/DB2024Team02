import java.awt.*; // java.awt.BorderLayout, java.awt.Color, java.awt.event.ActionListener, java.awt.event.ActionEvent ���� ����
import java.awt.event.*; // ������ �ߺ��� ActionListener�� ActionEvent�� ����
import javax.swing.*; // JFrame, JPanel, JTextArea, JButton, JTextField, JLabel, JEditorPane ���� ����
import java.sql.*; // SQL ���� ��� Ŭ������ ����
import java.io.File; // ���� ó���� ���� Ŭ����

public class MyPage {

    private JFrame frame;
    private JLabel userInfoLabel;
    private String userType; // "Doctor" or "Nurse"
    private JFrame doctorpatient; // ���� ȭ�� �ν��Ͻ��� ������ ����
    private JFrame nursepatient; // ���� ȭ�� �ν��Ͻ��� ������ ����
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private int currentUserId; // �ǻ� ID�� ����
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame doctorpatient = new Mypatient(); // MainScreen Ŭ������ �ν��Ͻ��� �����ؾ� �մϴ�.
                    JFrame nursepatient = new MypatientNurse(); // MainScreen Ŭ������ �ν��Ͻ��� �����ؾ� �մϴ�.
                    MyPage window = new MyPage(doctorpatient, nursepatient);
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MyPage(JFrame doctorpatient, JFrame nursepatient) {
        this.doctorpatient = doctorpatient; // ���� ȭ�� �ν��Ͻ� ����
        this.nursepatient = nursepatient; // ���� ȭ�� �ν��Ͻ� ����
        currentUserId = 91101; // ���� ����� ID
        userType = determineUserType(currentUserId);
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 914, 651);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JPanel Doctorinfo = new JPanel();
        Doctorinfo.setBackground(new Color(255, 255, 255));
        Doctorinfo.setBounds(0, 0, 273, 234);
        frame.getContentPane().add(Doctorinfo);
        Doctorinfo.setLayout(null);
        
        String imagePath = "C:\\Users\\������\\Desktop\\3�г�1�б�\\�����ͺ��̽�\\DBTeamProject\\91101.png";
        
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
        
        JButton ����ٽð� = new JButton("����� �Է�");
        ����ٽð�.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WorkHour(userType, currentUserId);
            }
        });
        ����ٽð�.setBounds(23, 46, 226, 33);
        menu.add(����ٽð�);
        
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
        
        JButton ������������ = new JButton("�������� ����");
        ������������.setBounds(23, 212, 226, 33);
        menu.add(������������);
        
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
                        //new LoginPage(); // LoginPage Ŭ������ �ν��Ͻ��� �����Ͽ� �α��� ȭ���� ǥ��
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
            }
        });
        
        JPanel ��� = new JPanel();
        ���.setBounds(272, 0, 628, 171);
        frame.getContentPane().add(���);
        
        userInfoLabel = new JLabel();
        userInfoLabel.setBounds(272, 169, 628, 445);
        userInfoLabel.setOpaque(true); // ������ ���̵��� ����
        frame.getContentPane().add(userInfoLabel);
        userInfoLabel.setForeground(new Color(0, 0, 0));
        userInfoLabel.setBackground(new Color(255, 255, 255));
        
        loadUserInfo();

        frame.setVisible(true);
    }

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

    private void loadUserInfo() {
        if ("Doctor".equals(userType)) {
            loadDoctorInfo();
        } else if ("Nurse".equals(userType)) {
            loadNurseInfo();
        } else {
            userInfoLabel.setText("<html>�� �� ���� ����� �����Դϴ�.</html>");
        }
    }

    private void loadDoctorInfo() {
        String query = "SELECT DoctorName, DoctorDepartment, ContactNumber, StartTime, EndTime FROM DB2024_Doctor WHERE DoctorID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
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

    private void loadNurseInfo() {
        String query = "SELECT NurseID, NurseName, NurseDepartment, ContactNumber FROM DB2024_Nurse WHERE NurseID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("NurseName");
                String department = rs.getString("NurseDepartment");
                String contact = rs.getString("ContactNumber");
                String startTime = rs.getTime("StartTime").toString();
                String endTime = rs.getTime("EndTime").toString();

                userInfoLabel.setText("<html><div style='padding-left: 20px;'>   ��ȣ��: " + name + "<br/>   �μ�: " + department + "<br/>   ����ó: " + contact + "<br/>   �ٹ� ���� �ð�: " + startTime + "<br/>   �ٹ� ���� �ð�: " + endTime + "</div></html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}


