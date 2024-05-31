package DB2024Team02;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class MedicationDiseasePage extends JFrame {
    // JDBC 드라이버 이름과 데이터베이스 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/db2024team02";
    // 데이터베이스 계정 정보
    static final String USER = "DB2024Team02";
    static final String PASS = "DB2024Team02";
    // 테이블 및 모델
    private JTable table;
    private DefaultTableModel model;
    
    public MedicationDiseasePage() {
        setTitle("Doctor List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        
        model = new DefaultTableModel(new String[]{"Number", "DiseaseName", "MedicationName"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);

        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDiseaseJoinMedication();
            }
        });

        JButton deleteButton = new JButton("Delete Entry");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteDiseaseMedication();
            }
        });

        JPanel panel = new JPanel();
        panel.add(loadButton);
        panel.add(deleteButton);
        c.add(panel, BorderLayout.SOUTH);

        loadDiseaseJoinMedication();
        setVisible(true);
    }
    
    private void loadDiseaseJoinMedication() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            String sql = "SELECT DB2024_Disease.DiseaseID, DB2024_Disease.DiseaseName, DB2024_Medication.MedicationName FROM DB2024_Disease JOIN DB2024_Medication ON DB2024_Disease.DiseaseID = DB2024_Medication.MedicationID";
            ResultSet rs = stmt.executeQuery(sql);
            model.setRowCount(0); // Clear existing data
            while (rs.next()) {
                int id = rs.getInt("DiseaseID");
                String diseaseName = rs.getString("DiseaseName");
                String medicationName = rs.getString("MedicationName");
                model.addRow(new Object[]{id, diseaseName, medicationName});
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    
    private void deleteDiseaseMedication() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this entry?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Object id = model.getValueAt(row, 0);
                try {
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    conn.setAutoCommit(false); // Start transaction
                    String sql = "DELETE FROM DB2024_Disease WHERE DiseaseID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, (Integer) id);
                    pstmt.executeUpdate();
                    
                    sql = "DELETE FROM DB2024_Medication WHERE MedicationID = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, (Integer) id);
                    pstmt.executeUpdate();

                    conn.commit(); // Commit transaction
                    conn.setAutoCommit(true);
                    model.removeRow(row);
                    
                    pstmt.close();
                    conn.close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an entry to delete.");
        }
    }
    
    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        new MedicationDiseasePage();
    }
}

