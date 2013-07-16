/*
 * Created on 2005-12-16
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index.data;

import javax.xml.namespace.QName;


public class DbResInfo {
    private String dbNamespace;
    private String dbLocalpart;
    
    private String jdbcUrl;
    private String jdbcDriver;
    private String jdbcUser;
    private String jdbcPwd;
    /**
     * @return Returns the dbLocalpart.
     */
    public String getDbLocalpart() {
        return dbLocalpart;
    }
    /**
     * @param dbLocalpart The dbLocalpart to set.
     */
    public void setDbLocalpart(String dbLocalpart) {
        this.dbLocalpart = dbLocalpart;
    }
    /**
     * @return Returns the dbNamespace.
     */
    public String getDbNamespace() {
        return dbNamespace;
    }
    /**
     * @param dbNamespace The dbNamespace to set.
     */
    public void setDbNamespace(String dbNamespace) {
        this.dbNamespace = dbNamespace;
    }
    /**
     * @return Returns the jdbcDriver.
     */
    public String getJdbcDriver() {
        return jdbcDriver;
    }
    /**
     * @param jdbcDriver The jdbcDriver to set.
     */
    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }
    /**
     * @return Returns the jdbcPwd.
     */
    public String getJdbcPwd() {
        return jdbcPwd;
    }
    /**
     * @param jdbcPwd The jdbcPwd to set.
     */
    public void setJdbcPwd(String jdbcPwd) {
        this.jdbcPwd = jdbcPwd;
    }
    /**
     * @return Returns the jdbcUrl.
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }
    /**
     * @param jdbcUrl The jdbcUrl to set.
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
    /**
     * @return Returns the jdbcUser.
     */
    public String getJdbcUser() {
        return jdbcUser;
    }
    /**
     * @param jdbcUser The jdbcUser to set.
     */
    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }
    
    public String getOntologyURI(){
        return dbNamespace+"/"+dbLocalpart;
    }
    
    public QName getQName(){
        QName qName=new QName(dbNamespace,dbLocalpart);
        return qName;
    }
}
