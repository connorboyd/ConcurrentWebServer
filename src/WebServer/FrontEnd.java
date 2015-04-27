package WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class FrontEnd {
	private static final int NUM_THREADS = 8;
    private static ServerSocket myServerSocket;
    private static AtomicInteger numActiveThreads = new AtomicInteger(0);

	public static void main(String args[]) {
        init();
        runLoop();
	}

    public static void threadEnd() {
        numActiveThreads.getAndDecrement();
    }

    private static void runLoop() {
        while(true) {
            while(numActiveThreads.get() >= NUM_THREADS) {
                Thread.yield(); // TODO: Measure performance with and without yielding
            }
            Socket newSocket;
            try {
                newSocket = myServerSocket.accept();
            } catch (IOException e) {
                System.err.println("Error accepting socket");
                continue;
            }

            new Thread(new Listener(newSocket)).start();
            numActiveThreads.incrementAndGet();
        }
    }

    private static void init() {
        initServerSocket();
    }

    private static void initServerSocket() {
        try {
            myServerSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
