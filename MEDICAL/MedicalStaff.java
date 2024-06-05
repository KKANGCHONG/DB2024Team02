package MEDICAL;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

// ���� �����ϴ� ��� ���� �η��� ���� â
public class MedicalStaff extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private JTable table;
    private DefaultTableModel tableModel;
    private JFrame mainScreen; // ���� ȭ�� �ν��Ͻ��� ������ ����

    public MedicalStaff() {
    getContentPane().setFont(new Font("HY������M", Font.PLAIN, 14));
    getContentPane().setBackground(new Color(255, 255, 255));
    this.mainScreen = mainScreen; // ���� ȭ�� �ν��Ͻ� ����
        setTitle("��� ���� �η�");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        //����
        JLabel label = new JLabel("���� �����ϴ� ��� ���� �η�");
        label.setForeground(new Color(255, 255, 255));
        label.setBackground(new Color(0, 64, 128));
        label.setOpaque(true); // ������ ����
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("HY������M", Font.PLAIN, 20)); // ���� ũ�� ����
        getContentPane().add(label, BorderLayout.NORTH);

        //���̺� ���̾ƿ�
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Department");
        tableModel.addColumn("Contact Number");
        tableModel.addColumn("Role");

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        table.setFont(new Font("HY������M", Font.PLAIN, 18)); // ���� ũ�� ����
        table.setRowHeight(25); // �� ���� ����

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        //�ڷΰ��� ��ư
        JPanel buttonPanel = new JPanel(); // ��ư�� ���� �г� ����
        buttonPanel.setForeground(new Color(255, 255, 255));
        buttonPanel.setBackground(new Color(0, 64, 128));
        buttonPanel.setOpaque(true); // ������ ����
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // �г��� ���̾ƿ��� �÷ο� ���̾ƿ����� �����ϰ� ������ ����

        JButton backButton = new JButton("�ڷ� ����"); // �ڷ� ���� ��ư ����
        backButton.setBackground(new Color(255, 255, 255)); // ���� ����
        backButton.setOpaque(true); // ������ ����
        backButton.setContentAreaFilled(true); // ���� ���� ä��� ����
        backButton.setFont(new Font("HY������M", Font.PLAIN, 14)); // ��Ʈ ����
        backButton.addActionListener(e -> {
            this.setVisible(false); // ���� â�� ����
            mainScreen.setVisible(true); // ���� ȭ���� ���̰� ��
        });
        
        

        buttonPanel.add(backButton); // �гο� ��ư �߰�
        getContentPane().add(buttonPanel, BorderLayout.SOUTH); // �г��� �������� SOUTH ������ �߰�


        loadData();
    }

    //DB�� ���� �����͸� �޾ƿ� ���̺� ǥ���ϴ� �Լ�
    private void loadData() {
        boolean hasData = false; // �����Ͱ� �ִ��� �������� �����ϴ� �÷���

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // CurrentOnDutyStaff �並 ��ȸ�ϴ� ����
            String query = "SELECT * FROM CurrentOnDutyStaff";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hasData = true; // �����Ͱ� ��� �� ���� ����
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String department = resultSet.getString("Department");
                String contactNumber = resultSet.getString("ContactNumber");
                String role = resultSet.getString("Role");

                tableModel.addRow(new Object[]{id, name, department, contactNumber, role});
            }

            if (!hasData) { // �����Ͱ� �� �൵ ���ٸ�
                tableModel.addRow(new Object[]{"����", "����", "����", "����", "����"}); // "����" �� �߰�
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "������ �ε� ����", "����", JOptionPane.ERROR_MESSAGE);
        }
    }

    //JTable�� �� ���� ���� �������� Ŀ���͸���¡
    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String role = (String) table.getValueAt(row, 4); // "Role" ���� ���� ������
   
                cell.setBackground(Color.WHITE);

            cell.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 15)); // ���� ���� ũ�� ����
            return cell;
        }
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
            MedicalStaff gui = new MedicalStaff();
            gui.setVisible(true);
        });
    }
}

