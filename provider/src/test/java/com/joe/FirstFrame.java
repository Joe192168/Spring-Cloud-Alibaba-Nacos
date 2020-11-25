package com.joe;

import java.awt.*;

public class FirstFrame extends Frame {

    public FirstFrame(String str) {
        super(str);     //调用父类的构造方法
    }

    public static void main(String[] args) {
        FirstFrame fr = new FirstFrame("First contianer!!");
        fr.setSize(240,240);    //设置Frame的大小
        fr.setBackground(Color.yellow);      //设置Frame的背景色
        fr.setVisible(true);         //设置Frame为可见
    }



}
