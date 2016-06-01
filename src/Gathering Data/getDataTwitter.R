ds <- searchTwitter("#PhilippineElections2016", n=100000, retryOnRateLimit = 99999, since="2000-01-01")
#dm = strip_retweets(ds)
df <- do.call("rbind",lapply(dm, as.data.frame))
write.csv(df,file="twitterdata #PhilippineElections2016.csv")

