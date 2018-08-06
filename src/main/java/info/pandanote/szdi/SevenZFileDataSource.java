package info.pandanote.szdi;

import static org.apache.solr.handler.dataimport.DataImportHandlerException.SEVERE;
import static org.apache.solr.handler.dataimport.DataImportHandlerException.wrapAndThrow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

import org.apache.solr.handler.dataimport.FileDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SevenZFileDataSource extends FileDataSource {
	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup
			().lookupClass());
	
	@Override
	public Reader getData(String query) {
	     File f = getFile(basePath,query);
	     try {
	       return openStream(f);
	     } catch (Exception e) {
	       wrapAndThrow(SEVERE,e,"Unable to open File : "+f.getAbsolutePath());
	       return null;
	     }
	 }

	static File getFile(String basePath, String query) {
	    try {
	      File file = new File(query);

	      // If it's not an absolute path, try relative from basePath. 
	      if (!file.isAbsolute()) {
	        // Resolve and correct basePath.
	        File basePathFile;
	        if (basePath == null) {
	          basePathFile = new File(".").getAbsoluteFile(); 
	          LOG.warn("FileDataSource.basePath is empty. " +
	              "Resolving to: " + basePathFile.getAbsolutePath());
	        } else {
	          basePathFile = new File(basePath);
	          if (!basePathFile.isAbsolute()) {
	            basePathFile = basePathFile.getAbsoluteFile();
	            LOG.warn("FileDataSource.basePath is not absolute. Resolving to: "
	                + basePathFile.getAbsolutePath());
	          }
	        }

	        file = new File(basePathFile, query).getAbsoluteFile();
	      }

	      if (file.isFile() && file.canRead()) {
	        LOG.debug("Accessing File: " + file.getAbsolutePath());
	        return file;
	      } else {
	        throw new FileNotFoundException("Could not find file: " + query + 
	            " (resolved to: " + file.getAbsolutePath());
	      }
	    } catch (FileNotFoundException e) {
	        throw new RuntimeException(e);
	      }
	}
	
	protected Reader openStream(File file) throws FileNotFoundException,
      UnsupportedEncodingException {
		InputStream is = null;
		if (file.getName().endsWith(".7z")) {
			is = new SevenZFileInputStream(file);
		} else {
			is = new FileInputStream(file);
		}
		if (encoding == null) {
			return new InputStreamReader(is, StandardCharsets.UTF_8);
		} else {
			return new InputStreamReader(is, encoding);
		}
	}
}
