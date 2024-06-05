package MEDICAL;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class WorkHour extends JFrame { 

    private String userType; // ����� ���� ���� ("Doctor" �Ǵ� "Nurse")
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private int currentUserId; // ���� ����� ID ����
    
    public WorkHour(String userType, int currentUserId) {
        this.userType = userType;
        this.currentUserId = currentUserId;
        openWorkHoursDialog(); // ����� �ð� �Է� ���̾�α� ����
    }

    private void openWorkHoursDialog() {
        JFrame workHoursFrame = new JFrame("����� �ð� �Է�");
        workHoursFrame.setSize(400, 300);
        workHoursFrame.setLayout(new GridLayout(5, 2));

        // ���̺�, �Է� �ʵ�, ��ư ����
        JLabel roleLabel = new JLabel("���� : " + userType);
        JLabel idLabel = new JLabel("ID : " + currentUserId);
        JLabel startTimeLabel = new JLabel("��� �ð� (HH:MM:SS) : ");
        JTextField startTimeField = new JTextField();
        JLabel endTimeLabel = new JLabel("��� �ð� (HH:MM:SS) : ");
        JTextField endTimeField = new JTextField();
        JButton submitButton = new JButton("�Է�");

        // ������Ʈ�� �����ӿ� �߰�
        workHoursFrame.add(roleLabel);
        workHoursFrame.add(idLabel);
        workHoursFrame.add(startTimeLabel);
        workHoursFrame.add(startTimeField);
        workHoursFrame.add(endTimeLabel);
        workHoursFrame.add(endTimeField);
        workHoursFrame.add(new JLabel());
        workHoursFrame.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String role = userType;
                int id = currentUserId;
                String startTime = startTimeField.getText().trim();
                String endTime = endTimeField.getText().trim();

                String tableName; 
                if (userType.equals("Doctor")) { // ����� ������ �ǻ��� ���
                    tableName = "DB2024_Doctor"; // �ǻ� ���̺� �̸� ����
                } else if (userType.equals("Nurse")) { // ����� ������ ��ȣ���� ���
                    tableName = "DB2024_Nurse"; // ��ȣ�� ���̺� �̸� ����
                } else {
                    JOptionPane.showMessageDialog(workHoursFrame, "�߸��� �Է��Դϴ�. 'doctor' �Ǵ� 'nurse'�� �Է��ϼ���.");
                    return;
                }

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    String updateQuery = "UPDATE " + tableName + " SET StartTime = ?, EndTime = ? WHERE " + role + "ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                    pstmt.setString(1, startTime); // ��� �ð� ����
                    pstmt.setString(2, endTime); // ��� �ð� ����
                    pstmt.setInt(3, id); // ����� ID ����

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) { // ������Ʈ ���� ��
                        JOptionPane.showMessageDialog(workHoursFrame, "������Ʈ ����!");
                    } else { // ������Ʈ ���� ��
                        JOptionPane.showMessageDialog(workHoursFrame, "������Ʈ ����: �ش� ID�� ã�� �� �����ϴ�.");
                    }

                    pstmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(workHoursFrame, "������ �߻��߽��ϴ�: " + ex.getMessage());
                }
            }
        });

        workHoursFrame.setVisible(true);
    }
}