package com.example.jarviss;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * Created by 肖 on 2018/4/28.
 */

public class P2P {
/*
    //输入scanner
    private static Scanner scanner = new Scanner(System.in);
    //是否等待输入
    private static boolean isWaitInput = true;
    //首次与外网主机通信的连接
    private static Socket socket;
    //首次与外网主机通信的本地端口
    private static int localPort;
    //UDP监听端口
    private static int MYUDPListenPort = 30001;

    private static PrintWriter pw;
    private static BufferedReader br;

    //填写另一台Client的ip与port
    private static String targetIP = "171.43.167.118";
    private static int targetport = 30001;

    private static String msg;

    static boolean LoopCoutinue = true;
    static boolean PacReceived = false;
    static boolean ServerAccept = false;
    static boolean ThreadFlag = false;

    private static DatagramSocket client;

    public static void ClientA(String input) {
        msg = input;
        try {

            // 新建一个socket通道
            socket = new Socket();
            // 设置reuseAddress为true
            socket.setReuseAddress(true);

            String ip = "63.209.35.169";
            int port = 30000;

            //Log.d("P2P", ip + " is " + InetAddress.getByName(ip).isReachable(5000));

            socket.connect(new InetSocketAddress(ip, port));

            //首次与外网服务器通信的端口
            //这就意味着我们内网服务要与其他内网主机通信，就可以利用这个通道
            localPort = socket.getLocalPort();


            System.out.println("本地TCP端口：" + localPort);

            System.out.println("请输入命令 notwait等待穿透，或者输入conn进行穿透");

            pw = new PrintWriter(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            try {
                while (true && LoopCoutinue) {
                    if (process()) {
                        break;
                    }
                }
            } finally {
                // 关闭资源
                try {
                    if (pw != null) {
                        pw.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                    if (socket != null) {
                        socket.close();
                        System.out.println("Server socket close");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean process() throws IOException {

        String in = null;

        if (isWaitInput) {
            //把输入的命令发往服务端
            //in = scanner.next();
            in = "conn";

            pw.write(in + "_" + MYUDPListenPort + "\n");

            //调用flush()方法将缓冲输出
            pw.flush();

            if ("notwait".equals(in)) {
                isWaitInput = false;
            }
        }


        if(!ThreadFlag){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10 && !ServerAccept; i++) {
                            //让服务器保存本地的UDP端口
                            SocketAddress target = new InetSocketAddress("63.209.35.169", 30001);

                            String message = "Server_Hello_" + i;
                            byte[] sendbuf = message.getBytes();
                            DatagramPacket pack = new DatagramPacket(sendbuf, sendbuf.length, target);
                            client.send(pack);
                            System.out.println("Server_Hello Send Succ   " + i);
                            sleep(500);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    client.close();
                }
            }).start();
            new Thread(new Runnable(){
                @Override
                public void run(){
                    try{
                        byte[] buf = new byte[1024];
                        DatagramPacket recpack = new DatagramPacket(buf, buf.length);
                        client.receive(recpack);
                        String receiveMessage = new String(recpack.getData(), 0, recpack.getLength());
                        System.out.println(receiveMessage);
                        if(receiveMessage.startsWith("Client_Accept")){
                            ServerAccept = true;
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
            ThreadFlag = true;
        }



        //获取服务器的响应信息
        String info = br.readLine();
        if (info != null) {
            System.out.println("Server：" + info);
        }


        //处理本地命令
        processLocalCommand(in);

        //处理服务器命令
        processRemoteCommand(info);

        return "exit".equals(in);
    }

    private static void processLocalCommand(String in) throws IOException {
        if ("conn".equals(in)) {
            System.out.println("请输入要连接的目标外网ip:");
            //String ip = scanner.next();
            String ip = targetIP;
            System.out.println("请输入要连接的目标外网UDP端口:");
            //int port = scanner.nextInt();
            int port = targetport;

            pw.write("newConn_" + ip + "_" + port + "_" + MYUDPListenPort + "\n");
            pw.flush();

            doPenetration(ip, port);

            isWaitInput = false;
        }
    }

    private static void processRemoteCommand(String info) throws IOException {
        if (info != null && info.startsWith("autoConn_")) {

            System.out.println("AutoConnect target");

            String[] infos = info.split("_");
            //目标外网地址
            String ip = infos[1];
            //目标外网端口
            String targetUDPPort = infos[2];

            doPenetration(ip, Integer.parseInt(targetUDPPort));
        }
    }

    private static void doPenetration(String targetIP, int targetUDPPort) throws IOException {

        SocketAddress target = new InetSocketAddress(targetIP, targetUDPPort);
        final DatagramSocket client = new DatagramSocket(MYUDPListenPort);
        String message = "";

        int baglost = 0;
        for (; baglost < 5 && LoopCoutinue; ) {
            try {
                //message = "Client_Hello";
                message = msg;
                byte[] sendbuf = message.getBytes();
                final DatagramPacket pack = new DatagramPacket(sendbuf, sendbuf.length, target);

                //client.send(pack);
                //System.out.println("Send pack!");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            while(!PacReceived) {
                                client.send(pack);
                                System.out.println("Send pack!");
                                sleep(500);
                                Log.d("P2P", String.valueOf(PacReceived));
                            }
                        }catch (Exception e) {
                                e.printStackTrace();
                        }
                    }
                }).start();

                receive(client);
                PacReceived = true;
                Log.d("P2P", "PacReceived");

            } catch (Exception e) {
                baglost++;
                if (baglost >= 4)
                    e.printStackTrace();
            }
        }
    }

    //收到UDPClientA的回复内容，穿透已完成
    private static void receive(DatagramSocket client) {
        for (; ; ) {
            try {
                //将接收到的内容打印
                byte[] buf = new byte[1024];
                DatagramPacket recpack = new DatagramPacket(buf, buf.length);
                client.receive(recpack);
                client.setSoTimeout(3000);
                Log.d("P2P", "Received");

                String receiveMessage = new String(recpack.getData(), 0, recpack.getLength());
                System.out.println(receiveMessage);

                Message message = new Message();
                message.what = DeviceInfo.UPDATE_TEXT;
                Bundle bundle = new Bundle();
                bundle.putString("text", receiveMessage);
                message.setData(bundle);
                DeviceInfo.handler.sendMessage(message);

                //记得重新收地址与端口，然后在以新地址发送内容到UPDClientA,就这样互发就可以了。
                int port = recpack.getPort();
                InetAddress address = recpack.getAddress();
                //String reportMessage = "I can hear you";
                String reportMessage = msg;

                //发送消息
                sendMessage(reportMessage, port, address, client);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void sendMessage(String reportMessage, int port, InetAddress address, DatagramSocket client) {
        try {
            byte[] sendBuf = reportMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, address, port);
            client.send(sendPacket);
            System.out.println("send success");

            //Close Thread
            LoopCoutinue = false;
            Shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void Shutdown() {
        try {
            if (pw != null) {
                pw.close();
            }
            if (br != null) {
                br.close();
            }
            if (socket != null) {
                socket.close();
                System.out.println("Server socket close");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
