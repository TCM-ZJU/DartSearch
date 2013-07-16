/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import cn.edu.zju.dart.core.resregistry.IDartResRegistry;
import cn.edu.zju.dart.core.resregistry.model.IDbRes;
import cn.edu.zju.dart.core.resregistry.model.IRelation;
import cn.edu.zju.dart.core.resregistry.model.IRelationColumn;
import cn.edu.zju.dart.core.resregistry.model.IRelationSpace;
import cn.edu.zju.dart.core.resregistry.model.impl.JdbcDbRes;
import cn.edu.zju.dart.core.semanticregistry.IDartSemanticRegistry;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import cn.edu.zju.dart.core.semanticregistry.model.Constraint;
import cn.edu.zju.dart.core.utils.DartCoreSettings;
import edu.zju.tcmsearch.lucene.index.DbInfoRetriever;
import edu.zju.tcmsearch.lucene.index.data.ColumnInfo;
import edu.zju.tcmsearch.lucene.index.data.DbResInfo;
import edu.zju.tcmsearch.lucene.index.data.TableInfo;

public class DbInfoRetrieverImpl implements DbInfoRetriever {
    private  List<String> ontoUriList;
    private List<TableInfo> tableInfoHashList;

    /*
     * (non-Javadoc)
     * 调用dartgrid内核的数据资源注册工具类得到注册的数据源dbreses,返回其QName List
     * @see edu.zju.tcmgrid2.lucene.index.DbInfoRetriever#getDbQNames()
     */
    public List<QName> getDbQNames() {
        List<QName> qnames = new LinkedList<QName>();
        //下面两行代码将调用dartgrid内核的数据资源注册工具类得到注册的数据源dbreses
        IDartResRegistry resreg = DartCoreSettings.getInstance()
                .getDartResRegistry();
        IDbRes[] dbreses = resreg.getAllDbRes();
        for (IDbRes dbres : dbreses) {
            qnames.add(dbres.getDbResQName());
        }
        return qnames;
    }

