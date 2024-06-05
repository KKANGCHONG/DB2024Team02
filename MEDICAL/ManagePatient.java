package MEDICAL;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagePatient extends JFrame {
    // 데이터베이스 연결 URL 및 사용자 정보 상수
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String USER = "root";
    private static final String PASS = "root";

    // 사용자 입력 필드 선언
    private JTextField idField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField birthField;
    private JTextField residentNumField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField guardianPhoneField;
    private JTextField nurseIdField;
    private JTextArea outputArea;

    public ManagePatient() {
        // 프레임 설정
        setTitle("환자 정보 수정");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // 사용자 입력을 위한 패널 생성 및 레이아웃 설정
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));

        // 각 입력 필드와 라벨 추가
        inputPanel.add(new JLabel("의사 ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("비밀번호:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("이름:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("생년월일(YYYY-MM-DD):"));
        birthField = new JTextField();
        inputPanel.add(birthField);

        inputPanel.add(new JLabel("주민번호:"));
        residentNumField = new JTextField();
        inputPanel.add(residentNumField);

        inputPanel.add(new JLabel("주소:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        inputPanel.add(new JLabel("전화번호:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("보호자 전화번호:"));
        guardianPhoneField = new JTextField();
        inputPanel.add(guardianPhoneField);

        inputPanel.add(new JLabel("담당 간호사ID:"));
        nurseIdField = new JTextField();
        inputPanel.add(nurseIdField);

        // 제출 버튼 생성 및 액션 리스너 추가
        JButton submitButton = new JButton("환자 정보 추가");
        submitButton.addActionListener(e -> {
            try {
                // 입력된 의사 ID와 비밀번호 가져오기
                int doctorId = Integer.parseInt(idField.getText());
                int password = Integer.parseInt(new String(passwordField.getPassword()));

                // 의사 ID가 유효한지 검사
                if (doctorId / 10000 == 0) {
                    outputArea.setText("환자 정보를 입력할 수 없습니다.");
                    return;
                }

                // 의사 인증 및 환자 정보 추가
                if (authenticateDoctor(doctorId, password)) {
                    String name = nameField.getText();
                    String birth = birthField.getText();
                    String residentNum = residentNumField.getText();
                    String address = addressField.getText();
                    String phone = phoneField.getText();
                    String guardianPhone = guardianPhoneField.getText();
                    int nurseId = Integer.parseInt(nurseIdField.getText());

                    addPatient(name, birth, residentNum, address, phone, guardianPhone, doctorId, nurseId);
                    outputArea.setText("새 환자 정보가 성공적으로 추가되었습니다.");
                } else {
                    outputArea.setText("인증에 실패했습니다. 프로그램을 종료합니다.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                outputArea.setText("오류가 발생했습니다.");
            }
        });

        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);

        // 출력 영역 생성 및 추가
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }

    // 의사 인증 메서드
    private boolean authenticateDoctor(int doctorId, int password) throws Exception {
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

    // 환자 정보 추가 메서드
    private void addPatient(String name, String birth, String residentNum, String address, String phone, String guardianPhone, int doctorId, int nurseId) throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String sql = "INSERT INTO DB2024_Patient (Name, Birth, ResidentNum, Address, Phone, GuardianPhone, DoctorID, NurseID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);

        statement.setString(1, name);
        statement.setString(2, birth);
        statement.setString(3, residentNum);
        statement.setString(4, address);
        statement.setString(5, phone);
        statement.setString(6, guardianPhone);
        statement.setInt(7, doctorId);
        statement.setInt(8, nurseId);

        statement.executeUpdate();

        statement.close();
        conn.close();
    }

    // 메인 메서드: 프로그램 시작점
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagePatient frame = new ManagePatient();
            frame.setVisible(true);
        });
    }
}