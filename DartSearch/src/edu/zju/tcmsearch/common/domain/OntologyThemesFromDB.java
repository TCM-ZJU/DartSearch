package edu.zju.tcmsearch.common.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.zju.tcmsearch.dao.meta.IMetaCategoryObserver;
import edu.zju.tcmsearch.dao.meta.MetaCategoryDAO;
import edu.zju.tcmsearch.query.meta.MetaCategoryData;
import edu.zju.tcmsearch.util.dartcore.OntoUriParseUtil;

public class OntologyThemesFromDB implements IMetaCategoryObserver{
	private MetaCategoryDAO metaCategoryDAO;
	
	private List<OntologyTheme> rootOntoTheme;
	
	private boolean isUpToDate=false;
	
	private Map<String,OntologyTheme> ontoThemeMap;	
	

	/**
	 * 锟斤拷锟截碉拷一锟斤拷专锟斤拷
	 * @return
	 */
	public List<OntologyTheme> getOntoThemeList() {
		if(!isUpToDate || null==rootOntoTheme){
			rootOntoTheme = retrieveTheme();
		}
		return rootOntoTheme;
	}

	private List<OntologyTheme> retrieveTheme() {
		List<OntologyTheme> ontoThemeList = new ArrayList<OntologyTheme>();
		List<MetaCategoryData> category1 = metaCategoryDAO.getCategory1();
		for(MetaCategoryData cate1 : category1){
			OntologyTheme  ontoTheme = createTheme(cate1);
			ontoThemeList.add(ontoTheme);
			for(MetaCategoryData cate2 : cate1.getChildCategoryList()){
				ontoTheme.addChild(createTheme(cate2));
			}
		}
		return ontoThemeList;
	}

	private OntologyTheme createTheme(MetaCategoryData cate1) {
		OntologyTheme ontoTheme = new OntologyTheme();
		ontoTheme.setId(cate1.getId());
		ontoTheme.setName(cate1.getDname());
		ontoTheme.setOntoIdentityList(metaCategoryDAO.getClassifiedOntoURI(cate1.getLevel(),cate1.getId()));
		return ontoTheme;
	}

	
	public List<OntologyTheme> getOntoBelongToThemes(String ontoIdentity){
		List<OntologyTheme> belongToThemes = new ArrayList<OntologyTheme>();
		for(OntologyTheme ontoTheme: getOntoThemeList()) {
			/*
			 * 浠庢暟鎹簱涓鍑虹殑getOntoIdentityList()褰㈠http://dart.zju.edu.cn/dart#涓崏鑽�
			 * 鑰屼粠璇箟娉ㄥ唽璇诲嚭锛堝彲鑳界粡杩囨煇浜涜浆鎹級鐨刼ntoIdentity褰㈠http://dart.zju.edu.cn/dart__涓崏鑽�
			 * 鍥犱负#涓嶈兘鍦╤ttprequest閲岄潰浼狅紝鐢╛_浠ｆ浛
			 * 褰撲袱鑰呰繘琛屾瘮杈冩椂,搴旇鎶婂畠浠浆鍖栦负鐩稿悓鐨勫舰寮廜ntoUriParseUtil.getOntoUri(..)
			 */			
			if(ontoTheme.getOntoIdentityList().contains( OntoUriParseUtil.getOntoUri(ontoIdentity))){
				belongToThemes.add(ontoTheme);
			}
			for(OntologyTheme child : ontoTheme.getChild()){
				if(child.getOntoIdentityList().contains( OntoUriParseUtil.getOntoUri(ontoIdentity))){
					belongToThemes.add(child);
				}
			}
		}
		return belongToThemes;
	}
	
	public Map<String,OntologyTheme> getOntoThemeMap(){
		if (null!=ontoThemeMap){
			return ontoThemeMap;
		}
		ontoThemeMap=new HashMap<String,OntologyTheme>();
		for (OntologyTheme ontologyTheme: getOntoThemeList()){
			ontoThemeMap.put(ontologyTheme.getName(),ontologyTheme);
			for(OntologyTheme child : ontologyTheme.getChild()){
				ontoThemeMap.put(child.getName(),child);
			}
		}
		return ontoThemeMap;
	}
	
	
	public MetaCategoryDAO getMetaCategoryDAO() {
		return metaCategoryDAO;
	}

	public void setMetaCategoryDAO(MetaCategoryDAO metaCategoryDAO) {
		this.metaCategoryDAO = metaCategoryDAO;
		/*
		 * 锟斤拷锟皆硷拷锟斤拷为Observer锟斤拷拥锟絤etaCategoryDAO锟斤拷锟叫�?锟斤拷锟斤拷metaCategoryDAO锟斤拷锟斤拷锟斤拷MetaCategoryChange锟铰硷拷
		 */
		if(null!=this.metaCategoryDAO)
			this.metaCategoryDAO.addObserver(this);
	}

	/*
	 * 锟斤拷锟杰碉拷MetaCategoryChange锟铰硷拷,锟斤拷锟斤拷专锟斤拷锟叫憋拷 (non-Javadoc)
	 * @see edu.zju.tcmgrid2.dao.IMetaCategoryObserver#onMetaCategoryChange(edu.zju.tcmgrid2.dao.MetaCategoryDAO)
	 */
	public void  onMetaCategoryChange(MetaCategoryDAO mcDao){
		this.isUpToDate = false;	
	}
}
