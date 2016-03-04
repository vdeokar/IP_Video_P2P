package requester;

import java.io.File;

public class VideoHandler extends Thread{
	private String filePath;
	//private VideoPlayer videoPlayer;
	private FileAssembler fileAssembler;
	private double durationInSecs;
	private int totalNumChunks;
	
	VideoHandler(FileAssembler fileAssembler){
		this.filePath = fileAssembler.getPathOfFile();
		this.fileAssembler = fileAssembler;
		if(fileAssembler!=null)
			this.totalNumChunks = fileAssembler.getTotalNumChunks();
		else
			this.totalNumChunks = -1;
	}
	
	synchronized void monitorBufferring(){
		try {
			while(true){	
				while(chunksAvailable() - chunksConsumed() <= 100){
						//videoPlayer.buffer();
						wait();
				}
				//videoPlayer.resume();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int chunksAvailable(){
		if(fileAssembler != null)
			return fileAssembler.getNextChunkToBeAssembled() - 1;
		else
			return Integer.MAX_VALUE;
	}
	
	private int chunksConsumed(){
		return (int)(( 1000 * totalNumChunks ) / durationInSecs);
	}
	
	public void run(){
		try{
			//videoPlayer = new VideoPlayer(this);
			System.out.println("Video about to begin");
			//VideoPlayer.main(new String[]{filePath});
			//new VideoCommandExecutor().playVideo(filePath);
			Runtime.getRuntime().exec("vlc "+filePath+" --fullscreen");
			//this.durationInSecs = videoPlayer.getDurationInSeconds();
			monitorBufferring();
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public String getOutputFilePath(){
		return this.filePath;
	}
	
	public synchronized void bufferringMayHaveCompleted(){
		notify();
	}
	
	public static void main(String[] args) {
		//String s = System.getProperty("user.home")+File.separatorChar+"Videos"+File.separatorChar;
		//new VideoHandler(s+"856419764-1448347491203.mp4", null).start();
		
	}
}
