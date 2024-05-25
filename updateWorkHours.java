//출퇴근 시간 입력
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class updateWorkHours {
public static void main(String[] args) {
// 데이터베이스 연결 정보
String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; // 데이터베이스 URL
String user = "yourUsername"; // 사용자 이름
String password = "yourPassword"; // 비밀번호

```
    Scanner scanner = new Scanner(System.in);
    try {
        // 데이터베이스 연결
        Connection conn = DriverManager.getConnection(url, user, password);

        // 사용자 입력 받기
        System.out.println("사용자 ID를 입력하세요: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // 숫자 입력 후 엔터 처리
        System.out.println("새로운 출근 시간을 입력하세요 (HH:MM:SS): ");
        String startTime = scanner.nextLine();
        System.out.println("새로운 퇴근 시간을 입력하세요 (HH:MM:SS): ");
        String endTime = scanner.nextLine();

        // 의사와 간호사의 출퇴근 시간을 업데이트하는 쿼리 준비
        String updateQuery = null;
        if (userId % 2 == 0) { // 간호사 ID가 짝수라고 가정
            updateQuery = "UPDATE DB2024_Nurse SET StartTime = ?, EndTime = ? WHERE NurseID = ?";
        } else { // 의사 ID가 홀수라고 가정
            updateQuery = "UPDATE DB2024_Doctor SET StartTime = ?, EndTime = ? WHERE DoctorID = ?";
        }

        // 쿼리 실행
        PreparedStatement ps = conn.prepareStatement(updateQuery);
        ps.setString(1, startTime);
        ps.setString(2, endTime);
        ps.setInt(3, userId);
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("출퇴근 시간이 성공적으로 변경되었습니다.");
        } else {
            System.out.println("해당 ID를 가진 사용자가 없습니다.");
        }

        // 자원 해제
        ps.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        scanner.close();
    }
}
