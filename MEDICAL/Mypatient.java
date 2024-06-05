package MEDICAL;

import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Mypatient extends JFrame {
    private JList<String> patientList; // ȯ�� ����� ǥ���� JList
    private DefaultListModel<String> patientListModel; // JList�� �����͸� ������ ��
    private JTextArea patientInfoArea; //ȯ�� ������ ǥ���� ����
    private JButton treatmentButton; // ġ�� ������ �ε��� ��ư
    private JLabel doctorInfoLabel; // �ǻ� ������ ǥ���� ���̺�

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    //���� �α����� ������� ID
    private static int currentUserId;

    public Mypatient(int currentUserId) {
        Mypatient.currentUserId = currentUserId;
        // LoginPage���� ����� ����� ID ���� ��������

        //frame ����
    setTitle("���� ���� �ý���");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
       
        //�ǻ����� ǥ��
        doctorInfoLabel = new JLabel();
        doctorInfoLabel.setBounds(1, 0, 199, 146);
        doctorInfoLabel.setFont(new Font("HY������M", Font.PLAIN, 16));
        doctorInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);    
        doctorInfoLabel.setOpaque(true); // ������ ���̵��� ����
        doctorInfoLabel.setBackground(new Color(255, 255, 255));
        getContentPane().add(doctorInfoLabel);
       
        //�α����� �ǻ��� ȯ�� ��� ǥ��
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setForeground(new Color(255, 255, 255));
        patientList.setBackground(new Color(0, 64, 128));
        patientList.setFont(new Font("HY������M", Font.PLAIN, 16));
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
        JScrollPane listScrollPane = new JScrollPane(patientList); //JScrollPane�� ȯ�� ��� ǥ��
        listScrollPane.setPreferredSize(new Dimension(50, 50));

        //ȯ�� �������� ǥ��
        patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("HY������M", Font.PLAIN, 16));
        JScrollPane infoScrollPane = new JScrollPane(patientInfoArea);

        //ȯ���� ġ������ ���� ��ư
        treatmentButton = new JButton("ġ�� ���� ����");
        treatmentButton.setFont(new Font("HY������M", Font.PLAIN, 16)); // ��ư �ؽ�Ʈ ũ�� ����
        treatmentButton.addActionListener(e -> {
            String selectedValue = patientList.getSelectedValue();
            if (selectedValue != null && !selectedValue.isEmpty()) {
                int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - �̸�" ���Ŀ��� ID�� �и�
                loadTreatmentInfo(patientId);
            }
        });
       

        //�α����� �ǻ����� ǥ���ϴ� pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, infoScrollPane);
        splitPane.setBounds(1, 145, 985, 580);
        splitPane.setDividerLocation(200);
        getContentPane().add(doctorInfoLabel); // �ǻ� ���� ���̺� �߰�
        getContentPane().add(splitPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(0, 722, 986, 41);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // �г��� ���̾ƿ��� �÷ο� ���̾ƿ����� �����ϰ� ������ ����

        //�ڷΰ���
        JButton backButton = new JButton("�ڷ� ����"); // �ڷ� ���� ��ư ����
        backButton.setFont(new Font("HY������M", Font.PLAIN, 16));
        backButton.addActionListener(e -> {
            this.setVisible(false); // ���� â�� ����
            // ���� ������ ����
            JFrame doctorpatient = new Mypatient(currentUserId); //mypatient ��ü ����
            JFrame nursepatient = new MypatientNurse(currentUserId); //mypatientnurse ��ü ����
            MyPage myPage = new MyPage(doctorpatient, nursepatient, currentUserId);//mypage ��ü ����
            myPage.frame.setVisible(true);//mypage�� ���̰�
        });
       
        JButton addButton_1 = new JButton("ġ�� ���� �߰�");
        addButton_1.setFont(new Font("HY������M", Font.PLAIN, 16));
        buttonPanel.add(addButton_1);
        addButton_1.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                ManageTreatment manageTreatmentFrame = new ManageTreatment();
                manageTreatmentFrame.setVisible(true);
            });
        });
       
       
      //ȯ�� �߰�
        JButton addButton = new JButton("ȯ�� ���� �߰�");
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
       
        loadDoctorInfo(); // �ǻ� ���� �ε�
        loadPatients(); // ȯ�� ��� �ε�
    }

    //�α����� �ǻ������� DB�κ��� �ҷ����� �Լ�
    private void loadDoctorInfo() {
        String query = "SELECT DoctorID, DoctorName, DoctorDepartment, ContactNumber FROM DB2024_Doctor WHERE DoctorID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, currentUserId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("DoctorID");
                String name = rs.getString("DoctorName");
                String specialty = rs.getString("DoctorDepartment");
                String phone = rs.getString("ContactNumber");

                doctorInfoLabel.setText("<html><div style='padding-left: 10px;'>�ǻ�: " + name + "<br/>ID: " + ID + "<br/>������: " + specialty + "<br/>����ó: " + phone + "</html>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    //�α����� �ǻ翡 ���� ȯ�ڸ���� DB�κ��� �ҷ����� �Լ�
    private void loadPatients() {
        String query = "SELECT PatientID, Name FROM DB2024_Patient WHERE DoctorID = ?";
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

    //�α����� �ǻ翡 ���� ȯ�������� DB�κ��� �ҷ����� �Լ�
    private void loadPatientInfo(int patientId) {
        patientInfoArea.setText("");
        String query = "SELECT * FROM DB2024_patient_privacy WHERE PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { //������ �޾ƿ�
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
        patientInfoArea.append("\nġ�� ����:\n");
        String query = "SELECT T.Date, D.DiseaseName, M.MedicationName, T.RecommendedTreatment, T.Dosage, T.KTAS " +
                       "FROM DB2024_Treatment T " +
                       "JOIN DB2024_Disease D ON T.DiseaseID = D.DiseaseID " +
                       "JOIN DB2024_Medication M ON T.MedicationID = M.MedicationID " +
                       "WHERE T.PatientID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) { //������ �޾ƿ�
                patientInfoArea.append("  ��¥: " + rs.getDate("Date") + "\n");
                patientInfoArea.append("  ����: " + rs.getString("DiseaseName") + "\n");
                patientInfoArea.append("  �๰��: " + rs.getString("MedicationName") + "\n");
                patientInfoArea.append("  ��õ ġ��: " + rs.getString("RecommendedTreatment") + "\n");
                patientInfoArea.append("  ���뷮: " + rs.getString("Dosage") + "\n");
                patientInfoArea.append("  KTAS: " + rs.getInt("KTAS") + "\n");
                patientInfoArea.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //main �Լ�
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            currentUserId = loginPage.getCurrentUserId();
            Mypatient gui = new Mypatient(currentUserId);
            gui.setVisible(true);
        });
    }
}