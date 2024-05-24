package 의료서비스;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Mypatient extends JFrame {
    private JList<String> patientList; // 환자 목록을 표시할 JList
    private DefaultListModel<String> patientListModel; // JList에 데이터를 제공할 모델
    private JTextArea patientInfoArea;
    private JButton treatmentButton; // 치료 정보를 로드할 버튼

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0614346";

    // 가정: 현재 로그인한 사용자의 ID (실제 응용 프로그램에서는 로그인 세션에서 이 값을 가져와야 함)
    private int currentUserId = 1; // 여기서는 1번 사용자가 로그인했다고 가정

    public Mypatient() {
        setTitle("병원 관리 시스템");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
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
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);
        add(treatmentButton, BorderLayout.SOUTH);

        loadPatients(); // 환자 목록 로딩
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
                patientInfoArea.append("환자ID: " + rs.getInt("PatientID") + "\n");
                patientInfoArea.append("이름: " + rs.getString("Name") + "\n");
                patientInfoArea.append("생년월일: " + rs.getString("Birth") + "\n");
                patientInfoArea.append("주소: " + rs.getString("Address") + "\n");
                patientInfoArea.append("전화번호: " + rs.getString("Phone") + "\n");
                patientInfoArea.append("보호자 전화번호: " + rs.getString("GuardianPhone") + "\n");
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
                patientInfoArea.append("날짜: " + rs.getDate("Date") + "\n");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Mypatient frame = new Mypatient();
            frame.setVisible(true);
        });
    }
}
