package clientSide;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import commonLibrary.Packet;
import commonLibrary.SenderReceiver;
import commonLibrary.Video;
import requestReceiver.RequestReceiver;
import requester.Requester;

public class Client {
	
	//String serverIP;
	//int serverPort;
	static int requestReceiverPortNumber = 6789;
	private Socket socketToVideoServer;
	//private HashMap<Integer, Integer> videoID = new HashMap<Integer, Integer>();
	//private HashMap<Integer, String> videoName = new HashMap<Integer, String>();
	private HashMap<Integer, Video> videoMap = new HashMap<Integer, Video>();
	
	Client(String serverIP, int port){
		//*this.serverIP = serverIP;
		//this.serverPort = port;
		
		System.out.println("Connecting to server at "+serverIP+":"+port);
		
		//Start Receiver Thread which will intercept request messages
		
		socketToVideoServer = new SenderReceiver().returnSocketTo(serverIP, port);// connect to server
		
		// get list of files in user.home/videos
		String pathToVideosDir = System.getProperty("user.home")+File.separatorChar+"Videos";
		ArrayList<File> listOfFiles = ListFiles.returnListOfFilesInDirectory(pathToVideosDir);
		
		//prepare list of video file names
		String fileInfo = getConcatenatedFileInfo(listOfFiles);
		String videoListPayload = new Packet(0, this.requestReceiverPortNumber+"::"+fileInfo).getPayload();//preparePayLoad(0, fileInfo); // FileNames is Option 0:
		
		System.out.println("videoListPayload packet:"+videoListPayload);
		// send list of videos to server
		new SenderReceiver().sendMesssageOn(socketToVideoServer, videoListPayload);
		interact();
		//closeSocketToServer(socketToVideoServer);
		
	}
	
	/*Socket returnSocketTo(String serverIP, int serverPort){
		try{
		return new Socket(serverIP, serverPort);
		}
		catch(IOException e){
			System.err.println("Error:");
			e.printStackTrace();
			return null;
		}
	}*/
	
