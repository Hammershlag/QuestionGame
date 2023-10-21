# Client manual

## How to run
To run Client run main inside ClientMain class

## Client side commands

| Command                | Explanation                                                                                         |
|------------------------|-----------------------------------------------------------------------------------------------------|
| `new:message`          | Send a message to the server from a new client.                                                     |
| `newXn:message`        | Send a message to the server from n new clients.                                                    |
| `clientNumber:message` | Send a message to the server from a client with the specified number.                               |
| `clientNumber:exit`    | Send an exit message to the server from a client with the specified number, closing the connection. |
| `all:exit`             | Send an exit message to the server from all clients, closing all connections.                       |
| `exit`                 | Exit program after closing all clients                                                              |
| `clear`                | Clears terminal                                                                                     |

## Client side messages
All Client side messages should be sent in the following format: clientNumber:message

| Message                                                                                                  | Explanation                                |
|----------------------------------------------------------------------------------------------------------|--------------------------------------------|
| `newClient:os:macAddress:ipAddress:port:##batteryPercentage:manufacturer:modelNumber##:username:message` | Sending Client info on connection          |
| `newUser:username:password`                                                                              | Sending new user to add to server database |

## Initialisation arguments

| Argument                      | Explanation                                                                                                       |
|-------------------------------|-------------------------------------------------------------------------------------------------------------------|
| `--help` , `-h`               | Shows help page                                                                                                   |
| `--max-response-time` , `-t`  | Sets max response time from server after which Client will determine Server unreachable (default: 1s, in seconds) |
| `--first-client-index` , `-c` | Index of the first newly created client (default: 1)                                                              |
| `--connection_tries` , `-r`   | Sets number of tries to connect to server before giving up (default: 10)                                          |
| `--log-file` , `-l`           | Sets the file to log console output to (default: serverConsole)                                                   |
| `--max-log-files` , `-m`      | Sets the maximum number of log files to keep (default: 5)                                                         |
| `--log-file-dir` , `-d`       | Sets the directory to store log files in (default: ./logs/)                                                       |
| `--test-mode`, `-e`           | Sets the client to test mode (default: false)                                                                     |

## Test Client

To run tests use `--test-mode` or `-e` as an argument.

## Test file (tests.t)

### Tests should be in following format:
```
Test Number:Test Name:Test Description
    command 1
    answer 1
    command2
    answer 2
    command 3
    answer 3
    ...
end
```
- `command` is the console input you want to test.
- `answer` is the console output you're expecting after running `command`
### Custom commands for testing that will not be considered as console input:
#### 1. For loop; i - how many times you want it to run
```
for i times
    command 1
    answer 1
    command 2
    answer 2
end for
```

#### 2. end
```
    ...
end
```
is equal to:
```
    ...
    1:exit
    ???
    exit
    ???
```

### Custom functions for testing that will be replaced by their output
#### How to use:
```
    ...
    command{functionName}commandContinuation
    ...
```
Just insert the function name between `{}` inside the command, and it will be replaced by its output.

#### Functions:
- `randomNumber(beg,end)` - returns random number between beg and end, beg and end should be integers
- `randomNumber(len)` - returns random number of length len, len should be an integer
- `randomString(len)` - returns random string of length len, len should be an integer

### Variables:

#### How to use:
How to declare a variable:
```
    {variableType variableName = value}
```

You can also initialize a variable with a value from the function:
```
    {variableType variableName = {function}}
```

If you want to use a variable in a command, you have to declare it first. And use ad following:
```
    command{var variableName}commandContinuation
```

They are only available in the test you write them in, and will be replaced by their value. You must declare and initialize them with a correct type before using them.
All variable names should be different inside one test.

#### Variable types:
- `int x = 0` - creates an integer variable named x and sets it to 0
- `string x = 0` - creates a string variable named x and sets it to 0, stored as a string, doesn't have to be in quotes
- `double x = 0` - creates a double variable named x and sets it to 0.0

