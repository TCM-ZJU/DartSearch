package edu.zju.tcmsearch.cnki;

import java.util.regex.Pattern;

public class PatternFactory {
	
	public static Pattern getResultListPattern(){
		return Pattern.compile(prl,Pattern.DOTALL);
	}
	
	public static Pattern getDocumentPatternA(){
		return Pattern.compile(p1,Pattern.DOTALL);
	}
	
	public static Pattern getDocumentPatternB(){
		return Pattern.compile(p2,Pattern.DOTALL);
	}
	
	public final static String p2=
		"<tr>\n"
		+"<td  bgcolor=#f1f7fe align=left STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\" target=_blank><img src=images/download.gif border=0  ></a><a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"    target=_blank >([\\d]*)</a></td>\n"
		+"<td  bgcolor=#f1f7fe align=left STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"    target=_blank >([^\\n]*)</a></td>\n"
		+"<td  bgcolor=#f1f7fe align=left STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"([^<]*)</td>\n"
		+"<td  bgcolor=#f1f7fe align=left STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"([^<]*)</td>\n"
		+"<td  bgcolor=#f1f7fe align=left STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"([^<]*)</td>\n"
		+"</tr>\n"
		;
	public final static String p1=
		"<tr>\n"
		+"<td  bgcolor=#ffffff STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\" target=_blank><img src=images/download.gif border=0  ></a><a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"    target=_blank >([\\d]*)</a></td>\n"
		+"<td  bgcolor=#ffffff STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"    target=_blank >([^\\n]*)</a></td>\n"
		+"<td  bgcolor=#ffffff STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"([^<]*)</td>\n"
		+"<td  bgcolor=#ffffff STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"([^<]*)</td>\n"
		+"<td  bgcolor=#ffffff STYLE=\"word-wrap: break-word  ;font-size:9pt\">\n"
		+"([^<]*)</td>\n"
		+"</tr>\n"
		;	
	
	public final static String prl = "(.*)\n来源数据库</td>\n</tr>\n"+
                              "(.*)"+
                              "</table>\n(.*)共有记录(.*)";
	
	public final static int INDEX_PRL = 2;
	public final static int INDEX_FILEURL_PAB = 1;
	public final static int INDEX_ABSURL_PAB = 2;
	public final static int INDEX_TITLE_PAB = 5;
	
	
	public static Pattern getResultListPatternR(){
		return Pattern.compile(PRL,Pattern.DOTALL);
	}
	
	public static Pattern getDocumentPatternAR(){
		return Pattern.compile(P1,Pattern.DOTALL);
	}
	
	public static Pattern getDocumentPatternBR(){
		return Pattern.compile(P2,Pattern.DOTALL);
	}	
	
	public final static String P1 = 
		 "                    <TR>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      bgColor=#ffffff><A \n"
		+"                        href=\"([/\\w\\.\\?\\&\\=\\:]*)\" \n"
		+"                        target=_blank><IMG \n"
		+"                        src=\"images/login_download\\.gif\" border=0></A><A \n"
		+"                        href=\"([/\\w\\.\\?\\&\\=\\:]*)\" \n"
		+"                        target=_blank>([\\d]*)</A></TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      bgColor=#ffffff><A \n"
		+"                        href=\"([/\\w\\.\\?\\&\\=\\:]*)\" \n"
		+"                        target=_blank>([^;]*)</A></TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      bgColor=#ffffff>([^;]*)</TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      bgColor=#ffffff>([^;]*)</TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      bgColor=#ffffff>([^;]*)</TD></TR>\n";
	
	public final static String P2 =
		 "                    <TR>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      align=left bgColor=#f1f7fe><A \n"
		+"                        href=\"([/\\w\\.\\?\\&\\=\\:]*)\" \n"
		+"                        target=_blank><IMG \n"
		+"                        src=\"images/login_download\\.gif\" border=0></A><A \n"
		+"                        href=\"([/\\w\\.\\?\\&\\=\\:]*)\" \n"
		+"                        target=_blank>([\\d]*)</A></TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      align=left bgColor=#f1f7fe><A \n"
		+"                        href=\"([/\\w\\.\\?\\&\\=\\:]*)\" \n"
		+"                        target=_blank>([^;]*)</A></TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      align=left bgColor=#f1f7fe>([^;]*)</TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      align=left bgColor=#f1f7fe>([^;]*)</TD>\n"
		+"                      <TD style=\"FONT-SIZE: 9pt; WORD-WRAP: break-word\" \n"
		+"                      align=left bgColor=#f1f7fe>([^;]*)</TD></TR>\n";
	
	public final static String PRL = 
	     "(.*)年/期</td>\n</tr>\n"+
         "([^共有]*)"+
         "</table>(.*)";
	
	public final static int INDEX_FILEURL_PAB_R = 1;
	public final static int INDEX_ABSURL_PAB_R = 2;
	public final static int INDEX_TITLE_PAB_R = 4;	
	
	
    public final static String  q1 =
		 "<tr>\n"
		+"<td  bgcolor=#ffffff align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"	target=_blank><img src=images/download.gif border=0	 ></a><input type=checkbox name=FileNameS  value=\"([^\\\"]*)\"  onclick=\"checkMark\\(this\\)\"  ><a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"	 	target=_blank >([\\d]*)</a></td>\n"
		+"<td  bgcolor=#ffffff align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"	 	target=_blank >([^\\n]*)</a></td>\n"
		+"<td  bgcolor=#ffffff align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"([^\\n]*)</td>\n"
		+"<td  bgcolor=#ffffff align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"([^\\n]*)</td>\n"
		+"<td  bgcolor=#ffffff align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"([^\\n]*)</td>\n"
		+"</tr>\n";
	    
	public final static String  q2=
		 "<tr>\n"
		+"<td  bgcolor=#f1f7fe align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"	target=_blank><img src=images/download.gif border=0	 ></a><input type=checkbox name=FileNameS  value=\"([^\\\"]*)\"  onclick=\"checkMark\\(this\\)\"  ><a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"	 	target=_blank >([\\d]*)</a></td>\n"
		+"<td  bgcolor=#f1f7fe align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"<a href=\"([/\\w\\.\\?\\&\\=\\:]*)\"	 	target=_blank >([^\\n]*)</a></td>\n"
		+"<td  bgcolor=#f1f7fe align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"([^\\n]*)</td>\n"
		+"<td  bgcolor=#f1f7fe align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"([^\\n]*)</td>\n"
		+"<td  bgcolor=#f1f7fe align=left valign=center STYLE=\"word-wrap: break-word  ;font-size:9pt ;Color:#000000\">\n"
		+"([^\\n]*)</td>\n"
		+"</tr>\n";

}
