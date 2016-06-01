group <- "2228732028"
dates <- seq(as.Date("2013/05/08"), as.Date("2016/05/08 "), by="month")
n <- length(dates)-1

df <- list()

for (i in 1:n){
  print(i)
  cat(as.character(dates[i]), " ")
  try(df[[i]] <- getGroup(group, "EAACEdEose0cBAH87ubZCrxSZAwi4EYh3mVtxv6ZCZBvsqUuwcDsIYImwzmoZACXU8Q77EcPFd2aSBiA2kQbumrjZBETdBHuTnGhuCAlW1phyJZC9m46Mf5vQLyAM6n1jTCCiuwOqLZAcs8ZBBuulmfpcuEjbPXosx6kYxgC43fkjxLAZDZD", since=dates[i], until=dates[i+1], n = 1000))
  cat("\n")
  Sys.sleep(60)
  
}

df <- do.call(rbind, df)
write.csv(df,file="Facebook University of the Philippines Group.csv")