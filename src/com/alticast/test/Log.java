package com.alticast.test;


public class Log {

	protected String m_str;
	
	public Log(String str){
		this.m_str = str;
	}
	
    public void dumpString(String word, String[] str){
    	println("----------------------- DUMP START -------------------");    	    	
    	for(int idx=0; idx<str.length; idx++){
    		if (str[idx]!= null)
    			println(""+word+"["+idx+"] "+str[idx]);
    	}
    	println("----------------------- DUMP ED -------------------");
    }
	
	public void println(String str){
		System.out.println("|"+m_str+"| "+str);
	}
	
	public void println(StringBuffer str){
		println(str.toString());
	}
	
    public void println(String[] str){
    	for(int i=0; i<str.length; i++)
    		if(str[i]!=null) println(str[i]);
    }

}