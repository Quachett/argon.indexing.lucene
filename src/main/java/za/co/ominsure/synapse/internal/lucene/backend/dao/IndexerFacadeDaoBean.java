package za.co.ominsure.synapse.internal.lucene.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import io.quarkus.agroal.DataSource;
import za.co.ominsure.synapse.internal.lucene.backend.vo.ColumnInfo;
import za.co.ominsure.synapse.internal.lucene.backend.util.LuceneUtils;
import za.co.ominsure.synapse.internal.lucene.backend.util.SynapseConstants;
import za.co.ominsure.synapse.internal.lucene.backend.vo.Attribute;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexingRequest;
import za.co.ominsure.synapse.internal.lucene.backend.vo.IndexingDoc;
import za.co.ominsure.synapse.internal.lucene.backend.vo.IndexingDocs;
import za.co.ominsure.synapse.internal.lucene.backend.vo.Type;

@ApplicationScoped
public class IndexerFacadeDaoBean implements IndexerFacadeDao {
	
	@Inject
	@DataSource("oracleDs")
	javax.sql.DataSource oracleDS;
	
	@Override
	public IndexingDocs getIndexingDocInfo(CRUDIndexingRequest request) throws SQLException {
		IndexingDocs inxDocs = new IndexingDocs();
		StringBuilder sb = new StringBuilder();
		
		System.out.println("\n\n================================================");
		System.out.println(LuceneUtils.getGson().toJson(request));
		System.out.println("================================================\n\n");
		
		List<String> cols = new ArrayList<String>();
		cols.add(request.getKeyName());
		cols.addAll(request.getFieldName());
		
		String columns = StringUtils.join(cols, SynapseConstants.COMMA);
		
		System.out.println("Columns: " + columns);
		
		sb.append("SELECT ").append(columns).append(" FROM ");
		sb.append(request.getTableInfo().getSchema()).append(SynapseConstants.DOT).append(request.getTableInfo().getTable());
		
		if(request.getAttributes()!=null) {
			String where = getAttributes(request.getAttributes());
			if(where != null)
				sb.append(" WHERE ").append(where);
		}
		
		String query = sb.toString();
		System.out.println("\n\n!!! " + query + " !!!\n\n");
		
		try(Connection conn = oracleDS.getConnection();
				PreparedStatement ps = conn.prepareStatement(query)) {
			
			if(request.getAttributes()!=null)
				setVariables(ps, request.getAttributes());
			
			try(ResultSet rs = ps.executeQuery()) {
				if(rs.next())
					while(rs.next())
						inxDocs.getDocumentInfo().add(populateIndexingDocs(rs, cols));
			}
		}
		
		return inxDocs;
	}

	private IndexingDoc populateIndexingDocs(ResultSet rs, List<String> cols) throws SQLException {
		List<String> newCols = new ArrayList<String>();
		newCols.addAll(cols);
		IndexingDoc inxDoc = new IndexingDoc();
		ColumnInfo key = new ColumnInfo();
		ColumnInfo fieldInfo = null;
		
		key.setName(newCols.get(0));
		key.setValue(String.valueOf(rs.getObject(newCols.get(0))));
		inxDoc.setKeyInfo(key);
		newCols.remove(0);
		
		for(String col: newCols) {
			fieldInfo = new ColumnInfo();
			fieldInfo.setName(col);
			fieldInfo.setValue(String.valueOf(rs.getObject(col)));
			inxDoc.getFieldInfo().add(fieldInfo);
		};
		
		return inxDoc;
	}
	
	private String getAttributes(List<Attribute> attributes) {
		if(attributes.size()>0) {
			int i = 0;
			StringBuilder sb = new StringBuilder();
			for(Attribute a: attributes) {
				sb.append(a.getName()).append(LuceneUtils.WHERE_CLAUSE);
				if(i+1<attributes.size())
					sb.append(" and ");
				i++;
			}
			return sb.toString();
		}
		return null;
	}
	
	private void setVariables(PreparedStatement ps, List<Attribute> attrs) throws SQLException {
		int i = 1;
		for(Attribute a: attrs) {
			if(a.getType().compareTo(Type.STRING)==0)
				ps.setString(i, a.getValue());
			if(a.getType().compareTo(Type.INT)==0)
				ps.setInt(i, Integer.parseInt(a.getValue()));
			if(a.getType().compareTo(Type.TIMESTAMP)==0)
				ps.setTimestamp(i, Timestamp.valueOf(a.getValue()));
			if(a.getType().compareTo(Type.DOUBLE)==0)
				ps.setDouble(i, Double.valueOf(a.getValue()));
		}
	}
}
