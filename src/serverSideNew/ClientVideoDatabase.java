package serverSideNew;


public class ClientVideoDatabase {

	ClientList clientList;
	VideoList videoList;
	
	ClientVideoDatabase(){
		clientList = new ClientList();
		videoList = new VideoList();
	}
	
	synchronized void storeAllVideosOfClient(Client c, String unformattedList){
		int clientID = clientList.addAndReturnID(c);
		videoList.addAllAndAttachClient(unformattedList, clientID);
	}
	
	String returnAllVideos(){
		return videoList.get();
	}
	
	String fetchListOfClientsHavingVideo(int videoID){
		StringBuffer strBuff = new StringBuffer();
		for(Integer clientID:videoList.getClientsWhoHaveVideo(videoID))
				strBuff.append(";"+clientList.getClient(clientID));
		strBuff.delete(0, 1);
		return strBuff.toString();
	}
	
	public static void main(String[] args) {
		ClientVideoDatabase clv = new ClientVideoDatabase();
		clv.storeAllVideosOfClient(new Client("192.168.1.1:4000"), "men in black:4;avatar:6;men in black2:3");
		clv.storeAllVideosOfClient(new Client("192.168.2.1:4000"), "men in black2:9;avatar:6;men in black:4");
		System.out.println(clv.returnAllVideos());
		System.out.println(clv.fetchListOfClientsHavingVideo(1));
		//System.out.println(InetAddress.getLocalHost().getHostAddress());
	}
	
}
