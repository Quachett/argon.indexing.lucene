package za.co.ominsure.synapse.internal.lucene.frontend;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import za.co.ominsure.synapse.internal.lucene.backend.IndexerFacade;
import za.co.ominsure.synapse.internal.lucene.backend.SearcherFacade2;
import za.co.ominsure.synapse.internal.lucene.backend.products.vo.Products;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexResponse;
import za.co.ominsure.synapse.internal.lucene.backend.vo.CRUDIndexingRequest;


@Path("/")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class Service {

	@Inject
	IndexerFacade facade;

	@Inject
	private SearcherFacade2 sf;

	
    @POST
    @Path("/index/create")
    public Response createIndex(CRUDIndexingRequest request) {
    	try {
    		if(request.getFieldName().size()==0)
    			return Response.status(Status.BAD_REQUEST).entity(request).build();
    		

    		System.out.println(request.getFieldName());
    		CRUDIndexResponse response = facade.createIndex(request);
    		if(response != null)
    			return Response.status(Status.OK).entity(response).build();
    		
    		return Response.status(Status.NO_CONTENT).entity(new CRUDIndexResponse()).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }
	
    @POST
    @Path("/index/update")
    public Response updateIndex(CRUDIndexingRequest request) {
    	try {
    		if(request.getFieldName().size()==0)
    			return Response.status(Status.BAD_REQUEST).entity(request).build();
    		

    		System.out.println(request.getFieldName());
    		CRUDIndexResponse response = facade.updateIndex(request);
    		if(response != null)
    			return Response.status(Status.OK).entity(response).build();
    		
    		return Response.status(Status.NO_CONTENT).entity(new CRUDIndexResponse()).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }
	
    @GET
    @Path("/index/delete")
    public Response deleteIndex(@QueryParam("indexName") String indexName) {
    	try {
    		CRUDIndexResponse response = facade.deleteIndex(indexName);
    		if(response != null)
    			return Response.status(Status.OK).entity(response).build();
    		
    		return Response.status(Status.NO_CONTENT).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }
	
    /*@POST
    @Path("/products")
    public Response getProducts(List<Long> seqNos) {
    	try {
    		System.out.println("Get Artist by ID");
    		Products response = sf.getProducts(seqNos);
    		if(response != null)
    			return Response.status(Status.OK).entity(response).build();
    		
    		return Response.status(Status.NO_CONTENT).entity(new Products()).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }*/
	
    @GET
    @Path("/products")
    public Response searchProducts(@QueryParam("query") String query) {
    	try {
    		Products response = (Products)sf.searchIndex(query);
    		if(response != null)
    			return Response.status(Status.OK).entity(response).build();
    		
    		return Response.status(Status.NO_CONTENT).entity(new Products()).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }
	
    /*@GET
    @Path("/products/spell")
    public Response spellChecker(@QueryParam("term") String term) {
    	try {
    		String response = sf.spellChecker(term);
    		if(response != null)
    			return Response.status(Status.OK).entity(response).build();
    		
    		return Response.status(Status.NO_CONTENT).entity(new Products()).build();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }*/
}