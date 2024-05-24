package 의료서비스;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

// 현재 상주하는 비상 대응 인력을 보는 창
public class MedicalStaff extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "0614346";

    private JTable table;
    private DefaultTableModel tableModel;

    public MedicalStaff() {
        setTitle("비상 대응 인력");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("현재 상주하는 비상 대응 인력 (Doctor: 노란색, Nurse: 연두색)");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 20)); // 글자 크기 설정
        add(label, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Department");
        tableModel.addColumn("Contact Number");
        tableModel.addColumn("Role");

        table = new JTable(tableModel);
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        table.setFont(new Font("Serif", Font.PLAIN, 18)); // 글자 크기 설정
        table.setRowHeight(25); // 행 높이 설정

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        boolean hasData = false; // 데이터가 있는지 없는지를 추적하는 플래그

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // CurrentOnDutyStaff 뷰를 조회하는 쿼리
            String query = "SELECT * FROM CurrentOnDutyStaff";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hasData = true; // 데이터가 적어도 한 행은 있음
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("Name");
                String department = resultSet.getString("Department");
                String contactNumber = resultSet.getString("ContactNumber");
                String role = resultSet.getString("Role");

                tableModel.addRow(new Object[]{id, name, department, contactNumber, role});
            }

            if (!hasData) { // 데이터가 한 행도 없다면
                tableModel.addRow(new Object[]{"없음", "없음", "없음", "없음", "없음"}); // "없음" 행 추가
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 로드 실패", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class CustomTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String role = (String) table.getValueAt(row, 4); // "Role" 열의 값을 가져옴
            if ("Doctor".equals(role)) {
                cell.setBackground(Color.yellow);
            } else if ("Nurse".equals(role)) {
                cell.setBackground(Color.GREEN);
            } else {
                cell.setBackground(Color.WHITE);
            }

            cell.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 15)); // 셀의 글자 크기 설정
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


