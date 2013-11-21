(My)SQL DATABASE STRUCTURE

TABLE People
int id primary key
varchar(50) fname
varchar(50) lname
int rankID
int age/bday

TABLE Ranks
int id
varchar(50) name (eg DCA, DCI)
int level

TABLE Positions
int id primary key
int level
varchar(50) name
boolean grant
int minAge