	void closeSocketToServer(Socket s){
		try{
			s.close();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	String getConcatenatedFileInfo(ArrayList<File> listOfFiles){
		StringBuffer ret = new StringBuffer();
		if(listOfFiles.size()==0)
			return "null";
		for(int i=0;i<listOfFiles.size();i++){
			ret.append(listOfFiles.get(i).getName()).append(":").append((int)Math.ceil(listOfFiles.get(i).length()/(256.0))).append(';');
		}
		return ret.toString();
	}
	
	void interact(){
		Scanner scan = new Scanner(System.in);
		try{
			while(true){
				printMainMenu();
				int input = scan.nextInt();
				switch(input){
				case 0: System.out.println("Thank you for watching! Paying for movies is too mainstream! :P");
					socketToVideoServer.close();
					return;
				case 1: //String getVideoListPayload = preparePayLoad(1, "");
					String getVideoListPayload = new Packet(1, "").getPayload();
					new SenderReceiver().sendMesssageOn(socketToVideoServer, getVideoListPayload);
					Packet videosPacket = new Packet(new SenderReceiver().receiveMessageOn(socketToVideoServer));
					printVideoList(videosPacket);
					askUserForVideo();
					break;
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	void printMainMenu(){
		System.out.println("---------------------------------------------------------------\n-----------------------------Menu------------------------------");
		System.out.println("\t1. View List of Movies available with the swarm");
		//System.out.println("\t2. View List of Local Movies");
		System.out.println("\t0. Exit");
		System.out.println("---------------------------------------------------------------");
		System.out.print("Enter Option: ");
	}
	
	void askUserForVideo(){
		Scanner scan = new Scanner(System.in);
		while(true){
			System.out.print("Enter the Line Number of the Video you wish to watch.\nPress 0 to Exit.\nEnter Option:");
			int choice = scan.nextInt();
			if(choice==0){
				//scan.close();
				return;
			}
			Video requestedVideo = videoMap.get(choice);
			int id = requestedVideo.getVideoID();
			if(id==0){
				System.out.println("Please enter a valid Line Number");
				continue;
			}
			new SenderReceiver().sendMesssageOn(socketToVideoServer, new Packet(3, Integer.toString(id)).getPayload());
			
			Packet option4packet = new Packet(new SenderReceiver().receiveMessageOn(socketToVideoServer));
			ArrayList<Peer> peerList = getPeersFromOption4packet(option4packet);
			
			if(peerList.isEmpty())
				try {
					throw new Exception("No Peers have that video currently.<Client.java: askUserForVideo>");
				} catch (Exception e) {
						try {
							socketToVideoServer.close();
							scan.close();
						} catch (IOException e1) {
						e1.printStackTrace();
						scan.close();
					}
					e.printStackTrace();
					scan.close();
				}
			//
			// start_video_while collating packets
			//
			System.out.println("Trying to get videoStream from these peers:"+peerList);
			//call requestor passing list of peers
			
			new Requester(requestedVideo, peerList);
			
			//EXITing CLIENT SERVER
			break;
		}
		//scan.close();
	}
	
	ArrayList<Peer> getPeersFromOption4packet(Packet option4packet){
		try{
			if(option4packet.getOption()!=4)
				throw new Exception("Message received as Option 4, is not option 4 message. Exiting");
			ArrayList<Peer> listOfPeers = new ArrayList<Peer> ();
			//create array list
			String[] peerList= option4packet.getData().split(";");
			for (int i = 0; i < peerList.length; i++)
			{	
				String[] peerDetails= peerList[i].split(":");
				//System.out.println(Arrays.toString(peerDetails));
				listOfPeers.add(new Peer(peerDetails[0],Integer.parseInt(peerDetails[1])));
				
			}		
			return listOfPeers;
		}
		catch(Exception e){
			if(e.getMessage().contains("Message received as Option 4"))
				System.exit(0);
			e.printStackTrace();
		}
		return null;	
	}
	
	
	
	void  printVideoList(Packet videosPacket){ //Also populate the HashMap videos with (index, videoID) format.
		try{
			if(videosPacket.getOption()!=2)
				throw new Exception("printVideoList():Message received as Option 2, is not option 2 message. Exiting");
			String videoData = videosPacket.getData();
			if(videosPacket.getData().length()<=2)
				throw new Exception("printVideoList():No data received in videos packet!"+videosPacket.getData());
			String[] list=videoData.split(";");
			//System.out.println(Arrays.toString(list));
		   
			System.out.println(".............Video Details................");
			System.out.println("Sr. No\t\t Video title\t\t No. of chunks \t      Format \t      Number of Peers");
			for (int i = 0; i < list.length; i++)
			{ 
				//System.out.println(list[i]);
				String[] videoDetails= list[i].split(",");
				//System.out.println(Arrays.toString(videoDetails));
				StringBuffer s = new StringBuffer(videoDetails[1]);
				s.append("                    ");
				String title = s.substring(0, 20);
			   System.out.println((i+1)+"\t\t"+title+"\t\t"+videoDetails[2]+"\t\t"+videoDetails[3]+"\t\t"+videoDetails[4]);
			   //videoID.put(i+1,Integer.parseInt(videoDetails[0]));
			   Video v = new Video(Integer.parseInt(videoDetails[0]), videoDetails[1], Integer.parseInt(videoDetails[2]));
			   videoMap.put((i+1), v);
			}
		}
		catch(Exception e){
			if(e.getMessage().contains("Message received as Option 2"))
				System.exit(0);
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args)throws IOException {
		String[] connectionInfo = new URLReader().getConnectionString().split(":");
		new RequestReceiver(requestReceiverPortNumber).start();
		Client c =new Client(connectionInfo[0],Integer.parseInt(connectionInfo[1]));
		//int i = Integer.parseInt(null);
		
		//Packet p = new Packet("|option|4|/option||data|192.168.1.1:20000;192.168.1.2:24000;|data|");
		//System.out.println(p);
		//System.out.println(getPeersFromOption4packet(p));
		
	}
}
