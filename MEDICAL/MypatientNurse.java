package MEDICAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
//�α����� ��ȣ�簡 �ڽ��� ȯ�� ����� ���� â

public class MypatientNurse extends JFrame {
    private JList<String> patientList; // ȯ�� ����� ǥ���� JList
    private DefaultListModel<String> patientListModel; // JList�� �����͸� ������ ��
    private JTextArea patientInfoArea;// ȯ�� ������ ǥ���� JTextArea
    private JButton treatmentButton; // ġ�� ������ �ε��� ��ư
    private JLabel nurseInfoLabel; // �ǻ� ������ ǥ���� ���̺�

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    //���� �α����� ��ȣ���� ID
    private static int currentUserId;

    public MypatientNurse(int currentUserId) {
        this.currentUserId = currentUserId;
   
        setTitle("���� ���� �ý��� - ��ȣ�� ����");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
       
        //��ȣ���� ������ ǥ�õǴ� Label
        nurseInfoLabel = new JLabel();
        nurseInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nurseInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);    
        nurseInfoLabel.setOpaque(true); // ������ ���̵��� ����
        nurseInfoLabel.setBackground(new Color(255, 255, 255));
        nurseInfoLabel.setBounds(1, 0, 199, 146);
        getContentPane().add(nurseInfoLabel);

