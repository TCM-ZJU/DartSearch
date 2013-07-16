/*
 * Returns a new XMLHttpRequest object, or false if this browser
 * doesn't support it
 */
 var lastVmlIndex=1;
 var currentVmlIndex=1;
function newXMLHttpRequest() {
	var xmlreq = false;
	if (window.XMLHttpRequest) {
		xmlreq = new XMLHttpRequest();
	} else {
		if (window.ActiveXObject) {
			try {
				xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
			}
			catch (e1) {
				try {
					xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
				}
				catch (e2) {
					alert("get error while creating XMLHttpRequest");
				}
			}
		}
	}
	return xmlreq;
}
//XmltHttpStatus  handler
function getReadyStateHandler(req, responseXmlHandler) {
	return function () {
		if (req.readyState == 4) {
			if (req.status == 200) {			
				responseXmlHandler(req.responseXML);
			} else {
				alert("HTTP error: " + req.statusText);
			}
		}
	};
}


//Detail: ajax 
function GetDetailInfo(tableIdentity,primaryKey,Count) { 
	
	lastVmlIndex=currentVmlIndex;
	currentVmlIndex=Count;
	var req = newXMLHttpRequest();
	var handlerFunction = getReadyStateHandler(req, SetDetailInfo);
	req.onreadystatechange = handlerFunction;
	req.open("POST", "intemDetail.luc", true);
	req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");	
	req.send("tableIdentity=" +tableIdentity+ "&primaryKey="+primaryKey);
}




/*
<?xml version="1.0" encoding="UTF-8"?>
<root tableName="TCM.JMZ_LCYJ" ontologyName="临床研究">
<item propertyName="年龄分组" propertyValue="儿童，学龄前（2-5）$儿童（6-12）$青年人（13-18）$成年人（19-44）$中年人（45-64）$老年人（65-79）"/>
<item propertyName="传染源" propertyValue=""/>
<item propertyName="临床研究分类" propertyValue="临床专题"/>
<item propertyName="病例采集时间" propertyValue="1996-1999"/>
<item propertyName="观察总数量" propertyValue="14"/>
<item propertyName="男性观察数量" propertyValue="8"/>
<item propertyName="传染途径" propertyValue=""/>
<item propertyName="临床研究名称" propertyValue="鲜芦荟外敷治烫伤14例"/>
<item propertyName="临床研究单位" propertyValue="昆明云南民族学院卫生科 650031"/>
<item propertyName="临床研究分组" propertyValue=""/>
<item propertyName="临床研究结果" propertyValue="明代李时珍记载“芦荟乃厥阴经药也。其功专于杀虫清热。”现代研究证明,鲜芦荟含有羟基4蒽醌类物质,对革兰氏阳性菌有较强的抑制作用,并可杀灭霉菌。用芦荟敷于烧伤表面,可在创面形成保护, 达到防止感染,消肿止痛的目的。本法治疗小面积轻度烧伤,具有效果确切、经济实惠、简便易行的优点,尤其适于家庭使用。"/>
<item propertyName="男性观察数量" propertyValue="6"/>
<item propertyName="病原体" propertyValue=""/>
<item propertyName="病例采集地区" propertyValue=""/>
<item propertyName="临床研究编号" propertyValue="30119"/></root>

*/


function SetDetailInfo(detailXml) 
{
			
			//valueNode arraylist, the last item in this arraylist is the tableName and ontologeName,
			//which is the central node of the vml graph; 
			
			var b = detailXml.getElementsByTagName("item");	
			var nodeArray = new Array();
			var linkArray = new Array();
				
			var nodecount=0;
			for(var i=0;i<b.length;i++)
			{	
				if(b[i].attributes[1].value.length!=0)	{							
			  		linkArray[nodecount]=b[i].attributes[0].value;
			  		nodeArray[nodecount]=b[i].attributes[1].value;
			  		nodecount++;		
			  	}			  
			}
			
			var a = detailXml.getElementsByTagName("root");		
			nodeArray[nodecount]=a[0].attributes[1].value+"<div>"+a[0].attributes[0].value+"</div>";
			
		
			var dstNode="detailInfo"+currentVmlIndex;		
			if (hNode != eval(dstNode)) 
			{
				if (hNode != null) clearBackGround();
				createBackGround(eval(dstNode));
				createContent(nodecount, nodeArray, linkArray);
			}
			else
			{
				clearBackGround();
			}
}

