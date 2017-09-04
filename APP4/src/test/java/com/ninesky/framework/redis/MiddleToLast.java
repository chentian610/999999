package com.ninesky.framework.redis;

import java.util.Stack;
public class MiddleToLast {
 
 public int prior(char op) {
  if (op == '+' || op == '-')
   return 1;
  if (op == '*' || op == '/')
   return 2;
  return 0;
 }
 
 public String middleToLast(String middle) {
  Stack op = new Stack();
  String ans = new String();
  char c[] = new char[50];
  int j = 0;
  for (int i = 0; i < middle.length(); i++) {
   char ch = middle.charAt(i);
   if (ch >= '0' && ch <= '9') {
    c[j] = ch;
    j++;
   }
   else {
    if (ch == '(') {
     op.push(Character.valueOf(ch));
    } else {
     if (ch == ')') {
      while (((Character) op.peek()).charValue() != '(') {
       c[j] = ((Character) op.peek()).charValue();
       j++;
       op.pop();
      }
      op.pop();
     } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
      if (op.empty())
       op.push(Character.valueOf(ch));
      else {
       if (prior(ch) > prior(((Character) op.peek())
         .charValue())) {
        op.push(Character.valueOf(ch));
       } else {
        while (!op.empty()
          && prior(ch) <= prior(((Character) op
            .peek()).charValue())) {
         c[j] = ((Character) op.peek()).charValue();
         j++;
         op.pop();
        }
        op.push(Character.valueOf(ch));
       }
      }
     }
    }
   }
  }
  while (!op.empty()) {
   c[j] = ((Character) op.peek()).charValue();
   j++;
   op.pop();
  }
  ans = String.valueOf(c, 0, j);
  return ans;
 }

}
 