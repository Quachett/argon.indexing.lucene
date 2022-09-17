package za.co.ominsure.synapse.internal.lucene.backend.exceptions;

public class HttpException extends Exception {
	private static final long serialVersionUID = -7877377604369147035L;

	private final int status;
	
	public HttpException() {		
		super();
		status=0;
	}

	public HttpException(String message) {
		super(message);
		status=0;
	}
	
	public HttpException(String message,int status) {
		super(message);
		this.status=status;
	}

	public HttpException(String message, Throwable cause) {
		super(message, cause);
		status=0;
	}
	
	public HttpException(String message, Throwable cause,int status) {
		super(message, cause);
		this.status=status;
	}

	public int getStatus() {
		return status;
	}
}