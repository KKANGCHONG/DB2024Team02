import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

//치료 정보 입력
public class ManageTreatment {
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
                // 치료 정보 입력
                System.out.println("새로운 치료 정보를 입력하세요.");
                
                System.out.print("환자 ID: ");
                int patientId = scanner.nextInt();
                scanner.nextLine(); // 입력 버퍼 지우기

                System.out.print("질병 ID: ");
                int diseaseId = scanner.nextInt();
                scanner.nextLine(); // 입력 버퍼 지우기

                System.out.print("약품 ID: ");
                int medicationId = scanner.nextInt();
                scanner.nextLine(); // 입력 버퍼 지우기

                System.out.print("권장 치료: ");
                String recommendedTreatment = scanner.nextLine();

                System.out.print("날짜 (YYYY-MM-DD): ");
                String date = scanner.nextLine();

                System.out.print("투약량: ");
                String dosage = scanner.nextLine();

                System.out.print("KTAS 점수: ");
                int ktas = scanner.nextInt();

                addTreatment(patientId, diseaseId, medicationId, recommendedTreatment, date, dosage, ktas);
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

        boolean isAuthenticated = statement.executeQuery().next();
        statement.close();
        conn.close();

        return isAuthenticated;
    }

    private static void addTreatment(int patientId, int diseaseId, int medicationId, String recommendedTreatment, String date, String dosage, int ktas) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String sql = "INSERT INTO DB2024_Treatment (PatientID, DiseaseID, MedicationID, RecommendedTreatment, Date, Dosage, KTAS) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setInt(1, patientId);
        statement.setInt(2, diseaseId);
        statement.setInt(3, medicationId);
        statement.setString(4, recommendedTreatment);
        statement.setString(5, date);
        statement.setString(6, dosage);
        statement.setInt(7, ktas);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("새 치료 정보가 성공적으로 추가되었습니다.");
        } else {
            System.out.println("치료 정보 추가에 실패했습니다.");
        }

        statement.close();
        conn.close();
    }
}
