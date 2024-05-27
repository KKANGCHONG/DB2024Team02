//치료 정보 입력

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorDatabaseManager {

```
private Connection connectToDatabase() {
    String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; // 데이터베이스 URL
    String user = "yourUsername"; // 데이터베이스 사용자 이름
    String password = "yourPassword"; // 데이터베이스 비밀번호

    try {
        return DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
        e.printStackTrace();
        return null;
    }
}

public String getUserRole(String username, String password) {
    String role = null;
    try (Connection conn = connectToDatabase()) {
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            role = rs.getString("role");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return role;
}

public void insertPatientTreatment(String username, String password, String insertQuery) {
    if ("Doctor".equals(getUserRole(username, password))) {
        try (Connection conn = connectToDatabase()) {
            PreparedStatement ps = conn.prepareStatement(insertQuery);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("환자 치료 정보가 성공적으로 입력되었습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("오직 의사만이 환자 정보를 입력할 수 있습니다.");
    }
}

public static void main(String[] args) {
    DoctorDatabaseManager manager = new DoctorDatabaseManager();

    // 사용자의 이름과 비밀번호
    String username = "doctorUser";
    String password = "doctorPassword";

    // 데이터베이스에 입력할 환자 치료 정보 쿼리
    String insertQuery = "INSERT INTO DB2024_Treatment (PatientID, DiseaseID, MedicationID, RecommendedTreatment, Date, Dosage, KTAS) VALUES (26, 01, 01, '고혈압 관리: 저염식 식이요법과 규칙적인 운동 병행', '2024-02-01', '10mg', 3), (27, 02, 02, '당뇨병 관리: 혈당 수치 모니터링 및 저탄수화물 식단', '2024-02-02', '500mg', 4), (28, 03, 03, '천식 관리: 규칙적인 약물 복용과 호흡 운동', '2024-02-03', '200mcg', 3), (29, 04, 04, '관절염 관리: 항염증제 복용과 물리치료 병행', '2024-02-04', '400mg', 3);";

    // 환자 치료 정보를 데이터베이스에 입력
    manager.insertPatientTreatment(username, password, insertQuery);
}