        //�α����� ��ȣ�翡 ���� ȯ�ڸ� ǥ��
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setForeground(new Color(255, 255, 255));
        patientList.setBackground(new Color(0, 64, 128));
        patientList.setFont(new Font("Serif", Font.BOLD, 16));
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = patientList.getSelectedValue();
                if (selectedValue != null && !selectedValue.isEmpty()) {
                    int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - �̸�" ���Ŀ��� ID�� �и�
                    loadPatientInfo(patientId);
                }
            }
        });
        JScrollPane listScrollPane = new JScrollPane(patientList);
        listScrollPane.setPreferredSize(new Dimension(50, 50));

        //ȯ�� ������ ǥ�õǴ� ����
        patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane infoScrollPane = new JScrollPane(patientInfoArea);

        //ȯ���� ġ�������� �� �� �ִ� ��ư
        treatmentButton = new JButton("ġ�� ���� ����");
        treatmentButton.setFont(new Font("���� ���", Font.BOLD, 16)); // ��ư �ؽ�Ʈ ũ�� ����
        treatmentButton.addActionListener(e -> {
            String selectedValue = patientList.getSelectedValue();
            if (selectedValue != null && !selectedValue.isEmpty()) {
                int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - �̸�" ���Ŀ��� ID�� �и�
                loadTreatmentInfo(patientId);
            }
        });

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, infoScrollPane);
        splitPane.setBounds(1, 145, 985, 580);
        splitPane.setDividerLocation(200);
        getContentPane().setLayout(null);
        getContentPane().add(nurseInfoLabel); // �ǻ� ���� ���̺� �߰�
        getContentPane().add(splitPane);

        // �ڷΰ��� ��ư
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 722, 986, 41);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // �г��� ���̾ƿ��� �÷ο� ���̾ƿ����� �����ϰ� ������ ����

        JButton backButton = new JButton("�ڷ� ����"); // �ڷ� ���� ��ư ����
        backButton.setFont(new Font("���� ���", Font.BOLD, 16));
        backButton.addActionListener(e -> {
            this.setVisible(false); // ���� â�� ����
            JFrame doctorpatient = new Mypatient(currentUserId); // Mypatient Ŭ������ �ν��Ͻ��� ����
            JFrame nursepatient = new MypatientNurse(currentUserId); // MypatientNurse Ŭ������ �ν��Ͻ��� ����
            MyPage myPage = new MyPage(doctorpatient, nursepatient, currentUserId); //MyPage Ŭ������ �ν��Ͻ��� ����
            myPage.frame.setVisible(true);//MyPage�� ǥ��
        });

        //�������� ��ư
        JButton modifyButton = new JButton("���� ����");
        modifyButton.setFont(new Font("Serif", Font.PLAIN, 16));
        modifyButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ManagePatient managePatientFrame = new ManagePatient();
                managePatientFrame.setVisible(true);
            });
        });
       
        //ȯ���� ���������� ����
        JButton addButton = new JButton("ȯ�� ���� ����");
        addButton.setFont(new Font("HY������M", Font.PLAIN, 16));
        buttonPanel.add(addButton);
        addButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ManagePatient managePatientFrame = new ManagePatient();
                managePatientFrame.setVisible(true);
            });
        });
       
       
        buttonPanel.add(treatmentButton,BorderLayout.CENTER);
        buttonPanel.add(backButton); // �гο� ��ư �߰�
        getContentPane().add(buttonPanel); // �г��� �������� SOUTH ������ �߰�
       
        loadNurseInfo(); // �ǻ� ���� �ε�
        loadPatients(); // ȯ�� ��� �ε�
        getContentPane().add(splitPane, BorderLayout.CENTER);

    }

    //�α����� ��ȣ�� ������ DB�κ��� �ҷ����� �Լ�
    private void loadNurseInfo() {
        String query = "SELECT NurseID, NurseName, NurseDepartment, ContactNumber FROM DB2024_Nurse WHERE NurseID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); //DB�� ����
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId); //���� �α����� ����� ���̵�
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("NurseID");
                String name = rs.getString("NurseName");
                String specialty = rs.getString("NurseDepartment");
                String phone = rs.getString("ContactNumber");

                nurseInfoLabel.setText("<html><div style='padding-left: 10px;'>��ȣ��: " + name + "<br/>ID: " + ID + "<br/>������: " + specialty + "<br/>����ó: " + phone + "</html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    //��ȣ�翡 ���� ȯ�� ����� DB�κ��� �ҷ����� �Լ�
    private void loadPatients() {
        String query = "SELECT PatientID, Name FROM DB2024_Patient WHERE NurseID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("PatientID");
                String name = rs.getString("Name");
                patientListModel.addElement(id + " - " + name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ȯ�������� DB�κ��� �ҷ����� �Լ�
    private void loadPatientInfo(int patientId) {
        patientInfoArea.setText("");
        String query = "SELECT * FROM DB2024_patient_privacy WHERE PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                patientInfoArea.append("  ȯ��ID: " + rs.getInt("PatientID") + "\n");
                patientInfoArea.append("  �̸�: " + rs.getString("Name") + "\n");
                patientInfoArea.append("  �������: " + rs.getString("Birth") + "\n");
                patientInfoArea.append("  �ּ�: " + rs.getString("Address") + "\n");
                patientInfoArea.append("  ��ȭ��ȣ: " + rs.getString("Phone") + "\n");
                patientInfoArea.append("  ��ȣ�� ��ȭ��ȣ: " + rs.getString("GuardianPhone") + "\n");
                patientInfoArea.append("  ��� ��ȣ��: " + rs.getInt("NurseID") + "\n");
                patientInfoArea.append("  ��ġ��: " + rs.getInt("DoctorID") + "\n");
               
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //ȯ���� ġ�������� DB�κ��� �ҷ����� �Լ�
    private void loadTreatmentInfo(int patientId) {
        patientInfoArea.append("\n#ġ�� ����#\n");
        String query = "SELECT T.Date, D.DiseaseName, M.MedicationName, T.RecommendedTreatment, T.Dosage, T.KTAS " +
                       "FROM DB2024_Treatment T " +
                       "JOIN DB2024_Disease D ON T.DiseaseID = D.DiseaseID " +
                       "JOIN DB2024_Medication M ON T.MedicationID = M.MedicationID " +
                       "WHERE T.PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                patientInfoArea.append("ġ�� ��¥: " + rs.getDate("Date") + "\n");
                patientInfoArea.append("����: " + rs.getString("DiseaseName") + "\n");
                patientInfoArea.append("�๰��: " + rs.getString("MedicationName") + "\n");
                patientInfoArea.append("��õ ġ��: " + rs.getString("RecommendedTreatment") + "\n");
                patientInfoArea.append("���뷮: " + rs.getString("Dosage") + "\n");
                patientInfoArea.append("KTAS: " + rs.getInt("KTAS") + "\n");
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //main �Լ�
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MypatientNurse frame = new MypatientNurse(currentUserId);
            frame.setVisible(true);
        });
    }
}