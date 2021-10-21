package com.alticast.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class PDFMaker {
	
	Document m_doc = null;
	PdfWriter m_writer = null;
	
	static PDFMaker instance = null;
	
	private Log log = new Log("PDFMaker");
	private String m_str = ""; 
	
	private Font m_font;
	
	public String m_status = "";
	
	private static final String FONTNAME="NanumGothic.ttf";
	private static final String TEMPFILE = "merge.pdf";
	
	// whiteyume patch
	private FileOutputStream tmpos=null;
	
	PDFMaker(){
	}
	
	public static PDFMaker getInstance(){
		if (instance == null){
			instance = new PDFMaker();
		}
		return instance;
	}
	
	public void init(File file) throws DocumentException, IOException{
		
		
		m_doc = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
		
		try {
			if(MainFrame.getInstance().b_pre_exist){
				m_str = "./temp.pdf";
			}else{
				m_str = file.toString();
			}
			log.println("ori: "+m_str);
			if (! m_str.endsWith(".pdf")){
				m_str = file.toString().concat(".pdf");
			}
			log.println("save as: "+m_str);
			
			// whiteyume patch
			tmpos = new FileOutputStream(m_str);
			m_writer = PdfWriter.getInstance(m_doc,tmpos);
						
		} catch (FileNotFoundException e) {
			m_status = "File Not Found Exception!";
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		m_doc.open();
		
		// for korean font
		URL url = FileUtils.class.getClassLoader().getResource(FONTNAME);
		String fontpath=null;
		
		if(url!=null) fontpath = url.toExternalForm();
		else{
			log.println("url null");
			m_doc.close();

			return;
		}
		
		if(fontpath!=null){
			BaseFont bf = BaseFont.createFont(fontpath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			m_font = new Font(bf, 12);
		}
		
		
	}
	
	public void makeTable() throws DocumentException, IOException{
		PdfPTable table = new PdfPTable(2);
		
		table.setTotalWidth(750f);
		table.setWidths(new int[]{200,550});
		table.setLockedWidth(true);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.getDefaultCell().setMinimumHeight(30);
		
		m_font.setSize(16);
		m_font.setStyle(Font.BOLD);
		PdfPCell cell = new PdfPCell(new Paragraph("  "+MainFrame.getInstance().m_title, m_font));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setFixedHeight(40);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		cell.setColspan(2);
		table.addCell(cell);
		
		m_font.setSize(14);
		m_font.setStyle(Font.NORMAL);
		table.addCell(new Paragraph(MainFrame.getInstance().m_mw_title, m_font));
		table.addCell(new Paragraph("\n"+makeMarginVertival(MainFrame.getInstance().m_mw)+"\n\n", m_font));
		table.addCell(new Paragraph(MainFrame.getInstance().m_glue_title, m_font));
		table.addCell(new Paragraph("\n"+makeMarginVertival(MainFrame.getInstance().m_glue)+"\n\n", m_font));

		m_doc.add(table);
	}
	
	public String makeMarginVertival(String str){
		
		String ret = "";
		for(int i=0; i<str.length(); i++){
			if(Character.toString(str.charAt(i)).equals("\n")){
				ret += "\n\n";
			}
			else{
				ret += Character.toString(str.charAt(i));
			}
		}
		
		return ret;
	}
	
	public static void removePages(PdfReader reader, int... pagesToRemove) {
	    int pagesTotal = reader.getNumberOfPages();
	    List<Integer> allPages = new ArrayList<>(pagesTotal);
	    for (int i = 1; i <= pagesTotal; i++) {
	        allPages.add(i);
	    }
	    for (int page : pagesToRemove) {
	        allPages.remove(new Integer(page));
	    }
	    reader.selectPages(allPages);
	}

	public boolean write(File file) throws DocumentException, IOException{
		log.println("file :" + file);
		if (file != null) this.init(file);
		
		if(m_font==null){
			m_status = "Cannot read font!";
			m_doc.close();
			return false;
		}
		
		if(MainFrame.getInstance().b_del_check){
			PdfReader del_reader = new PdfReader(MainFrame.getInstance().fileField.getText());

			int pagenumber = del_reader.getNumberOfPages();
			int prev_start_page = 1;
			
			for(int i=1; i<=pagenumber; i++){
				String line = PdfTextExtractor.getTextFromPage(del_reader,  i);
				
				if(i>1 && line.contains("OCAP")){
					prev_start_page = i;
					break;
				}
			}
			
			log.println("prev_start_page="+prev_start_page);
			
			for(int i=1; i<prev_start_page; i++){
				if(i>1 && i==prev_start_page) break;
				log.println("delete page="+i);
				removePages(del_reader, 1);
			}


			Document doc = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
			PdfCopy copy = new PdfCopy(doc, new FileOutputStream(TEMPFILE));
			
			doc.open();

			copy.addDocument(del_reader);
		
			doc.close();
			
			del_reader.close();
			
			Files.move(new File(TEMPFILE).toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			new File(TEMPFILE).delete();
			
			File t = new File(m_str);
			if(t.exists()){
				m_doc.add(new Paragraph(" ", m_font));
				m_doc.close();
				if(t.delete()) log.println("success to delete "+m_str);
				else log.println("something wrong to delete "+m_str);
			}
			
			m_status = "단결! 최신 릴리즈 노트 삭제에 성공하였습니다!";
			
			return true;
		}

		try {
			
			URL url = FileUtils.class.getClassLoader().getResource(FONTNAME);
			String fontpath=null;
			
			if(url!=null) fontpath = url.toExternalForm();
			else{
				log.println("url null");
				m_status = "폰트를 찾을 수 없습니다!";
				m_doc.close();
				return false;
			}
			
			Font l_font = null;
			
			if(fontpath!=null){
				BaseFont bf = BaseFont.createFont(fontpath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
				l_font = new Font(bf, 24);
				l_font.setColor(BaseColor.BLUE);
				l_font.setStyle(Font.BOLDITALIC);
			}
			
			Paragraph title = new Paragraph(MainFrame.getInstance().m_select_site + "  OCAP 미들웨어\n"+ MainFrame.getInstance().nameField.getText()+" Release Note\n\n", l_font);
			Chapter chapter = new Chapter(title, 1);
			chapter.setNumberDepth(0);
			
			m_doc.add(chapter);
			
			l_font.setSize(18);
			l_font.setColor(BaseColor.BLACK);
			
			m_doc.add(new Paragraph("- Revision\n\n", l_font));
			
			//m_doc.add(new Paragraph(str, m_font));
			makeTable();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		m_doc.close();
		
		
		// file merge

		if(MainFrame.getInstance().b_pre_exist){
			PdfReader reader1 = new PdfReader(MainFrame.getInstance().fileField.getText());
			PdfReader reader2 = new PdfReader(m_str);
			
			Document doc = new Document(PageSize.A4.rotate(), 50, 50, 50, 50);
			PdfCopy copy = new PdfCopy(doc, new FileOutputStream(TEMPFILE));
			
			doc.open();

			copy.addDocument(reader2);
			copy.addDocument(reader1);
		
			doc.close();
			
			reader1.close();
			reader2.close();
			
			Files.move(new File(TEMPFILE).toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			
			
		}
		
		m_status = "단결! 릴리즈 노트 생성에 성공하였습니다!";
		return true;
	}
	
	public boolean open(File file) throws IOException{
		PdfReader reader = new PdfReader(file.toString());
		int pagenumber = reader.getNumberOfPages();
		
		for(int i=1; i<=pagenumber; i++){
			//log.println("page number: "+i);
			String line = PdfTextExtractor.getTextFromPage(reader,  i);
			//System.out.println(line);
			MainFrame.getInstance().m_open_buf.append(line);
		}
		
		reader.close();
		
		return true;
	}
	
	// whiteyume patch
	public void closeTmpos() {
		try {
			if( m_writer != null) {
				m_writer.close();
				m_writer = null;
			}
			
			if(tmpos != null) {
				tmpos.flush();
				tmpos.close();
		    	tmpos=null;
			}
			
			System.gc();
			System.runFinalization();

			if ( MainFrame.getInstance().b_pre_exist ) {
				File file = new File(m_str);
				try {
					if (!file.exists()) {
						log.println("It doesn't exist in the first place.");
					} else if (file.isDirectory() && file.list().length > 0) {
						log.println("It's a directory and it's not empty.");
					} else {
						if (file.delete()) {
							log.println("success to delete temp files:" + m_str);
						} else {
							log.println("Somebody else has it open, we don't have write permissions, or somebody stole my disk.");
						}
					}
				} catch (SecurityException e) {
					log.println("We're sandboxed and don't have filesystem access.");
				}
			}
			
		} catch (Exception e) {
				e.printStackTrace();
		}
	    
	}
}
