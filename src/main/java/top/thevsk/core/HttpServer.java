package top.thevsk.core;

import top.thevsk.entity.BotRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {

    private int port;

    private boolean isRunning;

    private Bot bot;

    HttpServer() {
    }

    void setPort(int port) {
        this.port = port;
    }

    void setBot(Bot bot) {
        this.bot = bot;
    }

    void start() {
        isRunning = true;
        new Thread(this).start();
    }

    void close() {
        isRunning = false;
    }

    @Override
    public void run() {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(this.port);
            while (isRunning) {
                socket = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                onHttp(bufferedReader);
                echo(printWriter);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void echo(PrintWriter printWriter) {
        String body = "{}";
        String echo = "HTTP/1.1 204 OK\r\n";
        echo += "Content-Type: application/json; charset=utf-8\r\n";
        echo += "Content-Length: " + body.length() + "\r\n";
        echo += "\r\n";
        echo += body;
        printWriter.write(echo);
        printWriter.flush();
        printWriter.close();
    }

    private void onHttp(BufferedReader bufferedReader) throws IOException {
        try {
            String[] line = bufferedReader.readLine().split(" ");
            BotRequest.Headers headers = new BotRequest.Headers(bufferedReader);
            if (headers.getContentLength() <= 0) {
                bot.onHttp(new BotRequest(line[0], line[2], line[1], headers, null));
                return;
            }
            char[] chars = new char[headers.getContentLength()];
            int read = bufferedReader.read(chars, 0, headers.getContentLength());
            if (read != headers.getContentLength()) {
                return;
            }
            bot.onHttp(new BotRequest(line[0], line[2], line[1], headers, new String(chars)));
        } finally {
            bufferedReader.close();
        }
    }
}
