all:
    port=1234
    ip=192.168.0.94 # localhost
server:
    max_clients=5
    ping_interval=500 # milliseconds
    min_ping_interval=100 # milliseconds
client:
    max_response_time=1 # seconds
    first_client_index=1
    connection_tries=10

