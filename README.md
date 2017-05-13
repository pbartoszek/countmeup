# A vote counting and results generating system 

#### Running  tests
In order to run tests please invoke

```sh
 mvn test
```
#### Starting application
In order to start the application please invoke 

```sh
 mvn spring-boot:run
```
#### REST-ful API
Application will listen on port 8080

To register a void please use following REST-ful endpoint

```sh
 POST /vote

 {
    {"candidate" :"B", "voter":"B"}    
 }
```

To get the voting results please use following REST-ful endpoint

```sh
GET /results

 {
    {
      "A": 4,
      "B": 1,
      "C": 4,
      "D": 2
    }    
 }
```


#### Assumptions

I have made the following assumptions:
   - All voters are registered on the fly in a thread safe manner
  - There are 'hardcoded' 4 candidates A, B, C, D
  - AS there is only one pull no poll id is being passed in the register vote request
  - If registering 2 or 3 votes for the same candidate by the same voter corresponing 2 or 3 saparate requests must be made
  - In the code I used some synchronization to make code thread safe. If using database optimistic locking such synchronization wouldn't be needed 
