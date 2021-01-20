package vn.game.servers.socket;

import java.nio.ByteBuffer;

public class ParseInputData {
	byte buf[] = new byte[100000];
	int length =  0;
	public void put(byte[] data)
	{
		for(int i = 0; i < data.length; i++)
		{
			buf[length] = data[i];
			length++;
		}
	}
	public int validSize()
	{
		if(length < 5)
			return -1;
		ByteBuffer buffer = ByteBuffer.wrap(buf,1, 4);
		int validSize = buffer.getInt();
		if(validSize > length || validSize < 0){
			return -1;
		}
		return validSize + 5;
	}
	public byte[] getPacket(int size)
	{
		byte[] data = new byte[size];
		for(int i = 0; i < size; i++)
		{
			data[i] = buf[i];
		}
		
		int newSize = length - size;
		for(int i = 0; i < newSize; i++)
		{
			buf[i] = buf[i+size];
		}
		length = newSize;
		return data;
	}
}
