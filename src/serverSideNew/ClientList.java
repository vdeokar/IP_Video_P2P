package serverSideNew;

import java.util.HashMap;

public class ClientList {

	private HashMap<Integer, Client> clientList;
	private int count = 0;
	ClientList(){
		clientList = new HashMap<Integer, Client>();
		count = 0;
	}
	
	int addAndReturnID(String IPAddress, int port){
		try{
			if(clientList.containsValue(new Client(IPAddress, port)))
				throw new Exception("Client with "+IPAddress+":"+port+" already exists!");
			else{
				Client newClient = new Client(++count, IPAddress, port);
				clientList.put(newClient.ID, newClient);
				return newClient.ID;
			}
		}catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	int addAndReturnID(Client c){
		try{
			if(clientList.containsValue(c))
				throw new Exception("Client with "+c.IPAddress+":"+c.portNumber+" already exists!");
			else{
				Client newClient = new Client(++count, c.IPAddress, c.portNumber);
				clientList.put(newClient.ID, newClient);
				return newClient.ID;
			}
		}catch(Exception e){e.printStackTrace(); return -1;}
	}
	
	String get(){
		return clientList.values().toString().replace("[","").replace("]", "").replace(", ", ";");
	}
	
	String getClient(int ID){
		return clientList.get(ID).toString();
	}
	
	public static void main(String[] args) {
		ClientList cl = new ClientList();
		System.out.println(cl.addAndReturnID("123", 21));
		cl.addAndReturnID("456", 21);
		//cl.add("123", 21);
		cl.addAndReturnID("123", 28);
		System.out.println(cl.get());
	}
}
