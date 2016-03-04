package clientSide;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class URLReader {
	
	public String getConnectionString(){
		String connectionString = read("http://www4.ncsu.edu/~vpkatara/server.txt");
		try{
		if(connectionString.contains(":")==false)
			throw new Exception("Connection String has error");
		}catch(Exception e){e.printStackTrace();return "Error in connection string read from http://www4.ncsu.edu/~vpkatara/server.txt";}
		return connectionString;
	}
	
	private String read(String url){
		String ret = "";
		try{
		URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));
        System.out.println("\n\nReading connection String from "+url);
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            ret = inputLine;
        in.close();
        return ret;
		}
		catch(Exception e){
			e.printStackTrace();
			return "Error";
		}
	}
	
	public static void main(String[] args) {
		
	}
}
