# Instructions:

- Place the file ``` connection.properties ``` on the same directory of the parser.jar. This file contains the properties for database connection configuration

- The command for running the application should include the path to the mysql driver. Example:

```
java -cp "<mysql-driver-jar>": "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 
```

- There is a mysql driver located on ``` resources\db\mysql-connector-java-5.1.45-bin.jar ``` 

### SQL

1. Write MySQL query to find IPs that mode more than a certain number of requests for a given time period
The query can be found on ``` resources\db\query1.sql ```

2. Write MySQL query to find requests made by a given IP
The query can be found on ``` resources\db\query2.sql ```

# Author
- Pedro Henrique Veras Coelho - pedroveras@gmail.com 