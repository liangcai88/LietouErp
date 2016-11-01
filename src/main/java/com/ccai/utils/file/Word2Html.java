package com.ccai.utils.file;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;

public class Word2Html {
	
	 @Value("${web.app.word.media}")  
	private String mediaRootPath;

	 
	 @Value("${web.app.word.path}")  
	private String mediaPath;
	 
	 
	public  void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}

	public  String convert2Html(File file,String contextpath,String userName)
			throws TransformerException, IOException, ParserConfigurationException, OfficeXmlFileException {
		if(userName==null) userName="system";
		String userPath=userName+"/"+System.currentTimeMillis()+"/";
		try {
			HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(file));// WordToHtmlUtils.loadDoc(new
																					// FileInputStream(inputFile));
			WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
					DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
			wordToHtmlConverter.setPicturesManager(new PicturesManager() {
				public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
						float widthInches, float heightInches) {
					return "/" + suggestedName;
				}
			});
			wordToHtmlConverter.processDocument(wordDocument);
			// save pictures
			List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
			if (pics != null) {
				for (int i = 0; i < pics.size(); i++) {
					Picture pic = pics.get(i);
					try {
						File pathFile=new File(mediaRootPath+ "/word/media/"+userPath);
						pathFile.mkdirs();
						pic.writeImageContent(new FileOutputStream(pathFile.getPath()+"/"+pic.suggestFullFileName()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
			Document htmlDocument = wordToHtmlConverter.getDocument();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DOMSource domSource = new DOMSource(htmlDocument);
			StreamResult streamResult = new StreamResult(out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty(OutputKeys.METHOD, "html");
			serializer.transform(domSource, streamResult);
			out.close();
			// writeFile(new String(out.toByteArray()), outPutFile);
			String html= new String(out.toByteArray());
			html=html.replaceAll("<img src=\"/", "<img src=\""+contextpath+getMediaPath()+"/word/media/"+userPath+"/");
			return html;
		} catch (OfficeXmlFileException e) {

			// 1) Load DOCX into XWPFDocument
			InputStream in = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(in);

			// 2) Prepare XHTML options (here we set the IURIResolver to
			// load images from a "word/media" folder)
			File imageFolderFile = new File(mediaRootPath);
			XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
			options.setExtractor(new Word2HtmlFileImageExtractor(imageFolderFile,userPath));
			options.setIgnoreStylesIfUnused(false);
			options.setFragment(true);

			// 3) Convert XWPFDocument to XHTML
			// OutputStream out = new FileOutputStream(new File(
			// "d:/test.htm"));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			XHTMLConverter.getInstance().convert(document, out, options);
			out.close();
			// writeFile(new String(out.toByteArray()), outPutFile);
			String html= new String(out.toByteArray()); 
			html=html.replaceAll(getMediaRootPath()+"/word/media/", contextpath+getMediaPath()+"/word/media/"+userPath);
			return html;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return "";
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public String getMediaRootPath() {
		return mediaRootPath;
	}

	public void setMediaRootPath(String mediaRootPath) {
		this.mediaRootPath = mediaRootPath;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}
	
	
	
	
}
