/*
 * Created on 2005-12-28
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.splitter.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

import edu.zju.tcmsearch.exception.lucene.TcmLuceneException;

public class DictNode implements Serializable {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(DictNode.class);

    private int nodeId;
    /**
     * 
     */
    private static final long serialVersionUID = 6605791922243654331L;

    private String value;

    private int level;

    private DictNode parent;

    private boolean end;

    private int useCount = 0;

    private Map<String, DictNode> childNodes = new HashMap<String, DictNode>();

    public static DictNode EMPTY_NODE = new DictNode(0, "empty", null,0);

    /**
     * @return Returns the useCount.
     */
    public int getUseCount() {
        return useCount;
    }

    /**
     * @param useCount
     *            The useCount to set.
     */
    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public void incUseCount() {
        this.useCount++;
    }

    public DictNode(int level, String value, DictNode parent,int nodeId) {
        super();

        this.level = level;
        this.value = value;
        this.parent = parent;
        this.nodeId=nodeId;
    }

    /**
     * @return Returns the childNodes.
     */
    public Map<String, DictNode> getChildNodes() {
        return childNodes;
    }

    /**
     * @param childNodes
     *            The childNodes to set.
     */
    public void setChildNodes(Map<String, DictNode> childNodes) {
        this.childNodes = childNodes;
    }

    /**
     * @return Returns the level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level
     *            The level to set.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return Returns the parent.
     */
    public DictNode getParent() {
        return parent;
    }

    /**
     * @param parent
     *            The parent to set.
     */
    public void setParent(DictNode parent) {
        this.parent = parent;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    public void addChildNode(DictNode childNode) {
        this.childNodes.put(childNode.getValue(), childNode);
    }

    public DictNode getChildNode(String value) {
        return this.childNodes.get(value);
    }

    /**
     * @return Returns the end.
     */
    public boolean hasEnd() {
        return end;
    }

    /**
     * @param end
     *            The end to set.
     */
    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getTokenValue() {
        StringBuilder sb = new StringBuilder();
        DictNode curNode = this;
        while (curNode.level>0) {
            // logger.debug(curNode);
            sb.insert(0, curNode.getValue());
            curNode = curNode.getParent();
        }
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" nodeId: " + getNodeId() + ";");
        sb.append(" nodevalue: " + getValue() + ";");
        sb.append(" node level:" + getLevel() + ";");
        sb.append(" child node size: " + getChildNodes().size() + ";");
        sb.append(" token value:" + getTokenValue() + ";");
        sb.append(" hasEnd: " + hasEnd() + " \n");
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DictNode)) {
            throw new TcmLuceneException("要比较的对象不是DictNode类型!");
        }
        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        DictNode compareNode = (DictNode) obj;

        if (this.getNodeId() == compareNode.getNodeId()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 19).append(getNodeId()).toHashCode();
    }

    /**
     * @return Returns the nodeId.
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * @param nodeId The nodeId to set.
     */
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

}
