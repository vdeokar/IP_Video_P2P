package serverSideNew;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkAddress {
	static String getIPAddress(String interfaceName){
		try{
		Enumeration<InetAddress> list = getInetAddresses(interfaceName);
		String ret;
		if(list==null){
			System.err.println("No interface found with interface name: "+interfaceName+". Returning Localhost IP Address "+(ret = InetAddress.getLocalHost().getHostAddress()));
			return ret;
		}
		while(list.hasMoreElements()){
			String add = list.nextElement().getHostAddress();
			//System.out.println(add);
			if(add.contains("."))
				return add;
		}
		}catch(Exception e){e.printStackTrace();}
		return "--";
	}
	static Enumeration<InetAddress> getInetAddresses(String interfaceName){
		try{
		Enumeration<NetworkInterface> enumNet =NetworkInterface.getNetworkInterfaces();
		NetworkInterface curr;
		while(enumNet.hasMoreElements()){
			curr=enumNet.nextElement();
			if(curr.getName().equalsIgnoreCase(interfaceName))//enp0s8
				return curr.getInetAddresses();
			//System.out.println(curr.getName()+" "+curr.getDisplayName());
		}
		}catch(Exception e){}
		return null;
	}
	public static void main(String[] args) {
		System.out.println(getIPAddress("enp0s8"));
	}
}
