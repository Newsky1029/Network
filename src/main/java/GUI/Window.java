package GUI;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    JLabel line;
    JLabel head;
    JLabel body;
    JPanel jPanel;
    JTextField lineText;
    JTextArea headText;
    JTextArea bodyText;
    JButton button1;
    JButton button2;

    boolean isClick=false;

    public void init(String message) {
        //设置大小
        this.setSize(500, 1500);
        //标题
        this.setTitle(message);
        //关闭方式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
    }



    public void addComponents(JPanel jpanel){
        jpanel.setLayout(new GridLayout(8,1,0,10));
        Font font = new Font("宋体",Font.PLAIN,16);
        line = new JLabel("Line：");
        line.setFont(font);
        jpanel.add(line);

        lineText = new JTextField(40);
        jpanel.add(lineText);

        head = new JLabel("Head：");
        head.setFont(font);
        jpanel.add(head);

        headText = new JTextArea();
        jpanel.add(headText);

        body = new JLabel("Body：");
        body.setFont(font);
        jpanel.add(body);

        bodyText = new JTextArea();
        jpanel.add(bodyText);

        JScrollPane scroll = new JScrollPane(bodyText);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jpanel.add(scroll);

    }


    public JLabel getBody() {
        return body;
    }

    public JLabel getHead() {
        return head;
    }

    public JLabel getLine() {
        return line;
    }

    public JPanel getjPanel() {
        return jPanel;
    }

    public JButton getButton1() {
        return button1;
    }

    public JButton getButton2() {
        return button2;
    }

    public JTextArea getBodyText() {
        return bodyText;
    }

    public JTextArea getHeadText() {
        return headText;
    }

    public JTextField getLineText() {
        return lineText;
    }
}
