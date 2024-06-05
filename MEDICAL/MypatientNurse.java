package MEDICAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//로그인한 간호사가 자신의 환자 목록을 보는 창

public class MypatientNurse extends JFrame {
    private JList<String> patientList; // 환자 목록을 표시할 JList
    private DefaultListModel<String> patientListModel; // JList에 데이터를 제공할 모델
    private JTextArea patientInfoArea;// 환자 정보를 표시할 JTextArea
    private JButton treatmentButton; // 치료 정보를 로드할 버튼
    private JLabel nurseInfoLabel; // 의사 정보를 표시할 레이블

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    //현재 로그인한 간호사의 ID
    private static int currentUserId;

    public MypatientNurse(int currentUserId) {
        this.currentUserId = currentUserId;
   
        setTitle("병원 관리 시스템 - 간호사 버전");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
       
        //간호사의 정보가 표시되는 Label
        nurseInfoLabel = new JLabel();
        nurseInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nurseInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);    
        nurseInfoLabel.setOpaque(true); // 배경색이 보이도록 설정
        nurseInfoLabel.setBackground(new Color(255, 255, 255));
        nurseInfoLabel.setBounds(1, 0, 199, 146);
        getContentPane().add(nurseInfoLabel);

        //로그인한 간호사에 따른 환자목룍 표시
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setForeground(new Color(255, 255, 255));
        patientList.setBackground(new Color(0, 64, 128));
        patientList.setFont(new Font("Serif", Font.BOLD, 16));
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

        //환자 정보가 표시되는 공간
        patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane infoScrollPane = new JScrollPane(patientInfoArea);

        //환자의 치료정보를 볼 수 있는 버튼
        treatmentButton = new JButton("치료 정보 보기");
        treatmentButton.setFont(new Font("맑은 고딕", Font.BOLD, 16)); // 버튼 텍스트 크기 설정
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
        getContentPane().setLayout(null);
        getContentPane().add(nurseInfoLabel); // 의사 정보 레이블 추가
        getContentPane().add(splitPane);

        // 뒤로가기 버튼
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 722, 986, 41);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 패널의 레이아웃을 플로우 레이아웃으로 설정하고 오른쪽 정렬

        JButton backButton = new JButton("뒤로 가기"); // 뒤로 가기 버튼 생성
        backButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        backButton.addActionListener(e -> {
            this.setVisible(false); // 현재 창을 숨김
            JFrame doctorpatient = new Mypatient(currentUserId); // Mypatient 클래스의 인스턴스를 생성
            JFrame nursepatient = new MypatientNurse(currentUserId); // MypatientNurse 클래스의 인스턴스를 생성
            MyPage myPage = new MyPage(doctorpatient, nursepatient, currentUserId); //MyPage 클래스의 인스턴스를 생성
            myPage.frame.setVisible(true);//MyPage를 표시
        });

        //정보수정 버튼
        JButton modifyButton = new JButton("정보 수정");
        modifyButton.setFont(new Font("Serif", Font.PLAIN, 16));
        modifyButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ManagePatient managePatientFrame = new ManagePatient();
                managePatientFrame.setVisible(true);
            });
        });
       
        //환자의 개인정보를 수정
        JButton addButton = new JButton("환자 정보 수정");
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
       
        loadNurseInfo(); // 의사 정보 로딩
        loadPatients(); // 환자 목록 로딩
        getContentPane().add(splitPane, BorderLayout.CENTER);

    }

    //로그인한 간호사 정보를 DB로부터 불러오는 함수
    private void loadNurseInfo() {
        String query = "SELECT NurseID, NurseName, NurseDepartment, ContactNumber FROM DB2024_Nurse WHERE NurseID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); //DB와 연결
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId); //현재 로그인한 사람의 아이디
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("NurseID");
                String name = rs.getString("NurseName");
                String specialty = rs.getString("NurseDepartment");
                String phone = rs.getString("ContactNumber");

                nurseInfoLabel.setText("<html><div style='padding-left: 10px;'>간호사: " + name + "<br/>ID: " + ID + "<br/>전공과: " + specialty + "<br/>연락처: " + phone + "</html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    //간호사에 따른 환자 목록을 DB로부터 불러오는 함수
    private void loadPatients() {
        String query = "SELECT PatientID, Name FROM DB2024_Patient WHERE NurseID = ?";
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

    //환자정보를 DB로부터 불러오는 함수
    private void loadPatientInfo(int patientId) {
        patientInfoArea.setText("");
        String query = "SELECT * FROM DB2024_patient_privacy WHERE PatientID = ?";
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
        patientInfoArea.append("\n#치료 정보#\n");
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
                patientInfoArea.append("치료 날짜: " + rs.getDate("Date") + "\n");
                patientInfoArea.append("병명: " + rs.getString("DiseaseName") + "\n");
                patientInfoArea.append("약물명: " + rs.getString("MedicationName") + "\n");
                patientInfoArea.append("추천 치료: " + rs.getString("RecommendedTreatment") + "\n");
                patientInfoArea.append("복용량: " + rs.getString("Dosage") + "\n");
                patientInfoArea.append("KTAS: " + rs.getInt("KTAS") + "\n");
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //main 함수
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MypatientNurse frame = new MypatientNurse(currentUserId);
            frame.setVisible(true);
        });
    }
}