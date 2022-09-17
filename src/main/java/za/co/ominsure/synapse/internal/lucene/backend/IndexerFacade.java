package za.co.ominsure.synapse.internal.lucene.backend;

import javax.enterprise.context.ApplicationScoped;

import za.co.ominsure.synapse.internal.lucene.backend.exceptions.HttpException;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexResponse;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexingRequest;

@ApplicationScoped
public interface IndexerFacade {
	public CRUDIndexResponse createIndex(CRUDIndexingRequest inxDoc) throws HttpException;
	public CRUDIndexResponse updateIndex(CRUDIndexingRequest inxDoc) throws HttpException;
	public CRUDIndexResponse deleteIndex(String indexName) throws HttpException;
}
