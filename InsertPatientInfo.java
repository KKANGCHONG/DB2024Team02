//환자 정보 입력

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertPatientInfo {

```
public static void main(String[] args) {
    // 데이터베이스 연결 정보
    String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; // 데이터베이스 URL
    String user = "yourUsername"; // 사용자 이름
    String password = "yourPassword"; // 비밀번호

    Scanner scanner = new Scanner(System.in);
    try {
        // 데이터베이스 연결
        Connection conn = DriverManager.getConnection(url, user, password);

        // TODO: 사용자가 의사인지 확인하는 로직 추가 (가정: 사용자가 이미 의사로 확인됨)

        // 환자 정보 입력
        String insertQuery = "INSERT INTO DB2024_patient (PatientID, Name, Birth, Address, Phone, GuardianPhone, DoctorID, NurseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(insertQuery);

        // 환자 정보 입력 예제
        ps.setInt(1, 26); // PatientID
        ps.setString(2, "박진우"); // Name
        ps.setString(3, "1996-01-01"); // Birth
        ps.setString(4, "서울시 은평구"); // Address
        ps.setString(5, "010-5555-6666"); // Phone
        ps.setString(6, "010-7777-8888"); // GuardianPhone
        ps.setInt(7, 1); // DoctorID
        ps.setInt(8, 1); // NurseID

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("환자 정보가 성공적으로 입력되었습니다.");
        } else {
            System.out.println("환자 정보 입력 실패.");
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
