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
        


        setSize(800, 450);
        setVisible(true);
    }

    public static void main(String[] args) {
        LoginPage LoginPage = new LoginPage();
    }
}