# Connect1
# The description below is the project spec for the programming portion of our project (Iperfer). Project was performed for CS 640 at the University of Wisconsin - Madison. Instructor Aaron Gember-Jacobson in the Spring of 2015.
# I worked with Mike Dillenbeck on this assignment
# All that is attached from this homework is the code we used to create Iperfer. The actual measurements we recorded for the assignment are located else where. 
# Should run fairly similar to the iperf command: https://iperf.fr/

----------------------------------------------------------------------------------------------------------------------------
Iperfer

For the first part of the assignment you will write your own version of iperf to measure network bandwidth. Your tool, called Iperfer, will send and receive TCP packets between a pair of hosts using sockets. When operating in client mode, Iperfer will send TCP packets to a specific host for a specified time window and track how much data was sent during that time frame; it will calculate and display the bandwidth based on how much data was sent in the elapsed time. When operating in server mode, Iperfer will receive TCP packets and track how much data was received during the lifetime of a connection; it will calculate and display the bandwidth based on how much data was received and how much time elapsed between received the first and last byte of data.

******Client Mode******

To operate Iperfer in client mode, it should be invoked as follows:
java Iperfer -c -h <server hostname> -p <server port> -t <time>
-c indicates this is the iperf client which should generate data
server hostname is the hostname or IP address of the iperf server which will consume data
server port is the port on which the remote host is waiting to consume data; the port should be in the range 1024 < server port < 65535
time is the duration in seconds for which data should be generated
You can use the presence of the -c option to determine Iperfer should operate in client mode.
If any arguments are missing or additional arguments are provided, you should print the following and exit:
  Error: missing or additional arguments
If the server port argument is less than 1024 or greater than 65535, you should print the following and exit:
  Error: port number must be in the range 1024 to 65535
When running as a client, Iperfer must establish a TCP connection with the server and send data as quickly as possible for time seconds. Data should be sent in chunks of 1000 bytes and the data should be all zeros. Keep a running total of the number of bytes sent.
After time seconds have passed, Iperfer must stop sending data and close the connection.  Iperfer must print a one line summary that includes:
The total number of bytes sent (in kilobytes)
The rate at which traffic could be sent (in megabits per second (Mbps))
For example:
  sent=6543 KB rate=5.234 Mbps
You should assume 1 kilobyte (KB) = 1000 bytes (B) and 1 megabyte (MB) = 1000 KB. As always, 1 byte (B) = 8 bits (b).

******Server Mode*****

To operate Iperfer in server mode, it should be invoked as follows:
java Iperfer -s -p <listen port>
-s indicates this is the iperf server which should consume data
listen port is the port on which the host is waiting to consume data; the port should be in the range 1024 < listen port < 65536
You can use the presence of the -s option to determine Iperfer should operate in client mode.
If arguments are missing or additional arguments are provided, you should print the following and exit:
  Error: missing or additional arguments
If the listen port argument is less than 1024 or greater than 65535, you should print the following and exit:
  Error: port number must be in the range 1024 to 65535
When running as a server, Iperfer must listen for TCP connections from a client and receive data as quickly as possible until the client closes the connection. Data should be read in chunks of 1000 bytes. Keep a running total of the number of bytes received.
After the client has closed the connection, Iperfer must print a one line summary that includes:
The total number of bytes received (in kilobytes)
The rate at which traffic could be read (in megabits per second (Mbps))
For example:
  received=6543 KB rate=4.758 Mbps
The Iperfer server should shut down after it handles one connection from a client.
