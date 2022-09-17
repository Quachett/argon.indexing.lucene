package za.co.ominsure.synapse.internal.lucene.backend.indexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import za.co.ominsure.synapse.internal.lucene.backend.exceptions.HttpException;
import za.co.ominsure.synapse.internal.lucene.backend.util.UniqueTokenAnalyser;
import za.co.ominsure.synapse.internal.lucene.backend.vo.IndexingDocs;
import za.co.ominsure.synapse.internal.lucene.backend.vo.Message;

@ApplicationScoped
public class IndexFacadeBean implements IndexFacade {
	private IndexWriter iw;
	private Directory dir;
	
	private void createIndexDir(String indexDir) throws IOException {
		this.dir = FSDirectory.open(new File(indexDir).toPath());
	}
	
	private void createIndexWriter() throws IOException, HttpException {
		IndexWriterConfig iwc = new IndexWriterConfig(new UniqueTokenAnalyser());
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		this.iw =new IndexWriter(dir, iwc);
	}
	
	private void createDocuments(IndexingDocs inxDocs, List<Document> docList) {
		FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
		ft.setOmitNorms(false);
		inxDocs.getDocumentInfo().forEach(di ->{
			Document doc = new Document();
			
			doc.add(new StringField(di.getKeyInfo().getName(), di.getKeyInfo().getValue(), Field.Store.YES));
			di.getFieldInfo().forEach(ci -> doc.add(new Field(ci.getName(), ci.getValue(), ft)));
			
			docList.add(doc);
		});
	}
	
	private void indexer(IndexingDocs inxDocs) throws IOException, HttpException {
		List<Document> docList = new ArrayList<Document>();
		
		int[] count = {0};
				
		createDocuments(inxDocs, docList);
		
		System.out.println("Indexing..."); //replace with logging framework
		docList.forEach(d->{
			try {
				iw.addDocument(d);
				
				if(count[0]%30000 == 0)
					iw.commit();
				count[0]++;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		});
		iw.commit();
		iw.flush();
		iw.close();
		dir.close();
		System.out.println("Indexing Completed"); //replace with logging framework
	}
	
	@Override
	public String createIndex(String indexDir, IndexingDocs inxDocs) throws IOException, HttpException {
		createIndexDir(indexDir);
		if(!DirectoryReader.indexExists(dir)) {
			createIndexWriter();
			indexer(inxDocs);
			return Message.INDEX_CREATED_SUCCESSFULLY.value();
		}
		else
			return Message.INDEX_ALREADY_EXISTS.value();
	}
	
	@Override
	public void updateIndex(String indexDir, IndexingDocs inxDocs) throws IOException, HttpException {
		createIndexDir(indexDir);
		if(DirectoryReader.indexExists(dir)) {
			createIndexWriter();
			indexer(inxDocs);
		}
		else
			throw new HttpException("No Index found at: " + indexDir, 404);
	}
	
	@Override
	public void deleteIndex(String indexDir) throws IOException, HttpException {
		createIndexDir(indexDir);
		if(DirectoryReader.indexExists(dir)) {
			createIndexWriter();

			iw.deleteAll();
			iw.commit();
			iw.flush();
			iw.close();
			dir.close();
			FileUtils.cleanDirectory(new File(indexDir));
		}
		else
			throw new HttpException("No Index found at: " + indexDir, 404);
	}
}
