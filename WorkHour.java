import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class WorkHour extends JFrame { 

    private String userType; // ����� ������ �����ϴ� ���� ���� ("Doctor" �Ǵ� "Nurse")
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private int currentUserId; // ���� ����� ID�� �����ϴ� ���� ���� (�ǻ� ID�� ����)
    
    // WorkHour Ŭ������ ������ ����
    public WorkHour(String userType, int currentUserId) {
        this.userType = userType; // ����� ���� ����
        this.currentUserId = currentUserId; // ���� ����� ID ����
        openWorkHoursDialog();
    }

    // ����� �ð� �Է� ���̾�α׸� ���� ���� �޼ҵ� ����
    private void openWorkHoursDialog() {
        JFrame workHoursFrame = new JFrame("����� �ð� �Է�"); // �� JFrame �ν��Ͻ� ����
        workHoursFrame.setSize(400, 300);
        workHoursFrame.setLayout(new GridLayout(5, 2));

        JLabel roleLabel = new JLabel("���� : " + userType); // ���� ǥ�� ���̺� ����
        JLabel idLabel = new JLabel("ID : " + currentUserId); // ID ǥ�� ���̺� ����
        JLabel startTimeLabel = new JLabel("��� �ð� (HH:MM:SS) : "); // ��� �ð� �Է� ���̺� ����
        JTextField startTimeField = new JTextField(); // ��� �ð� �Է� �ʵ� ����
        JLabel endTimeLabel = new JLabel("��� �ð� (HH:MM:SS) : "); // ��� �ð� �Է� ���̺� ����
        JTextField endTimeField = new JTextField(); // ��� �ð� �Է� �ʵ� ����
        JButton submitButton = new JButton("�Է�"); // �Է� ��ư ����

        workHoursFrame.add(roleLabel); // �����ӿ� ���� ���̺� �߰�
        workHoursFrame.add(idLabel); // �����ӿ� ID ���̺� �߰�
        workHoursFrame.add(startTimeLabel); // �����ӿ� ��� �ð� ���̺� �߰�
        workHoursFrame.add(startTimeField); // �����ӿ� ��� �ð� �Է� �ʵ� �߰�
        workHoursFrame.add(endTimeLabel); // �����ӿ� ��� �ð� ���̺� �߰�
        workHoursFrame.add(endTimeField); // �����ӿ� ��� �ð� �Է� �ʵ� �߰�
        workHoursFrame.add(new JLabel()); // �� �� �߰� (���̾ƿ� ������ ����)
        workHoursFrame.add(submitButton); // �����ӿ� �Է� ��ư �߰�

        submitButton.addActionListener(new ActionListener() { // �Է� ��ư�� ���� ActionListener ����
            public void actionPerformed(ActionEvent e) { // ��ư Ŭ�� �̺�Ʈ �ڵ鷯
                String role = userType; // ����� ���� ����
                int id = currentUserId; // ����� ID ����
                String startTime = startTimeField.getText().trim(); // ��� �ð� �ʵ� �� ��������
                String endTime = endTimeField.getText().trim(); // ��� �ð� �ʵ� �� ��������

                String tableName; // �����ͺ��̽� ���̺� �̸��� ������ ���� ����
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
                    pstmt.setString(1, startTime); // ��� �ð� �Ű����� ����
                    pstmt.setString(2, endTime); // ��� �ð� �Ű����� ����
                    pstmt.setInt(3, id); // ����� ID �Ű����� ����

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) { // ���� ���� ���� �ִ� ���
                        JOptionPane.showMessageDialog(workHoursFrame, "������Ʈ ����!");
                        
                    } else { // ���� ���� ���� ���� ���
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
