/**
 * CS 640 - Computer Networks
 * Homework 1: Sockets, Mininet, & Performance
 * 
 * @author Jake Oney
 * @author Mike Dillenbeck
 */
import java.io.IOException;
import java.net.*;

/*
 * The purpose of Iperfer is to test the connection speed between to machines 
 * (a client and a server). Once a server connection is established, a client
 * will connect to it, and send it a constant stream of packets (thousand byte 
 * packets). It will send these packets for a specified amount of time that is
 * less than 10 minutes. Once the client is done sending and the server is
 * done receiving, both will print out the total number of packets sent/received
 * and the speed at which they sent/received those packets.
 */
public class Iperfer 
{
	public static void main(String[] args) 
	{
		/*Variable Declarations*/
		String arg;
		Boolean clientMode, serverMode, hostNameFound, portFound, timeFound;
		int i;
		String hostname;
		int portNumber;
		int time;

		/*Default Variable Values*/
		i = 0;

		clientMode = false;
		serverMode = false;
		hostNameFound = false;
		portFound = false;
		timeFound = false;

		hostname = null;
		portNumber = 0;
		time = 0;

		/*Parsing Command-Line arguments*/
		if((args.length == 7) || (args.length ==  3))
		{
			while(i < args.length) 
			{
				arg = args[i++];

				//specifies client
				if(arg.equals("-c"))
					clientMode = true;
				
				//specifies host name
				else if(arg.equals("-h"))
				{
					hostNameFound = true;
					hostname = args[i++];
				}

				//specifies port number
				else if(arg.equals("-p"))
				{
					portFound = true;
					try{
						portNumber = Integer.parseInt(args[i++]);
					} catch(NumberFormatException e)
					{
						System.out.println("Error: port number must be in the range 1024 to 65535");
						System.exit(2);
					}

					//Validate port range - 1024 to 66536
					if(portNumber < 1024 || portNumber > 65535)
					{
						System.out.println("Error: port number must be in the range 1024 to 65535");
						System.exit(2);
					}
				}

				//specifies server
				else if(arg.equals("-s"))
					serverMode = true;

				//specifies time to send
				else if(arg.equals("-t"))
				{
					timeFound = true;
					try{
						time = Integer.parseInt(args[i++]);
					} catch(NumberFormatException e)
					{
						System.out.println("Error: missing or additional arguments");
						System.exit(1);
					}
					//Validate that time is positive
					//max time that it can run
					if(time < 0 || time > 600)
					{
						System.out.println("Error: missing or additional arguments");
						System.exit(1);
					}
				}
				//any other command line arguments is an error
				else
				{
					System.out.println("Error: missing or additional arguments");
					System.exit(1);
				}
			}
		}
		//an incorrect number of command line arguments is an error
		else
		{
			System.out.println("Error: missing or additional arguments");
			System.exit(1);
		}

		//depending on which flags were set, runs in either client or server mode
		if(clientMode && hostNameFound && portFound && timeFound)
		{
			runClient(hostname, portNumber, time);
		}
		else if(serverMode && portFound)
		{
			runServer(portNumber);
		}
		else
		{
			System.out.println("Error: missing or additional arguments");
			System.exit(1);
		}
		System.exit(0);
	}

	/**
	 * Runs the client code. Connects to a server and continuously sends packets
	 * to the server for a specified amount of time. 
	 * Prints out the number of KB sent and the Mbps transfer speed
	 * 
	 * @param hostname 
	 * @param portNumber
	 * @param time amount of time to send
	 */
	private static void runClient(String hostname, int portNumber, int time)
	{
		double sent = 0;
		double speed = 0;
		byte[] packet = new byte[1000];
		long endTime;

		Socket client = new Socket();
		InetSocketAddress host = new InetSocketAddress(hostname, portNumber);

		try {
			//establishes a connection
			client.connect(host);
			endTime = System.currentTimeMillis() + (time * 1000);
		
			//sends packets until the time specified is up
			while (System.currentTimeMillis() < endTime)
			{
				client.getOutputStream().write(packet);
				sent++;
			}
			//closes the connection
			client.close();
		} catch (IOException e) {
			System.out.println("Error IO Exception");
			System.exit(4);
		}
		speed = (8.0*sent/1000.0)/(double)time;
		//prints the number of bytes sent and the speed it was sent
		System.out.println("sent=" + (int)(sent + 0.01) + " KB rate=" + speed + " Mbps");
	}

	/**
	 * Runs the server code. Waits for a connection and counts the number of
	 * packets that it receives.
	 * Prints out the number of KB received and the Mbps transfer speed
	 * 
	 * @param portNumber
	 */
	private static void runServer(int portNumber)
	{
		double received = 0;
		double speed = 0;
		long startTime = 0;
		long endTime = 0;
		double time;
		byte[] packet = new byte[1000];
		double num = 0;

		try {
			ServerSocket server = new ServerSocket();
			InetSocketAddress host = new InetSocketAddress(portNumber);
			server.bind(host);
			//waits to connect to a client
			Socket client = server.accept();
			startTime = System.currentTimeMillis();
			//receives all the packets that the client is sending
			while(num > -1)
			{
				num = client.getInputStream().read(packet, 0, 1000);
				received += num/1000;
			}
			endTime = System.currentTimeMillis();

			//ends the client and server connections
			client.close();
			server.close();
		} catch (IOException e) {
			System.out.println("Error IO Exception");
			System.exit(4);
		}

		time = (double)(endTime - startTime) / 1000.0;
		speed = (8.0*received/1000.0)/time;
		//displays the bytes received and the speed it received them
		System.out.println("received=" + (int)(received + 0.01) + " KB rate=" + speed + " Mbps");
	}
}
