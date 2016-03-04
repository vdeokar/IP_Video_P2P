package commonLibrary;

import java.util.Arrays;

public class Chunk implements Comparable<Chunk>{
	int chunkID;
	String byteString;
	
	public Chunk(byte[] b){
		this.byteString = Arrays.toString(b);
	}
	Chunk(String byteString){
		this.byteString = byteString;
	}
	public Chunk(int chunkID, String byteString){
		this.chunkID = chunkID;
		this.byteString = byteString;
	}
	
	public int getChunkID(){
		return this.chunkID;
	}
	
	public String toString(){
		return (byteString);//.replace(oldChar, newChar);
	}
	
	public byte[] returnBytes(){
		try{
			String[] stringByte = byteString.replace("[", "").replace("]", "").split(", ");
			byte[] byteArr= new byte[stringByte.length];
			for(int index=0;index < byteArr.length; index++)
				byteArr[index] = Byte.parseByte(stringByte[index]);
			return byteArr;
		}
		catch(NumberFormatException nfe){
			System.err.print(Thread.currentThread().getName()+" has encountered a NumberFormatException in Chunk.returnBytes:");
			System.err.println("It tried to parse "+this.byteString);
		}
		return null;
	}
	@Override
	public int compareTo(Chunk o) {
		return this.chunkID - o.chunkID;
	}
	public static void main(String[] args) {
		Chunk c = new Chunk(new byte[]{75, 78, -111, -59, -19, 33, 68, -11, 81, 48, -93, -48, -14, -74, 76, -114, -123, 29, -34, 61, 99, 70, 105, -50, 49, 8, 0, 3, 81, -104, 0, 15, 47, 8, 48, -16, 120, -59, -30, 67, 9, -122, -52, 124, 20, 11, 6, -126, -32, -26, 70, -70, 20, -101, 48, 71, 40, 113, 127, 33, -78, -108, -92, 43, -68, 87, 15, 1, -71, -32, -49, 72, 43, 41, 71, -97, 51, 116, -3, 106, -27, -84, -4, -54, 75, 93, -87, 42, -122, 38, 83, -83, -49, -61, -84, 0, 56, -48, -28, 10, 95, -4, -87, 117, 69, -55, -55, 102, -1, -81, 19, 112, 110, 126, -123, -37, 55, -48, -125, -69, 5, -96, 108, -46, 119, 72, 55, 45, 83, 71, 31, 29, 119, 126, -19, 101, -9, -40, -51, -104, 67, -75, 41, -87, -79, 19, 15, -113, -101, -65, 59, -61, 98, 38, 121, -73, -50, -6, -3, -97, 39, 109, -79, -97, -73, 127, -116, -3, 2, -95, -105, 12, -118, 41, -90, 24, -119, -30, 12, -103, 34, -29, 60, -101, 65, 3, 0, 116, -24, -63, -58, 80, 71, -98, 1, -101, -84, 126, 7, 24, 65, 2, -54, -64, 68, 69, -124, -75, 81, -32, 42, 50, -50, 92, 117, -3, 24, 105, -103, 77, 85, -128, 31, -64, 38, 88, 32, 72, 42, 17, -106, -62, -64, -76, -128, 106, -44, 72, -29, 40, 64, -120, 67, 49, -61, 44, -112, -26, -53, 86, -118, -33, 18, 69, -36, 98});
		System.out.println(c.byteString.getClass());
		System.out.println(Arrays.toString(c.returnBytes()));
	}
}
