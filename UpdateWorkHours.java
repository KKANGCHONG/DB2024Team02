import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

//근무시간 입력
public class UpdateWorkHours {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/Hospital";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("의사 또는 간호사(doctor/nurse)를 입력하세요: ");
        String role = scanner.nextLine().trim().toLowerCase();

        System.out.print("ID를 입력하세요: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 개행 문자 처리

        System.out.print("출근 시간을 입력하세요 (HH:MM:SS): ");
        String startTime = scanner.nextLine().trim();

        System.out.print("퇴근 시간을 입력하세요 (HH:MM:SS): ");
        String endTime = scanner.nextLine().trim();

        String tableName;
        if (role.equals("doctor")) {
            tableName = "DB2024_Doctor";
        } else if (role.equals("nurse")) {
            tableName = "DB2024_Nurse";
        } else {
            System.out.println("잘못된 입력입니다. 'doctor' 또는 'nurse'를 입력하세요.");
            scanner.close();
            return;
        }

        // 데이터베이스 연결 및 업데이트
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String updateQuery = "UPDATE " + tableName + " SET StartTime = ?, EndTime = ? WHERE " + role + "ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, startTime);
            pstmt.setString(2, endTime);
            pstmt.setInt(3, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("업데이트 성공!");
            } else {
                System.out.println("업데이트 실패: 해당 ID를 찾을 수 없습니다.");
            }

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
