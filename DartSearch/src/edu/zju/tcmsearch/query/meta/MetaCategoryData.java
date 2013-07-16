/*
 * Created on 2005-11-17
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.query.meta;

import java.util.List;

import org.springframework.util.Assert;

import edu.zju.tcmsearch.common.ApplicationContextHolder;
import edu.zju.tcmsearch.common.TcmConstants;
import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.util.web.CateNodeUtil;

public class MetaCategoryData {
	private Integer pid;

	private Integer id;

	private String dname;

	private Integer level;

	private List<MetaCategoryData> childCategoryList;

	// 这个数据的唯一标志,由level和它的id组成
	public String getCid() {
		return CateNodeUtil.generateCid(level, id);
	}

	/**
	 * @return Returns the dname.
	 */
	public String getDname() {
		return dname;
	}

	/**
	 * @param dname
	 *            The dname to set.
	 */
	public void setDname(String dname) {
		this.dname = dname;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the level.
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            The level to set.
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return Returns the pid.
	 */
	public Integer getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            The pid to set.
	 */
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	private MetaCategoryDAO getMetaCategoryDAO() {
		return (MetaCategoryDAO) ApplicationContextHolder.getContext().getBean(
				"metaCategoryDAO");
	}

	public List<MetaCategoryData> getChildCategoryList() {
		if (null != childCategoryList) {
			return childCategoryList;
		}
		return getMetaCategoryDAO().getCategoryByLevel(this.level + 1, this.id);
	}

	public boolean isLeaf() {
		return TcmConstants.isLeafCategory(this.level);
	}

	public MetaCategoryData getParentCategory() {
		if (level == 1) {
			return null;
		}
		int parentLevel = level - 1;

		MetaCategoryData parentMetaCategoryData = getMetaCategoryDAO()
				.getCategoryById(parentLevel, pid);
		// MetaCategoryData parentMetaCategoryData=CateNodeUtil.getNode(parentCid);
		Assert.notNull(parentMetaCategoryData);
		return parentMetaCategoryData;
	}

}
