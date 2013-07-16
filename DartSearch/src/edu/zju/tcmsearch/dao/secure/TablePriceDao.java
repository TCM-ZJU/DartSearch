package edu.zju.tcmsearch.dao.secure;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.object.SqlUpdate;

import org.apache.log4j.Logger;

import edu.zju.tcmsearch.secure.domain.access.TableAccessPrivilege;
import edu.zju.tcmsearch.secure.domain.fee.TablePrice;

public class TablePriceDao {
	private DataSource dataSource;
	
	public List<TablePrice> getAllTablePrice(){
		return new TablePriceMappingQuery(dataSource).execute();
	}

	public void update(TablePrice[] sets){
		  new SqlUpdate(dataSource,"delete from tablePrice").update();	
		  for(TablePrice price:sets){
			  String sql="insert into tablePrice(tableId,freeCharge,price) values";
			  String values="('"+price.getTableId()+"',"+(price.getFreeCharge().equals(",true")? 1:0)+","+price.getPrice()+")";
			  new SqlUpdate(dataSource,sql+values).update();
		  }
	}
	
	public void update(List<TablePrice> sets){
		  new SqlUpdate(dataSource,"delete from tablePrice").update();	
		  for(TablePrice price:sets){
			  String sql="insert into tablePrice(tableId,freeCharge,price) values";
			  String values="('"+price.getTableId()+"',"+(price.getFreeCharge().equals(",true")? 1:0)+","+price.getPrice()+")";
			  new SqlUpdate(dataSource,sql+values).update();
		  }
	}
	protected class TablePriceMappingQuery extends MappingSqlQuery {
		protected TablePriceMappingQuery(DataSource ds) {
			super(ds,"select * from tablePrice");
			compile();
		}

		protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
			TablePrice tablePrice = new TablePrice();
			tablePrice.setFreeCharge(rs.getBoolean("freeCharge")? ",true":"");
			tablePrice.setPrice(rs.getInt("price"));
			tablePrice.setTableId(rs.getString("tableId"));
			return tablePrice;
		}
	}	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}
}
