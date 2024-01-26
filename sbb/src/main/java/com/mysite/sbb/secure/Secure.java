package com.mysite.sbb.secure;

public class Secure {
	
	public String xssScript;
	public String filename;
	
	//XSS 필터링
	public static String xssSecure(String xssScript){
		return xssScript = xssScript
    				.replaceAll("<","&lt;")
    				.replaceAll(">","&gt;")
    				.replaceAll("&","&amp;")
    				.replaceAll("\"","&quot;");
	}

	//파일 다운로드 필터링
	public static void filedownloadSecure(String filename) {
		int dotIndex = filename.lastIndexOf(".");
		System.out.println("filename: "+filename.substring(0, dotIndex));
    	
    	if (filename.substring(0, dotIndex).contains("/") || 
    			filename.substring(0, dotIndex).contains(".") ||
    			filename.substring(0, dotIndex).contains("\\") || 
    			filename.substring(0, dotIndex).contains("%u002e") ||
    			filename.substring(0, dotIndex).contains("%u2215") || 
    			filename.substring(0, dotIndex).contains("%u2216") || 
    			filename.substring(0, dotIndex).contains("%252e") ||
    			filename.substring(0, dotIndex).contains("%252f") || 
    			filename.substring(0, dotIndex).contains("%255c")) {
    	        throw new IllegalArgumentException("파일을 다운 받을 수 없습니다.");
    	    }
	}
}
