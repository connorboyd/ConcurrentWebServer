package WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
        ExecutorService executor = Executors.newCachedThreadPool();
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        //ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        //ExecutorService executor = Executors.newWorkStealingPool();
        while(true) {
            Socket newSocket;
            try {
                newSocket = myServerSocket.accept();
            } catch (IOException e) {
                System.err.println("Error accepting socket");
                continue;
            }

            executor.execute(new Listener(newSocket));
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
