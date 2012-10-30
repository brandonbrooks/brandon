package brandon.gui;

import java.util.Hashtable;
import java.util.zip.ZipFile;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.zip.ZipInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class JarExtractor {
	private Hashtable sizes=new Hashtable(); 
	private Hashtable jarContents=new Hashtable(); 
	private String jarFileName; 
	public JarExtractor(String jarFileName) { 
		this.jarFileName=jarFileName; 
		initialize(); 
	}
	
	public byte[] getImageData(String name) { 
		return (byte[]) jarContents.get(name); 
	}
	
	private void initialize() { 
		ZipFile zipfile = null; 
		try { 
			// let's get the sizes. 
			zipfile = new ZipFile(jarFileName); 
			Enumeration e = zipfile.entries(); 
			while (e.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) e.nextElement(); 
				sizes.put(ze.getName(), new Integer((int)ze.getSize())); 
			} 
			zipfile.close();
			
			zipfile=null;
			
			// put resources into jarContents 
			FileInputStream fis = new FileInputStream(jarFileName); 
			BufferedInputStream bis = new BufferedInputStream(fis); 
			ZipInputStream zis = new ZipInputStream(bis); 
			ZipEntry entry = null; 
			while ((entry = zis.getNextEntry())!= null) {
				if (entry.isDirectory()) continue;
				int size = (int) entry.getSize(); 
				if (size == -1) {
					size = ((Integer) sizes.get(entry.getName())).intValue(); 
				}
				
				byte[] b = new byte[(int)size]; 
				int rb=0; 
				int chunk=0;
				while (((int)size - rb) > 0) { 
					chunk=zis.read(b,rb,(int)size - rb); 
					if (chunk==-1) break;
					rb+=chunk; 
				}
				zis.close(); 
				
				jarContents.put(entry.getName(),b);
			} 
		} catch (NullPointerException e){/*handle exception*/} 
		catch (FileNotFoundException e){/*handle exception*/} 
		catch (IOException e){/*handle exception*/} 
	} 
}
