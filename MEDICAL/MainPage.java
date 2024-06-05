package MEDICAL;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame {
    private static int currentUserId;

    //������ ����
    public static void main(String[] args) {
        MainPage mainPage = new MainPage(currentUserId);
    }

    public MainPage(int currentUserId) {
        this.currentUserId = currentUserId;
       
        setTitle("DB2024Team02 MainPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(662, 831);  // Set initial size for the JFrame

        Container contentPane = getContentPane();
        contentPane.setBackground(Color.WHITE); // ��� ��� ����
        contentPane.setLayout(null); // Set to null layout for absolute positioning

        // ��� �г� ����
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 160));
        panel.setBounds(0, 0, 647, 51);
        contentPane.add(panel);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(0, 168, 647, 10);
        panel_1.setBackground(new Color(0, 0, 160));
        contentPane.add(panel_1);

        JLabel lblNewLabel = new JLabel("��� �η� ���� ���α׷�");
        lblNewLabel.setBounds(135, 86, 396, 51);
        lblNewLabel.setFont(new Font("HY������M", Font.PLAIN, 35));
        lblNewLabel.setForeground(new Color(0, 0, 160));
        contentPane.add(lblNewLabel);

        JButton btnNewButton_2 = new JButton("�ǻ� ����");
        btnNewButton_2.setForeground(Color.WHITE);
        btnNewButton_2.setFont(new Font("HY������M", Font.PLAIN, 18));
        btnNewButton_2.setBackground(new Color(166, 191, 206));
        btnNewButton_2.setBounds(119, 292, 195, 192);
        contentPane.add(btnNewButton_2);

        JButton btnNewButton_2_1 = new JButton("��ȣ�� ����");
        btnNewButton_2_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton_2_1.setForeground(Color.WHITE);
        btnNewButton_2_1.setFont(new Font("HY������M", Font.PLAIN, 18));
        btnNewButton_2_1.setBackground(new Color(166, 191, 206));
        btnNewButton_2_1.setBounds(326, 292, 195, 192);
        contentPane.add(btnNewButton_2_1);

        JButton btnNewButton_2_1_1 = new JButton("ȯ�� ����");
        btnNewButton_2_1_1.setForeground(Color.WHITE);
        btnNewButton_2_1_1.setFont(new Font("HY������M", Font.PLAIN, 18));
        btnNewButton_2_1_1.setBackground(new Color(166, 191, 206));
        btnNewButton_2_1_1.setBounds(119, 497, 195, 192);
        contentPane.add(btnNewButton_2_1_1);

        JButton btnNewButton_2_1_1_1 = new JButton("ġ�� / �๰ ����");
        btnNewButton_2_1_1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnNewButton_2_1_1_1.setForeground(Color.WHITE);
        btnNewButton_2_1_1_1.setFont(new Font("HY������M", Font.PLAIN, 18));
        btnNewButton_2_1_1_1.setBackground(new Color(166, 191, 206));
        btnNewButton_2_1_1_1.setBounds(326, 497, 195, 192);
        contentPane.add(btnNewButton_2_1_1_1);

        JButton btnNewButton_1 = new JButton("��� ���� �η�");
        btnNewButton_1.setForeground(Color.WHITE);
        btnNewButton_1.setBackground(new Color(255, 170, 170));
        btnNewButton_1.setFont(new Font("HY������M", Font.PLAIN, 18));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // MyPage Ŭ������ �ν��Ͻ� ����
                MedicalStaff staff = new MedicalStaff();
                staff.setVisible(true);
            }
        });
        btnNewButton_1.setBounds(0, 719, 647, 74);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton = new JButton("���� ������");
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBackground(new Color(166, 191, 206));
        btnNewButton.setFont(new Font("HY������M", Font.PLAIN, 18));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ���� ȭ�� �ν��Ͻ� ����
            JFrame doctorpatient = new Mypatient(currentUserId);
            JFrame nursepatient = new MypatientNurse(currentUserId);

                // MyPage Ŭ������ �ν��Ͻ� ����
                MyPage myPage = new MyPage(doctorpatient,nursepatient,currentUserId);
                myPage.frame.setVisible(true);
                MainPage.this.dispose(); // ���� ������ �������� ������ �ݽ��ϴ�.
            }
        });
        btnNewButton.setBounds(219, 212, 207, 51);
        contentPane.add(btnNewButton);

        setVisible(true);
    }
}