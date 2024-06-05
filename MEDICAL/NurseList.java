package MEDICAL;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;

public class NurseList extends JFrame {
    // JDBC ����̹� �̸��� �����ͺ��̽� URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    // �����ͺ��̽� ���� ����
    static final String USER = "root";
    static final String PASS = "root";

    // ���̺� �� ��
    private JTable table;
    private DefaultTableModel model;
    
    public NurseList() {
        setTitle("Nurse List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        // ���̺� �� ����
        model = new DefaultTableModel(new String[]{"NurseID", "NurseName", "NurseDepartment", "ContactNumber"}, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        c.add(sp, BorderLayout.CENTER);

        
        loadNurses(); // ��ȣ�� ��� �ε�
        setVisible(true);
    }

    private void loadNurses() {
        model.setRowCount(0); // ���̺� �ʱ�ȭ
        String query = "SELECT * FROM DB2024_Nurse";  // ��� Į���� �����ϴ� ������ ����
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int NurseId = rs.getInt("NurseID");
                String NurseName = rs.getString("NurseName");
                String NurseDepartment = rs.getString("NurseDepartment");
                String contactNumber = rs.getString("ContactNumber");
                

                // �𵨿� �� �߰�, ��� Į���� ���̺� �߰��մϴ�.
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