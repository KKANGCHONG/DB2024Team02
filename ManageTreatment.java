import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ManageTreatment extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String USER = "root";
    private static final String PASS = "root";

    private JTextField doctorIdField, passwordField, patientIdField, diseaseIdField, medicationIdField, recommendedTreatmentField, dateField, dosageField, ktasField;
    private JButton authenticateButton, addButton;
    private JLabel statusLabel;

    public ManageTreatment() {
    	 setTitle("치료 관리 시스템");
    	    setSize(400, 600);
    	    setLayout(new GridLayout(11, 2, 5, 5)); // 11 rows, 2 columns, 5px horizontal and vertical gaps

    	    add(new JLabel("의사 ID : "));
    	    doctorIdField = new JTextField(20);
    	    add(doctorIdField);
    	    add(new JLabel());

    	    add(new JLabel("비밀번호 : "));
    	    passwordField = new JTextField(20);
    	    add(passwordField);

    	    authenticateButton = new JButton("인증");
    	    add(authenticateButton);

    	    add(new JLabel("환자 ID : "));
    	    patientIdField = new JTextField(20);
    	    add(patientIdField);
    	    add(new JLabel());

    	    add(new JLabel("질병 ID : "));
    	    diseaseIdField = new JTextField(20);
    	    add(diseaseIdField);
    	    add(new JLabel());

    	    add(new JLabel("약품 ID : "));
    	    medicationIdField = new JTextField(20);
    	    add(medicationIdField);
    	    add(new JLabel());

    	    add(new JLabel("권장 치료 : "));
    	    recommendedTreatmentField = new JTextField(20);
    	    add(recommendedTreatmentField);
    	    add(new JLabel());

    	    add(new JLabel("날짜 (YYYY-MM-DD) : "));
    	    dateField = new JTextField(20);
    	    add(dateField);
    	    add(new JLabel());

    	    add(new JLabel("투약량 : "));
    	    dosageField = new JTextField(20);
    	    add(dosageField);
    	    add(new JLabel());

    	    add(new JLabel("KTAS 점수 : "));
    	    ktasField = new JTextField(20);
    	    add(ktasField);
    	    add(new JLabel());

    	    addButton = new JButton("치료 정보 추가");
    	    add(new JLabel()); // Empty cell to align the button
    	    add(addButton);

    	    statusLabel = new JLabel("상태: 대기 중");
    	    add(new JLabel()); // Empty cell to align the label
    	    add(statusLabel);

        // 의사 인증 버튼 액션
        authenticateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int doctorId = Integer.parseInt(doctorIdField.getText());
                    int password = Integer.parseInt(passwordField.getText());
                    if (authenticateDoctor(doctorId, password)) {
                        statusLabel.setText("인증 성공");
                    } else {
                        statusLabel.setText("인증 실패");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 치료 정보 추가 버튼 액션
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int patientId = Integer.parseInt(patientIdField.getText());
                    int diseaseId = Integer.parseInt(diseaseIdField.getText());
                    int medicationId = Integer.parseInt(medicationIdField.getText());
                    String recommendedTreatment = recommendedTreatmentField.getText();
                    String date = dateField.getText();
                    String dosage = dosageField.getText();
                    int ktas = Integer.parseInt(ktasField.getText());

                    addTreatment(patientId, diseaseId, medicationId, recommendedTreatment, date, dosage, ktas);
                    statusLabel.setText("치료 정보 추가 성공");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("치료 정보 추가 실패");
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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

        statement.executeUpdate();

        statement.close();
        conn.close();
    }

    public static void main(String[] args) {
        new ManageTreatment();
    }
}