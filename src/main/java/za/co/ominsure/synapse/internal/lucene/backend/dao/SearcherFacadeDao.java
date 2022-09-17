package za.co.ominsure.synapse.internal.lucene.backend.dao;

import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import za.co.ominsure.synapse.internal.lucene.backend.products.vo.Products;


@ApplicationScoped
public interface SearcherFacadeDao<T> {
	public T getIntendaDbData(List<T> ids) throws SQLException;
	public String getConfig(String key) throws SQLException;
}
