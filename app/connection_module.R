library(dplyr)
library(RSQLite)
library(digest)


library(RSQLite)


init<-function(){
  db <- dbConnect(SQLite(), dbname="db.sqlite")
  rs<-dbSendQuery(conn = db,
              "CREATE TABLE if not exists users
        (name TEXT,
        surname TEXT,
            mail TEXT,
            age TEXT,
            gender TINYINT,
            login TEXT,
            pass TEXT,
            role TINYINT)")
  dbClearResult(rs)
  dbDisconnect(db)
}


register<-function(data){
  db <- dbConnect(SQLite(), dbname="db.sqlite")
  pass_enc = digest::digest(data$haslo,algo="sha256")
  tmp = paste("VALUES ('",paste(data$name,data$surname,data$mail,data$age,data$gender,data$username,pass_enc,0,sep="','"),"');")
  print(tmp)
  
  query=paste("INSERT INTO users ( name,surname,mail,age ,gender ,login , pass ,role ) ",tmp)
  print(query)
  rs1<-dbSendQuery(db,query)
  
  
  dbClearResult(rs1)
  
  
  return(TRUE)
  
}

login<-function(data){
  db <- dbConnect(SQLite(), dbname="db.sqlite")
  pass_enc = digest::digest(data$pass,algo="sha256")
  tmp = paste("login='",data$login,"' AND ","pass='",pass_enc,"';",sep="")
  # query = "SELECT * FROM users"
  query = paste("SELECT COUNT(*) as 'FOUND' FROM users WHERE ",tmp)
  print(query)
  rs1<-dbSendQuery(db,query)
  # print(dbFetch(rs1))
  result =  dbFetch(rs1)$FOUND
  print(result)
  dbClearResult(rs1)
  dbDisconnect(db)
  return(result)
  
}

findLogin<-function(login){
  db <- dbConnect(SQLite(), dbname="db.sqlite")
  tmp = paste("login='",login,"';",sep="")
  # query = "SELECT * FROM users"
  query = paste("SELECT COUNT(*) as 'FOUND' FROM users WHERE ",tmp)
  print(query)
  rs1<-dbSendQuery(db,query)
  # print(dbFetch(rs1))
  result =  dbFetch(rs1)$FOUND
  print(result)
  dbClearResult(rs1)
  dbDisconnect(db)
  return(result)
  
}

