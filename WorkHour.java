import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class WorkHour extends JFrame { 

    private String userType; // 사용자 유형 저장 ("Doctor" 또는 "Nurse")
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private int currentUserId; // 현재 사용자 ID 저장
    
    public WorkHour(String userType, int currentUserId) {
        this.userType = userType;
        this.currentUserId = currentUserId;
        openWorkHoursDialog(); // 출퇴근 시간 입력 다이얼로그 열기
    }

    private void openWorkHoursDialog() {
        JFrame workHoursFrame = new JFrame("출퇴근 시간 입력");
        workHoursFrame.setSize(400, 300);
        workHoursFrame.setLayout(new GridLayout(5, 2));

        // 레이블, 입력 필드, 버튼 생성
        JLabel roleLabel = new JLabel("역할 : " + userType);
        JLabel idLabel = new JLabel("ID : " + currentUserId);
        JLabel startTimeLabel = new JLabel("출근 시간 (HH:MM:SS) : ");
        JTextField startTimeField = new JTextField();
        JLabel endTimeLabel = new JLabel("퇴근 시간 (HH:MM:SS) : ");
        JTextField endTimeField = new JTextField();
        JButton submitButton = new JButton("입력");

        // 컴포넌트를 프레임에 추가
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
                if (userType.equals("Doctor")) { // 사용자 유형이 의사인 경우
                    tableName = "DB2024_Doctor"; // 의사 테이블 이름 설정
                } else if (userType.equals("Nurse")) { // 사용자 유형이 간호사인 경우
                    tableName = "DB2024_Nurse"; // 간호사 테이블 이름 설정
                } else {
                    JOptionPane.showMessageDialog(workHoursFrame, "잘못된 입력입니다. 'doctor' 또는 'nurse'를 입력하세요.");
                    return;
                }

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    String updateQuery = "UPDATE " + tableName + " SET StartTime = ?, EndTime = ? WHERE " + role + "ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                    pstmt.setString(1, startTime); // 출근 시간 설정
                    pstmt.setString(2, endTime); // 퇴근 시간 설정
                    pstmt.setInt(3, id); // 사용자 ID 설정

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) { // 업데이트 성공 시
                        JOptionPane.showMessageDialog(workHoursFrame, "업데이트 성공!");
                    } else { // 업데이트 실패 시
                        JOptionPane.showMessageDialog(workHoursFrame, "업데이트 실패: 해당 ID를 찾을 수 없습니다.");
                    }

                    pstmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(workHoursFrame, "오류가 발생했습니다: " + ex.getMessage());
                }
            }
        });

        workHoursFrame.setVisible(true);
    }
}

    }
}
