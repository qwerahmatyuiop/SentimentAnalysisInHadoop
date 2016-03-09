import facebook    #sudo pip install facebook-sdk
from collections import Counter
import itertools
import time
import MySQLdb
import json
import requests

cnx = MySQLdb.connect(user='root', passwd='',
                              host='localhost',
                              db='DataForSP')
cursor=cnx.cursor()

# the access token should be stored as a string
access_token = "CAACEdEose0cBAFbsA8GKZAaHfnOnMjdj29W6SmANG28IBek4tUTZB9wsGx3lW9KV2U3TvGbCVisfJCy6OVpaeFcUKYBoek80voZBoXUZCaa4nnoRaFRp3rmECD1a3hbGkXPCnM6pHZAYuGavKB8UCj0VLW3cgSZC8OpvK8rWmvZBpv15ucoPLgHO9ZCRt1lNzCS4gtZAr6VGWNAZDZD"

g = facebook.GraphAPI(access_token) #creating connection to the Facebook Graph API through facebook-sdk

user='uplbconfessions'
profile=g.get_object(user)
posts = g.get_connections(profile['id'],'posts',limit=100)
count = 0
while True:
	try:
		for post in posts['data']:
			print post["message"]
			print post["created_time"]
        	#print "page ", i
        	#print posts
        	#try:
        	print "-------------------------------"
        	count = count + 1
	        posts= requests.get(posts['paging']['next']).json()
	except KeyError:
		break
print count
