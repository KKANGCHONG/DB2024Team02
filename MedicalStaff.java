import java.awt.EventQueue;

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
    private JFrame mainScreen; // 메인 화면 인스턴스를 저장할 변수

    public MedicalStaff() {
    	getContentPane().setFont(new Font("맑은 고딕", Font.BOLD, 14));
    	getContentPane().setBackground(new Color(255, 255, 255));
    	this.mainScreen = mainScreen; // 메인 화면 인스턴스 저장
        setTitle("비상 대응 인력");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JLabel label = new JLabel("현재 상주하는 비상 대응 인력");
        label.setForeground(new Color(255, 255, 255));
        label.setBackground(new Color(0, 64, 128));
        label.setOpaque(true); // 불투명 설정
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // 글자 크기 설정
        getContentPane().add(label, BorderLayout.NORTH);

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

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

     // 기존의 "뒤로 가기" 버튼 추가 부분을 대체합니다.
        JPanel buttonPanel = new JPanel(); // 버튼을 담을 패널 생성
        buttonPanel.setForeground(new Color(255, 255, 255));
        buttonPanel.setBackground(new Color(0, 64, 128));
        buttonPanel.setOpaque(true); // 불투명 설정
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // 패널의 레이아웃을 플로우 레이아웃으로 설정하고 오른쪽 정렬

        JButton backButton = new JButton("뒤로 가기"); // 뒤로 가기 버튼 생성
        backButton.setBackground(new Color(255, 255, 255)); // 배경색 설정
        backButton.setOpaque(true); // 불투명 설정
        backButton.setContentAreaFilled(true); // 내용 영역 채우기 설정
        backButton.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 폰트 설정
        backButton.addActionListener(e -> {
            this.setVisible(false); // 현재 창을 숨김
            mainScreen.setVisible(true); // 메인 화면을 보이게 함
        });

        buttonPanel.add(backButton); // 패널에 버튼 추가
        getContentPane().add(buttonPanel, BorderLayout.SOUTH); // 패널을 프레임의 SOUTH 영역에 추가


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
   
                cell.setBackground(Color.WHITE);

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
    }
}


