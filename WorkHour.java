import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.*;

public class WorkHour extends JFrame { 

    private String userType; // 사용자 유형을 저장하는 변수 선언 ("Doctor" 또는 "Nurse")
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    
    private int currentUserId; // 현재 사용자 ID를 저장하는 변수 선언 (의사 ID로 가정)
    
    // WorkHour 클래스의 생성자 정의
    public WorkHour(String userType, int currentUserId) {
        this.userType = userType; // 사용자 유형 설정
        this.currentUserId = currentUserId; // 현재 사용자 ID 설정
        openWorkHoursDialog();
    }

    // 출퇴근 시간 입력 다이얼로그를 열기 위한 메소드 정의
    private void openWorkHoursDialog() {
        JFrame workHoursFrame = new JFrame("출퇴근 시간 입력"); // 새 JFrame 인스턴스 생성
        workHoursFrame.setSize(400, 300);
        workHoursFrame.setLayout(new GridLayout(5, 2));

        JLabel roleLabel = new JLabel("역할 : " + userType); // 역할 표시 레이블 생성
        JLabel idLabel = new JLabel("ID : " + currentUserId); // ID 표시 레이블 생성
        JLabel startTimeLabel = new JLabel("출근 시간 (HH:MM:SS) : "); // 출근 시간 입력 레이블 생성
        JTextField startTimeField = new JTextField(); // 출근 시간 입력 필드 생성
        JLabel endTimeLabel = new JLabel("퇴근 시간 (HH:MM:SS) : "); // 퇴근 시간 입력 레이블 생성
        JTextField endTimeField = new JTextField(); // 퇴근 시간 입력 필드 생성
        JButton submitButton = new JButton("입력"); // 입력 버튼 생성

        workHoursFrame.add(roleLabel); // 프레임에 역할 레이블 추가
        workHoursFrame.add(idLabel); // 프레임에 ID 레이블 추가
        workHoursFrame.add(startTimeLabel); // 프레임에 출근 시간 레이블 추가
        workHoursFrame.add(startTimeField); // 프레임에 출근 시간 입력 필드 추가
        workHoursFrame.add(endTimeLabel); // 프레임에 퇴근 시간 레이블 추가
        workHoursFrame.add(endTimeField); // 프레임에 퇴근 시간 입력 필드 추가
        workHoursFrame.add(new JLabel()); // 빈 셀 추가 (레이아웃 정렬을 위해)
        workHoursFrame.add(submitButton); // 프레임에 입력 버튼 추가

        submitButton.addActionListener(new ActionListener() { // 입력 버튼에 대한 ActionListener 정의
            public void actionPerformed(ActionEvent e) { // 버튼 클릭 이벤트 핸들러
                String role = userType; // 사용자 역할 설정
                int id = currentUserId; // 사용자 ID 설정
                String startTime = startTimeField.getText().trim(); // 출근 시간 필드 값 가져오기
                String endTime = endTimeField.getText().trim(); // 퇴근 시간 필드 값 가져오기

                String tableName; // 데이터베이스 테이블 이름을 저장할 변수 선언
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
                    pstmt.setString(1, startTime); // 출근 시간 매개변수 설정
                    pstmt.setString(2, endTime); // 퇴근 시간 매개변수 설정
                    pstmt.setInt(3, id); // 사용자 ID 매개변수 설정

                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) { // 영향 받은 행이 있는 경우
                        JOptionPane.showMessageDialog(workHoursFrame, "업데이트 성공!");
                        
                    } else { // 영향 받은 행이 없는 경우
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
