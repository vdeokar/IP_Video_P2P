package commonLibrary;

import java.util.ArrayList;

public class Video {
	
	String videoTitle;
	int videoID;
	int numChunks;
	String format;//not in use
	ArrayList<Integer> clientIDList;
	
	public Video(String videoTitle, int numChunks){//only comparison
		this.videoID = -1;
		this.videoTitle = videoTitle;
		this.numChunks = numChunks;
		this.format = "mp4";
	}
	
	public Video(int id, String videoTitle, int numChunks, int clientID){
		this.videoID = id;
		this.videoTitle = videoTitle;
		this.numChunks = numChunks;
		this.format = "mp4";
		clientIDList = new ArrayList<Integer>();
		clientIDList.add(clientID);
	}
	
	public Video(int id, String videoTitle, int numChunks){ // only for use with Client to display and derive menu
		this.videoID = id;
		this.videoTitle = videoTitle;
		this.numChunks = numChunks;
		this.format = "mp4";
	}
	
	public Video(Video v){
		this.videoID = -1;
		this.videoTitle = v.videoTitle;
		this.numChunks = v.numChunks;
	}
	
	public Video(String unformatted){ //only comparison
		try{
		String[] splitted = unformatted.split(":");
		this.videoTitle = splitted[0];
		this.numChunks = Integer.parseInt(splitted[1]);
		}
		catch(NumberFormatException nfe){System.err.println("Exception found in Video Class:");nfe.printStackTrace();}
	}
	
	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public int getVideoID() {
		return videoID;
	}

	public void setVideoID(int videoID) {
		this.videoID = videoID;
	}

	public int getNumChunks() {
		return numChunks;
	}

	public void setNumChunks(int numChunks) {
		this.numChunks = numChunks;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setClientIDList(ArrayList<Integer> clientIDList) {
		this.clientIDList = clientIDList;
	}

	public ArrayList<Integer> getClientIDList(){
		return this.clientIDList;
	}
	
	public String toString(){
		if(this.clientIDList != null)
			return this.videoID+","+this.videoTitle+","+this.numChunks+","+this.format+","+this.clientIDList.size();
		else
			return this.videoID+","+this.videoTitle+","+this.numChunks+","+this.format+",0";
	}
	
	public String completeToString(){
		return this.videoID+","+this.videoTitle+","+this.numChunks+","+this.format+"--"+this.clientIDList;
	}
	
	public void addClient(int newClientID){
		try{
			if(this.clientIDList!=null)
				this.clientIDList.add(newClientID);
			else{
				throw new Exception("Comparison object being used improperly");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean equals(Object o){
		try{
			if(o instanceof Video ==false)
				throw new Exception(o+" is not an instance of Video");
			else{
				Video other = (Video)o;
				return (this.videoTitle+","+this.numChunks).equals(other.videoTitle+","+other.numChunks);
			}
		}
		catch(Exception e){e.printStackTrace(); return false;}
	}
	
	public static void main(String[] args) {
		Video v = new Video(1, " sda", 8, 7);
		System.out.println(v.completeToString());
		v.addClient(5);
		System.out.println(v.completeToString());
		System.out.println(v);
	}
	
}
