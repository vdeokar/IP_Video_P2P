package requestReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RequestReceiver extends Thread
{
   private DatagramSocket UDPserverSocket;
   
   public RequestReceiver(int port) throws IOException
   {
	   UDPserverSocket = new DatagramSocket(port);
	   System.out.println("UDP Request Receiver Running on "+UDPserverSocket.getInetAddress()+":"+UDPserverSocket.getLocalPort());
	   UDPserverSocket.setSoTimeout(0);
   }

   public void run()
   {
      while(true)
      {
         try
         {
            //System.out.println("Waiting for client on port " +serverSocket.getLocalPort() + "...");
        	 byte[] receiveBuffer = new byte[512];
        	 DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        	 UDPserverSocket.receive(receivePacket);
        	 System.out.println("Request received from "+ receivePacket.getSocketAddress()+":"+receivePacket.getPort());
            
            // Start new 'Servant' thread and make it serve the peer
            new Servant(receivePacket, UDPserverSocket).start();
            //socketFromPeer.close();
            
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
   public static void main(String [] args)
   {
      int port = Integer.parseInt(args[0]);
      try
      {
         Thread t = new RequestReceiver(port);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
}