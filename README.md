Instructions:

- Place the file ``` connection.properties ``` on the same directory of the parser.jar. This file contains the properties for database connection configuration

- The command for running the application should include the path to the mysql driver. Example:

```
java -cp "<mysql-driver-jar>": "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 
```

# Author
- Pedro Henrique Veras Coelho - pedroveras@gmail.com 