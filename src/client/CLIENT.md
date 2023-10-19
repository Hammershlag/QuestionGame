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