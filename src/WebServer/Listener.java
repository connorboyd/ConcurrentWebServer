package WebServer;

import java.io.*;
import java.net.Socket;


public class Listener implements Runnable {

    private Socket mySocket;
    private InputStream myIS;
    private OutputStream myOS;

    public Listener(Socket reqSocket) {
        this.mySocket = reqSocket;
        try {
            this.myIS = this.mySocket.getInputStream();
            this.myOS = this.mySocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void run() {
        try {
            String filePath = getFilePath();
            String response = FileCache.getFile(filePath);
            // TODO HTTP response code and headers
            // HTTP/1.1 200 OK
            String headers = "HTTP/1.1 ";
            String statusCode = (response != null) ? "200 OK" : "404 Not Found";

            myOS.write(headers.getBytes());
            myOS.write(statusCode.getBytes());
            myOS.write('\n');
            myOS.write('\n');
            if (response != null) {
                myOS.write(response.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                mySocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FrontEnd.threadEnd();
        }
    }


    private String getFilePath() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(myIS));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if(line.contains("GET")) {
                    String filePath = System.getProperty("user.dir") + line.split(" ")[1];
                    return filePath;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "blah.txt";
    }
}
