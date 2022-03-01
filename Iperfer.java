import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Iperfer
{
	public static void main(String[] args) throws IOException 
	{
		boolean clientMode = false;
		boolean serverMode = false;
		String hostName = null;
		int portNumber = 0;
		double time = 0;
		String argError = "Error: missing or additional arguments";
		
		// handle case in which Iperfer is run with no command line arguments
		if ((args.length != 7) && (args.length != 3))
		{
			System.out.println(argError);
			return; // NOTE: calling System.exit(1) terminates unit tests early, so just return
		}
		
		/*
		 * parse through arguments
		 * for clientMode args = [-c, -h, <hostName>, -p, <serverPort>, -t, <time>]
		 * for serverMode args = [-s, -p, <listenPort>]
		 */
		for (int i = 0; i < args.length; i++) 
		{
			if (args[i].equals("-c")) 
			{
				clientMode = true;
				if (args.length != 7) 
				{
					System.out.println(argError);
					return;
				}
			}
			
			if (args[i].equals("-s")) 
			{
				serverMode = true;
				if (args.length != 3) 
				{
					System.out.println(argError);
					return;
				}
			}
			
			if (args[i].equals("-h")) 
			{
				hostName = args[i + 1];
				if (hostName == null)
				{
					System.out.println("Error: host name cannot be null");
					return;
				}
			}
			
			if (args[i].equals("-p")) 
			{
				try 
				{
					portNumber = Integer.parseInt(args[i + 1]);
				} 
				catch (NumberFormatException e) 
				{
					System.out.println("Error: port number not converted to int");
					return;
				}
				
				if (portNumber < 1024 || portNumber > 65535) 
				{
					System.out.println("Error: port number must be in the range 1024 to 65535");
					return;
				}
			}
			
			if (args[i].equals("-t")) 
			{
				try 
				{
					time = Integer.parseInt(args[i + 1]);
				} 
				catch (NumberFormatException e) 
				{
					System.out.println("Error: time not converted to int");
					return;
				}
			}
		}
		
		if (clientMode) 
		{
			clientMethod(hostName, portNumber, time);
		}
		
		if (serverMode) 
		{
			serverMethod(portNumber);
		}
	}
	
	private static void clientMethod(String hostName, int serverPort, double timeInSec) throws IOException 
	{
		Socket clientSocket = null;
		
		try 
		{
			clientSocket = new Socket(hostName, serverPort);
		} 
		catch (ConnectException ce) 
		{
			System.out.println("Error: Connection to " + hostName + " was refused.");
			return;
		}
		
		OutputStream out = clientSocket.getOutputStream();
		double packets = 0;
		long durationInNano = (long) (timeInSec * 1000000000); // convert seconds to nanoseconds
		long startTime = System.nanoTime();
		
		while (System.nanoTime() - startTime < durationInNano) 
		{
			out.write(new byte[1000]); // packet of 1000 bytes (1 KB)
			packets++; // increment count of packets
		}
		
		// close stream and socket
		out.close();
		clientSocket.close();
		
		double megabits = packets * 0.008; // 1 kilobyte = 0.008 megaBITS
		double rate = megabits/timeInSec;
		System.out.println("sent=" + packets + " KB rate=" + rate + " Mbps");
	}
	
	private static void serverMethod(int listenPort) throws IOException 
	{
		ServerSocket serverSocket = new ServerSocket(listenPort);
		Socket clientSocket = serverSocket.accept();
		InputStream in = clientSocket.getInputStream();
		double packets = 0;
		double totalBytes = 0;
		double bytesReadFromStream = 0;
		long startTime = System.nanoTime();
		
		do 
		{
			totalBytes = totalBytes + bytesReadFromStream;
			bytesReadFromStream = in.read(new byte[1000]); // packet of 1000 bytes (1 KB)	
		} 
		while (bytesReadFromStream != -1);
		
		packets = totalBytes/1000; // 1000 bytes in a packet
		
		long endTime = System.nanoTime();
		double durationInSec = TimeUnit.SECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS);
		
		// close stream and sockets
		in.close();
		clientSocket.close();
		serverSocket.close();
		
		double megabits = packets * 0.008; // 1 kilobyte = 0.008 megaBITS
		double rate = megabits/durationInSec;
		System.out.println("received=" + packets + " KB rate=" + rate + " Mbps");
	}
}
