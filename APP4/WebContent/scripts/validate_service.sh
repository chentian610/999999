#!/bin/bash

pid=$(ps -ef |grep tomcat |grep java | awk ' { print $2 } ') 
echo tomcat process number is ${pid}
  if [ -n "${pid}" ];then
    echo app4 deployed successfully!
    rm -rf /data/tempversion/*
    exit 0
  else
    echo app4 deployed failed!
    exit 1
  fi
