package serverSideNew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import commonLibrary.Video;

public class VideoList {

	private int count;
	private HashMap<Integer, Video> videoList;
	VideoList(){
		videoList = new HashMap<Integer, Video>();
		count = 0;
	}
	
	ArrayList<Integer> getClientsWhoHaveVideo(int videoID){
		return videoList.get(videoID).getClientIDList();
	}
	
	void addAndAttachClient(Video v, int clientID){
		if(videoList.containsValue(v)){
			for (Entry<Integer, Video> entry : videoList.entrySet()) // horrible efficiency
	            if (entry.getValue().equals(v))
	                entry.getValue().addClient(clientID);
		}
		else{
			Video newVideo = new Video(++count, v.getVideoTitle(), v.getNumChunks(), clientID);
			videoList.put(newVideo.getVideoID(), newVideo);
		}
	}
	
	void addAllAndAttachClient(String unformattedList, int ownerID){
		String[] unformattedArray = unformattedList.split(";");
		for(int i=0; i<unformattedArray.length;i++)
			addAndAttachClient(new Video(unformattedArray[i]), ownerID);
	}
	
	String get(){
		return videoList.values().toString().replace("[","").replace("]", "").replace(", ", ";");
	}
	
	String getComplete(){
		StringBuffer ret = new StringBuffer();
		for(Entry<Integer, Video> entry : videoList.entrySet())
			ret.append(entry.getValue().completeToString());
		return ret.toString();
	}
	
	public String toString(){
		return this.getComplete();
	}
	
	public static void main(String[] args) {
		VideoList v = new VideoList();
//		System.out.println(v.addAndReturnID(new Video("men in black:5")));
//		System.out.println(v.get());
//		System.out.println(v.addAndReturnID(new Video("avatar:7")));
//		System.out.println("should return some value:"+v.addAndReturnInteger(new Video("men in black:5")));
//		System.out.println(v.get());
		v.addAllAndAttachClient("men in black:4;avatar:6;men in black2:3", 5);
		v.addAllAndAttachClient("men in black:2;avatar:6;men in black2:3", 4);
		System.out.println(v.getComplete());
		System.out.println(v.getClientsWhoHaveVideo(3));
	}
}
