package com.franco.client;

import com.franco.netty.message.RequestMessage;

import java.io.*;
import java.net.Socket;

/**
 * ClientTest
 *
 * @author franco
 */
public class ClientTest {

    public static void main(String[] args) {
        try(Socket socket = new Socket("127.0.0.1", 9000)) {
            OutputStream out = socket.getOutputStream();
            String content = "a=1&b=2";
            RequestMessage message = new RequestMessage("test@add", 1, content.getBytes());
//            socket.setSoTimeout(5000);
            // 写出
            System.out.println("-----————————————————————————————");
            out.write(message.toByte());
            InputStream in = socket.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            while((len = in.read(bytes)) != -1) {
                String str = new String(bytes, 0, len);
                System.out.println(str);
                if(str.contains("push")) {
                    out.write(message.toByte());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
