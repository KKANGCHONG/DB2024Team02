package MEDICAL;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class DoctorList extends JFrame {
    // JDBC ����̹� �̸��� �����ͺ��̽� URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    // �����ͺ��̽� ���� ����
    static final String USER = "root";
    static final String PASS = "root";

    // ���̺� �� ��
    private JTable table;
    private DefaultTableModel model;
    
    public DoctorList() {
        setTitle("Doctor List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(662, 831);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // ���̺� �� ����
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
                    loadDoctors(); // ����Ʈ �ٽ� �ҷ�����
                }
            }
        });
        c.add(deleteButton, BorderLayout.SOUTH);
        
        loadDoctors(); // �ǻ� ��� �ε�
        setVisible(true);
    }

    private void loadDoctors() {
        model.setRowCount(0); // ���̺� �ʱ�ȭ
        String query = "SELECT * FROM DB2024_Doctor WHERE DoctorID != 00000";  // ��� Į���� �����ϴ� ������ ����
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int doctorId = rs.getInt("DoctorID");
                String doctorName = rs.getString("DoctorName");
                String doctorDepartment = rs.getString("DoctorDepartment");
                String contactNumber = rs.getString("ContactNumber");
                

                // �𵨿� �� �߰�, ��� Į���� ���̺� �߰��մϴ�.
                model.addRow(new Object[]{
                    doctorId, doctorName, doctorDepartment, contactNumber
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void deleteDoctor(int doctorId) {
        String query = "UPDATE DB2024_Patient SET DoctorID = '00000' WHERE DoctorID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);  // �ڵ� Ŀ�� ��Ȱ��ȭ

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, doctorId);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                // DoctorID�� '00000'���� ������ ȯ�ڰ� ���� ���� �ǻ� ������ ����
                query = "DELETE FROM DB2024_Doctor WHERE DoctorID = ?";
                pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, doctorId);
                affectedRows = pstmt.executeUpdate();

                if (affectedRows == 1) {
                    conn.commit();  // ���� ���� Ŀ��
                    JOptionPane.showMessageDialog(this, "Doctor successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    conn.rollback();  // ���� ������ ���ų� ����ġ ���� ����� �߻��� ��� �ѹ�
                    JOptionPane.showMessageDialog(this, "No doctor found with ID: " + doctorId, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                conn.rollback();  // ���� ������ ���ų� ����ġ ���� ����� �߻��� ��� �ѹ�
                JOptionPane.showMessageDialog(this, "No patients found with DoctorID: " + doctorId, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();  // ���� �߻� �� �ѹ�
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
                    conn.setAutoCommit(true); // Ŀ�ؼ��� ������� �ڵ� Ŀ�� Ȱ��ȭ
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }


    

    public static void main(String[] args) {
        new DoctorList();
    }
}
