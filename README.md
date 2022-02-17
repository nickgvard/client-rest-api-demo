
### This pet-project using an open source Stocks feed API. The documentation is here: https://iexcloud.io/docs/api/
##### Async query the stocks API. There are multiple threads processing the queue. 
##### Saves data from each stock quote to a database structure.
##### Output statistics, for example: 
> ##### The top 5 highest value stocks (in order – largest first, then order by company name)
> ##### The most recent 5 companies that have the greatest change percent in stock value.
##### Project deploymend on AWS ECS cluster.

### Technology stack:
+ Java
+ Spring (IoC, Data)
+ MySQL
+ Hibernate
+ Gradle
+ Flyway
+ Docker
+ AWS SDK (ECR, ECS)
