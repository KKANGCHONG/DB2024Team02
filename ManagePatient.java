import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

//환자 정보 입력
public class ManagePatient {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Hospital";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("의사 ID와 비밀번호를 입력해주세요.");
            System.out.print("ID: ");
            int doctorId = scanner.nextInt();
            if(doctorId/10000 == 0) {
            	System.out.print("환자 정보를 입력할 수 없습니다.");
            	System.exit(0);
            }
            System.out.print("비밀번호: ");
            int password = scanner.nextInt();
            scanner.nextLine(); // 입력 버퍼 지우기

            // 의사 인증
            if (authenticateDoctor(doctorId, password)) {
                // 환자 정보 입력
                System.out.println("새 환자의 정보를 입력하세요.");
                
                System.out.print("환자ID: ");
                int PatientID = scanner.nextInt();
                scanner.nextLine();

                System.out.print("이름: ");
                String name = scanner.nextLine();

                System.out.print("생년월일(YYYY-MM-DD): ");
                String birth = scanner.nextLine();

                System.out.print("주소: ");
                String address = scanner.nextLine();

                System.out.print("전화번호: ");
                String phone = scanner.nextLine();

                System.out.print("보호자 전화번호: ");
                String guardianPhone = scanner.nextLine();

                System.out.print("담당 간호사ID: ");
                int nurseId = scanner.nextInt();

                addPatient(PatientID, name, birth, address, phone, guardianPhone, doctorId, nurseId);
            } else {
                System.out.println("인증에 실패했습니다. 프로그램을 종료합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static boolean authenticateDoctor(int doctorId, int password) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String sql = "SELECT * FROM DB2024_Doctor WHERE DoctorID = ? AND PassWord = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setInt(1, doctorId);
        statement.setInt(2, password);

        ResultSet resultSet = statement.executeQuery();
        boolean isAuthenticated = resultSet.next();

        resultSet.close();
        statement.close();
        conn.close();

        return isAuthenticated;
    }

    private static void addPatient(int PatientID, String name, String birth, String address, String phone, String guardianPhone, int doctorId, int nurseId) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String sql = "INSERT INTO DB2024_Patient (PatientID, Name, Birth, Address, Phone, GuardianPhone, DoctorID, NurseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, PatientID);
        statement.setString(2, name);
        statement.setString(3, birth);
        statement.setString(4, address);
        statement.setString(5, phone);
        statement.setString(6, guardianPhone);
        statement.setInt(7, doctorId);
        statement.setInt(8, nurseId);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("새 환자 정보가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("환자 정보 추가에 실패했습니다.");
        }

        statement.close();
        conn.close();
    }
}
