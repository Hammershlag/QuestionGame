# Client side commands

| Command                | Explanation                                                                                         |
|------------------------|-----------------------------------------------------------------------------------------------------|
| `new:message`          | Send a message to the server from a new client.                                                     |
| `newXn:message`        | Send a message to the server from n new clients.                                                    |
| `clientNumber:message` | Send a message to the server from a client with the specified number.                               |
| `clientNumber:exit`    | Send an exit message to the server from a client with the specified number, closing the connection. |
| `all:exit`             | Send an exit message to the server from all clients, closing all connections.                       |
| `exit`                 | Exit program after closing all clients                                                              |
| `clear`                | Clears terminal                                                                                     |


Send client info on first connection
message: `newClient:os:macAddress:ipAddress:batteryPercentage:manufacturer:modelNumber:username`