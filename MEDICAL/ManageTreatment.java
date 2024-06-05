package MEDICAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ManageTreatment extends JFrame {
    // �����ͺ��̽� ���� URL �� ����� ���� ���
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String USER = "root";
    private static final String PASS = "root";

    // ����� �Է� �ʵ� ����
    private JTextField doctorIdField, passwordField, patientIdField, diseaseIdField, medicationIdField, recommendedTreatmentField, dateField, dosageField, ktasField;
    private JButton authenticateButton, addButton;
    private JLabel statusLabel;

    public ManageTreatment() {
        // ������ ����
        setTitle("ġ�� ���� �ý���");
        setSize(400, 600);
        setLayout(new GridLayout(11, 2, 5, 5)); // 11��, 2��, 5px ���� �� ���� ����

        // �� �Է� �ʵ�� �� �߰�
        add(new JLabel("�ǻ� ID : "));
        doctorIdField = new JTextField(20);
        add(doctorIdField);
        add(new JLabel());

        add(new JLabel("��й�ȣ : "));
        passwordField = new JTextField(20);
        add(passwordField);

        authenticateButton = new JButton("����");
        add(authenticateButton);

        add(new JLabel("ȯ�� ID : "));
        patientIdField = new JTextField(20);
        add(patientIdField);
        add(new JLabel());

        add(new JLabel("���� ID : "));
        diseaseIdField = new JTextField(20);
        add(diseaseIdField);
        add(new JLabel());

        add(new JLabel("��ǰ ID : "));
        medicationIdField = new JTextField(20);
        add(medicationIdField);
        add(new JLabel());

        add(new JLabel("���� ġ�� : "));
        recommendedTreatmentField = new JTextField(20);
        add(recommendedTreatmentField);
        add(new JLabel());

        add(new JLabel("��¥ (YYYY-MM-DD) : "));
        dateField = new JTextField(20);
        add(dateField);
        add(new JLabel());

        add(new JLabel("���෮ : "));
        dosageField = new JTextField(20);
        add(dosageField);
        add(new JLabel());

        add(new JLabel("KTAS ���� : "));
        ktasField = new JTextField(20);
        add(ktasField);
        add(new JLabel());

        addButton = new JButton("ġ�� ���� �߰�");
        add(new JLabel()); // �� ���� �߰��Ͽ� ��ư�� ����
        add(addButton);

        statusLabel = new JLabel("����: ��� ��");
        add(new JLabel()); // �� ���� �߰��Ͽ� ���� ����
        add(statusLabel);

        // �ǻ� ���� ��ư �׼�
        authenticateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // �Էµ� �ǻ� ID�� ��й�ȣ ��������
                    int doctorId = Integer.parseInt(doctorIdField.getText());
                    int password = Integer.parseInt(passwordField.getText());
                    // �ǻ� ����
                    if (authenticateDoctor(doctorId, password)) {
                        statusLabel.setText("���� ����");
                    } else {
                        statusLabel.setText("���� ����");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // ġ�� ���� �߰� ��ư �׼�
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // �Էµ� ġ�� ���� ��������
                    int patientId = Integer.parseInt(patientIdField.getText());
                    int diseaseId = Integer.parseInt(diseaseIdField.getText());
                    int medicationId = Integer.parseInt(medicationIdField.getText());
                    String recommendedTreatment = recommendedTreatmentField.getText();
                    String date = dateField.getText();
                    String dosage = dosageField.getText();
                    int ktas = Integer.parseInt(ktasField.getText());

                    // ġ�� ���� �߰�
                    addTreatment(patientId, diseaseId, medicationId, recommendedTreatment, date, dosage, ktas);
                    statusLabel.setText("ġ�� ���� �߰� ����");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    statusLabel.setText("ġ�� ���� �߰� ����");
                }
            }
        });

        
        setVisible(true);
    }

    // �ǻ� ���� �޼���
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

    // ġ�� ���� �߰� �޼���
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

    // ���� �޼���: ���α׷� ������
    public static void main(String[] args) {
        new ManageTreatment();
    }
}