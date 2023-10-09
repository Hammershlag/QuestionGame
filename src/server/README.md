# Server side commands

| Command | Explanation                            |
|---------|----------------------------------------|
| `exit`  | Exit program after closing all clients |
| `ping`  | Force ping all existing connections    |
| `clear` | Clears terminal                        |

# Server side messages

| Message                                                                                                  | Explanation                             |
|----------------------------------------------------------------------------------------------------------|-----------------------------------------|
| `newClient:os:macAddress:ipAddress:port:##batteryPercentage:manufacturer:modelNumber##:username:message` | Getting Client info on first connection |

# Initialisation arguments

| Argument                | Explanation                                                                    |
|-------------------------|--------------------------------------------------------------------------------|
| `--help` , `-h`         | Shows help page                                                                |
| `--port` , `-p`         | Sets the port to listen on (default: 1337)                                     |
| `--outgoing` , `-o`     | If true, the server will listen for outgoing connections (default: false)      |
| `--max-clients` , `-c`  | Sets the maximum amount of clients that can connect to the server (default: 5) |
| `--ping-interval` , `-i` | Sets the interval between pings (default: 500, minimum: 100) - measured in ms  |