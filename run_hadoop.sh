#!/usr/bin/bash

cd /usr/local/hadoopUser/etc/hadoop
hdfs namenode -format


start-dfs.sh

start-yarn.sh

