/*
 * Created on 2005-11-24
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.util.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import edu.zju.tcmsearch.query.meta.MetaCategoryData;
import edu.zju.tcmsearch.web.form.query.TreeNode;

public class CateNodeUtil {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(CateNodeUtil.class);
    
    private static Map<String,MetaCategoryData> nodeMap=new HashMap<String,MetaCategoryData>();
    
    /**
     * @return Returns the nodeMap.
     */
    public static Map<String, MetaCategoryData> getNodeMap() {
        return nodeMap;
    }
    
    public static String generateCid(Integer level,Integer id){
        return  String.valueOf(level)+"_"+String.valueOf(id);
    }
    
    public static void putNode(MetaCategoryData metaCategoryData){
        Assert.notNull(metaCategoryData);
        Assert.notNull(metaCategoryData.getCid());
        
        if (null!=nodeMap.get(metaCategoryData.getCid())){
            return;
        }       
        nodeMap.put(metaCategoryData.getCid(),metaCategoryData);
    }
    
    public static MetaCategoryData getNode(String cid){
        Assert.notNull(cid);
        return nodeMap.get(cid);
    }
    
    public static MetaCategoryData getNode(Integer level,Integer id){
        return getNode(generateCid(level,id));
    }
    
    public static void setExpanding(MetaCategoryData metaCategoryData, boolean expanding,
            HttpSession session) {
        if (true == expanding) {
            addExpanding(metaCategoryData, session);
        } else if (false == expanding) {
            removeExpanding(metaCategoryData, session);
        }
    }


    public static void addExpanding(MetaCategoryData metaCategoryData, HttpSession session) {
        List<String> expandingNodes = getExpandingNodesFromSession(session);

        Assert.notNull(metaCategoryData);
        if (!isExpanding(metaCategoryData, session)) {
            expandingNodes.add(metaCategoryData.getCid());
        }
    }

    /**
     * true表示有东西被remove了
     * 
     * @param domainCode
     * @param session
     * @return
     */
    public static boolean removeExpanding(MetaCategoryData metaCategoryData,
            HttpSession session) {
        List<String> expandingNodes = getExpandingNodesFromSession(session);
        for (String cid:expandingNodes){
            if (cid.equals(metaCategoryData.getCid())) {
                expandingNodes.remove(cid);
                return true;
            }
        }
        return false;
    }

    public static boolean isExpanding(MetaCategoryData metaCategoryData, HttpSession session) {
        List<String> expandingNodes = getExpandingNodesFromSession(session);
        for (String nodeIdentity:expandingNodes){
            if (nodeIdentity.equals(metaCategoryData.getCid())) {
                return true;
            }
        }
        return false;
    }

    /**
     * expandingNodes中存放哪些node是需要展开的
     * 这个node中仅存放String类型的ID,从而使HttpSession中的内容减到最少
     * 
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<String> getExpandingNodesFromSession(HttpSession session) {
        List<String>  expandingNodes = (List<String> ) session.getAttribute("expandingCateNodes");
        if (null == expandingNodes) {
            expandingNodes = new ArrayList<String> ();
            session.setAttribute("expandingCateNodes", expandingNodes);
        }
        return expandingNodes;
    }
    
    public static void clearExpanding(HttpSession session){
        getExpandingNodesFromSession(session).clear();
    }


}
