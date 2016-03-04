package serverSideNew;

public class Client {
	
	static int count = 0;

	String IPAddress;
	int portNumber;
	int ID;
	
	Client(int ID, String IPAddress, int portNumber){
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;
		this.ID = ID;
	}
	
	Client(String unformatted){ // only comparison
		this.IPAddress = unformatted.split(":")[0];
		this.portNumber = Integer.parseInt(unformatted.split(":")[1]);
	}
	
	Client(String IPAddress, int portNumber){ // only comparison;
		this.IPAddress = IPAddress;
		this.portNumber = portNumber;
		this.ID = -1;
	}
	
	public String toString(){
		return this.IPAddress+":"+this.portNumber;
	}
	
	public String completeToString(){
		return this.ID+"@"+this.IPAddress+":"+this.portNumber;
	}
	
	public boolean equals(Object o) {
		try{
			if(o instanceof Client ==false)
				throw new Exception(o+" is not an instance of Client");
			else{
				Client other = (Client)o;
				return this.toString().contentEquals(other.toString());
			}
		}
		catch(Exception e){System.out.println("Exception found in Client Class");e.printStackTrace(); return false;}
				
	}
	
	public static void main(String[] args) {
		Client c1 = new Client(1, "sds", 21);
		Client c2 = new Client(2, "aca", 22);
		Client c11 = new Client(3, "sds", 21);
		System.out.println(c1.completeToString());
		System.out.println(c2.completeToString());
		System.out.println(c11.equals(c1));
	}
}
