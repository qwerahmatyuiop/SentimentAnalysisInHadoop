Author: Rahmat Peter I. Dabalos

README:

Setup Hadoop: 
1. Create 4 instances in P2C(http://srg.ics.uplb.edu.ph/projects/peak-two-cloud/peak-two-cloud-resources/user-guide).
	1 Masternode
	3 Slavenodes
2. Edit all the /etc/hosts with all the ip-addresses of each instance
3. Extract hadoop-2.7.2 in /usr/local/hadoop for Masternode, and Slavenodes
4. make ssh passwordless for each instance
5. put into .bashrc the bin path of hadoop
6. Configure Hadoop config files:  follow instructions from : 
			http://chaalpritam.blogspot.com/2015/05/hadoop-270-single-node-cluster-setup-on.html


Running Naive Bayes / Dictionary Based
1. Run start-all.sh
2. make directories(hadoop fs -mkdir path)
					/user/ubuntu/dictionary 
					/user/ubuntu/dataset
					/user/ubuntu/Final/Final
3. put into hdfs(hadoop fs -put file)
				hadoop fs -put negative-words-combined.text /user/ubuntu/dictionary/
				hadoop fs -put positive-words-combined.text /user/ubuntu/dictionary/
				hadoop fs -put stop-words.text /user/ubuntu/dictionary/
				hadoop fs -put positive-text negative-text /user/ubuntu/dataset
				hadoop fs -put finalData /user/ubuntu/Final/Final
4. run ./compileandrun.sh from either bayes(Naive Bayes) or wordcountprof(Dictionary Based) folder
5. results will be in hdfs /user/ubuntu/output/ for Dictionary Based and /user/ubuntu/output2/ for Naive Bayes

Gathering Data:

1. Rfacebook: follow instructions from this link: http://pablobarbera.com/blog/archives/3.html
2. twitteR:  follow instruction from this link: http://www.r-bloggers.com/getting-started-with-twitter-in-r/
3. Apache Flume and Apache Hive: from this link: http://www.thecloudavenue.com/2013/03/analyse-tweets-using-flume-hadoop-and.html

WordCloud tool: tagul.com
Bell Curve Plot: used excel