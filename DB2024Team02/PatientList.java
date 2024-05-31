import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

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
    
    public PatientList() {
        setTitle("Doctor List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // 테이블 모델 설정
        model = new DefaultTableModel(new String[]{"PatientId","Name", "Birth", "contactNumber","GuardianNumber","DoctorId", "NurseId"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);
        
        loadPatients(); // 환자 목록 로드
        setVisible(true);
    }

    private void loadPatients() {
        model.setRowCount(0); // 테이블 초기화
        String query = "SELECT * FROM DB2024_Patient";  // 모든 칼럼을 선택하는 쿼리로 변경
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int PatientId = rs.getInt("PatientID");
                String Name = rs.getString("Name");
                String Birth = rs.getString("Birth");
                String contactNumber = rs.getString("Phone");
                String GuardianNumber = rs.getString("GuardianPhone");
                int DoctorId = rs.getInt("DoctorId");
                int NurseId = rs.getInt("NurseID");
                
                // 모델에 행 추가, 모든 칼럼을 테이블에 추가합니다.
                model.addRow(new Object[]{
                		PatientId,Name, Birth, contactNumber,GuardianNumber,DoctorId, NurseId
                    
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PatientList();
    }
}
