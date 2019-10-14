import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Clicent {
    public static void main(String[] args) throws IOException {
        Socket cs = new Socket("127.0.0.1", 9999);//客户端套接字对象

        System.out.println("想加入聊天室吗？请输入你想的昵称：");
        String name = new Scanner(System.in).nextLine();


        //客户端开启两个线程，一个发送给服务端消息，一个接受服务端发来的消息
        new Thread(() -> {//一个接受服务端发来的消息
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(cs.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        //持续从控制台读入消息，然后把消息发送给服务端
        new Thread(new ClientSendMsgThread(cs, name)).start();
    }
}

class ClientSendMsgThread implements Runnable {
    private Socket cs;
    private String name;

    public ClientSendMsgThread(Socket cs, String name) {
        this.cs = cs;
        this.name = name;
    }

    @Override
    public void run() {//持续从控制台读入消息，然后把消息发送给服务端
        System.out.println("恭喜你加入聊天室，你可以更大家聊天了，退出聊天室输入 886");
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(cs.getOutputStream()));
            String line2;
            while ((line2 = br2.readLine()) != null) {
                String msg = name + ":" + line2;
                bw2.write(msg);
                bw2.newLine();
                bw2.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

