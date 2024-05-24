package DB2024Team02;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JFrame {

    public MainPage() {
        setTitle("DB2024Team02 MainPage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setBackground(Color.WHITE); // 흰색 배경 설정

        //패널 생성
        JPanel jpanel1 = new JPanel();
        jpanel1.setBackground(Color.WHITE);
        
        //절대 위치와 크기 이용
        jpanel1.setLayout(null);

        //아이콘 추가
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\leehy\\OneDrive\\바탕 화면\\free-icon-hospital-3304592.png");
        Image img = originalIcon.getImage();
        Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH); //크기 설정
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel HospitalImage = new JLabel(scaledIcon);
        HospitalImage.setLocation(385,60); //위치 설정
        HospitalImage.setSize(40,40); //크기 설정

        //제목 추가
        JLabel systemName = new JLabel("DB2024Team02's Database");
        systemName.setFont(new Font("Arial", Font.BOLD, 27));
        systemName.setForeground(Color.black);
        systemName.setLocation(230,105); //위치 설정
        systemName.setSize(400,50); //크기 설정
        
        //아이콘과 제목 패널에 붙이기
        jpanel1.add(HospitalImage);
        jpanel1.add(systemName);
        
        //메뉴 버튼 생성
        JButton b1 = new JButton("Reservation");
        b1.setBackground(new Color(230, 230, 230));
        b1.setLocation(260,170); //위치 설정
        b1.setSize(130,35); //크기 설정
        
        JButton b2 = new JButton("Patient Chart");
        b2.setBackground(new Color(230, 230, 230));
        b2.setLocation(420,170); //위치 설정
        b2.setSize(130,35); //크기 설정
        
        JButton b3 = new JButton("Start/End Time");
        b3.setBackground(new Color(230, 230, 230));
        b3.setLocation(260,260); //위치 설정
        b3.setSize(130,35); //크기 설정
        
        JButton b4 = new JButton("Logout");
        b4.setBackground(new Color(230, 230, 230));
        b4.setLocation(420,260); //위치 설정
        b4.setSize(130,35); //크기 설정
        
        //메뉴 버튼 패널에 붙이기
        jpanel1.add(b1);
        jpanel1.add(b2);
        jpanel1.add(b3);
        jpanel1.add(b4);
        
        //컨텐트팬에 패널 붙이기
        contentPane.add(jpanel1);

        setSize(800, 450);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainPage mainPage = new MainPage();
    }
}
