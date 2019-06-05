package GUI;

import javax.swing.*;

import java.awt.*;   //导入必要的包
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectWindow extends JFrame{
    JTextField jTextField1 ;//定义文本框组件
    JTextField jTextField2;
    JLabel jLabel1,jLabel2;
    JPanel jp1,jp2,jp3;
    JButton jb1,jb2; //创建按钮
    private boolean isClick=false;
    public ConnectWindow(){
        jTextField1 = new JTextField(15);
        jTextField2 = new JTextField(5);
        jLabel1 = new JLabel("远程主机地址");
        jLabel2 = new JLabel("端口号");
        jb1 = new JButton("确认");
        jb2 = new JButton("取消");
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 isClick=true;
            }
        });
        jb2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });
        //设置布局
        this.setLayout(new GridLayout(3,1));

        jp1.add(jLabel1);
        jp1.add(jTextField1);

        jp2.add(jLabel2);
        jp2.add(jTextField2);

        jp3.add(jb1);
        jp3.add(jb2); //第三块面板添加确认和取消

        this.add(jp1);
        this.add(jp2);
        this.add(jp3);  //将三块面板添加到登陆框上面
        //设置显示
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("连接窗口");
        this.setLocation(400,300);

    }

    public String[] getAddressAndPort(){
        while(!isClick){
            try {
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
                return null;
            }
        }
        String[]result = new String[2];
        result[0]=jTextField1.getText();
        result[1]=jTextField2.getText();
        return result;
    }
}