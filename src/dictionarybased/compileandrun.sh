#!/bin/bash

rm wordcountc/*
rm -rf output
hadoop fs -rm -r /user/ubuntu/output/
javac -classpath /usr/local/hadoop/share/hadoop/common/hadoop-common-2.7.2.jar:/usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.2.jar:/usr/local/hadoop/share/hadoop/common/lib/commons-cli-1.2.jar:/usr/local/hadoop/share/hadoop/common/lib/hadoop-annotations-2.7.2.jar   *.java

mv *.class wordcountc
jar -cvf wordcount.jar -C wordcountc .


/usr/local/hadoop/bin/hadoop jar wordcount.jar WordCountPro Final/Final output

hadoop fs -cat /user/ubuntu/output/*
hadoop fs -get /user/ubuntu/output/* Results/
javac GetAnalytics.java
java GetAnalytics
