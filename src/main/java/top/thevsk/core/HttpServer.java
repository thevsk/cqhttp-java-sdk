package top.thevsk.core;

import top.thevsk.entity.BotConfig;
import top.thevsk.log.BotLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer implements Runnable {

    private int port;

    private BotConfig botConfig;

    private boolean isRunning;

    HttpServer() {
    }

    void setPort(int port) {
        this.port = port;
    }

    void setBotConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
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
                String[] top = bufferedReader.readLine().split(" ");
                if (top[0].toUpperCase().equals("POST")) {
                    int length = readHeadersGetLength(bufferedReader);
                    if (length > 0) {
                        char[] chars = new char[length];
                        int read = bufferedReader.read(chars, 0, length);
                        if (read == length) {
                            System.out.println(new String(chars));
                        }
                    }
                }
                echo(printWriter);
                bufferedReader.close();
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

    private int readHeadersGetLength(BufferedReader bufferedReader) {
        int length = 0;
        try {
            BotLog.debug("read request headers");
            String line;
            while ((line = bufferedReader.readLine()) != null && !line.equals("")) {
                BotLog.debug(line);
                if (line.toLowerCase().contains("content-length")) {
                    try {
                        length = Integer.valueOf(line.substring(line.indexOf(":") + 1).trim());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            BotLog.debug("read request headers over");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return length;
    }
}
