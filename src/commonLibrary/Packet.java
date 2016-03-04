package commonLibrary;

public class Packet {
	private int option;
	private String data;
	
	public Packet(int option, String data){
		this.option = option;
		this.data = data;
	}
	
	public int getOption(){
		return this.option;
	}
	
	public String getData(){
		return this.data;
	}
	
	public Packet(String payload){
		String[] arr = payload.split("\\|");
		//System.out.println(Arrays.toString(arr));
		this.option = Integer.parseInt(arr[2]);
		this.data = arr[6];
	}
	
	public String toString(){
		return option+":"+data;
	}
	
	public String getPayload(){
		return "|option|"+option+"|/option||data|"+data+"|/data|";
	}
	
	public static void main(String[] args) {
		Packet p = new Packet("|option|2|/option||data|1,Intro to Algos,8,mp3;2,Intro to networks,5,mp4;3,Intro to AI,6,mp4|data|");
		System.out.println(p);
		//String s = ":abc:xyz";
		//System.out.println(Arrays.toString(s.split(":")));
	}
	
}
