package MEDICAL;

import java.awt.*; // java.awt.BorderLayout, java.awt.Color, java.awt.event.ActionListener, java.awt.event.ActionEvent 등을 포함
import java.awt.event.*; // 이전에 중복된 ActionListener와 ActionEvent를 포함
import javax.swing.*; // JFrame, JPanel, JTextArea, JButton, JTextField, JLabel, JEditorPane 등을 포함
import java.sql.*; // SQL 관련 모든 클래스를 포함
import java.io.File; // 파일 처리를 위한 클래스

public class MyPage {


    JFrame frame;
    private JLabel userInfoLabel;
    private String userType; // "Doctor" or "Nurse"
    private JFrame doctorpatient; // doctor의 환자정보 인스턴스를 저장할 변수
    private JFrame nursepatient; // nurse 환자정보 인스턴스를 저장할 변수
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
   
    private static int currentUserId; // 의사 ID로 가정
    private JTextField textField;
   
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JFrame doctorpatient = new Mypatient(currentUserId); // MainScreen 클래스의 인스턴스를 생성해야 합니다.
                    JFrame nursepatient = new MypatientNurse(currentUserId); // MainScreen 클래스의 인스턴스를 생성해야 합니다.
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
        this.doctorpatient = doctorpatient; //  doctor의 환자정보 인스턴스를 저장할 변수
        this.nursepatient = nursepatient; // nurse 환자정보 인스턴스를 저장할 변수
        userType = determineUserType(currentUserId);
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 914, 651);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
       
        //로그인한 의사 정보
        JPanel Doctorinfo = new JPanel();
        Doctorinfo.setBackground(new Color(255, 255, 255));
        Doctorinfo.setBounds(0, 0, 273, 234);
        frame.getContentPane().add(Doctorinfo);
        Doctorinfo.setLayout(null);
       
        //프로필사진
        String imagePath = "images/" + currentUserId + ".png";
       
        // 이미지 파일이 실제로 존재하는지 확인합니다.
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            // ImageIcon을 사용해 이미지를 불러옵니다.
            ImageIcon imageIcon = new ImageIcon(imagePath);
            // 이미지를 Image 객체로 변환합니다.
            Image image = imageIcon.getImage();
           
            // 이미지를 원하는 크기로 조정합니다.
            Image scaledImage = image.getScaledInstance(273, 234, Image.SCALE_SMOOTH);
           
            // 조정된 이미지를 새로운 ImageIcon으로 변환합니다.
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
               
            JLabel lblNewLabel = new JLabel();
            lblNewLabel.setIcon(scaledIcon); // JLabel에 이미지 아이콘 설정
            lblNewLabel.setBounds(0, 0, 273, 234);
            Doctorinfo.add(lblNewLabel);
        } else {
            // 이미지 파일이 존재하지 않는 경우, 오류 메시지를 표시합니다.
            System.err.println("Image file not found: " + imagePath);
        }
               
        JPanel menu = new JPanel();
        menu.setBackground(new Color(0, 64, 128));
        menu.setBounds(0, 232, 273, 382);
        frame.getContentPane().add(menu);
        menu.setLayout(null);
       
        //출퇴근시간
        JButton 출퇴근시간 = new JButton("출퇴근 입력");
        출퇴근시간.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new WorkHour(userType, currentUserId);
            }
        });
        출퇴근시간.setBounds(23, 46, 226, 33);
        menu.add(출퇴근시간);
       
        //환자 조회
        JButton 환자조회 = new JButton("환자 조회");
        환자조회.setBounds(23, 127, 226, 33);
        menu.add(환자조회);
       
        환자조회.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userRole = determineUserType(currentUserId); // 로그인한 사용자의 역할 확인
                try {
                    if (userRole.equals("Doctor")) {
                        doctorpatient.setVisible(true); // 메인 화면을 보이게 함
                       
                    } else if (userRole.equals("Nurse")) {
                        nursepatient.setVisible(true); // 메인 화면을 보이게 함
                    } else {
                        JOptionPane.showMessageDialog(null, "알 수 없는 사용자 역할입니다.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "오류가 발생했습니다: " + ex.getMessage());
                }
            }
        });
       
        //개인정보변경
        JButton 개인정보변경 = new JButton("개인정보 변경");
        개인정보변경.setBounds(23, 212, 226, 33);
        menu.add(개인정보변경);
       
        //로그아웃
        JButton 로그아웃 = new JButton("로그아웃");
        로그아웃.setBounds(23, 304, 226, 33);
        menu.add(로그아웃);
       
        로그아웃.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 현재 창을 닫고
                frame.dispose();
                // 로그인 페이지를 표시
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                     new LoginPage(); // LoginPage 클래스의 인스턴스를 생성하여 로그인 화면을 표시
                    }
                });
            }
        });
       
        JButton backButton = new JButton("뒤로가기");
        backButton.setBounds(770, 575, 118, 29);
        frame.getContentPane().add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // JFrame 닫기
                // 메인 페이지 열기
                MainPage mainPage = new MainPage(currentUserId);
                mainPage.setVisible(true);
            }
        });
       
        JPanel 배경 = new JPanel();
        배경.setBounds(272, 0, 628, 171);
        frame.getContentPane().add(배경);
        배경.setLayout(null);
       
        //병원로고
        JPanel logo = new JPanel();
        logo.setBounds(544, 10, 72, 67);
        배경.add(logo);
     
        // 이미지 파일 로드
        ImageIcon imageIcon = new ImageIcon("images/logo.png");
        Image image = imageIcon.getImage().getScaledInstance(72, 67, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);

        // 이미지를 JLabel에 설정하고 로고 패널에 추가
        JLabel logoLabel = new JLabel(imageIcon);
        logo.add(logoLabel);
       
        textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setFont(new Font("굴림", Font.PLAIN, 20));
        textField.setText("땡땡땡땡 병원");
        textField.setBackground(new Color(255, 255, 255));
        textField.setBounds(349, 87, 267, 51);
        배경.add(textField);
        textField.setColumns(10);
       
        //로그인해있는 사람의 정보
        userInfoLabel = new JLabel();
        userInfoLabel.setBounds(272, 169, 628, 445);
        userInfoLabel.setOpaque(true); // 배경색이 보이도록 설정
        frame.getContentPane().add(userInfoLabel);
        userInfoLabel.setForeground(new Color(0, 0, 0));
        userInfoLabel.setBackground(new Color(255, 255, 255));
       
        //정보 로드
        loadUserInfo();

        frame.setVisible(true);
    }

    //로그인한 사람이 의사인지 간호사인지 판단
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

    //로그인한 사람의 정보에 따라 데이터 표출
    private void loadUserInfo() {
        if ("Doctor".equals(userType)) {
            loadDoctorInfo();
        } else if ("Nurse".equals(userType)) {
            loadNurseInfo();
        } else {
            userInfoLabel.setText("<html>알 수 없는 사용자 유형입니다.</html>");
        }
    }

    //로그인한 의사정보 표출
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

                userInfoLabel.setText("<html><div style='padding-left: 20px;'>   의사: " + name + "<br/>   부서: " + department + "<br/>   연락처: " + contact + "<br/>   근무 시작 시간: " + startTime + "<br/>   근무 종료 시간: " + endTime + "</div></html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //로그인한 간호사 정보 표출
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

                userInfoLabel.setText("<html><div style='padding-left: 20px;'> 간호사: " + name + "<br/>   부서: " + department + "<br/>   연락처: " + contact + "<br/>   근무 시작 시간: " + startTime + "<br/>   근무 종료 시간: " + endTime + "</div></html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}