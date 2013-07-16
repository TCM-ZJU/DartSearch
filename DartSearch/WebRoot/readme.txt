中医药数据搜索引擎(tcmsearch)安装使用


前提:
tomcat的编码方式要正确，
修改 %CATALINA_HOME%\conf\server.xml下的一段，在

 <Connector port="8080" maxHttpHeaderSize="8192"
               maxThreads="150" minSpareThreads="25" maxSpareThreads="75"
               enableLookups="false" redirectPort="8443" acceptCount="100"
               connectionTimeout="20000" disableUploadTimeout="true">

后加上： URIEncoding="UTF-8"


安装:


把tcmsearch.war放到应用服务器的发布文件夹下(tomcat下为:%CATALINA_HOME%\webapps),启动应用服务器,系统就自动解压完成安装.

使用:
1、浏览：在浏览器中输入 http://服务器的IP地址/tcmsearch，即可浏览页面

   给本体分类到源目录树：由于目前没有集成权限控制功能，因此直接用以下地址访问：http://服务器的IP地址/tcmsearch2/ontoClassify.cla

2、建索引，
  A、配置索引文件夹

在tcmsearch2/WEB-INF/classes下有文件：luceneConfig.properties。内容大致如下：

lucene.indexDir=g:/webIndex
lucene.dictDir=d:/WebDict
lucene.dictFile=d:/WebDict/tcm.dict
lucene.schedule.time=0 20 14 04 * ?
lucene.index.showrate=3000

修改lucene.indexDir后面的文件夹内容g:/webIndex为你需要的文件夹。

建议该文件夹所在盘有足够大的硬盘空间，推荐10G以上！

B、直接建索引
在解压后的tcmsearch2文件夹下面有个批处理命令buildIndex.bat，双击后即可建索引。

C、定时建索引
由于建索引需要一定的时间，又需要定时更新。因此可配置它定时建索引，索引时间的配置仍然在luceneConfig.properties文件中，
将lucene.schedule.time的时间改为需要的时间就可，时间分六部分，分别代表 秒、分、小时、日、月、年，例如前面的0 20 14 04 * ?
就表示任何年月的4日14时20分0秒，也就是每月的4日14时20分开始定时建索引。
配置完后运行tcmsearch2文件夹下面有个批处理命令buildIndexSchedule.bat就可（弹出的窗口要一直开着才能）。

3、增加或修改检索数据库内容(修改本体与注册配置)
修改本体与注册的文档见注册工具的说明。将已有的tcmsearch2/WEB-INF/classes/etc删除，修改完后的文件copy到
tcmsearch2/WEB-INF/classes下即可。


4、配置文件及说明

A、WEB-INF/classes下有一个jdbc.properties文件是配置tcmsearch2应用中的辅助数据源的（不是我们查询信息的数据源），
由于有些功能还未启用（如中文分词等），现在的配置只有meta部分是有用的（当然IP最后都配置正确），如下：

meta.jdbc.driverClassName=oracle.jdbc.driver.OracleDriver
meta.jdbc.url=jdbc:oracle:thin:@192.168.200.66:1521:sun3500
meta.jdbc.username=TCM_BASIC
meta.jdbc.password=BASIC

meta是配置元目录树对应的本体的数据，主要用到以下几张表：
META_CATEGORY_1 一级元目录树内容
META_CATEGORY_3 二级元目录树内容
META_CATEGORY_3 三级元目录树内容
META_CATE_CLASSIFICATION 元目录树的分类信息

B、本体及数据资源和注册信息
这才是数据查询和建索引的主要配置。位于 WEB-INF/classes/etc
它的目录结构如下：
etc/DartCore/store/ontology  本体信息
etc/DartCore/store/resregistry 资源信息
etc/DartCore/store/semregistry 注册信息
注意，它的配置信息是与文件夹对应的，每个文件夹下可包含任意多，任意文件名的文件（只要扩展名正确），如
ontology下就可以有dart.rdfs， dart_expriment.rdfs， 还可以加入任意文件。

本体信息是由于protege来建立和修改的，不建议手工修改；
资源信息可手工配置，它的一个片段如下：
     <dbResInfo>
		<dbResQname>
			<namespace>http://www.zju.edu.cn/tcm_basic</namespace>
			<localpart>tcm_basic_db</localpart>
		</dbResQname>
		<localDbResType dbType="Oracle" canBeTempDb="false">
			<jdbcUrl>jdbc:oracle:thin:@192.168.200.66:1521:sun3500</jdbcUrl>
			<jdbcDriver>oracle.jdbc.driver.OracleDriver</jdbcDriver>
			<jdbcUser>tcm_basic</jdbcUser>
			<jdbcPwd>basic</jdbcPwd>
		</localDbResType>
		<tableNameFilter>
			<include>TCM_BASIC(.*)</include>
		</tableNameFilter>
	</dbResInfo>	

注册信息要用王恒的本体注册工具（在ftp上的wangheng下）完成。如果是简单改动也可以手工完成，但不建议使用，容易出错。


