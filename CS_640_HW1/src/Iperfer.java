
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
				 //TODO Probably need to do some error checking here to validate
				 //that it is an Integer
				 portNumber = Integer.parseInt(args[i++]);
				 
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
				 //TODO Probably need to do some error checking here to validate
				 //that it is an Integer
				 time = Integer.parseInt(args[i++]);
			 }
			 else
			 {
				 System.out.println("Error: missing or additional arguments");
				 System.exit(1);
			 }
		 }
		 
		 if(clientMode && hostNameFound && portFound && timeFound)
		 {
			 System.out.println("We are in client mode.");
			 System.out.println("Hostname/IP address is: " + hostname);
			 System.out.println("Port number is: " + portNumber);
			 System.out.println("Time duration that data should be generated: " + time);
		 }
		 else if(serverMode && portFound)
		 {
			 System.out.println("We are in server mode.");
			 System.out.println("Port number is: " + portNumber);
		 }
		 
		 else
		 {
			 System.out.println("Error: missing or additional arguments");
			 System.exit(1);
		 }
		 
	 }
	
}
