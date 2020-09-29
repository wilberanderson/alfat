package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Represents a "file" inside a Jar File. Used for accessing resources (models, textures), as they
 * are all inside a jar file when exported.
 * 
 * @author Karl
 *
 */
public class MyFile implements AutoCloseable {

	private static final String FILE_SEPARATOR = "/";

	private String path;
	private String name;
	private Class cls;
	private ClassLoader classLoader;

	public MyFile(String path) {
		this.path = path;
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
		try{
			cls = Class.forName("utils.MyFile");
			classLoader = cls.getClassLoader();
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Couldn't get class loader for " + "utils.MyFile");
		}
	}

	public MyFile(String... paths) {
		this.path = "";
		for (String part : paths) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public MyFile(MyFile file, String subFile) {
		this.path = file.path + FILE_SEPARATOR + subFile;
		this.name = subFile;
	}

	public MyFile(MyFile file, String... subFiles) {
		this.path = file.path;
		for (String part : subFiles) {
			this.path += (FILE_SEPARATOR + part);
		}
		String[] dirs = path.split(FILE_SEPARATOR);
		this.name = dirs[dirs.length - 1];
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return getPath();
	}

//	public InputStream getInputStream() {
//		return Class.class.getResourceAsStream(path);
//	}

	public InputStream getInputStream() {
		InputStream is = null;
		try {
			is = MyFile.class.getResourceAsStream(path);//classLoader.getResourceAsStream(path);//new FileInputStream(path);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return is;
	}

	public InputStream getResourceInputStream() {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return is;
	}

	public BufferedReader getReader(){
		try {
			InputStreamReader isr = new InputStreamReader(getInputStream());
			return new BufferedReader(isr);
		} catch (Exception e) {
			System.err.println("Couldn't get reader for " + path);
			throw e;
		}
	}

	public BufferedReader getResourceReader(){
		try {
			InputStreamReader isr = new InputStreamReader(getInputStream());
			return new BufferedReader(isr);
		} catch (Exception e) {
			System.err.println("Couldn't get reader for " + path);
			throw e;
		}
	}

	public String getName() {
		return name;
	}

	@Override
	public void close(){

	}
}
