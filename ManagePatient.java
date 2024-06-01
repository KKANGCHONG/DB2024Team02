import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagePatient extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String USER = "root";
    private static final String PASS = "root";

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
        setTitle("ȯ�� ���� ����");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));

        inputPanel.add(new JLabel("�ǻ� ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("��й�ȣ:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        inputPanel.add(new JLabel("�̸�:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("�������(YYYY-MM-DD):"));
        birthField = new JTextField();
        inputPanel.add(birthField);

        inputPanel.add(new JLabel("�ֹι�ȣ:"));
        residentNumField = new JTextField();
        inputPanel.add(residentNumField);

        inputPanel.add(new JLabel("�ּ�:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        inputPanel.add(new JLabel("��ȭ��ȣ:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("��ȣ�� ��ȭ��ȣ:"));
        guardianPhoneField = new JTextField();
        inputPanel.add(guardianPhoneField);

        inputPanel.add(new JLabel("��� ��ȣ��ID:"));
        nurseIdField = new JTextField();
        inputPanel.add(nurseIdField);

        JButton submitButton = new JButton("ȯ�� ���� �߰�");
        submitButton.addActionListener(e -> {
            try {
                int doctorId = Integer.parseInt(idField.getText());
                int password = Integer.parseInt(new String(passwordField.getPassword()));

                if (doctorId / 10000 == 0) {
                    outputArea.setText("ȯ�� ������ �Է��� �� �����ϴ�.");
                    return;
                }

                if (authenticateDoctor(doctorId, password)) {
                    String name = nameField.getText();
                    String birth = birthField.getText();
                    String residentNum = residentNumField.getText();
                    String address = addressField.getText();
                    String phone = phoneField.getText();
                    String guardianPhone = guardianPhoneField.getText();
                    int nurseId = Integer.parseInt(nurseIdField.getText());

                    addPatient(name, birth, residentNum, address, phone, guardianPhone, doctorId, nurseId);
                    outputArea.setText("�� ȯ�� ������ ���������� �߰��Ǿ����ϴ�.");
                } else {
                    outputArea.setText("������ �����߽��ϴ�. ���α׷��� �����մϴ�.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                outputArea.setText("������ �߻��߽��ϴ�.");
            }
        });

        inputPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagePatient frame = new ManagePatient();
            frame.setVisible(true);
        });
    }
}
