import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Mypatient extends JFrame {
    private JList<String> patientList; // 환자 목록을 표시할 JList
    private DefaultListModel<String> patientListModel; // JList에 데이터를 제공할 모델
    private JTextArea patientInfoArea;
    private JButton treatmentButton; // 치료 정보를 로드할 버튼
    private JFrame mainScreen; // 메인 화면 인스턴스를 저장할 변수
    private JLabel doctorInfoLabel; // 의사 정보를 표시할 레이블

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    // 가정: 현재 로그인한 사용자의 ID (실제 응용 프로그램에서는 로그인 세션에서 이 값을 가져와야 함)
    private int currentUserId = 91101; // 로그인 페이지에서 ID 정보 가져오기 admin

    public Mypatient() {
    	this.mainScreen = mainScreen; // 메인 화면 인스턴스 저장
        setTitle("병원 관리 시스템");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        
        doctorInfoLabel = new JLabel();
        doctorInfoLabel.setBounds(1, 0, 199, 146);
        doctorInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        doctorInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);    
        doctorInfoLabel.setOpaque(true); // 배경색이 보이도록 설정
        doctorInfoLabel.setBackground(new Color(255, 255, 255));
        getContentPane().add(doctorInfoLabel);
        
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setForeground(new Color(255, 255, 255));
        patientList.setBackground(new Color(0, 64, 128));
        patientList.setFont(new Font("Arial", Font.BOLD, 16));
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = patientList.getSelectedValue();
                if (selectedValue != null && !selectedValue.isEmpty()) {
                    int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - 이름" 형식에서 ID를 분리
                    loadPatientInfo(patientId);
                }
            }
        });
        JScrollPane listScrollPane = new JScrollPane(patientList);
        listScrollPane.setPreferredSize(new Dimension(50, 50));

        patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane infoScrollPane = new JScrollPane(patientInfoArea);

        treatmentButton = new JButton("치료 정보 보기");
        treatmentButton.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 16)); // 버튼 텍스트 크기 설정
        treatmentButton.addActionListener(e -> {
            String selectedValue = patientList.getSelectedValue();
            if (selectedValue != null && !selectedValue.isEmpty()) {
                int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - 이름" 형식에서 ID를 분리
                loadTreatmentInfo(patientId);
            }
        });
        

        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, infoScrollPane);
        splitPane.setBounds(1, 145, 985, 580);
        splitPane.setDividerLocation(200);
        getContentPane().add(doctorInfoLabel); // 의사 정보 레이블 추가
        getContentPane().add(splitPane);

     // 기존의 "뒤로 가기" 버튼 추가 부분을 대체합니다.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 722, 986, 41);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 패널의 레이아웃을 플로우 레이아웃으로 설정하고 오른쪽 정렬

        JButton backButton = new JButton("뒤로 가기"); // 뒤로 가기 버튼 생성
        backButton.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 16));
        backButton.addActionListener(e -> {
            this.setVisible(false); // 현재 창을 숨김
            mainScreen.setVisible(true); // 메인 화면을 보이게 함
        });
        
        JButton modify = new JButton("정보 수정");
        modify.setFont(new Font("Serif", Font.PLAIN, 16));
        buttonPanel.add(modify);

        buttonPanel.add(treatmentButton,BorderLayout.CENTER);
        buttonPanel.add(backButton); // 패널에 버튼 추가
        getContentPane().add(buttonPanel); // 패널을 프레임의 SOUTH 영역에 추가
        


        
        loadDoctorInfo(); // 의사 정보 로딩
        loadPatients(); // 환자 목록 로딩
    }

    private void loadDoctorInfo() {
        String query = "SELECT DoctorID, DoctorName, DoctorDepartment, ContactNumber FROM DB2024_Doctor WHERE DoctorID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("DoctorID");
                String name = rs.getString("DoctorName");
                String specialty = rs.getString("DoctorDepartment");
                String phone = rs.getString("ContactNumber");

                doctorInfoLabel.setText("<html><div style='padding-left: 10px;'>의사: " + name + "<br/>ID: " + ID + "<br/>전공과: " + specialty + "<br/>연락처: " + phone + "</html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadPatients() {
        String query = "SELECT PatientID, Name FROM DB2024_Patient WHERE DoctorID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("PatientID");
                String name = rs.getString("Name");
                patientListModel.addElement(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadPatientInfo(int patientId) {
        patientInfoArea.setText("");
        String query = "SELECT * FROM DB2024_Patient_no_address WHERE PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                patientInfoArea.append("  환자ID: " + rs.getInt("PatientID") + "\n");
                patientInfoArea.append("  이름: " + rs.getString("Name") + "\n");
                patientInfoArea.append("  생년월일: " + rs.getString("Birth") + "\n");
                patientInfoArea.append("  주소: " + rs.getString("Address") + "\n");
                patientInfoArea.append("  전화번호: " + rs.getString("Phone") + "\n");
                patientInfoArea.append("  보호자 전화번호: " + rs.getString("GuardianPhone") + "\n");
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTreatmentInfo(int patientId) {
        patientInfoArea.append("\n치료 정보:\n");
        String query = "SELECT T.Date, D.DiseaseName, M.MedicationName, T.RecommendedTreatment, T.Dosage, T.KTAS " +
                       "FROM DB2024_Treatment T " +
                       "JOIN DB2024_Disease D ON T.DiseaseID = D.DiseaseID " +
                       "JOIN DB2024_Medication M ON T.MedicationID = M.MedicationID " +
                       "WHERE T.PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                patientInfoArea.append("  날짜: " + rs.getDate("Date") + "\n");
                patientInfoArea.append("  병명: " + rs.getString("DiseaseName") + "\n");
                patientInfoArea.append("  약물명: " + rs.getString("MedicationName") + "\n");
                patientInfoArea.append("  추천 치료: " + rs.getString("RecommendedTreatment") + "\n");
                patientInfoArea.append("  복용량: " + rs.getString("Dosage") + "\n");
                patientInfoArea.append("  KTAS: " + rs.getInt("KTAS") + "\n");
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	JFrame mainScreen = new MainPage(); // MainScreen 클래스의 인스턴스를 생성해야 합니다.
            Mypatient gui = new Mypatient();
            gui.setVisible(true);
        });
    }
}
