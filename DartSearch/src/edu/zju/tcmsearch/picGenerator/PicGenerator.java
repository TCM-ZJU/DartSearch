package edu.zju.tcmsearch.picGenerator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.commons.io.IOUtils;
import edu.zju.tcmsearch.query.service.QueryTemplate;

/**
 * @author zhm
 * 
 */
public class PicGenerator {

	private static final Logger logger = Logger.getLogger(QueryTemplate.class);
	
	// 百度
	// public static String
	// baidupatterString="<dl><dd><div><a((?!<dd>).)*<img(((?!<dd>).)*)></a></div></dd><dt>(((?!<dd>).)*)<a
	// href=(((?!<dd>).)*)&u=(((?!<dd>).)*)&f=(((?!<dd>).)*)&jn=(((?!<dd>).)*)\"
	// target=\"_blank\">(((?!<dd>).)*)</a></dt></dl>";
	// public static String
	// BaiduSourceUrl="http://image.baidu.com/i?tn=baiduimage&ct=201326592&cl=2&lm=-1&pv=&word=ccntdartsearch%20医药&z=0";

	// google
	// public static String
	// googlepatterString="dyn(((?!<dd>).)*)Img(((?!<dyn>).)*)&h=(((?!<dyn>).)*),\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\",\"(((?!<,>).)*)\"";
	// public static String
	// googleSourceUrl="http://images.google.cn/images?um=1&complete=1&hl=zh-CN&newwindow=1&q=ccntdartsearch&btnG=医药&aq=f";

	// yahoo
	public static String yahooPattenString = "<div class=\"outer\">\r\n((.)*)<a href=\"(((?!<).)*)\" target=\"_blank\"(((?!<img).)*)<img src=\"(((?!<onload).)*)\" onload((.)*)\r\n((.)*)\r\n((.)*)\r\n(((?!h3).)*)<h3>(((?!<).)*)</h3>";
	public static String yahooSourceUrl = "http://one.cn.yahoo.com/s?p=ccntdartsearch+医药&v=image&source=ysearch_img_result_topsearch";

	public ArrayList<PictureInfo> picRetrival(String keyword) {
		if (keyword.trim().length() == 0)
			return null;
		String picWebsiteUrl = yahooSourceUrl.replaceAll("ccntdartsearch",
				keyword.trim());

		RegExMatch mp = new RegExMatch();
		Matcher m = mp.GetMatcherFromURL(yahooPattenString, picWebsiteUrl);

		ArrayList<PictureInfo> picList = new ArrayList<PictureInfo>();
		int counter = 1;
		while (m.find()) {
			counter++;
			PictureInfo picture = new PictureInfo();
			// baidu
			/*
			 * picture.setPicName(m.group(14));
			 * picture.setSmallPicLink(m.group(2));
			 * picture.setBigPicLink(m.group(8));
			 * picture.setOriginalWebsite(m.group(10));
			 */

			// yahoo
			logger.debug("SmallPicLink: "+m.group(7));
			logger.debug("PicName: "+m.group(17));
			getBigGPic(m.group(3), picture);
			picture.setPicName(changeCharset(m.group(17).trim(),"GBK","UTF-8"));
			picture.setSmallPicLink(m.group(7));

			logger.debug("______________________________");
			picList.add(picture);
			if (counter > 12)
				break;
		}

		return picList;

	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            待转换编码的字符串
	 * @param oldCharset
	 *            原编码
	 * @param newCharset
	 *            目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String changeCharset(String str, String oldCharset, String newCharset){
		 try { 
			String utf8String = IOUtils.toString(IOUtils.toInputStream(str, "UTF-8"));
			return utf8String;
//			 if (str != null) {
//				 	// 用旧的字符编码解码字符串。解码可能会出现异常。
//				 	byte[] bs = null;
//				 	bs = str.getBytes(oldCharset);	
//				 	// 用新的字符编码生成字符串
//				 	logger.debug(new String(bs, newCharset));
//				 	return new String(bs, newCharset);
//			 }
//			 else
//				 return null;
		 } catch (IOException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
			 return null;
		 }
			
	 }

	// this method is specially used for yahoo search
	public void getBigGPic(String secendQueryWebsiteUrl, PictureInfo picture) {

		secendQueryWebsiteUrl = "http://one.cn.yahoo.com/"
				+ secendQueryWebsiteUrl;

		MatchSource ms = new MatchSource();
		String SourceData = ms.getDocumentAtURL(secendQueryWebsiteUrl);

		Pattern p = Pattern
				.compile("<div class=\"mediafrom\">(((?!http).)*)<a href=\"(((?!>).)*)\">");
		Matcher m = p.matcher(SourceData);

		Pattern p1 = Pattern
				.compile("<center><a href=\"(((?!name).)*)\" name=");
		Matcher m1 = p1.matcher(SourceData);

		if (m.find()) {
			logger.debug("OriginalWebsite: "+m.group(3));
			picture.setOriginalWebsite(m.group(3));
		} else
			picture.setOriginalWebsite("");

		if (m1.find()) {
			logger.debug("bigPicLink: "+m1.group(1));
			picture.setBigPicLink(m1.group(1));
		} else
			picture.setBigPicLink("");
	}

	public static void main(String[] args) {
		ArrayList<PictureInfo> picList = new PicGenerator().picRetrival("李时珍");
	}

}
