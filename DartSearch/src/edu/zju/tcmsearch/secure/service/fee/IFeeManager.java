package edu.zju.tcmsearch.secure.service.fee;
import java.util.List;

import edu.zju.tcmsearch.common.domain.DartOntology;
import edu.zju.tcmsearch.secure.domain.fee.FeeRecord;
import edu.zju.tcmsearch.web.form.query.TreeNode;

public interface IFeeManager {

	public int getTableFee(String tableId);
	
	public int getOntologyFee(String ontoId);
	
	public int getOntologyFee(List<DartOntology> ontology);
	
	public int getTreeNodeFee(TreeNode node);
	
	public void log(FeeRecord fee);
	
	public List<FeeRecord> getFeeRecord(int id);
	
	public List<FeeRecord> getFeeRecord(int id,int type);
	
	public List<FeeRecord> getFeeRecord();	
	
}
