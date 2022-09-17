package za.co.ominsure.synapse.internal.lucene.backend.dao;

import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;

import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexingRequest;
import za.co.ominsure.synapse.internal.lucene.backend.vo.IndexingDocs;

@ApplicationScoped
public interface IndexerFacadeDao {
	public IndexingDocs getIndexingDocInfo(CRUDIndexingRequest inxDoc) throws SQLException;
}
