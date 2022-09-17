package za.co.ominsure.synapse.internal.lucene.backend.searcher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.spell.SuggestMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import za.co.ominsure.synapse.internal.lucene.backend.util.LuceneUtils;
import za.co.ominsure.synapse.internal.lucene.backend.util.UniqueTokenAnalyser;


public class SearchIndexSingleton {

	private Map<String, IndexReader> indexReaderMap;
	private Map<String, IndexSearcher> indexSearcherMap;
	private Map<String, Directory> indexDirectoriesMap;
	
	private SearchIndexSingleton() {
		this.indexReaderMap = new HashMap<String, IndexReader>();
		this.indexSearcherMap = new HashMap<String, IndexSearcher>();
		this.indexDirectoriesMap = new HashMap<String, Directory>();
	}
	
	private static final class SearchIndexSingletonHelper {
		private static final SearchIndexSingleton instance = new SearchIndexSingleton();
	}
	
	public static SearchIndexSingleton getInstance() {
		return SearchIndexSingletonHelper.instance;
	}
	
	private void createIndexDir(String indexName, String dir) throws IOException {
		if(!indexDirectoriesMap.containsKey(indexName))
			indexDirectoriesMap.put(indexName, FSDirectory.open(new File(dir).toPath()));
		if(!DirectoryReader.indexExists(indexDirectoriesMap.get(indexName)))
			throw new RuntimeException("Search Failed, Index " + indexName + " does not exist");
	}
	
	private void createIndexReader(String indexName) throws IOException {
		if(!indexReaderMap.containsKey(indexName))
			indexReaderMap.put(indexName, DirectoryReader.open(indexDirectoriesMap.get(indexName)));
	}
	
	private void createIndexSearcher(String indexName) throws IOException {
		if(indexDirectoriesMap.get(indexName).listAll().length<=1)
			throw new IllegalArgumentException("Index is empty");
		createIndexReader(indexName);
		indexSearcherMap.put(indexName, new IndexSearcher(indexReaderMap.get(indexName)));
	}
	
	private Query createQuery(String searchString, String field) throws ParseException {
		QueryParser qp = new QueryParser(field, new UniqueTokenAnalyser());
		qp.setDefaultOperator(QueryParser.Operator.AND);
		return qp.parse(QueryParser.escape(searchString));
	}
	
	public TopDocs search(String indexName, String searchString, String field, String dir) throws IOException, ParseException {
		createIndexDir(indexName, dir);
		Query query = createQuery(termSuggestion(searchString, indexName, field, dir), field);

		if(!indexSearcherMap.containsKey(indexName))
			createIndexSearcher(indexName);
		
		return indexSearcherMap.get(indexName).search(query, LuceneUtils.MAX_SEARCH);
	}
	
	public Document getDocument(String indexName, ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
		return indexSearcherMap.get(indexName).doc(scoreDoc.doc);
	}
	
	public String termSuggestion(String term, String indexName, String field, String dir) throws IOException {
		if(term!=null) {
			createIndexDir(indexName, dir);
			
			IndexWriterConfig iwc = new IndexWriterConfig(new UniqueTokenAnalyser());
			
			if(!indexDirectoriesMap.containsKey(indexName))
				throw new RuntimeException("Index not found");
			
			try(SpellChecker spellChecker = new SpellChecker(indexDirectoriesMap.get(indexName))) {
								
				spellChecker.indexDictionary(new LuceneDictionary(DirectoryReader.open(indexDirectoriesMap.get(indexName)),
					field), iwc, false);
				spellChecker.setStringDistance(new org.apache.lucene.search.spell.JaroWinklerDistance());
				
				StringBuilder sb = new StringBuilder();
				String temp = "";
				String[] t_Array = term.split(StringUtils.SPACE);
				
				createIndexReader(indexName);
				for(String str: t_Array) {
					String [] sg = spellChecker.suggestSimilar(str, 5, indexReaderMap.get(indexName), field, SuggestMode.SUGGEST_WHEN_NOT_IN_INDEX);
					
					JaroWinklerDistance jd = new JaroWinklerDistance();
					
					for(String s: sg) {
						System.out.println(str + "\t" + s + "\t" + jd.apply(s, str));
						temp = jd.apply(s, str)>jd.apply(temp, str)?s:temp;
					}
					sb.append(temp).append(StringUtils.SPACE); 
				}
				String searchStr = sb.toString();
	    		System.out.println("Search String: === " + searchStr);
				
				return searchStr;
			}
		}
		
		return null;
	}
}
