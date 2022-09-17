package za.co.ominsure.synapse.internal.lucene.backend.exceptions;

public class InterceptorException extends Exception {
	private static final long serialVersionUID = -7877377604369147035L;

	public InterceptorException() {		
		super();
	}

	public InterceptorException(String message) {
		super(message);
	}
	
	public InterceptorException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
