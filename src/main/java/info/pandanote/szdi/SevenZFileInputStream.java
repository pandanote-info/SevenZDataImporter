package info.pandanote.szdi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;

public class SevenZFileInputStream extends InputStream {
	SevenZFile sevenZFile = null;
	
	public SevenZFileInputStream(File file) {
		try {
			sevenZFile = new SevenZFile(file);
			sevenZFile.getNextEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int read() throws IOException {
		return sevenZFile.read();
	}

	public int read(byte[] b) throws IOException {
		return sevenZFile.read(b);
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		return sevenZFile.read(b,off,len);
	}
	
	public void close() throws IOException {
		sevenZFile.close();
	}
}
