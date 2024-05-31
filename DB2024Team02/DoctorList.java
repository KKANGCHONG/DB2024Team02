package DB2024Team02;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class DoctorList extends JFrame {
    // JDBC 드라이버 이름과 데이터베이스 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db2024team02";
    // 데이터베이스 계정 정보
    static final String USER = "DB2024Team02";
    static final String PASS = "DB2024Team02";

    // 테이블 및 모델
    private JTable table;
    private DefaultTableModel model;
    
    public DoctorList() {
        setTitle("Doctor List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // 테이블 모델 설정
        model = new DefaultTableModel(new String[]{"DoctorID", "DoctorName", "DoctorDepartment", "ContactNumber"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);
        
        JButton deleteButton = new JButton("Delete Doctor");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int doctorId = Integer.parseInt(model.getValueAt(row, 0).toString());
                    deleteDoctor(doctorId);
                    loadDoctors(); // 리스트 다시 불러오기
                }
            }
        });
        c.add(deleteButton, BorderLayout.SOUTH);
        
        loadDoctors(); // 의사 목록 로드
        setVisible(true);
    }

    private void loadDoctors() {
        model.setRowCount(0); // 테이블 초기화
        String query = "SELECT * FROM DB2024_Doctor";  // 모든 칼럼을 선택하는 쿼리로 변경
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int doctorId = rs.getInt("DoctorID");
                String doctorName = rs.getString("DoctorName");
                String doctorDepartment = rs.getString("DoctorDepartment");
                String contactNumber = rs.getString("ContactNumber");
                

                // 모델에 행 추가, 모든 칼럼을 테이블에 추가합니다.
                model.addRow(new Object[]{
                    doctorId, doctorName, doctorDepartment, contactNumber
                    
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void deleteDoctor(int doctorId) {
        String query = "DELETE FROM DB2024_Doctor WHERE DoctorID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);  // 자동 커밋 비활성화

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, doctorId);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 1) {
                conn.commit();  // 변경 사항 커밋
                JOptionPane.showMessageDialog(this, "Doctor successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                conn.rollback();  // 변경 사항이 없거나 예상치 못한 결과가 발생한 경우 롤백
                JOptionPane.showMessageDialog(this, "No doctor found with ID: " + doctorId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  // 오류 발생 시 롤백
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            JOptionPane.showMessageDialog(this, "Error deleting doctor.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // 커넥션을 원래대로 자동 커밋 활성화
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }A
    }

    

    public static void main(String[] args) {
        new DoctorList();
    }
}
