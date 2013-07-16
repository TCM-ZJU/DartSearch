/*
 * Created on 2005-11-17
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.dao.impl.meta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.util.Assert;

import edu.zju.tcmsearch.common.TcmConstants;
import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.dao.meta.IMetaCategoryObserver;
import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;


public class MetaCategoryDAOImpl implements MetaCategoryDAO{
	private static Logger logger= Logger.getLogger(MetaCategoryDAOImpl.class);
	
	private Set<IMetaCategoryObserver> mcObserver= new HashSet<IMetaCategoryObserver>();
    private DataSource dataSource; 

    /**
     * @return Returns the dataSource.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource The dataSource to set.
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MetaCategoryData> getCategory1() {
        return getCategoryByLevel(1,null);
    }

    public List<MetaCategoryData> getCategory2(int pid) {
        return getCategoryByLevel(2,pid);
    }

    public List<MetaCategoryData> getCategory3(int pid) {
        return getCategoryByLevel(3,pid);
    }


    
    public List<MetaCategoryData> getCategoryByLevel(Integer level,Integer pid) {
        if (level>3 || level<1){
            return new ArrayList<MetaCategoryData>();
        }
        MetaCategoryMappingQuery metaCategoryMappingQuery=new MetaCategoryMappingQuery(getDataSource(),level,pid);
        return metaCategoryMappingQuery.execute();
    }
    
    private String generateParentSQL(Integer level,Integer pid){
        String queryFieldStr="ID,DNAME";
        if (level>1){
            queryFieldStr+=",PID";
        }

        String  queryStr= "select "+queryFieldStr+" from META_CATEGORY_"+level;
        if (pid!=null){
            queryStr+=" where PID="+pid;
        }
        return queryStr;
    }
    
    private class MetaCategoryMappingQuery extends MappingSqlQuery {
        private Integer level;
        public MetaCategoryMappingQuery(DataSource ds,Integer level,Integer pid) {
            super(ds, generateParentSQL(level,pid));
            //super.declareParameter(new SqlParameter("PID", Types.INTEGER));
            this.level=level;
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            MetaCategoryData mtData=new MetaCategoryData();
            mtData.setLevel(this.level);
            mtData.setDname(rs.getString("DNAME"));
            mtData.setId(rs.getInt("ID"));
            if (level>1){
              mtData.setPid(rs.getInt("PID"));
            }
            return mtData;
        }
    }

    public List<String> getClassifiedOntoURI(int level, int id) {
        CategoryClassifyMappingQuery categoryClassifyMappingQuery=new CategoryClassifyMappingQuery(getDataSource(),level,id);
        return categoryClassifyMappingQuery.execute();
    }

    private String getClassifyQuery(Integer level, Integer id){
        String fieldname="CATEGORY"+level;
        String queryStr="select ONTOURI,ONTONAME from meta_cate_classification where "+fieldname+"="+id;
        return queryStr;
    }
    
    private class CategoryClassifyMappingQuery extends MappingSqlQuery {
        public CategoryClassifyMappingQuery(DataSource ds,Integer level,Integer cid) {
            super(ds, getClassifyQuery(level,cid));
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            String ontoURI=rs.getString("ONTOURI");
            
            return ontoURI;
        }
    }


    public int addClassification(MetaCategoryData metaCategoryData, DartOntology dartOntology) {
        
        Assert.isTrue(metaCategoryData.getLevel()==TcmConstants.LEAF_CategoryLevel);
        String sql="insert into meta_cate_classification (ID,Category1,Category2,ontoURI,ontoName) values("+getNextID()+ ",?,?,?,?)";
        SqlUpdate sqlUpdate = new SqlUpdate();
        sqlUpdate.setDataSource(getDataSource() );
        sqlUpdate.setSql(sql);
        sqlUpdate.declareParameter(
          new SqlParameter("Category1", Types.INTEGER));
        sqlUpdate.declareParameter(new SqlParameter("Category2", Types.INTEGER));
        //sqlUpdate.declareParameter(new SqlParameter("Category3", Types.INTEGER));
        sqlUpdate.declareParameter(new SqlParameter("ontoURI", Types.VARCHAR));
        sqlUpdate.declareParameter(new SqlParameter("ontoName", Types.VARCHAR));
        sqlUpdate.compile();
        Object[] parameters = new Object[4];
        Integer category2=metaCategoryData.getId();
        Integer category1=metaCategoryData.getPid();
        //Integer category1=metaCategoryData.getParentCategory().getPid();
        parameters[0]=category1;
        parameters[1]=category2;
        //parameters[2]=category3;
        parameters[2]=dartOntology.getURI();
        parameters[3]=dartOntology.getName();                            
                      
        int count = sqlUpdate.update(parameters); 
        publishChangeEvent();
        return count;        
    }

    public int removeClassification(MetaCategoryData metaCategoryData,DartOntology dartOntology) {
        
        Assert.isTrue(metaCategoryData.getLevel()==TcmConstants.LEAF_CategoryLevel);
        String sql="delete from meta_cate_classification where Category1=? and Category2=? and ontoURI=?";
        SqlUpdate sqlUpdate = new SqlUpdate();
        sqlUpdate.setDataSource(getDataSource());
        sqlUpdate.setSql(sql);
        sqlUpdate.declareParameter(
          new SqlParameter("Category1", Types.INTEGER));
        sqlUpdate.declareParameter(new SqlParameter("Category2", Types.INTEGER));
        //sqlUpdate.declareParameter(new SqlParameter("Category3", Types.INTEGER));
        sqlUpdate.declareParameter(new SqlParameter("ontoURI", Types.VARCHAR));
        sqlUpdate.compile();
        Object[] parameters = new Object[3];
        Integer category2=metaCategoryData.getId();
        Integer category1=metaCategoryData.getPid();
       // Integer category1=metaCategoryData.getParentCategory().getPid();
        parameters[0]=category1;
        parameters[1]=category2;
        //parameters[2]=category3;
        parameters[2]=dartOntology.getURI();
        int count = sqlUpdate.update(parameters); 
        
        publishChangeEvent();
        return count;
    }

    public boolean existClassification(MetaCategoryData metaCategoryData, DartOntology dartOntology) {
        ExistClassifyMappingQuery existClassifyMappingQuery=new ExistClassifyMappingQuery(getDataSource());
        Object[] parameters = new Object[3];
        Integer category2=metaCategoryData.getId();
        Integer category1=metaCategoryData.getPid();
        //Integer category1=metaCategoryData.getParentCategory().getPid();
        parameters[0]=category1;
        parameters[1]=category2;
        //parameters[2]=category3;
        parameters[2]=dartOntology.getURI();    
        List result= existClassifyMappingQuery.execute(parameters);
        Integer count= (Integer)result.get(0);
        return count>0;
    }

    
    private class ExistClassifyMappingQuery extends MappingSqlQuery {
        public ExistClassifyMappingQuery(DataSource ds) {
            super(ds, "select count(*) counts from meta_cate_classification where Category1=? and Category2=? and ontoURI=?");
            declareParameter(
                    new SqlParameter("Category1", Types.INTEGER));
            declareParameter(new SqlParameter("Category2", Types.INTEGER));
            //declareParameter(new SqlParameter("Category3", Types.INTEGER));
            declareParameter(new SqlParameter("ontoURI", Types.VARCHAR));
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return rs.getInt("counts");       
      
        }
    }
    
    private String generateThisLevelSQL(Integer level,Integer id){
        String queryFieldStr="ID,DNAME";
        if (level>1){
            queryFieldStr+=",PID";
        }

        String  queryStr= "select "+queryFieldStr+" from META_CATEGORY_"+level+ " where ID="+id;
        return queryStr;
    }
    
    private class ThisLevelIdMappingQuery extends MappingSqlQuery {
        private Integer level;
        public ThisLevelIdMappingQuery(DataSource ds,Integer level,Integer id) {
            super(ds, generateThisLevelSQL(level,id));
            this.level=level;
            compile();
        }

        public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
            MetaCategoryData mtData=new MetaCategoryData();
            mtData.setLevel(this.level);
            mtData.setDname(rs.getString("DNAME"));
            mtData.setId(rs.getInt("ID"));
            if (level>1){
              mtData.setPid(rs.getInt("PID"));
            }
            return mtData;
        }
    }

    public MetaCategoryData getCategoryById(int level, int id) {
        ThisLevelIdMappingQuery thisLevelIdMappingQuery=new ThisLevelIdMappingQuery(getDataSource(),level,id);
        
        List resultList=thisLevelIdMappingQuery.execute();
        return (MetaCategoryData)resultList.get(0);
    }
    /**
     * 支持2层分类结构
     * 删除父节点将同时删除下层子节点和叶子节点
     */
    private String[] SqlRemoveCategory(int level,int id){
    	String[] sql;
    	if(level==1){
    		sql= new String[3];
    		sql[0]="delete from META_CATEGORY_1 where ID="+id;
    		sql[1]="delete from META_CATEGORY_2 where PID="+id;
    		sql[2]="delete from meta_cate_classification where Category1="+id;
    	}else if(level==2){
    		sql= new String[2];
    		sql[0]="delete from META_CATEGORY_2 where ID="+id;
    		sql[1]="delete from meta_cate_classification where Category2="+id;    		
    	}else{
    		sql=new String[0];
    	}
    	return sql;
    }
    /**
     * 支持2层分类结构
     */
    public boolean removeCategory(int level,int id){
    	if(level<1||level>2||id<0){
    		return false;
    	}
   	
    	String[] sqlSet= SqlRemoveCategory(level,id);
    	publishChangeEvent();
    	return sqlExecute(sqlSet);
    }

	private boolean sqlExecute(String[] sqlSet) {
		try{
          Connection con=this.dataSource.getConnection();
          Statement stmt=con.createStatement();
          for(String sql:sqlSet){
        	logger.debug(sql);
          	stmt.execute(sql);        	  
          }
          con.close();
    	}catch(SQLException e){
    		logger.debug(e.toString());
    		return false;
    	}
    	return true;
	}
    
    private String SqlAddCategory(int level,int pid,String dname){
    	String sql;
    	if(level==1){
    		sql="insert into META_CATEGORY_1 (ID,dname) values("+getNextID()+",'"+dname+"')";
    	}else if(level==2){
    		sql="insert into META_CATEGORY_2 (PID,ID,dname) values("+pid+","+getNextID()+",'"+dname+"')";
    	}else{
    		sql=null;
    	}
    	return sql;
    }
    public boolean addCategory(int level,int pid,String dname){
    	if(level<1||level>2||null==dname){
    		return false;
    	}
    	String[] sqlSet=new String[1];
    	sqlSet[0]=SqlAddCategory(level,pid,dname);
    	publishChangeEvent();
    	return sqlExecute(sqlSet);
    }
    
    private String SqlRenameCategory(int level,int id,String dname){
    	String sql;
    	if(level==1){
    		sql="update meta_category_1 set DNAME='"+dname+"' where id="+id;
    	}else if(level==2){
    		sql="update meta_category_2 set DNAME='"+dname+"' where id="+id;
    	}else{
    		sql="";
    	}
    	return sql;
    }
    public boolean renameCategory(int level,int id,String dname){
    	String[] sqlSet=new String[1];
    	sqlSet[0]=SqlRenameCategory(level,id,dname);  
    	publishChangeEvent();
    	return sqlExecute(sqlSet);
    }
    
    public void addObserver(IMetaCategoryObserver mcObserver){
    	this.mcObserver.add(mcObserver);
    }
    
    synchronized protected int getNextID(){
		try{
	    	   int id=0;
	    	   String sqlMaxID = "select max(id)  as MaxId from  Category_ID_Table";			
	          Connection con=this.dataSource.getConnection();
	          Statement stmt=con.createStatement();
              ResultSet result = stmt.executeQuery(sqlMaxID);   
              result.absolute(1);
              id = result.getInt("MaxId")+1;
              stmt.executeUpdate("delete from Category_ID_Table");
              stmt.executeUpdate("insert into Category_ID_Table(id) values("+id+")");
	          con.close();
	          return id;
	    	}catch(SQLException e){
	    		logger.debug(e.toString());
                return 0;
	    	}    	
    }
    /*
     * 向所有Observer发布MetaCategoryChange事件
     */
    private void publishChangeEvent(){
    	logger.debug("Publish 'MetaCategoryChangeEvent' to Observer(s)");
    	for(IMetaCategoryObserver observer : this.mcObserver ){
    		observer.onMetaCategoryChange(this);
    	}
    }
}
