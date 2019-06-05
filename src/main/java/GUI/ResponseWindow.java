package GUI;

import javax.swing.*;
import java.awt.event.*;
public class ResponseWindow extends Window {
    private boolean isFlush=false;
    public void init(String message){
        super.init(message);
        jPanel = new JPanel();
        this.addComponents(jPanel);
        this.add(jPanel);
        this.setVisible(true);
        this.setLocation(750,0);
        while(!isClick){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.exit(0);//退出程序
            }
        }
    }

    public void addComponents(JPanel jpanel){
        super.addComponents(jpanel);
        button1=new JButton("查看响应报文");
        jpanel.add(button1);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isClick = true;
            }
        });
        button2=new JButton("刷新");
        jpanel.add(button2);
    }

    public void flush(){
        this.setFlush();
        while(!isFlush){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.exit(0);//退出程序
            }
        }
    }

    public void setFlush(){
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                isFlush = true;
            }
        });
    }


}
