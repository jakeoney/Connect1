/**
 * CS 640 - Computer Networks
 * Homework 1: Sockets, Mininet, & Performance
 * 
 * @author Jake Oney
 * @author Mike Dillenbeck
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

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

				if(arg.equals("-c"))
					clientMode = true;

				else if(arg.equals("-h"))
				{
					hostNameFound = true;
					hostname = args[i++];
				}

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

				else if(arg.equals("-s"))
					serverMode = true;

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
				else
				{
					System.out.println("Error: missing or additional arguments");
					System.exit(1);
				}
			}
		}
		else
		{
			System.out.println("Error: missing or additional arguments");
			System.exit(1);
		}

		if(clientMode && hostNameFound && portFound && timeFound)
		{
			runClient(hostname, portNumber, time);
			//below print lines are just used to show that we are gettting the correct
			//values. remove these before handin
			/*
			 System.out.println("We are in client mode.");
			 System.out.println("Hostname/IP address is: " + hostname);
			 System.out.println("Port number is: " + portNumber);
			 System.out.println("Time duration that data should be generated: " + time);
			 */
		}
		else if(serverMode && portFound)
		{
			runServer(portNumber);
			//below print lines are just used to show that we are gettting the correct
			//values. remove these before handin
			/*System.out.println("We are in server mode.");
			 System.out.println("Port number is: " + portNumber);
			 */
		}
		else
		{
			System.out.println("Error: missing or additional arguments");
			System.exit(1);
		}
		System.exit(0);
	}

	/**
	 * 
	 * 
	 * @param hostname
	 * @param portNumber
	 * @param time
	 */
	private static void runClient(String hostname, int portNumber, int time)
	{
		int sent = 0;
		int speed = 0;
		byte[] packet = new byte[1000];
		OutputStream outToServer;
		long endTime;

		Socket client = new Socket();
		InetSocketAddress host = new InetSocketAddress(hostname, portNumber);

		try {
			client.connect(host);
			outToServer = client.getOutputStream();

			endTime = System.currentTimeMillis() + (time * 1000);

			do{
				outToServer.write(packet);
				sent++;
			} while (System.currentTimeMillis() < endTime);

			client.close();
		} catch (IOException e) {
			System.out.println("Error IO Exception");
			System.exit(4);
		}
		speed = (8*sent/1000)/time;
		System.out.println("sent=" + sent + " KB rate=" + speed + " Mbps");
	}

	/**
	 *
	 * @param portNumber
	 */
	private static void runServer(int portNumber)
	{
		int received = 0;
		long speed = 0;
		InputStream toServer;
		long startTime = 0;
		long endTime = 0;
		long time;
		byte[] packet = new byte[1000];

		try {
			ServerSocket server = new ServerSocket();
			InetSocketAddress host = new InetSocketAddress(portNumber);
			server.bind(host);

			Socket client = server.accept();
			toServer = client.getInputStream();
			startTime = System.currentTimeMillis();
			while(toServer.read() != -1)
			{
				
				received++;
			}
			endTime = System.currentTimeMillis();

			client.close();
			server.close();
		} catch (IOException e) {
			System.out.println("Error IO Exception");
			System.exit(4);
		}

		time = (endTime - startTime) / 1000;
		speed = (8*received/1000000)/time;
		System.out.println("received=" + received + " KB rate=" + speed + " Mbps");
	}
}
