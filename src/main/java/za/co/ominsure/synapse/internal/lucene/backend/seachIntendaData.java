package za.co.ominsure.synapse.internal.lucene.backend;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import za.co.ominsure.synapse.internal.lucene.backend.dao.SearcherFacadeDao;
import za.co.ominsure.synapse.internal.lucene.backend.products.vo.Products;
import za.co.ominsure.synapse.internal.lucene.backend.searcher.AbstractSearcher;
import za.co.ominsure.synapse.internal.lucene.backend.searcher.SearchIndexSingleton;
import za.co.ominsure.synapse.internal.lucene.backend.util.LuceneUtils;

@ApplicationScoped
public class seachIntendaData<T> extends AbstractSearcher<T> implements SearcherFacade2<T> {

	@Inject
	private SearcherFacadeDao dao;
	
	private String lookupConfig(String key) throws SQLException {
		return dao.getConfig(key);
	}

	@Override
	public T getDbData(List<T> ids) throws SQLException {
		// TODO Auto-generated method stub
		return (T) dao.getIntendaDbData(ids);
	}

	@Override
	public void setIndexName(String indexName) throws SQLException {
		this.indexName = indexName;
		
	}

	@Override
	public void setIndexDir(String indexDir) throws SQLException {
		this.indexDir = indexDir;
		
	}

	@Override
	public void setSearchField(String searchField) {
		this.searchField = searchField;
		
	}

	@Override
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@Override
	public T searchIndex(String searchStr) throws IOException, ParseException, SQLException {
		setIndexName(lookupConfig("INTENDA_INDEX_NAME"));
		setIndexDir(lookupConfig(indexName + "_DIR"));
		setSearchField(LuceneUtils.LONG_DESC);
		setUniqueId(LuceneUtils.SEQ_NO);
		return getDbData(searchProducts(searchStr));
	}

}
