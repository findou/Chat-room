import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(9999);//创建服务端socket对象
        ArrayList<Socket> sockets = new ArrayList<Socket>();

        //服务端持续监测链接到服务端的套接字，并新建一个线程处理这个套接字
        while (true) {
            Socket ssc = ss.accept();
            if (!sockets.contains(ssc)) {
                System.out.println("新的成员加入了聊条室");
            }
            //监听到了一个客户端连接到了服务器就把它加入到集合中，
            sockets.add(ssc);
            new Thread(() -> {//使用lambda表达式
                try {
                    //写一个方法，将服务端读取到客户端发来的消息，发送给其他客户端
                    serverSendMsg(sockets, ssc);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void serverSendMsg(ArrayList<Socket> sockets, Socket ssc) throws IOException {
        //在这个套接字中读取客户端发来的消息(包括客户端的名字与发送的消息),并将客户端发来的消息发送给链接到的每一个客户端套接字
        BufferedReader br = new BufferedReader(new InputStreamReader(ssc.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {

            System.out.println(line);
            //从集合中读取链接到服务器的套接字
            for (Socket s : sockets) {
                if (s != ssc) {//判断是不是当前的客户端套接字，如果是，服务端就不用发给这个客户端套接字了
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    bw.write(line);
                    bw.newLine();
                    bw.flush();
                }
            }
            if("886".equals(line.split(":")[1])){
                sockets.remove(ssc);
                break;
            }
        }
    }
}
