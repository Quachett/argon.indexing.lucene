package za.co.ominsure.synapse.internal.lucene.backend.searcher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import za.co.ominsure.synapse.internal.lucene.backend.util.LuceneUtils;

public abstract class AbstractSearcher<T> {
	protected String indexName;
	protected String indexDir;
	protected String searchField;
	protected String uniqueId;

	public String getIndexName() {
		return indexName;
	}

	public abstract void setIndexName(String indexName) throws SQLException;
	
	public abstract void setIndexDir(String indexDir) throws SQLException;

	public abstract void setSearchField(String SearchField);
	
	public abstract T getDbData(List<T> ids) throws SQLException;

	public abstract void setUniqueId(String uniqueId);

	public String getIndexDir() {
		return indexDir;
	}

	public String spellChecker(String term) throws IOException, SQLException {
		SearchIndexSingleton ic = SearchIndexSingleton.getInstance();
		return ic.termSuggestion(term, indexName, LuceneUtils.LONG_DESC, indexDir);
	}
	
	public List<T> searchProducts(String searchStr) throws IOException, ParseException, SQLException {
		TopDocs td = null;
		SearchIndexSingleton sis = SearchIndexSingleton.getInstance();

		
		td = sis.search(indexName, searchStr, searchField, indexDir);
		return getProductsSeqNo(td, sis);
	}

	private List<T> getProductsSeqNo(TopDocs td, SearchIndexSingleton sis) throws NumberFormatException, CorruptIndexException, IOException, SQLException {

		if(td!=null) {
			List<T> seqNos = new ArrayList<>();
			for(ScoreDoc sd: td.scoreDocs) {
				String id = sis.getDocument(indexName, sd).get(uniqueId);
				System.out.print(id + " ");
				seqNos.add((T) id);
			}
			
			System.out.println(seqNos);
			
			if(!seqNos.isEmpty())
				return seqNos;
		}
		
		return null;
	}

}
