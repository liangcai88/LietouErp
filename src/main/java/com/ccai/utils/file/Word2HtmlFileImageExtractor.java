package com.ccai.utils.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xwpf.converter.core.IImageExtractor;

public class Word2HtmlFileImageExtractor implements IImageExtractor {

	private final File baseDir;
	
	private String userPath;

	public Word2HtmlFileImageExtractor( File baseDir,String userPath)
	{
	    this.baseDir = baseDir;
	    this.userPath=userPath;
	}

	public void extract(String imagePath, byte[] imageData) throws IOException {
		int stn=imagePath.lastIndexOf("/"); 
		imagePath=imagePath.substring(0,stn)+"/"+userPath+imagePath.substring(stn+1,imagePath.length());
		File imageFile = new File(baseDir, imagePath);
		imageFile.getParentFile().mkdirs();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new ByteArrayInputStream(imageData);
			out = new FileOutputStream(imageFile);
			IOUtils.copy(in, out);
		} finally {
			if (in != null) {
				IOUtils.closeQuietly(in);
			}
			if (out != null) {
				IOUtils.closeQuietly(out);
			}
		}
	}

}