package vn.com.landsoft.servers.socket;

import java.nio.ByteBuffer;
import java.util.Vector;

import allinone.data.AIOConstants;

public class BytesDataReader {
	public volatile int curPos = 0;
	public Vector<Byte> bytesData = new Vector<Byte>();
	
	public void putData(byte[] data){
		for(int i = 0; i < data.length; i++){
			bytesData.add(data[i]);
		}
	}
	
	public Byte[] getPacket(){
		Byte firstByte = bytesData.get(curPos);
		//read nobyte
		if(firstByte == null || firstByte != AIOConstants.FIRST_BYTE_GAME){
			return null;
		}
		//read 1 byte
		Byte[] lengthData = getByte(4);
		if(lengthData == null){
			curPos -= 1;
			return null;
		}
		
		int dataSize = convertByteArrayToInt(lengthData);
		Byte[] jsonData = getByte(dataSize);
		if(jsonData == null){
			curPos -= 5;
			return null;
		}
		reIndexData();
		return jsonData;
	}
	public void reIndexData(){
		int c = 0;
		while(c <= curPos){
			bytesData.remove(0);
		}
		curPos = 0;
	}
	public int convertByteArrayToInt(Byte[] value){
		byte[] v = new byte[4];
		for(int i = 0; i < 4; i++){
			v[i] = value[i];
		}
		ByteBuffer sizeBuffer = ByteBuffer.wrap(v);
		int messDataSize = sizeBuffer.getInt();
		return messDataSize;
	}
	
	public Byte readByte(){
		if(curPos == bytesData.size()){
			return null;
		}
		byte firstByte = bytesData.get(curPos);
		curPos++;
		return firstByte;
	}
	public Byte[] getByte(int length){
		int remainBytes = bytesData.size() - curPos;
		if(length > remainBytes){
			return null;
		}else{
			Byte[] readData = new Byte[length];
			int c = 0;
			for(int i = curPos; i < curPos + length; i++){
				readData[c++] = bytesData.get(i);
			}
			curPos += length;
			return readData;
		}
	}
	
}
