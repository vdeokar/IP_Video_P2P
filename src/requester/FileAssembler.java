package requester;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.PriorityBlockingQueue;

import commonLibrary.Chunk;
import commonLibrary.Video;

public class FileAssembler extends Thread{

	private boolean done[];
	private Video video;
	private int nextChunkToBeAssembled;
	private PriorityBlockingQueue<Chunk> chunkQueue;
	private String outputFileName;
	private Requester req;
	private VideoHandler videoHandler;
	private boolean videoPlayerHasBeenStarted;
	
	FileAssembler(Video video, Requester req){
		this.video = video;
		this.chunkQueue = new PriorityBlockingQueue<Chunk>();
		done = new boolean[video.getNumChunks()];
		this.nextChunkToBeAssembled = 0;
		outputFileName = System.getProperty("user.home")+File.separatorChar+"Videos"+File.separatorChar+video.hashCode()+"-"+System.currentTimeMillis()+".mp4";
		this.req = req;	
		videoHandler = new VideoHandler(this);
		videoPlayerHasBeenStarted = false;
	}	
	
/*	private boolean chunkQueueHasNewElements(){
		return this.chunkQueueHasNewElements;
	}*/
	
	public synchronized void run(){
		while(this.nextChunkToBeAssembled < this.video.getNumChunks()){
			try{
				while(chunkQueue.isEmpty())
					wait();
				while(chunkQueue.peek().getChunkID() != this.nextChunkToBeAssembled)
					wait();
				assemble(chunkQueue.poll());
				if(assemblyComplete()){
					System.out.println("Exit or whatever: FileAssembler.run()...");
					videoHandler.start();
				}
				if(videoPlayerHasBeenStarted==false){
					if(this.nextChunkToBeAssembled >= 2000){
						videoPlayerHasBeenStarted = true;
						//videoHandler.start();
					}
				}
			}catch(InterruptedException ie){ie.printStackTrace();}
		}
	}
	
	private synchronized void assemble(Chunk toBeAppended){
		try {
			if(done[toBeAppended.getChunkID()] == true)
				throw new Exception("Chunk already written in file");
			//RandomAccessFile raf = new RandomAccessFile(outputFileName, "rw");
			//raf.seek(toBeAppended.getChunkID()*256);
			//raf.write(toBeAppended.returnBytes());
			//raf.close();
			
			FileOutputStream fos = new FileOutputStream(outputFileName, true);
			fos.write(toBeAppended.returnBytes());
			fos.close();
			
			
			done[toBeAppended.getChunkID()] = true;
			if(chunkQueue.size()==40000)
				resumeAwaitingChunkFetchers();
			videoHandler.bufferringMayHaveCompleted();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean assemblyComplete(){
		if(++nextChunkToBeAssembled>=video.getNumChunks())
			return true;
		return false;
	}
	
	public synchronized void offerChunk(Chunk newChunk){
		this.chunkQueue.offer(newChunk);
		notify();
	}
	
	public boolean chunkQueueIsOverflowing(){
		if(chunkQueue == null)
			return false;
		return this.chunkQueue.size()>50000 ? true:false;
	}
	
	private void resumeAwaitingChunkFetchers(){
		req.resumeAnyWaitingChunkFetcher();
	}
	
	public int getNextChunkToBeAssembled(){
		return nextChunkToBeAssembled;
	}
	
	public int getTotalNumChunks(){
		return video.getNumChunks();
	}
	
	public String getPathOfFile(){
		return this.outputFileName;
	}
	
}
