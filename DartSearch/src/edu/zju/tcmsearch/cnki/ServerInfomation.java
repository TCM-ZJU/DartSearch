package edu.zju.tcmsearch.cnki;

public class ServerInfomation {
	
	public final static String HostUrl = "http://cnki.cintcm.ac.cn";
	public final static String Username = "cnki";
	public final static String Password = "cnki";
	
	public final static String ASP_NET_SessionId = "ASP.NET_SessionId";
	public final static String Cookie = "Cookie";
	public final static String Set_Cookie = "Set-Cookie";
	public final static String RecordID = "RecordID";
	public final static String kuakuSearchSelectionID = "kuakuSearchSelectionID";
	public final static String LID = "LID";

	
	
	
	public final static String QueryPattern = "searchflag=0&selectdatabase=CHKD期刊全文数据库,CHKD博硕士学位论文全文数据库,CHKD会议论文全文数据库,CHKD报纸全文数据库&baseorder=&basesearchmatch=1&basesearchdup=1&baseextension=&slctBaseSearchFields=题名&txtBaseSearchValue=大黄&slctBaseStartYear=1994&slctBaseEndYear=2006&hdnMinYear=1994&hdnMaxYear=2006&mainwordsearchno=&mainwordnodetype=";
	//*/	
	/* 浙江大学图书管帐号
	public final static String HostUrl = "http://c59.cnki.net";
	public final static String Username = "sh0283";
	public final static String Password = "zjdxtsg";
	
	public final static String ASP_NET_SessionId = "ASP.NET_SessionId";
	public final static String Cookie = "Cookie";
	public final static String Set_Cookie = "Set-Cookie";
	public final static String RecordID = "RecordID";
	public final static String kuakuSearchSelectionID = "kuakuSearchSelectionID";
	public final static String LID = "LID";

	
	
	
	public final static String QueryPattern = "searchflag=0&hdnInput=&hdnMinYear=1979&hdnMaxYear=2007&KuaKuID=&hdnIsAll=true&postfix=&NaviField=专题子栏目代码&NaviDatabaseName=ZJCLS&systemno=&hdnFathorCode=sysAll&selectbox=A&selectbox=B&selectbox=C&selectbox=D&selectbox=E&selectbox=F&selectbox=G&selectbox=H&selectbox=I&selectbox=J&strNavigatorValue=,A,B,C,D,E,F,G,H,I,J&strNavigatorName=,理工A(数学物理力学天地生),理工B(化学化工冶金环境矿业),理工C(机电航空交通水利建筑能源),农业,医药卫生,文史哲,政治军事与法律,教育与社会科学综合,电子技术及信息科学,经济与管理&singleleafcode=&slctBaseSearchFields=题名&txtBaseSearchValue=大黄&mainwordnodetype=&mainwordsearchno=&mainwordtj=1&mainwordtjword=大黄&slctBaseStartYear=1979&slctBaseEndYear=2007&baseorder=&basesearchmatch=0&basesearchdup=1&selectdatabase=中国学术期刊网络出版总库&selectdatabase=中国期刊全文数据库&selectdatabase=中国期刊全文数据库_世纪期刊&selectdatabase=中国博士学位论文全文数据库&selectdatabase=中国优秀硕士学位论文全文数据库&hddnFields=题名(题名)|关键词(关键词)|摘要(摘要)|全文(全文)|作者(作者)|第一责任人(第一作者)|单位(作者单位)|来源(来源)|主题(主题)|基金(基金)|引文(参考文献)|&advancedfield1=题名&advancedvalue11=&advancedfrequency11=&advancedrelation1=*&advancedvalue12=&advancedfrequency12=&logical2=and&advancedfield2=题名&advancedvalue21=&advancedfrequency21=&advancedrelation2=*&advancedvalue22=&advancedfrequency22=&logical3=and&advancedfield3=题名&advancedvalue31=&advancedfrequency31=&advancedrelation3=*&advancedvalue32=&advancedfrequency32=&logical4=and&advancedfield4=题名&advancedvalue41=&advancedfrequency41=&advancedrelation4=*&advancedvalue42=&advancedfrequency42=&TableType=PY&display=chinese&encode=gb&TablePrefix=&userright=&VarNum=4&slctAdvanceStartYear=1979&slctAdvanceEndYear=2007&advanceorder=&advancesearchmatch=0&advancesearchdup=1&expertValue=";
	//public final static String QueryPatternZjuLib = "ID=CJFD&hdnSearchType=&turnpage=&hdnIsAll=true&NaviField=专题子栏目代码&NaviDatabaseName=ZJCLS&systemno=&hdnFathorCode=sysAll&selectbox=A&selectbox=B&selectbox=C&selectbox=D&selectbox=E&selectbox=F&selectbox=G&selectbox=H&selectbox=I&selectbox=J&strNavigatorValue=,A,B,C,D,E,F,G,H,I,J&strNavigatorName=,理工A(数学物理力学天地生),理工B(化学化工冶金环境矿业),理工C(机电航空交通水利建筑能源),农业,医药卫生,文史哲,政治军事与法律,教育与社会科学综合,电子技术及信息科学,经济与管理&singleleafcode=&searchAttachCondition=&SearchQueryID=&SearchFieldRelationDirectory=主题/[],篇名/[],题名/[],作者/[SYS_Author_Relevant],关键词[],第一责任人/[SYS_Author_Relevant],机构/[SYS_Organization_Relevant],摘要/[],引文/[],基金/[SYS_Fund_Relevant],全文/[]&updateTempDB=cjfdytmp,CJFDTEMP&bCurYearTempDB=1&fieldnowordfrequency=关键词,作者,第一责任人,机构,中文刊名&英文刊名,年',期',基金,分类号',ISSN',CN',DOI'&fieldtips=篇名/[在文献标题中检索。对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],关键词/[检索文献的关键词中满足检索条件的文献。对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],第一责任人/[请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],作者/[可输入作者完整姓名，或只输入连续的一部分。],机构/[可输入完整的机构名称，或只输入连续的一部分。],中文摘要/[对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],引文/[请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],全文/请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],基金/[检索受满足条件的基金资助的文献。],中文刊名/[请输入部分或全部刊名。],ISSN/[请输入完整的ISSN号。],年/[输入四位数字的年份。],期/[输入期刊的期号，如果不足两位数字，请在前面补“0”，如“08”。],主题/[主题包括篇名、关键词、中文摘要。可检索出这三项中任一项或多项满足指定检索条件的文献。对主题是按词检索的，请尽可能输入完整的词，以避免漏检。]&advancedfield1=主题&advancedvalue1=大黄&advancedfrequency1=&yearstart=1999&yearend=2007&PublicationDate=&SearchRange=All&searchmatch=0&order=dec&RecordsPerPage=20&TableType=PY&display=chinese&encode=gb&TablePrefix=CJFD&View=中国期刊全文数据库&yearFieldName=年&userright=&VarNum=1&imageField.x=30&imageField.y=10";
	public final static String QueryPatternZjuLib = "ID=CJFD&hdnSearchType=&hdnIsAll=true&NaviField=专题子栏目代码&NaviDatabaseName=ZJCLS&systemno=&hdnFathorCode=sysAll&selectbox=A&selectbox=B&selectbox=C&selectbox=D&selectbox=E&selectbox=F&selectbox=G&selectbox=H&selectbox=I&selectbox=J&strNavigatorValue=,A,B,C,D,E,F,G,H,I,J&strNavigatorName=,理工A(数学物理力学天地生),理工B(化学化工冶金环境矿业),理工C(机电航空交通水利建筑能源),农业,医药卫生,文史哲,政治军事与法律,教育与社会科学综合,电子技术及信息科学,经济与管理&singleleafcode=&searchAttachCondition=&SearchQueryID=4&SearchFieldRelationDirectory=主题/[],篇名/[],题名/[],作者/[SYS_Author_Relevant],关键词[],第一责任人/[SYS_Author_Relevant],机构/[SYS_Organization_Relevant],摘要/[],引文/[],基金/[SYS_Fund_Relevant],全文/[]&updateTempDB=cjfdytmp,CJFDTEMP&bCurYearTempDB=1&fieldnowordfrequency=关键词,作者,第一责任人,机构,中文刊名&英文刊名,年',期',基金,分类号',ISSN',CN',DOI'&fieldtips=篇名/[在文献标题中检索。对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],关键词/[检索文献的关键词中满足检索条件的文献。对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],第一责任人/[请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],作者/[可输入作者完整姓名，或只输入连续的一部分。],机构/[可输入完整的机构名称，或只输入连续的一部分。],中文摘要/[对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],引文/[请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],全文/请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],基金/[检索受满足条件的基金资助的文献。],中文刊名/[请输入部分或全部刊名。],ISSN/[请输入完整的ISSN号。],年/[输入四位数字的年份。],期/[输入期刊的期号，如果不足两位数字，请在前面补“0”，如“08”。],主题/[主题包括篇名、关键词、中文摘要。可检索出这三项中任一项或多项满足指定检索条件的文献。对主题是按词检索的，请尽可能输入完整的词，以避免漏检。]&advancedfield1=题名&advancedvalue1=大黄&advancedfrequency1=&yearstart=1999&yearend=2007&PublicationDate=&SearchRange=All&searchmatch=0&order=dec&RecordsPerPage=20&TableType=PY&display=chinese&encode=gb&TablePrefix=CJFD&View=中国期刊全文数据库&yearFieldName=年&userright=&VarNum=1&lastpage=167&RecordsPerPage2=20&systemno=&classtype=&QueryID=4&turnpage=&curpage=1&curpage1=1&curpage2=1&imageField.x=30&imageField.y=6";
	//*/
	
