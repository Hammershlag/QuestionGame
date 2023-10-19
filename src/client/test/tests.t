1: Client Creation: Testing if you cas create a single client and then exit
    new:Testing
    ???
    1:exit
    ???
    exit
    ???
end

2: Client Creation: Testing if you can create multiple clients and then exit
    newX3:Testing
    ???
    all:exit
    ???
    exit
    ???
end

3: Sending message: Testing invalid message
    new:Testing
    ???
    1:This is wrong message that should give wrong server response
    ???
    1:exit
    ???
    exit
    ???
end

4: Sending message: Valid message type with not valid input
    new:Testing
    ???
    1:newUser:ThisShouldBeWrong
    ???
    1:exit
    ???
    exit
    ???
end

5: New User: Testing newUser, non existing
    new:Testing
    ???
    1:newUser:notExisting:notExisting
    ???
    1:exit
    ???
    exit
    ???
end

6: New User: Testing newUser, already existing
    new:Testing
    ???
    1:newUser:existing:existing
    ???
    1:exit
    ???
    exit
    ???
end

7: Get User: Testing getUser, non existing, by id
    new:Testing
    ???
    1:getUser:id:notExistingId
    ???
    1:exit
    ???
    exit
    ???
end

8: Get User: Testing getUser, already existing, by id
    new:Testing
    ???
    1:getUser:id:alreadyExistingId
    ???
    1:exit
    ???
    exit
    ???
end

9: Get User: Testing getUser, non existing, by username
    new:Testing
    ???
    1:getUser:username:nonExistingUsername
    ???
    1:exit
    ???
    exit
    ???
end

10: Get User: Testing getUser, already existing, by username
    new:Testing
    ???
    1:getUser:username:alreadyExistingUsername
    ???
    1:exit
    ???
    exit
    ???
end

11: Login: Testing login, non existing username
    new:Testing
    ???
    1:login:nonExistingUsername:somePassword
    ???
    1:exit
    ???
    exit
    ???
end

12: Login: Testing login, existing username, wrong password
    new:Testing
    ???
    1:login:alreadyExistingUsername:wrongPassword
    ???
    1:exit
    ???
    exit
    ???
end

13: Login: Testing login, existing username, good password
    new:Testing
    ???
    1:login:alreadyExistingUsername:goodPassword
    ???
    1:exit
    ???
    exit
    ???
end

14: Get Question: Testing getQuestion, non existing, by id
    new:Testing
    ???
    1:getQuestion:id:notExistingId
    ???
    1:exit
    ???
    exit
    ???
end

15: Get Question: Testing getQuestion, existing, by id
    new:Testing
    ???
    1:getQuestion:id:alreadyExistingId
    ???
    1:exit
    ???
    exit
    ???
end

16: Get Question: Testing getQuestion, by getting random
    new:Testing
    ???
    1:getQuestion:random
    ???
    1:exit
    ???
    exit
    ???
end

17: Get Question: Testing getQuestion, by getting random multiple times to see if you get different questions
    new:Testing
    ???
    for 10 times:
        1:getQuestion:random
        ???
    end for
    1:exit
    ???
    exit
    ???
end

18: Add Question: Testing addQuestion, already existing question
    new:Testing
    ???
    1:addQuestion:specifyType:alreadyExistingQuestion:correct_answer:answer;answer;answer
    ???
    1:exit
    ???
    exit
    ???
end

18: Add Question: Testing addQuestion, non existing question
    new:Testing
    ???
    1:addQuestion:specifyType:nonExistingQuestion:correct_answer:answer;answer;answer
    ???
    1:getQuestion:id:addedQuestionId
    ???
    1:exit
    ???
    exit
    ???
end

19: Answer Question: Testing answerQuestion, non existing question
    new:Testing
    ???
    1:answerQuestion:id:nonExistingId:someAnswer:someUser
    ???
    1:exit
    ???
    exit
    ???
end

20: Answer Question: Testing answerQuestion, already existing question, wrong answer, non existing username
    new:Testing
    ???
    1:answerQuestion:id:alreadyExistingId:wrongAnswer:wrongUsername
    ???
    1:exit
    ???
    exit
    ???
end

21: Answer Question: Testing answerQuestion, already existing question, wrong answer, already existing username
    new:Testing
    ???
    1:answerQuestion:id:alreadyExistingId:wrongAnswer:goodUsername
    ???
    1:exit
    ???
    exit
    ???
end

22: Answer Question: Testing answerQuestion, already existing question, good answer, non existing username
    new:Testing
    ???
    1:answerQuestion:id:alreadyExistingId:goodAnswer:wrongUsername
    ???
    1:exit
    ???
    exit
    ???
end

23: Answer Question: Testing answerQuestion, already existing question, good answer, already existing username
    new:Testing
    ???
    1:answerQuestion:id:alreadyExistingId:goodAnswer:goodUsername
    ???
    1:exit
    ???
    exit
    ???
end











