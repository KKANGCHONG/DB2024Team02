package DB2024Team02;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("DB2024Team02 MainPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container contentPane = getContentPane();
        contentPane.setBackground(Color.WHITE);
        contentPane.setLayout(new GridLayout(2,1)); // 2행 1열

        //패널1 생성
        JPanel1 jpanel1 = new JPanel();
        jpanel1.setBackground(Color.WHITE);

        //절대 위치와 크기 이용
        jpanel1.setBackground(null);

        //아이콘 추가
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\leehy\\OneDrive\\바탕 화면\\free-icon-hospital-3304592.png");
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel HospitalImage = new JLabel(scaledIcon);
        HospitalImage.setLocation(385,80);
        HospitalImage.setSize(40,40);

        //제목 추가
        JLabel systemName = new JLabel("DB2024Team02's Database");
        systemName.setFont(new Font("Arial", Font.BOLD, 27));
        systemName.setForeground(Color.black);
        systemName.setLocation(230,125); //위치 설정
        systemName.setSize(400,50); //크기 설정
        
        //아이콘과 제목 패널에 붙이기
        jpanel1.add(HospitalImage);
        jpanel1.add(systemName);
        
        //패널 생성
        JPanel jpanel2 = new JPanel();
        jpanel2.setBackground(Color.WHITE);
        
        //절대 위치와 크기 이용
        jpanel2.setLayout(null);
        
        //id, pw 입력하는 필드 만들기
        
        JLabel Id = new JLabel("ID");
        Id.setFont(new Font("Arial", Font.BOLD, 25));
        Id.setForeground(Color.black);
        Id.setLocation(275,0); //위치 설정
        Id.setSize(130,35); //크기 설정
        
        JTextField IdField = new JTextField("");
        IdField.setBackground(new Color(230, 230, 230));
        IdField.setLocation(420,0); //위치 설정
        IdField.setSize(130,35); //크기 설정
        
        JLabel Pw = new JLabel("PW");
        Pw.setFont(new Font("Arial", Font.BOLD, 25));
        Pw.setForeground(Color.black);
        Pw.setLocation(260,80); //위치 설정
        Pw.setSize(130,35); //크기 설정
        
        JTextField PwField = new JTextField("");
        PwField.setBackground(new Color(230, 230, 230));
        PwField.setLocation(420,80); //위치 설정
        PwField.setSize(130,35); //크기 설정
        
        //로그인 요소들 패널에 붙이기
        jpanel2.add(Id);
        jpanel2.add(IdField);
        jpanel2.add(Pw);
        jpanel2.add(PwField);
        
        
         //컨텐트팬에 패널 붙이기
        contentPane.add(jpanel1);
        contentPane.add(jpanel2);


        setSize(800, 450);
        setVisible(true);
    }

    public static void main(String[] args) {
        LoginPage LoginPage = new LoginPage();
    }
}