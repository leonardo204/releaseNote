package com.alticast.test;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.DocumentException;

public class MainFrame extends JFrame implements ActionListener, DropTargetListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Log log = new Log("MainFrame");
	
	private JLabel nameLabel = null;
	private JLabel MWVersionLabel = null;
	private JLabel GlueVersionLabel = null;
	private JLabel mwLabel = null;
	private JLabel glueLabel = null;
	private JLabel fileLabel = null;
	private JLabel siteLabel = null;
	private JLabel dateLabel = null;
	private JLabel checkDelLabel = null;
	
	public JTextField nameField = null;
	private JTextField MWVersionField = null;
	private JTextField GlueVersionField = null;
	public JTextField fileField = null;
	private JTextField dateField = null;
	
	private JCheckBox checkDelChkbox = null;
	private JComboBox siteCombo = null;
	
	private JTextPane mwTA = null;
	private JTextPane glueTA = null;
	
	private JButton fileButton = null;
	private JButton createButton = null;
	
	private JScrollPane mwScroll = null;
	private JScrollPane glueScroll = null;
	
	private JFileChooser fileButtonChooser = null;
	private File m_open_file = null;
	private File m_save_file = null;
	
	DropTarget dropTarget = null;
	DropTarget dropTarget2 = null;
	DropTarget dropTarget3 = null;
	DropTarget dropTarget4 = null;
	DropTarget dropTarget5 = null;
	DropTarget dropTarget6 = null;
	DropTarget dropTarget7 = null;
	DropTarget dropTarget8 = null;
	
	public String m_title = "";
	public String m_mw_title = "";
	public String m_glue_title = "";
	public String m_mw = "";
	public String m_glue = "";
	
	public StringBuffer m_open_buf = null;
	
	private boolean m_mwTABlank = false;
	private boolean m_glueTABlank = false;
	private boolean m_fileHistory = false;
	
	public static MainFrame m_instance = null;
	
	public boolean b_pre_exist = false;
	public boolean b_del_check = false;
	
	public String m_select_site = "TBroad";
	
	private static final int LABEL_X = 20;
	private static final int LABEL_Y = 20;
	private static final int LABEL_W = 100;
	private static final int LABEL_H = 20;
	
	private static final int FIELD_X = 120;
	private static final int FIELD_Y = 20;
	private static final int FIELD_W = 100;
	private static final int FIELD_H = 20;
	
	private static final String[] siteList = {"TBroad", "CJ", "LGHV", "JEJU", "CCS", "HCN", "CNM"};
	
	private static final int SITE_TBROAD = 0;
	private static final int SITE_CJ = 1;
	private static final int SITE_LGHV = 2;
	private static final int SITE_JEJU = 3;
	private static final int SITE_CCS = 4;
	private static final int SITE_HCN = 5;
	private static final int SITE_CNM = 6;
	
	private static final String MAINTITLE = "릴리즈 노트 작성 툴 v1.6 (태양의 후예 Edition)";
	
	public MainFrame(){
		super(MAINTITLE);
	}
	
	public static MainFrame getInstance(){
		if(m_instance == null){
			m_instance = new MainFrame();
		}
		return m_instance;
	}
	
	public void init(){
		setLayout(null);
		
		siteLabel = new JLabel("Select Site   ", SwingConstants.RIGHT);
		siteLabel.setBounds(LABEL_X, LABEL_Y, LABEL_W, LABEL_H);
		add(siteLabel);
		
		siteCombo = new JComboBox(siteList);
		siteCombo.setSelectedIndex(SITE_TBROAD);
		siteCombo.setBounds(FIELD_X, FIELD_Y, FIELD_W, FIELD_H);
		add(siteCombo);
		
		///////////////////////////////////////////////////////////
		
		nameLabel = new JLabel("Model Name   ", SwingConstants.RIGHT);
		nameLabel.setBounds(LABEL_X+200, LABEL_Y, LABEL_W, LABEL_H);
		add(nameLabel);
		
		nameField = new JTextField();
		//nameField.setText("삼성5012");
		nameField.setBounds(FIELD_X+200, FIELD_Y, FIELD_W, FIELD_H);
		add(nameField);

		///////////////////////////////////////////////////////////
		checkDelLabel = new JLabel("Remove Latest version   ", SwingConstants.RIGHT);
		checkDelLabel.setBounds(LABEL_X+400, LABEL_Y, LABEL_W+50, LABEL_H);
		add(checkDelLabel);
		
		checkDelChkbox = new JCheckBox();
		checkDelChkbox.setBounds(FIELD_X+450, FIELD_Y, FIELD_W, FIELD_H);
		add(checkDelChkbox);
		///////////////////////////////////////////////////////////
		
		dateLabel = new JLabel("Date   ", SwingConstants.RIGHT);
		dateLabel.setBounds(LABEL_X, LABEL_Y+20, LABEL_W, LABEL_H);
		add(dateLabel);
		
		dateField = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
		dateField.setBounds(FIELD_X, FIELD_Y+20, FIELD_W, FIELD_H);
		add(dateField);
		
		///////////////////////////////////////////////////////////
		
		MWVersionLabel = new JLabel("MW version   ", SwingConstants.RIGHT);
		MWVersionLabel.setBounds(LABEL_X+200, LABEL_Y+20, LABEL_W, LABEL_H);
		add(MWVersionLabel);
		
		MWVersionField = new JTextField();
		//MWVersionField.setText("1078");
		MWVersionField.setBounds(FIELD_X+200, FIELD_Y+20, FIELD_W, FIELD_H);
		add(MWVersionField);
		///////////////////////////////////////////////////////////
		
		GlueVersionLabel = new JLabel("Glue version   ", SwingConstants.RIGHT);
		GlueVersionLabel.setBounds(LABEL_X+400, LABEL_Y+20, LABEL_W, LABEL_H);
		add(GlueVersionLabel);
		
		GlueVersionField = new JTextField();
		//GlueVersionField.setText("1133");
		GlueVersionField.setBounds(FIELD_X+400, FIELD_Y+20, FIELD_W, FIELD_H);
		add(GlueVersionField);
		///////////////////////////////////////////////////////////
		
		mwLabel = new JLabel("MW 변경사항   ", SwingConstants.RIGHT);
		mwLabel.setBounds(LABEL_X, LABEL_Y+40, LABEL_W, LABEL_H);
		add(mwLabel);
		
		mwTA = new JTextPane();
		mwTA.setText("- MW 버전 변경");
		mwScroll = new JScrollPane(mwTA);
		mwScroll.setBounds(FIELD_X, FIELD_Y+40, FIELD_W+450, FIELD_H+150);
		mwScroll.setBorder(new LineBorder(Color.BLACK, 1));
		add(mwScroll);
		///////////////////////////////////////////////////////////
		
		glueLabel = new JLabel("glue 변경사항   ", SwingConstants.RIGHT);
		glueLabel.setBounds(LABEL_X, LABEL_Y+212, LABEL_W, LABEL_H);
		add(glueLabel);
		
		glueTA = new JTextPane();
		//glueTA.setText("- glue 변경");
		glueScroll = new JScrollPane(glueTA);
		glueScroll.setBounds(FIELD_X, FIELD_Y+212, FIELD_W+450, FIELD_H+150);
		glueScroll.setBorder(new LineBorder(Color.BLACK, 1));
		add(glueScroll);
		///////////////////////////////////////////////////////////
		
		fileLabel = new JLabel("이전 파일   ", SwingConstants.RIGHT);
		fileLabel.setBounds(LABEL_X, LABEL_Y+384, LABEL_W, LABEL_H);
		add(fileLabel);
		
		fileField = new JTextField();
		fileField.setBounds(FIELD_X+80, FIELD_Y+384, FIELD_W+372, FIELD_H);
		add(fileField);
		
		fileButton = new JButton("Open");
		fileButton.setBounds(FIELD_X, FIELD_Y+384, FIELD_W-20, FIELD_H);
		add(fileButton);
		
		fileButtonChooser = new JFileChooser(".");
		fileButtonChooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
		fileButtonChooser.setMultiSelectionEnabled(false);
		///////////////////////////////////////////////////////////
		
		createButton = new JButton("Create !");
		createButton.setBounds(550/2, 450, 120, 30);
		add(createButton);
		
		dropTarget = new DropTarget(this,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget2 = new DropTarget(mwTA,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget3 = new DropTarget(glueTA,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget4 = new DropTarget(fileField,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget5 = new DropTarget(nameField,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget6 = new DropTarget(MWVersionField,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget7 = new DropTarget(GlueVersionField,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		dropTarget8 = new DropTarget(dateField,DnDConstants.ACTION_COPY_OR_MOVE, (DropTargetListener) this,true,null);
		
		setSize(700, 540);
		setVisible(true);
		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setActionListener();
		
		m_open_buf = new StringBuffer();
	}
	
	public boolean checkInputForm(){
		
		m_mwTABlank = false;
		m_glueTABlank = false;
		m_fileHistory = false;
		
		if(nameField.getText().equals("") || MWVersionField.getText().equals("") || GlueVersionField.getText().equals(""))
		{
			return false;
		}/*
		else{
			log.println("nameField.getText()="+nameField.getText());
			log.println("MWVersionField.getText()="+MWVersionField.getText());
			log.println("GlueVersionField.getText()="+GlueVersionField.getText());
		}*/
		
		if(mwTA.getText().equals("")) m_mwTABlank = true;
		//else log.println("mwTA.getText()="+mwTA.getText());
		
		if(glueTA.getText().equals("")) m_glueTABlank = true;
		//else log.println("glueTA.getText()="+glueTA.getText());
		
		if(fileField.getText().equals("")) m_fileHistory = true;
		//else log.println("fileField.getText()="+fileField.getText());
		
		if(m_mwTABlank && m_glueTABlank) return false;
		
		//whiteyume patch
		if(GlueVersionField.getText().length() >9) 	return false; 
						
		return true;	
	}
	
	public void clearAllText(){
		nameField.setText("");
		MWVersionField.setText("");
		GlueVersionField.setText("");
		mwTA.setText("- MW 버전 변경");
		glueTA.setText("");
		b_pre_exist = false;
		fileField.setText("");
		dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString());
	}
	
	public void copyInputToBuffer(){
		m_title = nameField.getText() + " ("+ dateField.getText() + ")";
		m_mw_title = mwLabel.getText() + "(v" + MWVersionField.getText() + ")";
		m_glue_title = glueLabel.getText() + "(v" + GlueVersionField.getText() + ")";
		m_mw = mwTA.getText();
		m_glue = glueTA.getText();
	}
	
	public static boolean isStringDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public void parseAndSet(StringBuffer strbuf){
		String str = strbuf.toString();
		
		// site
		String t_site = str.substring(0, str.indexOf("OCAP")-2);
		
		// name
		String t_name = str.substring(str.indexOf("Revision")+13, str.indexOf("(")-1);
		
		// mw version
		// 파일 이름의 버전이 표시되어 있는 경우, 이를 우선하여 적용한다.
		String t_mw_ver = "";
		String t_filename = m_open_file.getName();
		String t_filepath = m_open_file.getPath().substring(0,m_open_file.getPath().lastIndexOf("\\")+1);
		log.println("read filename="+t_filename);
		log.println("read path="+ t_filepath);
		String t_filename_ver = t_filename.substring(t_filename.lastIndexOf("_")+1, t_filename.lastIndexOf(".pdf"));
		String t_body_ver = str.substring(str.indexOf("MW 변경사항")+12, str.indexOf("MW 변경사항")+16);
		log.println("t_filename_ver="+t_filename_ver);
		if(isStringDouble(t_filename_ver)){
			if(Integer.parseInt(t_filename_ver) > Integer.parseInt(t_body_ver))
				t_mw_ver = t_filename_ver;
			else
				t_mw_ver = t_body_ver;
		}
		else{
			t_mw_ver = t_body_ver;
		}
		
		String t_save_filename = "";
		
		if(b_del_check){
			t_save_filename = t_filepath + t_filename.substring(0, t_filename.lastIndexOf("_")+1) + Integer.toString(Integer.parseInt(t_mw_ver)-1) + ".pdf";
		}else{
			t_save_filename = t_filepath + t_filename.substring(0, t_filename.lastIndexOf("_")+1) + Integer.toString(Integer.parseInt(t_mw_ver)+1) + ".pdf";
		}
		
		log.println("save filename="+t_save_filename);		
		m_save_file = new File(t_save_filename);
		
		// glue version
		// whiteyume : glue version이 10자 이상이 되면 version 정보가 줄바꿈이 되면서 parsing에 문제가 발생, 입력창에서 제한하여 방어함. parsing 개선
		
		int sIndex = str.indexOf("glue 변경사항");
		int eIndex = str.indexOf("glue 변경사항")+25;
		
		if(str.length() < eIndex){
			eIndex = str.length();
		}
	    String temp_glue_string = str.substring(sIndex, eIndex);
   
	    //String t_glue_ver = temp_glue_string.substring(temp_glue_string.indexOf("glue 변경사항")+14, temp_glue_string.lastIndexOf(")"));
	    String t_glue_ver = temp_glue_string.substring(temp_glue_string.indexOf("(")+2, temp_glue_string.lastIndexOf(")"));
		
		String tt = "";
		
		for(int i=0; i<t_site.length();  i++){
			if( Character.toString(t_site.charAt(i)).equals(" ") ||  Character.toString(t_site.charAt(i)).equals("\n")) continue;
			else tt += Character.toString(t_site.charAt(i));
		}
		
		t_site = tt;
		
		log.println("SITE="+ t_site);
		log.println("NAME="+ t_name );
		log.println("MW VERSION="+ t_mw_ver );
		log.println("GLUE VERSION="+ t_glue_ver );

		switch(t_site){
			case "TBroad":
				siteCombo.setSelectedIndex(SITE_TBROAD);
				break;
			case "CJ":
				siteCombo.setSelectedIndex(SITE_CJ);
				break;
			case "JEJU":
				siteCombo.setSelectedIndex(SITE_JEJU);
				break;
			case "LGHV":
				siteCombo.setSelectedIndex(SITE_LGHV);
				break;
			case "CCS":
				siteCombo.setSelectedIndex(SITE_CCS);
				break;
			case "HCN":
				siteCombo.setSelectedIndex(SITE_HCN);
				break;
			case "CNM":
				siteCombo.setSelectedIndex(SITE_CNM);
				break;
			default:
				break;
		}
		m_select_site = t_site;
		nameField.setText(t_name);
		MWVersionField.setText(Integer.toString(Integer.parseInt(t_mw_ver)+1));
		// glue 버전 올리는 것 막음 
		//GlueVersionField.setText(Integer.toString(Integer.parseInt(t_glue_ver)+1));
		GlueVersionField.setText(t_glue_ver);
	}
	
    public void runFileOpen() throws HeadlessException, IOException{
    	if(m_open_file==null){
    		log.println("Not exist open file");
    		return;
    	}
    	
    	fileField.setText(m_open_file.toString());
    	
    	if(m_open_buf!=null) m_open_buf.delete(0, m_open_buf.length());
    	
    	if(PDFMaker.getInstance().open(m_open_file)){
    		log.println("Successed to read file="+m_open_file.toString());
    		parseAndSet(m_open_buf);
    		b_pre_exist = true;
    	}
    	else{
    		log.println("Cannot read file="+m_open_file.toString());
    		JOptionPane.showMessageDialog(this, "파일을 읽을 수 없지 말입니다.", "경고", JOptionPane.ERROR_MESSAGE);
    	}
    }

    public void runFileCreate() throws DocumentException, IOException{
		//log.println("create dialog");
		log.println("file: "+m_save_file.toString());
		if(PDFMaker.getInstance().write(m_save_file)){
			log.println("Success to create release note!");
			// whiteyume patch
			PDFMaker.getInstance().closeTmpos();
			JOptionPane.showMessageDialog(this, PDFMaker.getInstance().m_status);
			clearAllText();
		}else{
			JOptionPane.showMessageDialog(this, PDFMaker.getInstance().m_status, "경고", JOptionPane.ERROR_MESSAGE);
		}
    }
	
	private void setActionListener(){
		fileButton.addActionListener(this);
		createButton.addActionListener(this);
		siteCombo.addActionListener(this);
		checkDelChkbox.addActionListener(this);
	}
	
	private void checkSaveFilename(){
		String t_name = m_save_file.toString().substring(0, m_save_file.toString().lastIndexOf("_")+1) + MWVersionField.getText() + ".pdf";
		log.println("check save filename="+t_name);
		m_save_file.delete();
		m_save_file = null;
		m_save_file = new File(t_name);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//log.println("action event="+e.getSource());
		
    	if(e.getSource() == fileButton){
    		if(fileButtonChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
   			    m_open_file = fileButtonChooser.getSelectedFile();
   				try {
					runFileOpen();
				} catch (HeadlessException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
    	}
    	else if(e.getSource() == checkDelChkbox){
    		if(b_del_check){
    			b_del_check = false;
    			log.println("Check delete button: "+b_del_check);
    		}else{
    			b_del_check = true;
    			log.println("Check delete button: "+b_del_check);
    		}
    		try {
				runFileOpen();
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    	else if(e.getSource() == siteCombo){
    		JComboBox cb = (JComboBox)e.getSource();
    		m_select_site = (String)cb.getSelectedItem();
    		log.println("select:"+m_select_site);
    	}
    	else if(e.getSource() == createButton){
    		
    		int ret=0;
    		
    		if(checkInputForm()){
    			   				
    			if(m_fileHistory){
    				ret = JOptionPane.showConfirmDialog(this, "이전 릴리즈 노트를 선택하지 않았지 말입니다. 신규 릴리즈 노트를 작성합니다.", "확인", JOptionPane.YES_NO_CANCEL_OPTION);
    				log.println("ret="+ret);
    			}

    			if(b_del_check==false){
    				if(ret<1 && m_mwTABlank) JOptionPane.showMessageDialog(this, "MW 변경 사항이 없지 말입니다. 계속 합니다.");
    				if(ret<1 && m_glueTABlank) JOptionPane.showMessageDialog(this, "GLUE 변경 사항이 없지 말입니다. 계속 합니다.");
    			
    				copyInputToBuffer();
    				if(this.b_pre_exist) checkSaveFilename();
    			}
    			
				if(m_save_file!=null){
					fileButtonChooser.setSelectedFile(m_save_file);
				}
    			
    			if(ret<1 && fileButtonChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
   					m_save_file = fileButtonChooser.getSelectedFile();
	    			try {
						runFileCreate();
					} catch (DocumentException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
    			}
    		}
    		else{
    			// whiteyume patch
    			if(GlueVersionField.getText().length() >9) {
    			    JOptionPane.showMessageDialog(this, "Glue version은 9자 이하로 작성하세요.", "에러", JOptionPane.ERROR_MESSAGE);
    			    GlueVersionField.setText("");
    			}else if(b_del_check==false){
    				JOptionPane.showMessageDialog(this, "입력 칸을 모두 입력 합니다.", "에러", JOptionPane.ERROR_MESSAGE);
    			}else{
    				
    				if(b_pre_exist){
	    				JOptionPane.showMessageDialog(this, "가장 최신의 릴리즈 노트를 삭제합니다.");
	    				
	        			if(fileButtonChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
	       					m_save_file = fileButtonChooser.getSelectedFile();
	    	    			try {
	    						runFileCreate();
	    					} catch (DocumentException e1) {
	    						e1.printStackTrace();
	    					} catch (IOException e1) {
	    						e1.printStackTrace();
	    					}
	        			}
    				}else{
    					JOptionPane.showMessageDialog(this, "이전 파일이 없지 말입니다. 이전 파일을 확인 합니다.", "에러", JOptionPane.ERROR_MESSAGE);
    				}
    			}
			}
    	}
   	
	}
	
	@Override
	public void drop(DropTargetDropEvent dtde) {
		 if((dtde.getDropAction() &
	                DnDConstants.ACTION_COPY_OR_MOVE)!=0){
			dtde.acceptDrop(dtde.getDropAction());
			Transferable tr = dtde.getTransferable();

			java.util.List list = null;
			try {
				list = (java.util.List)
				tr.getTransferData(DataFlavor.javaFileListFlavor);
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			m_open_file = (File)list.get(0);
	    	try {
				runFileOpen();
			} catch (HeadlessException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }		
		 
 	}
	
	//////////////////////////////////////////////////////////////////////////////////

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}
}
