package requester;

import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

import clientSide.Peer;
import commonLibrary.Chunk;
import commonLibrary.Packet;
import commonLibrary.SenderReceiver;
import commonLibrary.Video;

public class ChunkFetcher extends Thread{
	
	//Socket socketToRequestReceiver;
	Peer requestReceiverPeer;
	Video video;
	int startNum;
	int offset;
	//PriorityBlockingQueue<Chunk> chunkQueue;
	FileAssembler fileAssembler;
	
	ChunkFetcher(Video video, int startNum, int offset, Peer requestReceiverPeer, FileAssembler fileAssembler){
		System.out.println("Starting new ChunkFetcher thread for chunks "+startNum+"+ "+offset+"*k to fetch "+video+ " from "+requestReceiverPeer);
		this.video = video;
		this.startNum = startNum;
		this.offset = offset;
		this.requestReceiverPeer = requestReceiverPeer;
		//this.socketToRequestReceiver = new SenderReceiver().returnSocketTo(requestReceiver.getIpAddress(), requestReceiver.getPortNumber());
		//this.chunkQueue = chunkQueue;
		this.fileAssembler = fileAssembler;
	}
	
	public void run(){
		for(int chunkNumber=startNum; chunkNumber<video.getNumChunks(); chunkNumber+=offset){
			//new SenderReceiver().sendMesssageOn(socketToRequestReceiver, new Packet(100, video.getVideoTitle()+":"+chunkNumber).getPayload());
			//new SenderReceiver().sendDatagramAndGetUDPReplyOn(requestReceiverPeer, new Packet(100, video.getVideoTitle()+":"+chunkNumber).getPayload());
			Packet option101Packet = new Packet(new SenderReceiver().sendDatagramAndGetUDPReplyOn(requestReceiverPeer, new Packet(100, video.getVideoTitle()+":"+chunkNumber).getPayload()));
			if(option101Packet.getOption()!=101)
				try {
					throw new Exception("option101Packet has option "+option101Packet.getOption()+" . Something went wrong!: ChunkFetcher.run()");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			System.out.println("Received Chunk #"+chunkNumber+" of "+video.getVideoTitle()+" from "+requestReceiverPeer.getIpAddress()+":"+requestReceiverPeer.getPortNumber());
			fileAssembler.offerChunk(new Chunk(chunkNumber, option101Packet.getData().split(":")[2]));
			while(fileAssembler.chunkQueueIsOverflowing())
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
	
	public synchronized void resumeIfWaiting(){
		notify();
	}
	
}
