package edu.zju.tcmsearch.query.service;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import cn.edu.zju.dart.core.DartCoreConstants;
import cn.edu.zju.dart.core.engine.IEngine;
import cn.edu.zju.dart.core.engine.engine.MultithreadEngine;
import cn.edu.zju.dart.core.exceptions.DartCoreResourceReleaseException;
import cn.edu.zju.dart.core.exceptions.DartCoreResultInvalidException;
import cn.edu.zju.dart.core.exceptions.DartCoreRuntimeException;
import cn.edu.zju.dart.core.ontology.IOntologySchema;
import cn.edu.zju.dart.core.ontology.model.IOntoClass;
import cn.edu.zju.dart.core.query.SemanticQuery;
import cn.edu.zju.dart.core.query.SemanticQueryFactory;
import cn.edu.zju.dart.core.query.sqlplan.ISqlPlan;
import cn.edu.zju.dart.core.query.sqlplan.result.IQueryResult;
import cn.edu.zju.dart.core.semanticregistry.IDartSemanticRegistry;
import cn.edu.zju.dart.core.semanticregistry.IRelation2Ontology;
import cn.edu.zju.dart.core.utils.DartCoreSettings;
import cn.edu.zju.dart.core.utils.ModelImporter;
import cn.edu.zju.dart.core.utils.ModelParser;
import cn.edu.zju.dart.core.utils.ModelToString;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import edu.zju.tcmsearch.exception.query.TcmRuntimeException;
import edu.zju.tcmsearch.query.domain.DartQuery;
import edu.zju.tcmsearch.query.domain.QueryResult;
import edu.zju.tcmsearch.util.web.DartQueryUtil;

import cn.edu.zju.dart.core.query.util.ISemregSelector;

/*
 * 浙江大学网格实验室
 * @author 谢骋超 
 * 2005年
 */
/**
 * 模仿Spring的JdbcTemplate执行查询的类 由于资源释放上的问题,目前一般使用一下方法: 1. 设置closeResource =
 * true,openSessionInView=false。
 * 然后调用queryForList这样每次返回的数据提取出来后就立即关闭资源。适合数据量比较少的情况。
 * 或者直接调exectue，然后在回调函数里，自己把结果处理掉。 2.
 * 设置closeResource=false，openSessionInView=false。这样执行完后，它的资源都是开的。用户设法将这些
 * 保存在Session中，取下一页数据时再调出来。适合分页并且数据量大的数据。但这种方法有危险，目前未严格测试。 3.
 * 设置closeResource=false，openSessionInView=true。这样我们可以用queryForRrs直接返回QueryResult，
 * 而不必放入List。再把资源存入ThreadLocal，在这个request结束页面显示成功后，用Filter自动关闭资源。
 * 
 * @author xiecc
 * 
 */
public class QueryTemplate {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(QueryTemplate.class);

    
   
    
    public Object execute(DartQuery dartQuery,
            QueryResultExtractCallBack queryResultExtractCallBack,ISemregSelector relationSelector ) {
        // Model query = ModelImporter.getFromRdfsFile(queryFile);
        long preQueryStart = System.currentTimeMillis();
        SemanticQuery sq = SemanticQueryFactory.createQuery(dartQuery.getQuery(),dartQuery.getPrefix(),relationSelector);
        IQueryResult sqrs = null;
        IEngine engine = MultithreadEngine.getInstance();
// IEngine engine = SinglethreadEngine.getInstance();
        try {
            long queryStart = System.currentTimeMillis();
            sqrs = sq.execQueryActivity(engine);
            long queryEnd = System.currentTimeMillis();
            logger.info("PreQuery start at: " + preQueryStart + " and end at: " + queryStart);
            logger.info("Query start at: " + queryStart + " and end at: " + queryEnd);
            logger.info("PreQuery using " + (queryStart - preQueryStart)/1000.0 + " second.");
            logger.info("Query using " + (queryEnd - queryStart)/1000.0 + " second.");
            if(logger.isDebugEnabled())
                logger.debug("@@ Print query result -- ");
            return queryResultExtractCallBack.execute(sqrs);
// sqrs.logResultStruct();
// int count = 0;
//            
// while(sqrs.next()){
// count++;
// sqrs.getDataItem().logDataItem();
// }
// return count;
        } catch (Exception e) {
            logger.error("Semantic Query Error",e);
            throw new TcmRuntimeException("Semantic Query Error",e);
        }finally{
                if(sq != null)
                    sq.release(engine);
//                if(sqrs != null)
//                    sqrs.close();
//                if(dartQuery != null)
//                    dartQuery.closeQuery();
        }
    }


