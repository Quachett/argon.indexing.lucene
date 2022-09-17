package za.co.ominsure.synapse.internal.lucene.backend.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class UniqueTokenAnalyser extends Analyzer {
	public UniqueTokenAnalyser() {
		super();
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokeniser = new StandardTokenizer();
		TokenStream tokenStream = new LowerCaseFilter(tokeniser);
		tokenStream = new ASCIIFoldingFilter(tokenStream);
		tokenStream = new LengthFilter(tokenStream, 3, Integer.MAX_VALUE);
		tokenStream = new UniqueTokenFilter(tokenStream);
		return new TokenStreamComponents(tokeniser, tokenStream);
	}
}
