#!/bin/bash

javac -classpath /usr/local/hadoop/share/hadoop/common/hadoop-common-2.7.2.jar:/usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.2.jar:/usr/local/hadoop/share/hadoop/common/lib/commons-cli-1.2.jar:/usr/local/hadoop/share/hadoop/common/lib/hadoop-annotations-2.7.2.jar -d ~/Desktop/wordcountprof *.java

mv *.class ~/Desktop/wordcountprof/wordcountc
jar -cvf wordcount.jar -C ~/Desktop/wordcountprof/wordcountc .


/usr/local/hadoop/bin/hadoop jar ~/Desktop/wordcountprof/wordcount.jar WordCountPro ~/Desktop/testdata output