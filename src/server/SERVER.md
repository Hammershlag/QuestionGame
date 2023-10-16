# Server manual

## How to run
To run Server run main inside ServerMain class

## Server side commands

| Command | Explanation                            |
|---------|----------------------------------------|
| `exit`  | Exit program after closing all clients |
| `ping`  | Force ping all existing connections    |
| `clear` | Clears terminal                        |

## Server side messages

| Message                                                                                                                                                           | Explanation                                                                                                  |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| `newClient:os:macAddress:ipAddress:port:##batteryPercentage:manufacturer:modelNumber##:username:message`                                                          | Getting Client info on first connection                                                                      |
| `newUser:username:password`                                                                                                                                       | Getting new user to add to database (users.txt)                                                              |
| `getUser:id:ACTUALID`                                                                                                                                             | Getting user from database by id, where ACTUALID is id (users.txt)                                           |
| `getUser:username:ACTUALUSERNAME`                                                                                                                                 | Getting user from database by username, where ACTUALUSERNAME is username (users.txt)                         |
| `login:username:password`                                                                                                                                         | Getting user to login from database (users.txt)                                                              |
| `getQuestion:id:ACTUALID`                                                                                                                                         | actualGetting question from database by id (questions.txt)                                                   |
| `getQuestion:random`                                                                                                                                              | Getting random question from database (questions.txt)                                                        |
| `addQuestion:type:question:correct_answer:answer;answer;answer:...`                                                                                               | Getting new question to add to database (questions.txt)                                                      |
| `answerQuestion:id:ACTUALID:answer:userId`                                                                                                                        | Getting answer to question from database by id (questions.txt)                                               |
 | `addRelation:user1Id:user2Id`                                                                                                                                     | Getting new relation to add to database (relation.txt)                                                       |
| `getRelation:id:ID`                                                                                                                                               | Getting relation from database by relation Id (relation.txt)                                                 |
| `getRelation:users:userId1:userId2`                                                                                                                               | Getting relation from database by users ids (relation.txt)                                                   |
| `updateRelation:id:ID:totalQuestionsAnswered:correctQuestionsAnswered:user1QuestionIdList:user2QuestionIdList:user1UnansweredList:user2UnansweredList`            | Getting relation to update in database by relation Id (relation.txt)                                         |
| `updateRelation:users:Id1:Id2:totalQuestionsAnswered:correctQuestionsAnswered:user1QuestionIdList:user2QuestionIdList:user1UnansweredList:user2UnansweredList`    | Getting relation to update in database by relation users ids (relation.txt)                                  |
| `getRelations:userId:id`                                                                                                                                          | Getting all relations from database by userId (relation.txt)                                                 |
| `getRelations:all`                                                                                                                                                | Getting all relations from database (relation.txt)                                                           |
| `getRelationQuestions:id:RELATIONID:userId:ID:id:random`                                                                                                          | Getting random question from database for relation with RELATIONID (relation.txt)                            |
| `answerRelationQuestion:id:RELATIONID:userId:questionId:answer`                                                                                                   | Getting answer to question from database for relation with RELATIONID (relation.txt)                         |
| `relationAddQuestionAnswer:id:RELATIONID:userId:id:questionId:id:answer`                                                                                          | Getting answer to question to database for relation with RELATIONID (to unanswered questions) (relation.txt) |
## Initialisation arguments

| Argument                         | Explanation                                                                                                       |
|----------------------------------|-------------------------------------------------------------------------------------------------------------------|
| `--help` , `-h`                  | Shows help page                                                                                                   |
| `--port` , `-p`                  | Sets the port to listen on (default: 1234)                                                                        |
| `--outgoing` , `-o`              | If true, the server will listen for outgoing connections (default: false)                                         |
| `--max-clients` , `-c`           | Sets the maximum amount of clients that can connect to the server (default: 5)                                    |
| `--ping-interval` , `-i`         | Sets the interval between pings (default: 500, minimum: 100) - measured in ms                                     |
| `--log-file` , `-l`              | Sets the file to log console output to (default: serverConsole)                                                   |
| `--max-log-files` , `-m`         | Sets the maximum number of log files to keep (default: 5)                                                         |
| `--log-file-dir` , `-d`          | Sets the directory to store log files in (default: ./logs/)                                                       |
| `--user-database-dir` , `-u`     | Sets the directory to store users in - user Database (default: ./database/userDatabase/users.txt)                 |
| `--question-database-dir` , `-q` | Sets the directory to store questions in - question Database (default: ./database/questionDatabase/questions.txt) |
| `--relation-database-dir` , `-r` | Sets the directory to store relations in - relation Database (default: ./database/relationDatabase/relation.txt)   |

## Users database (users.txt)
- Change database/userDatabase/users.txt to actual users database
- Users passwords should be hashed and never stored in plain text
- Format: `id:username:password`

## Questions database (questions.txt)
- Change database/questionsDatabase/questions.txt to actual questions database
- Format: `id:type:question:correct_answer:answer;answer;answer;...` - infinitely many answers supported
- `correct_answer` should be one of the answers, is taken into consideration only when question is of type `0`, not if `1`

## Relation database (relation.txt)
- Change database/relationDatabase/relation.txt to actual relation database
- Format: `relationId:user1Id:user2Id:totalQuestionsAnswered:correctQuestionsAnswered:user1QuestionIdList(q1;q2;...):user2QuestionIdList(q1;q2;...):user1UnansweredList(q1-a1;q2-a2;...):user2UnaansweredList(q1-a1;q2-a2;...)`
