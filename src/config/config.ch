all:
    port=1337                   # port of the server
    ip=192.168.0.94             # ip of the server allowing outside connections
    iploc=localhost             # ip of the server allowing only local connections
    outgoing=true               # true if you want to connect on ip false if you want to connect on iploc
server:
    max_clients=5               # max clients connected to the server at once
    ping_interval=500           # interval between pings in milliseconds
    min_ping_interval=100       # minimum interval between pings in milliseconds
client:
    max_response_time=1         # maximum time to wait for a response from the server in seconds
    first_client_index=1        # index of the first client
    connection_tries=10         # number of tries to connect to the server before giving up