	/*   湖南理工大学帐号
	public final static String HostUrl = "http://211.151.93.100"; 
	public final static String Username = "syhnlgdx";
	public final static String Password = "hnlgdx";
	
	public final static String ASP_NET_SessionId = "ASP.NET_SessionId";
	public final static String Cookie = "Cookie";
	public final static String Set_Cookie = "Set-Cookie";
	public final static String RecordID = "RecordID";
	public final static String kuakuSearchSelectionID = "kuakuSearchSelectionID";
	public final static String LID = "LID";
	
	public final static String QueryPattern = "searchflag=0&hdnInput=&hdnMinYear=1979&hdnMaxYear=2007&KuaKuID=&hdnIsAll=true&postfix=&NaviField=专题子栏目代码&NaviDatabaseName=ZJCLS&systemno=&hdnFathorCode=sysAll&selectbox=A&selectbox=B&selectbox=C&selectbox=D&selectbox=E&selectbox=F&selectbox=G&selectbox=H&selectbox=I&selectbox=J&strNavigatorValue=,A,B,C,D,E,F,G,H,I,J&strNavigatorName=,理工A(数学物理力学天地生),理工B(化学化工冶金环境矿业),理工C(机电航空交通水利建筑能源),农业,医药卫生,文史哲,政治军事与法律,教育与社会科学综合,电子技术及信息科学,经济与管理&singleleafcode=&slctBaseSearchFields=题名&txtBaseSearchValue=大黄&mainwordnodetype=&mainwordsearchno=&mainwordtj=1&mainwordtjword=大黄&slctBaseStartYear=1979&slctBaseEndYear=2007&baseorder=&basesearchmatch=0&basesearchdup=1&selectdatabase=中国学术期刊网络出版总库&selectdatabase=中国期刊全文数据库&selectdatabase=中国期刊全文数据库_世纪期刊&selectdatabase=中国博士学位论文全文数据库&selectdatabase=中国优秀硕士学位论文全文数据库&hddnFields=题名(题名)|关键词(关键词)|摘要(摘要)|全文(全文)|作者(作者)|第一责任人(第一作者)|单位(作者单位)|来源(来源)|主题(主题)|基金(基金)|引文(参考文献)|&advancedfield1=题名&advancedvalue11=&advancedfrequency11=&advancedrelation1=*&advancedvalue12=&advancedfrequency12=&logical2=and&advancedfield2=题名&advancedvalue21=&advancedfrequency21=&advancedrelation2=*&advancedvalue22=&advancedfrequency22=&logical3=and&advancedfield3=题名&advancedvalue31=&advancedfrequency31=&advancedrelation3=*&advancedvalue32=&advancedfrequency32=&logical4=and&advancedfield4=题名&advancedvalue41=&advancedfrequency41=&advancedrelation4=*&advancedvalue42=&advancedfrequency42=&TableType=PY&display=chinese&encode=gb&TablePrefix=&userright=&VarNum=4&slctAdvanceStartYear=1979&slctAdvanceEndYear=2007&advanceorder=&advancesearchmatch=0&advancesearchdup=1&expertValue=";
	public final static String QueryPatternZjuLib = "ID=CJFD&hdnSearchType=&turnpage=&hdnIsAll=true&NaviField=专题子栏目代码&NaviDatabaseName=ZJCLS&systemno=&hdnFathorCode=sysAll&selectbox=A&selectbox=B&selectbox=C&selectbox=D&selectbox=E&selectbox=F&selectbox=G&selectbox=H&selectbox=I&selectbox=J&strNavigatorValue=,A,B,C,D,E,F,G,H,I,J&strNavigatorName=,理工A(数学物理力学天地生),理工B(化学化工冶金环境矿业),理工C(机电航空交通水利建筑能源),农业,医药卫生,文史哲,政治军事与法律,教育与社会科学综合,电子技术及信息科学,经济与管理&singleleafcode=&searchAttachCondition=&SearchQueryID=&SearchFieldRelationDirectory=主题/[],篇名/[],题名/[],作者/[SYS_Author_Relevant],关键词[],第一责任人/[SYS_Author_Relevant],机构/[SYS_Organization_Relevant],摘要/[],引文/[],基金/[SYS_Fund_Relevant],全文/[]&updateTempDB=cjfdytmp,CJFDTEMP&bCurYearTempDB=1&fieldnowordfrequency=关键词,作者,第一责任人,机构,中文刊名&英文刊名,年',期',基金,分类号',ISSN',CN',DOI'&fieldtips=篇名/[在文献标题中检索。对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],关键词/[检索文献的关键词中满足检索条件的文献。对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],第一责任人/[请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],作者/[可输入作者完整姓名，或只输入连续的一部分。],机构/[可输入完整的机构名称，或只输入连续的一部分。],中文摘要/[对该检索项的检索是按词进行的，请尽可能输入完整的词，以避免漏检。],引文/[请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],全文/请选择检索项并指定相应的检索词，选择排序方式、匹配模式、文献时间等限定条件，然后点击“检索”。],基金/[检索受满足条件的基金资助的文献。],中文刊名/[请输入部分或全部刊名。],ISSN/[请输入完整的ISSN号。],年/[输入四位数字的年份。],期/[输入期刊的期号，如果不足两位数字，请在前面补“0”，如“08”。],主题/[主题包括篇名、关键词、中文摘要。可检索出这三项中任一项或多项满足指定检索条件的文献。对主题是按词检索的，请尽可能输入完整的词，以避免漏检。]&advancedfield1=主题&advancedvalue1=大黄&advancedfrequency1=&yearstart=1999&yearend=2007&PublicationDate=&SearchRange=All&searchmatch=0&order=dec&RecordsPerPage=20&TableType=PY&display=chinese&encode=gb&TablePrefix=CJFD&View=中国期刊全文数据库&yearFieldName=年&userright=&VarNum=1&imageField.x=30&imageField.y=10";
	
	//*/
	
	public static String getQueryPattern(String value){
		return QueryPattern.replace("大黄",value);
	}
}
