import java.awt.EventQueue;

import javax.swing.JFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Mypatient extends JFrame {
    private JList<String> patientList; // 환자 목록을 표시할 JList
    private DefaultListModel<String> patientListModel; // JList에 데이터를 제공할 모델
    private JTextArea patientInfoArea;
    private JButton treatmentButton; // 치료 정보를 로드할 버튼
    private JFrame mainScreen; // 메인 화면 인스턴스를 저장할 변수
    private JLabel doctorInfoLabel; // 의사 정보를 표시할 레이블

    private static final String DB_URL = "jdbc:mysql://localhost:3306/DB2024Team02";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    // 가정: 현재 로그인한 사용자의 ID (실제 응용 프로그램에서는 로그인 세션에서 이 값을 가져와야 함)
    private int currentUserId = 1; // 로그인 페이지에서 ID 정보 가져오기 admin

    public Mypatient() {
    	this.mainScreen = mainScreen; // 메인 화면 인스턴스 저장
        setTitle("병원 관리 시스템");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        doctorInfoLabel = new JLabel();
        doctorInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        doctorInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);    
        doctorInfoLabel.setOpaque(true); // 배경색이 보이도록 설정
        doctorInfoLabel.setBackground(new Color(255, 255, 255));
        doctorInfoLabel.setBounds(1, 0, 199, 146);
        getContentPane().add(doctorInfoLabel);
        
        patientListModel = new DefaultListModel<>();
        patientList = new JList<>(patientListModel);
        patientList.setForeground(new Color(255, 255, 255));
        patientList.setBackground(new Color(0, 64, 128));
        patientList.setFont(new Font("Arial", Font.BOLD, 16));
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = patientList.getSelectedValue();
                if (selectedValue != null && !selectedValue.isEmpty()) {
                    int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - 이름" 형식에서 ID를 분리
                    loadPatientInfo(patientId);
                }
            }
        });
        JScrollPane listScrollPane = new JScrollPane(patientList);
        listScrollPane.setPreferredSize(new Dimension(50, 50));

        patientInfoArea = new JTextArea();
        patientInfoArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane infoScrollPane = new JScrollPane(patientInfoArea);

        treatmentButton = new JButton("치료 정보 보기");
        treatmentButton.setFont(new Font("Serif", Font.LAYOUT_LEFT_TO_RIGHT, 16)); // 버튼 텍스트 크기 설정
        treatmentButton.addActionListener(e -> {
            String selectedValue = patientList.getSelectedValue();
            if (selectedValue != null && !selectedValue.isEmpty()) {
                int patientId = Integer.parseInt(selectedValue.split(" - ")[0]); // "ID - 이름" 형식에서 ID를 분리
                loadTreatmentInfo(patientId);
            }
        });
