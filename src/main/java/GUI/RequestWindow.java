package GUI;

import client.Cache;
import client.Validator;

import javax.swing.*;
import java.awt.event.*;

public class RequestWindow extends Window {
    String []request;
    Cache cache;
    public RequestWindow(Cache cache){
        this.cache=cache;
    }

    public void init(String message){
        super.init(message);
        jPanel = new JPanel();
        this.addComponents(jPanel);
        this.add(jPanel);
        this.setVisible(true);
        this.setLocation(200,0);
        while (!isClick){
            try{
                Thread.sleep(500);
            }catch(Exception e){
                System.exit(0);//退出程序
            }
        }
    }

    public void addComponents(final JPanel jpanel){
        super.addComponents(jpanel);
        button1 = new JButton("检查报文");
        jpanel.add(button1);
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Validator validator = new Validator();
                request = new String[3];
                request[0] = lineText.getText();
                request[1] = headText.getText();
                request[2] = bodyText.getText();
                if(!validator.isPassed(request)){
                    JOptionPane.showMessageDialog(jpanel, validator.getMessage(), "提示信息",JOptionPane.ERROR_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(jpanel, "报文检查通过", "提示信息",JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        button2 = new JButton("发送报文");
        jpanel.add(button2);
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                request = new String[3];
                request[0] = lineText.getText();
                request[1] = headText.getText();
                String modified = getURLDate(request[0]);
                if(modified!=null)request[1]+="\n"+modified;
                headText.setText(request[1]);
                request[2] = bodyText.getText();
                isClick = true;
                return;
            }
        });

    }

    public String[] getRequest(){
        return request;
    }

    private String getURLDate(String line){
        String[]lines = line.split(" ");
        String URL = lines[1];
        String date = cache.getLastModified(URL);
        if(date==null)return null;
        else return "If_Modified_Since:"+date;
    }
}
