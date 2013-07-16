/*
 * Created on 2005-12-21
 * author 谢骋超
 * 
 */
package edu.zju.tcmsearch.lucene.index;

import edu.zju.tcmsearch.lucene.index.data.DbRecData;

public interface IndexBuildCallBack {
   public void execute(int indexCount,DbRecData dbRecData);
}
