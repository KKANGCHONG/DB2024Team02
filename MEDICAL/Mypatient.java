package MEDICAL;

import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Mypatient extends JFrame {
    private JList<String> patientList; // 환자 목록을 표시할 JList
    private DefaultListModel<String> patientListModel; // JList에 데이터를 제공할 모델
    private JTextArea patientInfoArea; //환자 정보를 표시할 영역
    private JButton treatmentButton; // 치료 정보를 로드할 버튼
    private JLabel doctorInfoLabel; // 의사 정보를 표시할 레이블

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    //현재 로그인한 사용자의 ID
    private static int currentUserId;

    public Mypatient(int currentUserId) {
        Mypatient.currentUserId = currentUserId;
        // LoginPage에서 저장된 사용자 ID 값을 가져오기

        //frame 설정
    setTitle("병원 관리 시스템");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
       
        //의사정보 표시
        doctorInfoLabel = new JLabel();
        doctorInfoLabel.setBounds(1, 0, 199, 146);
        doctorInfoLabel.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
        doctorInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);    
        doctorInfoLabel.setOpaque(true); // 배경색이 보이도록 설정
        doctorInfoLabel.setBackground(new Color(255, 255, 255));
        getContentPane().add(doctorInfoLabel);
       
        //로그인한 의사의 환자 목록 표시
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setForeground(new Color(255, 255, 255));
        patientList.setBackground(new Color(0, 64, 128));
        patientList.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
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
        JScrollPane listScrollPane = new JScrollPane(patientList); //JScrollPane에 환자 목록 표시
        listScrollPane.setPreferredSize(new Dimension(50, 50));

        //환자 개인정보 표시
        patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
        JScrollPane infoScrollPane = new JScrollPane(patientInfoArea);

        //환자의 치료정보 보기 버튼
        treatmentButton = new JButton("치료 정보 보기");
        treatmentButton.setFont(new Font("HY헤드라인M", Font.PLAIN, 16)); // 버튼 텍스트 크기 설정
        treatmentButton.addActionListener(e -> {
            String selectedValue = patientList.getSelectedValue();
            if (selectedValue != null && !selectedValue.isEmpty()) {
                int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - 이름" 형식에서 ID를 분리
                loadTreatmentInfo(patientId);
            }
        });
       

        //로그인한 의사정보 표시하는 pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, infoScrollPane);
        splitPane.setBounds(1, 145, 985, 580);
        splitPane.setDividerLocation(200);
        getContentPane().add(doctorInfoLabel); // 의사 정보 레이블 추가
        getContentPane().add(splitPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 722, 986, 41);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 패널의 레이아웃을 플로우 레이아웃으로 설정하고 오른쪽 정렬

        //뒤로가기
        JButton backButton = new JButton("뒤로 가기"); // 뒤로 가기 버튼 생성
        backButton.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
        backButton.addActionListener(e -> {
            this.setVisible(false); // 현재 창을 숨김
            // 메인 페이지 열기
            JFrame doctorpatient = new Mypatient(currentUserId); //mypatient 객체 생성
            JFrame nursepatient = new MypatientNurse(currentUserId); //mypatientnurse 객체 생성
            MyPage myPage = new MyPage(doctorpatient, nursepatient, currentUserId);//mypage 객체 생성
            myPage.frame.setVisible(true);//mypage가 보이게
        });
       
        JButton addButton_1 = new JButton("치료 정보 추가");
        addButton_1.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
        buttonPanel.add(addButton_1);
        addButton_1.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ManageTreatment manageTreatmentFrame = new ManageTreatment();
                manageTreatmentFrame.setVisible(true);
            });
        });
       
       
      //환자 추가
        JButton addButton = new JButton("환자 정보 추가");
        addButton.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
        buttonPanel.add(addButton);
        addButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ManagePatient managePatientFrame = new ManagePatient();
                managePatientFrame.setVisible(true);
            });
        });
       
       
       
        buttonPanel.add(treatmentButton,BorderLayout.CENTER);
        buttonPanel.add(backButton); // 패널에 버튼 추가
        getContentPane().add(buttonPanel); // 패널을 프레임의 SOUTH 영역에 추가
       
        loadDoctorInfo(); // 의사 정보 로딩
        loadPatients(); // 환자 목록 로딩
    }

    //로그인한 의사정보를 DB로부터 불러오는 함수
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
   
    //로그인한 의사에 따른 환자목록을 DB로부터 불러오는 함수
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

    //로그인한 의사에 따른 환자정보을 DB로부터 불러오는 함수
    private void loadPatientInfo(int patientId) {
        patientInfoArea.setText("");
        String query = "SELECT * FROM DB2024_patient_privacy WHERE PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { //정보를 받아옴
                patientInfoArea.append("  환자ID: " + rs.getInt("PatientID") + "\n");
                patientInfoArea.append("  이름: " + rs.getString("Name") + "\n");
                patientInfoArea.append("  생년월일: " + rs.getString("Birth") + "\n");
                patientInfoArea.append("  주소: " + rs.getString("Address") + "\n");
                patientInfoArea.append("  전화번호: " + rs.getString("Phone") + "\n");
                patientInfoArea.append("  보호자 전화번호: " + rs.getString("GuardianPhone") + "\n");
                patientInfoArea.append("  담당 간호사: " + rs.getInt("NurseID") + "\n");
                patientInfoArea.append("  주치의: " + rs.getInt("DoctorID") + "\n");
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //환자의 치료정보를 DB로부터 불러오는 함수
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

            while (rs.next()) { //정보를 받아옴
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

    //main 함수
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            currentUserId = loginPage.getCurrentUserId();
            Mypatient gui = new Mypatient(currentUserId);
            gui.setVisible(true);
        });
    }
}