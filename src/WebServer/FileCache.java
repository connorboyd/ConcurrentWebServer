package WebServer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class FileCache {

	private static ConcurrentHashMap<String, byte[]> fileHashMap = new ConcurrentHashMap<String, byte[]>();

	public static byte[] getFile(String filePath) {
		return fileHashMap.computeIfAbsent(filePath, path -> readFileToString(path) );
	}

	private static byte[] readFileToString(String filePath) {
		try {
			return Files.readAllBytes(Paths.get(filePath));
		} catch (IOException e) {
			return null;
		}
	}
}
