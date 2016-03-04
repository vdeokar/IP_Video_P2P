package serverSideNew;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.jcraft.jsch.*;

public class UploadToServer {
	
	public void upload(String ipAddr, int port){
		writeToFile(ipAddr+":"+port, "server.txt");
		upload("server.txt");
	}
 
	private void writeToFile(String content, String fileName){
		File f = new File(fileName);
		try{
				FileWriter fw = new FileWriter(f);
				fw.write(content);
				fw.flush();
			    fw.close();
		}
		catch(IOException ioe){ioe.printStackTrace();}
	}
	/*private void read(String url){
		try{
		URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        System.out.println("/n/n/nReading from "+url);
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
		}catch(Exception e){e.printStackTrace();}
	}*/
	
	private void upload(String fileName) {
        String SFTPHOST = "ftp.ncsu.edu";
        int SFTPPORT = 22;
        String SFTPUSER = "vpkatara";
        String SFTPPASS = "SuperKam@1";

        Session session = null;
        Channel channel = null;
        ChannelSftp channelSftp = null;
        System.out.println("preparing the host information for sftp.");
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
            session.setPassword(SFTPPASS);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            System.out.println("Host connected.");
            channel = session.openChannel("sftp");
            channel.connect();
            System.out.println("sftp channel opened and connected.");
            channelSftp = (ChannelSftp) channel;
            //channelSftp.cd(SFTPWORKINGDIR);
            channelSftp.cd("www");
            File f = new File(fileName);
            if(f.exists()==false)
            	throw new Exception("server.txt not found");
            else{
            	BufferedReader br = new BufferedReader(new FileReader(f));
            	System.out.println("server.txt contains "+br.readLine()+". Uploading..." );
            	br.close();
            }
            channelSftp.put(new FileInputStream(f), f.getName());
            System.out.println("File transfered successfully to host.");
        } catch (Exception ex) {
             System.out.println("Exception found while tranfer the response.:");
             ex.printStackTrace();
        }
        finally{
            channelSftp.exit();
            System.out.println("sftp Channel exited.");
            channel.disconnect();
            System.out.println("Channel disconnected.");
            session.disconnect();
            System.out.println("Host Session disconnected.");
        }
    }
	public static void main(String[] args) {
		//send("server.txt");
		//read("http://www4.ncsu.edu/~vpkatara/server.txt");
		//writeToFile("123","123.txt");
		//NetworkInterface.getNetworkInterfaces();
	}
 
}