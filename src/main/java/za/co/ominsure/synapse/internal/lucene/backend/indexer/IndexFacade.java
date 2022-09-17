package za.co.ominsure.synapse.internal.lucene.backend.indexer;

import java.io.IOException;

import za.co.ominsure.synapse.internal.lucene.backend.exceptions.HttpException;
import za.co.ominsure.synapse.internal.lucene.backend.vo.IndexingDocs;

public interface IndexFacade {
	public String createIndex(String indexDir, IndexingDocs inxDocs) throws IOException, HttpException;
	public void updateIndex(String indexDir, IndexingDocs inxDocs) throws IOException, HttpException;
	public void deleteIndex(String indexDir) throws IOException, HttpException;
}
