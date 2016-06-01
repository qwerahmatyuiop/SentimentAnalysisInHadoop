#!/bin/bash 
#this is the script to run the Naive Bayes Classifier
rm wordcountc/*
hadoop fs -rm -r /user/ubuntu/output2

javac -classpath /usr/local/hadoop/share/hadoop/common/hadoop-common-2.7.2.jar:/usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.2.jar:/usr/local/hadoop/share/hadoop/common/lib/commons-cli-1.2.jar:/usr/local/hadoop/share/hadoop/common/lib/hadoop-annotations-2.7.2.jar   *.java

mv *.class wordcountc
jar -cvf wordcount.jar -C wordcountc .


/usr/local/hadoop/bin/hadoop jar wordcount.jar WordCountBayes Final/Final output2

hadoop fs -cat /user/ubuntu/output2/*
rm Results/*
hadoop fs -get /user/ubuntu/output2/* Results/
#javac GetAnalytics.java
#java GetAnalytics
