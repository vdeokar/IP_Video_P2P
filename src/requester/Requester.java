package requester;
import java.util.ArrayList;
import java.util.concurrent.PriorityBlockingQueue;

import clientSide.Peer;
import commonLibrary.Chunk;
import commonLibrary.Video;

public class Requester {
	
	Video requestedVideo;
	ArrayList<Peer> listOfPeers;
	//PriorityBlockingQueue<Chunk> chunkQueue;
	ChunkFetcher[] chunkFetcherArr;
	FileAssembler fileAssembler;
	
	public Requester(Video requestedVideo, ArrayList<Peer> listOfPeers){
		this.requestedVideo = requestedVideo;
		this.listOfPeers = listOfPeers;
		fileAssembler = new FileAssembler(requestedVideo, this);
		fileAssembler.start();
		dispatch();
	}
	
	void dispatch(){
		chunkFetcherArr = new ChunkFetcher[Math.min(listOfPeers.size(),requestedVideo.getNumChunks())];
		for(int chunkFetcherNumber=0; chunkFetcherNumber<chunkFetcherArr.length; chunkFetcherNumber++){
			chunkFetcherArr[chunkFetcherNumber] = new ChunkFetcher(requestedVideo, chunkFetcherNumber, listOfPeers.size(), listOfPeers.get(chunkFetcherNumber),fileAssembler);
			chunkFetcherArr[chunkFetcherNumber].start();
		}
	}
	
	void resumeAnyWaitingChunkFetcher(){
		for(int chunkFetcherNumber=0; chunkFetcherNumber<listOfPeers.size(); chunkFetcherNumber++){
			chunkFetcherArr[chunkFetcherNumber].resumeIfWaiting();
		}
	}
	
	public static void main(String args[]){
		
	}
	
}
