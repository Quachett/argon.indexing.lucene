package za.co.ominsure.synapse.internal.lucene.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

import io.quarkus.agroal.DataSource;
import za.co.ominsure.synapse.internal.lucene.backend.products.vo.NameCode;
import za.co.ominsure.synapse.internal.lucene.backend.products.vo.Product;
import za.co.ominsure.synapse.internal.lucene.backend.products.vo.Products;
import za.co.ominsure.synapse.internal.lucene.backend.products.vo.YN;
import za.co.ominsure.synapse.internal.lucene.backend.util.LuceneUtils;
import za.co.ominsure.synapse.internal.lucene.backend.util.SynapseConstants;

@ApplicationScoped
public class SearcherFacadeDaoBean<T> implements SearcherFacadeDao<T> {

	@Inject
	@DataSource("oracleDs")
	javax.sql.DataSource oracleDS;

	@Override
	public T getIntendaDbData(List<T> ids) throws SQLException {
		Products prs = new Products();
		int i=1;
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(getConfig("INTENDA_SCHEMA"));
		sb.append(SynapseConstants.DOT);
		sb.append(getConfig("INTENDA_TABLE"));
		if(ids != null) {
			sb.append(" WHERE SEQ_NO IN (");
			sb.append(getLineOfQs(ids.size()));
			sb.append(")");
		}
		
		String query = sb.toString();
		System.out.println("\n\n!!! " + query + " !!!\n\n");
		
		try(Connection conn = oracleDS.getConnection();
				PreparedStatement ps = conn.prepareStatement(query)) {
			
			if(ids != null)
				for(T id: ids)
					ps.setLong(i++, Long.parseLong((String) id));
			
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next())
					prs.getProduct().add(populateProoduct(rs));	
			}
		}
		return (T)prs;
	}
	
	@Override
	public String getConfig(String key) throws SQLException {
		String query = "SELECT value FROM synapse.service_configuration WHERE business_unit = 'INTERNAL' AND module = 'LUCENE' AND name = ?";
		
		try(Connection conn = oracleDS.getConnection();
				PreparedStatement ps = conn.prepareStatement(query)) {
			
			ps.setString(1, key);
			
			try(ResultSet rs = ps.executeQuery()) {
				while(rs.next())
					return rs.getString("VALUE");	
			}
		}
		return null;
	}
	
	private Product populateProoduct(ResultSet rs) throws SQLException {

		NameCode segment = new NameCode();
		NameCode family = new NameCode();
		NameCode productClass = new NameCode();
		NameCode commodity = new NameCode();
		Product pr = new Product();

		family.setName(rs.getString(LuceneUtils.FAMILY_NAME));
		family.setCode(rs.getString(LuceneUtils.FAMILY_CODE));
		pr.setFamily(family);

		segment.setName(rs.getString(LuceneUtils.SEGMENT_NAME));
		segment.setCode(rs.getString(LuceneUtils.SEGMENT_CODE));
		pr.setSegment(segment);

		commodity.setName(rs.getString(LuceneUtils.COMMODITY_NAME));
		commodity.setCode(rs.getString(LuceneUtils.COMMODITY_CODE));
		pr.setCommodity(commodity);

		productClass.setName(rs.getString(LuceneUtils.CLASS_NAME));
		productClass.setCode(rs.getString(LuceneUtils.CLASS_CODE));
		pr.setProductClass(productClass);
		
		pr.setItemCode(rs.getString(LuceneUtils.ITEM_CODE));
		pr.setStockCode(rs.getString(LuceneUtils.STOCKCODE));
		pr.setBrand(rs.getString(LuceneUtils.BRAND));
		pr.setShortDescription(rs.getString(LuceneUtils.SHORT_DESC));
		pr.setRemoved(YN.fromValue(rs.getString(LuceneUtils.REMOVED)));

		pr.setSequenceNo(rs.getLong(LuceneUtils.SEQ_NO));
		pr.setLongDescription(rs.getString(LuceneUtils.LONG_DESC));
		
		return pr;
	}
    
    public static String getLineOfQs(int num) {
        return Joiner.on(", ").join(Iterables.limit(Iterables.cycle("?"), num));
    }
}
