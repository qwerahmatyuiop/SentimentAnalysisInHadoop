#token <- "CAACEdEose0cBAO2u7vacY3cNQgi4mHMxD5jJECpkvdKutv4JKQjwF93BZATY4sU1KjPeuOneql7N759EIIVVPijWgcTpZAb7FTaim9lncXZAgYqI0ZBWotaAEJwS5rNCfq62wHywABoG7kiDOLQnRjklqiNrw2kJj5Qsp3nsgOk561dAHWb5W04LZAts6VZCW7QGKM44GzyHNJmCZBXwLPn"
page <- ''
dates <- seq(as.Date("2013/07/17"), as.Date("2016/05/17 "), by="month")
n <- length(dates)-1
df <- list()
for (i in 1:n){
  cat(as.character(dates[i]), " ")
  try(df[[i]] <- getPage(page, fb_oauth, since=dates[i], until=dates[i+1], n = 50000))
  cat("\n")
}
df <- do.call(rbind, df)
write.csv(df,file="Facebook upsahalalan.csv")