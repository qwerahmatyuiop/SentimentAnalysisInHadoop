ds <- searchTwitter("UPLB", n=10000, retryOnRateLimit = 120, since="2000-01-01")
dm = strip_retweets(ds)
df <- do.call("rbind",lapply(dm, as.data.frame))
write.csv(df,file="twitterdata UPLBWalkout2.csv")


