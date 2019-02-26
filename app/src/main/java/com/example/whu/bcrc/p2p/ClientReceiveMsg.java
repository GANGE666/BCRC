package com.example.whu.bcrc.p2p;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientReceiveMsg extends Thread {
	private DatagramSocket client;
	
	ClientReceiveMsg(DatagramSocket client){
		this.client = client;
	}
	
	public void run() {
		System.out.println("ClientReceiveMsg Begin");
		while(true)
			try{
				//��������
				byte[] buf = new byte[1024];
				DatagramPacket recpack = new DatagramPacket(buf, buf.length);
				client.receive(recpack);
				String receiveMessage = new String(recpack.getData(), 0, recpack.getLength());
    		
				//System.out.println("From : \t" + recpack.getAddress() + "Receive : \t" + receiveMessage);
				
				//����ظ�����
				if(! receiveMessage.contains("_" + Integer.toString(ClientMain.RECEIVE_CONFIRM_CONNECT) + "_")){
    			
					//ȥ��
//					if(ClientMain.GetLastMsg() != null && ClientMain.GetLastMsg().endsWith(receiveMessage))
//						continue;
					    		
					//ServerMain.RepeatQueue.offer(recpack.getSocketAddress());
					ClientMain.InsertMsg(recpack.getAddress() + "_" + recpack.getPort() + "_" + receiveMessage);
				}
    		
    	
			}catch (IOException e) {
				e.printStackTrace();
			}
	
	}
}
