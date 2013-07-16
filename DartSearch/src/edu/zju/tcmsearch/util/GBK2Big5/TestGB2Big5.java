package edu.zju.tcmsearch.util.GBK2Big5;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

public class TestGB2Big5 extends TestCase {
	GB2Big5 g2b;
	public void setUp(){
		g2b = GB2Big5.getInstance();
	}
	
	public void test(){
		try {
			String g = new String(g2b.gb2big5("中华人民共和国"),"big5");
			String k = new String(g.getBytes("gbk"));
			String h = new String(k.getBytes("UTF-8"),"UTF-8");
			System.out.println(g);
			System.out.println(k);
			System.out.println(h);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test2() throws UnsupportedEncodingException, Exception{
		String utf_8 = g2b.simple2traditional("中华人民共和国","utf-8");
		System.out.println(utf_8);
	}
	
	public void test3(){
		String utf_8 = g2b.simple2traditional("中华人民共和国");
		System.out.println(utf_8);
	}	

}