	/**
     * 核心的模板执行方法,用来执行数据查询的整个流程,其它类只要改写传进去的两个callback就行了
     * 
     * @param queryGenCallBack
     *            生成查询条件和语法的callback
     * @param queryResultExtractCallBack
     *            生成查询结果的callback
     * @return 查询结果对象,可以是List或其它东西
     */
// public Object execute(DartQuery dartQuery,
// QueryResultExtractCallBack queryResultExtractCallBack) {
// logger.debug("Query after preProces start.");
// IDartSemanticRegistry semreg = DartCoreSettings.getInstance()
// .getDartSemanticRegistry();
// IOntologySchema onto = DartCoreSettings.getInstance()
// .getDartOntoSchema();
// onto.logSchema();
// Model query=dartQuery.getQuery();
// String[] uris = ModelParser.parseOntoURI(query);
//        
// ArrayList<String> uriList = new ArrayList<String>();
// for(String uri : uris){
// uriList.add(uri);
// Model tempModel = ModelFactory.createDefaultModel();
// IOntoClass ontoClass = onto.getOntoClass(tempModel.createResource(uri));
// extendSubOntoUris(ontoClass,uriList);
// }
// uris = uriList.toArray(new String[uriList.size()]);
//        
// IRelation2Ontology[] r2os = semreg.getSemRegByOntologyURIs(uris);
// for(String uri : uris){
// IKeyProp[] keyProps = semreg.getKeyPropsByOntologyURI(uri);
// for(IKeyProp kp : keyProps){
// for(String uri2 : uris){
// if(uri2.equals(uri))
// continue;
// if(kp.getConcernedOntology().contains(uri2)){
// addQueryProp(query,uri,kp.getPropURI(),onto,dartQuery.getPrefix());
// }
// }
// }
// }
// if(logger.isDebugEnabled()){
// logger.debug("Query after preProcess:");
// logger.debug(ModelToString.getResultAsString(query,""));
// }
// SemanticQuery sq = new SemanticQuery(query, onto, r2os);
//
// ISqlPlan newPlan = null;
// IQueryResult sqrs = null;
// try {
// long preQueryStart = System.currentTimeMillis();
// newPlan = sq.getPrePerformedSqlPlan(ISqlPlan.OPTIMIZATION_HIGH);
//            
// long preQueryEnd = System.currentTimeMillis();
//            
// long queryStart = System.currentTimeMillis();
// sqrs = newPlan.executeQuery();
// long queryEnd = System.currentTimeMillis();
// logger.info("PreQuery start at: " + preQueryStart + " and end at: "
// + preQueryEnd);
// logger.info("PreQuery using " + (preQueryEnd - preQueryStart)
// / 1000.0 + " second.");
// logger.info("Query start at: " + queryStart + " and end at: "
// + queryEnd);
// logger.info("Query using " + (queryEnd - queryStart) / 1000.0
// + " second.");
// if (logger.isDebugEnabled())
// logger.debug("@@ Print query result -- ");
// Object result = queryResultExtractCallBack.execute(sqrs);
// return result;
// } catch (Exception e) {
// throw new DartCoreRuntimeException("Semantic Query Error", e);
// } finally {
// closeResourceIfNessessary(sqrs, newPlan, query);
// }
// }
// private int keyValueNodeIndex = 0;
// private int keyInstanceNodeIndex = 0;
// private void addQueryProp(Model query, String ontoURI, String propURI,
// IOntologySchema onto,String prefix) {
// Resource ontoType = query.createResource(ontoURI);
// ResIterator resIter =
// query.listSubjectsWithProperty(DartCoreConstants.RDF_TYPE,ontoType);
// Property keyProp = query.createProperty(propURI);
// Resource ontoRes = null;
// if(resIter.hasNext()){
// ontoRes = resIter.nextResource();
// StmtIterator stmtIter = ontoRes.listProperties(keyProp);
// if(stmtIter.hasNext()){
// if(logger.isDebugEnabled()){
// logger.debug("The KeyProp is already added!");
// }
// return;
// }
//                
// }else{
// ontoRes = query.createResource(prefix + "?key_var_ins_" +
// keyInstanceNodeIndex++);
// ontoRes.addProperty(DartCoreConstants.RDF_TYPE,ontoType);
// }
//        
// logger.info("Add Key Prop: " + ontoRes + " --> " + keyProp);
// ontoRes.addProperty(keyProp,createKeyValueNode(query,prefix));
// }
//    
// private Resource createKeyValueNode(Model query,String prefix){
// Resource vn = query.createResource(prefix + "?key_var_vn_" +
// keyValueNodeIndex++);
// vn.addProperty(DartCoreConstants.RDF_TYPE,DartCoreConstants.RDF_DART_VALUE_NODE);
// vn.addProperty(DartCoreConstants.RDF_DART_VALUE_NODE_IS_SELECTED,false);
// return vn;
// }



	


	
	public QueryResult queryForResult(DartQuery dartQuery,ISemregSelector relationSelector ){
		QueryResult queryResult=(QueryResult)execute(dartQuery,new CommonQueryResultExtractCallBack(dartQuery),relationSelector);
		return queryResult;
	}

    private class CommonQueryResultExtractCallBack implements QueryResultExtractCallBack{
        /**
         * Logger for this class
         */
        private final Logger logger = Logger
                .getLogger(CommonQueryResultExtractCallBack.class);

        private DartQuery dartQuery;

        public CommonQueryResultExtractCallBack(DartQuery dartQuery){
            this.dartQuery=dartQuery;
        }
        
		public Object execute(IQueryResult qrs) {
			QueryResult queryResult=new QueryResult();
			try {                
                queryResult.setDartQuery(dartQuery);
                queryResult.setQueryResult(qrs);            
                logger.debug("query rdf is \n"+ ModelToString.getResultAsString(dartQuery.getQuery(), null));

//				while (qrs.next()){
//					queryResult.addResultDataItem(qrs.getDataItem());
//				}
	
				/*
				 * 返回数据源@ming
				 */
				queryResult.setUsedRelation(qrs.getMetaData().getDataSources());
			} catch (DartCoreResultInvalidException e) {
				throw new TcmRuntimeException("execution error while iterator query result", e);
			} 
			return queryResult;
		}    	
    }


}

