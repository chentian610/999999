package com.ninesky.framework.redis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
public class test extends JFrame implements ActionListener {
 private JTextField jtf;
 private JButton jb;
 private String s = new String();
 private Compute cp;
 public test() {
  super("表达式计算");
  jtf = new JTextField();
  jtf.setSize(200, 30);
  jtf.setLocation(5, 5);
  jb = new JButton("计算");
  jb.addActionListener(this);
  jb.setSize(70, 30);
  jb.setLocation(5, 50);
  setLayout(null);
  add(jtf);
  add(jb);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setBounds(getToolkit().getScreenSize().width / 2 - 150, getToolkit()
    .getScreenSize().height / 2 - 150, 300, 300);
  setVisible(true);
 }
 public void actionPerformed(ActionEvent e) {
  this.s = jtf.getText();
  int result = this.count(s);
  s += " = " + result;
  this.jtf.setText(s);
 }
 public int count(String s) {
  int result = 0;
  cp = new Compute(s);
  result = cp.getResult();
  return result;
 }
 public static void main(String[] args) {
  new test();
 }
}