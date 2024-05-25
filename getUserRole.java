//의사인지 간호사인지 확인 + 역할 부여
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRoleCheck {

```
private static String getUserRole(String username, String password) {
    String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; // 데이터베이스 URL
    String dbUser = "yourUsername"; // 데이터베이스 사용자 이름
    String dbPassword = "yourPassword"; // 데이터베이스 비밀번호

    String role = null;

    try {
        Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
        String query = "SELECT role FROM users WHERE username = ? AND password = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            role = rs.getString("role");
        }

        rs.close();
        ps.close();
        conn.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return role;
}

public static void main(String[] args) {
    // 사용자 이름과 비밀번호 입력
    String username = "DoctorUser"; // 예제 사용자 이름
    String password = "doctor_password"; // 예제 비밀번호

    // 사용자 역할 확인
    String role = getUserRole(username, password);

    if ("Doctor".equals(role)) {
        System.out.println("사용자는 의사입니다. 환자 정보 입력이 가능합니다.");
        // 환자 정보 입력 로직 실행
    } else if ("Nurse".equals(role)) {
        System.out.println("사용자는 간호사입니다. 환자 정보 입력이 불가능합니다.");
    } else {
        System.out.println("사용자 역할을 확인할 수 없습니다.");
    }
}
