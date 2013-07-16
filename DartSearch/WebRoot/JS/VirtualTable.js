function VirtualTable(node,num)
{
 this.rows=null;
 this.initialized=false;
 this.orgTable=node;
 this.rowsInPage=num;
 this.beginIndex=0;
 this.preBeginIndex=-1;
 this.init=VTInit;
 this.showCurrentPage=VTShowPage;
 this.turnNextPage=VTTurnPage;
 this.submit=VTSubmit;
 this.saveCheckBoxState=saveCheckBoxState;
 this.promotion=VTPromotion;
 
 this.cacheArray=null;
}
/*
 remove all the rows of orgTable.tBodies[0] into new Array 
 */
function VTInit()
{
 this.initialized=true;
 this.rows=new Array();

 var bodyNode=this.orgTable.tBodies[0];
 for(var j=bodyNode.childNodes.length-1; j>=0; j--)
   if(j<this.rowsInPage)
    this.rows[j]=bodyNode.childNodes[j];
   else 
    this.rows[j]=bodyNode.removeChild(bodyNode.childNodes[j]);   
	
 this.rowsInPage = this.rowsInPage > this.rows.length ? this.rows.length:this.rowsInPage;
}

/*
 put rows between beginIndex and beginIndex+rowsInPage-1 into table 
 they will show up
 */
function VTShowPage()
{
 if(this.initialized==false) 
   return;

 if(this.preBeginIndex>=0)
    this.saveCheckBoxState();

 var bodyNode=this.orgTable.tBodies[0];
 while(bodyNode.childNodes.length>0)
	bodyNode.removeChild(bodyNode.childNodes[0]);
 for(var i=0; i<this.rowsInPage; i++)
    bodyNode.appendChild(this.rows[i+this.beginIndex]);
}
/*
 there is much trouble dealing with checkbox: when checkbox is removed from talbe , it will lose its checked status (I do not know why )
 my resolution: before removing checkbox , set its defaultChecked = checked , and then romve it
 this is a not_so_good method , but it just works.
 */
function saveCheckBoxState()
{
 var bodyNode = this.orgTable.tBodies[0];
 if(this.cacheArray==null)
 {
   /*
    * cacheArray is used hold positions in row which is checkbox
	* when row structure changed , set cacheArray = null to record new positions
    */
   this.cacheArray=new Array();	
   var firstRow = bodyNode.childNodes[0];
   for(var i=0; i<firstRow.all.length;i++)
     if(firstRow.all[i].type=="checkbox")
	    this.cacheArray[this.cacheArray.length]=i;
 }
 
 if(this.cacheArray.length>0)
    for(var r=0; r<bodyNode.childNodes.length;r++)
    {
     var row = bodyNode.childNodes[r];
     for(var i=0; i<this.cacheArray.length; i++) 
	    row.all[this.cacheArray[i]].defaultChecked = row.all[this.cacheArray[i]].checked;
    } 
}

function VTTurnPage()
{
 this.preBeginIndex = this.beginIndex;
 this.beginIndex = this.beginIndex + this.rowsInPage >= this.rows.length ?  0:this.beginIndex + this.rowsInPage;
 this.beginIndex = this.beginIndex + this.rowsInPage >= this.rows.length ?  this.rows.length-this.rowsInPage:this.beginIndex;
}

/*
 submit VirtualTable , remember some rows are out of form and needed to be put back 
 */
function VTSubmit(formNode,doNotWait)
{
 if(this.initialized==false) 
   return;
   
 this.saveCheckBoxState();
 
 var bodyNode = this.orgTable.tBodies[0];

 var PVSet = new Array();
 /*
  when fecthing values of element_nodes in each row, I ignore the rows which are currently in the form 
  only in this way , can I avoid submitting those rows trwice 
  */
 var i;
 for(i=0;i<this.beginIndex;i++)
    getValueParameters(this.rows[i],PVSet);
 for(i=this.beginIndex+this.rowsInPage;i<this.rows.length;i++)
	getValueParameters(this.rows[i],PVSet);
 putValueParameters(formNode,PVSet);
 
 if(doNotWait==true)
    formNode.submit();
}

/*
  VTPromotion call func on element_nodes which are in the same colum
  the element_node on firstRow is given by name , so I could find this element_node's position
 */
function VTPromotion(name, func)
{
 var colum=-1;;
 var firstRow=this.rows[0];
 for(var i=0;i<firstRow.all.length;i++)
   if(typeof(firstRow.all[i].name)=="string" && firstRow.all[i].name.indexOf(name)>=0)
   {
      colum=i;
	  break;
   }
 
 if(colum==-1)
   return;
 
 for(var r=0; r<this.rows.length; r++)
   func(this.rows[r].all[colum]);
}

function NameValuePair(n,v)
{
 this.name=n;
 this.value=v;
}

/*
 fetch value/name pairs from HTML node , including its childNodes
 */
function getValueParameters(node,paraValueArray)
{
 //Do not support Radio && Multiple SELECT List
 /*
  @VirtualTable Version 1.1 
  IE  will ignore  unchecked checkbox , without name without value
  */
 if(node.nodeName=="INPUT" && node.type=="checkbox" && node.checked==false)
   {/*As IE Doing */}
 else if(node.nodeName=="INPUT" || node.nodeName=="SELECT" || node.nodeName=="TEXTAREA")
   paraValueArray[paraValueArray.length]=new NameValuePair(node.name,node.value);
 
 for(var i=0;i<node.childNodes.length;i++)
   getValueParameters(node.childNodes[i],paraValueArray);
}

/*
 put value/name pairs as hidden element into form
 */
function putValueParameters(formNode,paraValueArray)
{
 for(var i=0; i<paraValueArray.length;i++)
 {
   var el = document.createElement("INPUT");
   el.type="hidden";
   el.id=paraValueArray[i].name;
   el.name=paraValueArray[i].name;
   el.value=paraValueArray[i].value;   
   formNode.appendChild(el);
 } 
}

