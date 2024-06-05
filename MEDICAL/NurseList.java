package MEDICAL;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class NurseList extends JFrame {
    // JDBC 드라이버 이름과 데이터베이스 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    // 데이터베이스 계정 정보
    static final String USER = "root";
    static final String PASS = "root";

    // 테이블 및 모델
    private JTable table;
    private DefaultTableModel model;
    
    public NurseList() {
        setTitle("Nurse List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // 테이블 모델 설정
        model = new DefaultTableModel(new String[]{"NurseID", "NurseName", "NurseDepartment", "ContactNumber"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);

        
        loadNurses(); // 간호사 목록 로드
        setVisible(true);
    }

    private void loadNurses() {
        model.setRowCount(0); // 테이블 초기화
        String query = "SELECT * FROM DB2024_Nurse";  // 모든 칼럼을 선택하는 쿼리로 변경
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int NurseId = rs.getInt("NurseID");
                String NurseName = rs.getString("NurseName");
                String NurseDepartment = rs.getString("NurseDepartment");
                String contactNumber = rs.getString("ContactNumber");
                

                // 모델에 행 추가, 모든 칼럼을 테이블에 추가합니다.
                model.addRow(new Object[]{
                    NurseId, NurseName, NurseDepartment, contactNumber
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new NurseList();
    }
}