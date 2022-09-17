package za.co.ominsure.synapse.internal.lucene.backend;

import java.io.IOException;
import java.sql.SQLException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import za.co.ominsure.synapse.internal.lucene.backend.dao.IndexerFacadeDao;
import za.co.ominsure.synapse.internal.lucene.backend.dao.SearcherFacadeDao;
import za.co.ominsure.synapse.internal.lucene.backend.exceptions.HttpException;
import za.co.ominsure.synapse.internal.lucene.backend.indexer.IndexFacade;
import za.co.ominsure.synapse.internal.lucene.backend.util.LuceneUtils;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexResponse;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexingRequest;
import za.co.ominsure.synapse.internal.lucene.backend.vo.Message;
import za.co.ominsure.synapse.internal.lucene.backend.vo.Status;

@ApplicationScoped
public class IndexerFacadeBean implements IndexerFacade {
	
	@Inject
	private IndexerFacadeDao dao;

	@Inject
	private SearcherFacadeDao sfDao;
	
	@Inject
	private IndexFacade ci;
	
	@Override
	public CRUDIndexResponse createIndex(CRUDIndexingRequest request) throws HttpException {
		CRUDIndexResponse cir = new CRUDIndexResponse();
		String msg = null;
		
		try {
			msg = ci.createIndex(directoryLookup(request.getIndexName()), dao.getIndexingDocInfo(request));
		} catch (IOException | HttpException | SQLException e) {
			cir.setStatus(Status.FAILED);
			cir.setMessage(Message.FAILED_TO_CREATE_INDEX.value() + " "+ e.getMessage());
			e.printStackTrace();
			throw new HttpException(LuceneUtils.getGson().toJson(cir), 500);
		}
		
		cir.setStatus(Status.SUCCESS);
		cir.setMessage(msg);
		
		return cir;
	}
	
	@Override
	public CRUDIndexResponse updateIndex(CRUDIndexingRequest inxDoc) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CRUDIndexResponse deleteIndex(String indexName) throws HttpException {
		CRUDIndexResponse cir = new CRUDIndexResponse();
		
		try {
			ci.deleteIndex(directoryLookup(indexName));
		}catch (Exception e) {
			cir.setStatus(Status.FAILED);
			cir.setMessage(Message.FAILED_TO_DELETE_INDEX.value() + " "+ e.getMessage());
			e.printStackTrace();
			throw new HttpException(LuceneUtils.getGson().toJson(cir), 500);
		}
		
		cir.setStatus(Status.SUCCESS);
		cir.setMessage(Message.INDEX_DELETED_SUCCESSFULLY.value());
		
		return cir;
	}
	
	private String directoryLookup(String indexname) throws SQLException {
		return sfDao.getConfig(indexname + "_DIR");
	}
}
