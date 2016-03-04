package serverSideNew;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import commonLibrary.Packet;
import commonLibrary.SenderReceiver;

public class Server implements Runnable {

	Socket csocket;
	static ClientVideoDatabase database = new ClientVideoDatabase();
	Server(Socket csocket) {
		this.csocket = csocket;
		System.out.println("New thread "+Thread.currentThread().getId()+" started to service "+csocket.getInetAddress().getHostAddress()+":"+csocket.getPort());
	}
	
	public void run() {
		while(true){
			Packet receivedPacket = new Packet(new SenderReceiver().receiveMessageOn(csocket));
			System.out.println("Option "+receivedPacket.getOption()+" - "+receivedPacket.getData()+" received!");
			switch(receivedPacket.getOption()){
				case 0:
					System.out.println("From: "+csocket.getInetAddress().getHostAddress()+":");
					String[] breakUp = receivedPacket.getData().split("::");
					try{
						if(breakUp.length!=2){
							if(breakUp[1].equals("null"))
								continue;
							else{
								System.out.println(Arrays.toString(breakUp));
								throw new Exception("Packet 0 not in expected format:"+receivedPacket);
							}
						}
						Server.database.storeAllVideosOfClient(new Client(csocket.getInetAddress().getHostAddress()+":"+breakUp[0]), breakUp[1]);
						
						// no reply sent
						break;
					}catch(Exception e){System.out.println("Exception raised in class Server: "); e.printStackTrace();}
				case 1:
					Packet toBeSent2 = new Packet(2, Server.database.returnAllVideos());
					if(toBeSent2.getData()==null)
						System.out.println("Server.database.returnAllVideos() returned empty:"+toBeSent2.getData());
					System.out.println("Sending Option 2 reply to "+toBeSent2);
					new SenderReceiver().sendMesssageOn(csocket, toBeSent2.getPayload());
					break;
				case 3:
					int videoID = Integer.parseInt(receivedPacket.getData());
					Packet toBeSent4 = new Packet(4, Server.database.fetchListOfClientsHavingVideo(videoID));
					System.out.println("Sending Option 4 reply"+toBeSent4);
					new SenderReceiver().sendMesssageOn(csocket, toBeSent4.getPayload());
			}
		}
	}
	
	public static void main(String args[]) 
	   throws Exception {
		int serverPort = 65423; // changed by Vicky
		ServerSocket ssock = new ServerSocket(serverPort); // changed by Vicky
		Socket connectionSocket;
		
		//Get IP Address of 'enp0s8'
		String ipAddress = NetworkAddress.getIPAddress("enp0s8");
		// Upload IPAddress:Port number at http://www4.ncsu.edu/~vpkatara/server.txt
		new UploadToServer().upload(ipAddress, serverPort);
		
		System.out.println("Server Started!! at "+ipAddress+":"+serverPort);
		
		while (true) {
			Socket sock = ssock.accept();
			new Thread(new Server(sock)).start();
		}
	}
	
}
