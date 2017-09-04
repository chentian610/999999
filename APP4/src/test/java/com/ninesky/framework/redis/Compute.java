package com.ninesky.framework.redis;

import java.util.Stack;

public class Compute {
 private String s = new String();
 private MiddleToLast mtl = new MiddleToLast();
 public Compute() {
  this("");
 }
 public Compute(String s) {
  this.s = s;
 }
 public void setComputeString(String s) {
  this.s = s;
 }
 public String getComputeString() {
  return this.s;
 }
 public String getLastComputeString() {
  return this.mtl.middleToLast(this.s);
 }
 public int getResult() {
  int right = 0, left = 0, result = 0;
  Stack number = new Stack();
  number.push(Character.valueOf('='));
  char c;
  String last = this.mtl.middleToLast(this.s);
  for (int i = 0; i < last.length(); i++) {
   c = last.charAt(i);
   if (!isOperateSign(c))
    number.push(Integer.valueOf(c - 48));
   if (isOperateSign(c)) {
    right = ((Integer) number.pop()).intValue();
    left = ((Integer) number.pop()).intValue();
    result = this.ComputeResult(right, left, c);
    number.push(Integer.valueOf(result));
   }
  }
  return result;
 }
 public int ComputeResult(int right, int left, char op) {
  int result = 0;
  switch (op) {
  case '+':
   result = left + right;
   break;
  case '-':
   result = left - right;
   break;
  case '*':
   result = left * right;
   break;
  case '/':
   result = left / right;
   break;
  }
  return result;
 }
 public boolean isOperateSign(char c) {
  if (c == '+' || c == '-' || c == '/' || c == '*')
   return true;
  else
   return false;
 }
 
}