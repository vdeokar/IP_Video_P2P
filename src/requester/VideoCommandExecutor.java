package requester;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
public class VideoCommandExecutor {
	public static void main(String[] args) {
		VideoCommandExecutor obj = new VideoCommandExecutor();
		//in mac oxs
		String command = "vlc /home/servo/Videos/2101973421-1448825095508.mp4 ";
		//in windows
		//String command = "ping -n 3 " + domainName;
		String videoName = "2101973421-1448825095508.mp4";
		String output = obj.playVideo(videoName);
		System.out.println(output);
	}
	public String playVideo(String videoName) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec("vlc "+videoName+" --fullscreen");
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
}