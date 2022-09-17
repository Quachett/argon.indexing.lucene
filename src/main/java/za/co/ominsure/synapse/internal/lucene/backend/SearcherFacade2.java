package za.co.ominsure.synapse.internal.lucene.backend;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.apache.lucene.queryparser.classic.ParseException;

import za.co.ominsure.synapse.internal.lucene.backend.products.vo.Products;


@ApplicationScoped
public interface SearcherFacade2<T> {
	public T searchIndex(String searchStr) throws IOException, ParseException, SQLException;
}