    /*
     * (non-Javadoc)
     * 通过QName得到特定的数据资源对象dbres,并构建DbResInfo
     * @see edu.zju.tcmgrid2.lucene.index.DbInfoRetriever#getDbRes(java.lang.String)
     */
    public DbResInfo getDbRes(QName dbQName) {
    	//下面两行代码将调用dartgrid内核的数据资源注册工具类得到注册的QName数据源dbres
        IDartResRegistry resreg = DartCoreSettings.getInstance()
                .getDartResRegistry();
        IDbRes dbres = resreg.getDbRes(dbQName);
        if (dbres == null || !(dbres instanceof JdbcDbRes)) {
            // TODO service dbres -- read from prop file
        }
        DbResInfo resInfo = new DbResInfo();
        JdbcDbRes jdbcres = (JdbcDbRes) dbres;

        resInfo.setDbNamespace(dbQName.getNamespaceURI());
        resInfo.setDbLocalpart(dbQName.getLocalPart());
        resInfo.setJdbcDriver(jdbcres.getDriverClass());
        resInfo.setJdbcUrl(jdbcres.getJdbcUrl());
        resInfo.setJdbcUser(jdbcres.getUser());
        resInfo.setJdbcPwd(jdbcres.getPwd());
        return resInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.zju.tcmgrid2.lucene.index.DbInfoRetriever#getOntoUriList()
     */
    public Set<String> getOntoUris(QName dbQName) {
        Set<String> ontoUriList = new HashSet<String>();
        IDartResRegistry resreg = DartCoreSettings.getInstance()
                .getDartResRegistry();
        IDartSemanticRegistry semreg = DartCoreSettings.getInstance()
                .getDartSemanticRegistry();

        IDbRes dbres = resreg.getDbRes(dbQName);
        for (IRelationSpace rs : dbres.getRelationSpaces()) {
            IRelation[] rr = rs.getRelations();
            String[] rids = new String[rr.length];
            for (int i = 0; i < rr.length; i++) {
                rids[i] = rr[i].getUnifiedID();
            }
            IRelation2Ontology[] r2os = semreg.getSemRegByRelationIDs(rids);
            for (IRelation2Ontology r2o : r2os) {
                Collections.addAll(ontoUriList, r2o.getOntologyURIs());
            }
        }
        return ontoUriList;
    }

    public List<TableInfo> getTableInfoList(QName dbQName) {
        List<TableInfo> tableInfoList = new LinkedList<TableInfo>();
        
        //下面两行代码将调用dartgrid内核的数据资源注册工具类得到注册的QName数据源dbres
        IDartResRegistry resreg = DartCoreSettings.getInstance()
                .getDartResRegistry();
        IDartSemanticRegistry semreg = DartCoreSettings.getInstance()
                .getDartSemanticRegistry();

        IDbRes dbres = resreg.getDbRes(dbQName);
        //对数据库资源dbres中的每一个RelationSpaces进行分析，得到其中的TableInfo信息，及每个tableInfo中的
        //ColumnInfo信息
        for (IRelationSpace rs : dbres.getRelationSpaces()) {
            IRelation[] rr = rs.getRelations();
            String[] rids = new String[rr.length];
            for (int i = 0; i < rr.length; i++) {
                rids[i] = rr[i].getUnifiedID();
            }
            IRelation2Ontology[] r2os = semreg.getSemRegByRelationIDs(rids);
            for (IRelation2Ontology r2o : r2os) {
                TableInfo ti = new TableInfo();
                ti.setInFullIndex(r2o.isInFullIndex());
                ti.setTableSchema(rs.getRelationSpaceName());
                ti.setDbNamespace(dbQName.getNamespaceURI());
                ti.setDbLocalpart(dbQName.getLocalPart());
                ti.setTableName(r2o.getRelation().getRelationName());
                //下面几行代码跟内核的本体有关，主要是辅助构建索引，但对索引的建立方法基本上没什么影响，可以不看
                String[] ontoURIs = r2o.getOntologyURIs();
                List<String> mainOntoWithoutCons = new ArrayList<String>();
                List<String> sndOntoWithoutCons = new ArrayList<String>();
                Map<String,String> mainOntoWithCons = new HashMap<String,String>();
                Map<String,String> sndOntoWithCons = new HashMap<String,String>();
                IRelation r = r2o.getRelation();
                
                for (String ou : ontoURIs) {
                	Constraint c = r2o.getConstraint(ou); 
                	if(c!= null){
                		if(r2o.isMainOnto(ou))
                			mainOntoWithCons.put(ou,c.asString(r));
                		else
                			sndOntoWithCons.put(ou,c.asString(r));
                	}else{
	                    if (r2o.isMainOnto(ou))
	                        mainOntoWithoutCons.add(ou);
	                    else
	                        sndOntoWithoutCons.add(ou);
                	}
                }
                ti.setMainOntoWithoutCons(mainOntoWithoutCons);
                ti.setMainOntoWithCons(mainOntoWithCons);
                ti.setSecondaryOntoWithoutCons(sndOntoWithoutCons);
                ti.setSecondaryOntoWithCons(sndOntoWithCons);
                
                List<ColumnInfo> columns = new LinkedList<ColumnInfo>();
                //下面代码对每个Relation分析RelationColumn信息
                for (IRelationColumn rc : r2o.getRelation().getColumns()) {
                    if (null==r2o.getOntoUriByColName(rc
                            .getColumnName())){
                        continue;
                    }
                    ColumnInfo ci = new ColumnInfo();
                    ci.setTableColumn(rc.getColumnName());
                    ci.setColumnType(rc.getSqlType());
                    ci.setOntologyURI(r2o.getOntoUriByColName(rc
                            .getColumnName()));
                    ci.setPrimaryKey(r.getPrimaryKeys().contains(
                            rc.getColumnName()));
                    columns.add(ci);
                }
                ti.setColumnList(columns);
                tableInfoList.add(ti);
            }
        }
        return tableInfoList;
    }

    public List<TableInfo> getAllTableInfos() {
        if (null!=tableInfoHashList){
            return tableInfoHashList;
        }
        tableInfoHashList = new ArrayList<TableInfo>();
        for (QName dbQName : getDbQNames())
            tableInfoHashList.addAll(getTableInfoList(dbQName));
        return tableInfoHashList;
    }

    public List<String> getAllOntoUris() {
        if (null!=ontoUriList){
            return ontoUriList;
        }
        Set<String> ontoUris = new TreeSet<String>();
        for (QName dbQName : getDbQNames()) {
            Set<String> qNameOntoUris=getOntoUris(dbQName);
            ontoUris.addAll(qNameOntoUris);
            qNameOntoUris=null;
        }
        ontoUriList = new ArrayList<String>();
        ontoUriList.addAll(ontoUris);
        ontoUris=null;
        return ontoUriList; 
        
             
    }
}
