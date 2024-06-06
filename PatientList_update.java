package MEDICAL;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PatientList extends JFrame {
    // JDBC 드라이버 이름과 데이터베이스 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db2024team02";
    // 데이터베이스 계정 정보
    static final String USER = "DB2024Team02";
    static final String PASS = "DB2024Team02";

    // 테이블 및 모델
    private JTable table;
    private DefaultTableModel model;
    
    private static int currentUserId;

    public PatientList(int currentUserId) {
    	
    	this.currentUserId = currentUserId;
    	
        setTitle("Patient Doctor Treatment List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 651);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // 테이블 모델 설정
        model = new DefaultTableModel(new String[]{
            "PatientId", "Name", "Birth", "PatientPhone", "GuardianPhone",
            "DoctorId", "DoctorName", "DoctorDepartment", "DoctorPhone",
            "DiseaseID", "DiseaseName", "FirstDiseaseName", "MedicationID", "MedicationName",
            "RecommendedTreatment", "TreatmentDate", "Dosage", "KTAS"
        }, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);
        
        JButton backButton = new JButton("뒤로가기");
        backButton.setBounds(770, 575, 118, 29);
        backButton.setFont(new Font("HY헤드라인M", Font.PLAIN, 16));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 현재 창 닫기
                MainPage mainPage = new MainPage(currentUserId); // MainPage 인스턴스 생성
                mainPage.setVisible(true); // MainPage 창 표시
            }
        });

        JPanel panel = new JPanel();
        panel.add(backButton);
        c.add(panel, BorderLayout.SOUTH);

        loadPatients(); // 환자 목록 로드
        setVisible(true);
    }

    private void loadPatients() {
        model.setRowCount(0); // 테이블 초기화
        String query = "SELECT p.PatientID, p.Name AS PatientName, p.Birth AS DateOfBirth, p.Phone AS PatientPhone, p.GuardianPhone, "
                     + "d.DoctorID, d.DoctorName, d.DoctorDepartment, d.ContactNumber AS DoctorPhone, "
                     + "t.DiseaseID, dis.DiseaseName, t.MedicationID, m.MedicationName, "
                     + "t.RecommendedTreatment, t.Date AS TreatmentDate, t.Dosage, t.KTAS, "
                     + "(SELECT DiseaseName FROM DB2024_Disease dd "
                     + " WHERE dd.DiseaseID = (SELECT DiseaseID FROM DB2024_Treatment tt "
                     + " WHERE tt.PatientID = p.PatientID LIMIT 1)) AS FirstDiseaseName "
                     + "FROM DB2024_Patient p "
                     + "JOIN DB2024_Treatment t ON p.PatientID = t.PatientID "
                     + "JOIN DB2024_Doctor d ON p.DoctorID = d.DoctorID "
                     + "JOIN DB2024_Disease dis ON t.DiseaseID = dis.DiseaseID "
                     + "JOIN DB2024_Medication m ON t.MedicationID = m.MedicationID";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int PatientId = rs.getInt("PatientID");
                String Name = rs.getString("PatientName");
                String Birth = rs.getString("DateOfBirth");
                String PatientPhone = rs.getString("PatientPhone");
                String GuardianPhone = rs.getString("GuardianPhone");
                int DoctorId = rs.getInt("DoctorID");
                String DoctorName = rs.getString("DoctorName");
                String DoctorDepartment = rs.getString("DoctorDepartment");
                String DoctorPhone = rs.getString("DoctorPhone");
                int DiseaseID = rs.getInt("DiseaseID");
                String DiseaseName = rs.getString("DiseaseName");
                String FirstDiseaseName = rs.getString("FirstDiseaseName"); // 첫 번째 질병 이름 추가
                int MedicationID = rs.getInt("MedicationID");
                String MedicationName = rs.getString("MedicationName");
                String RecommendedTreatment = rs.getString("RecommendedTreatment");
                String TreatmentDate = rs.getString("TreatmentDate");
                String Dosage = rs.getString("Dosage");
                String KTAS = rs.getString("KTAS");

                // 모델에 행 추가
                model.addRow(new Object[]{
                    PatientId, Name, Birth, PatientPhone, GuardianPhone,
                    DoctorId, DoctorName, DoctorDepartment, DoctorPhone,
                    DiseaseID, DiseaseName, FirstDiseaseName, MedicationID, MedicationName,
                    RecommendedTreatment, TreatmentDate, Dosage, KTAS
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PatientList(currentUserId);
    }
}
