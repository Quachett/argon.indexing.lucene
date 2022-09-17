package za.co.ominsure.synapse.internal.lucene.backend.exceptions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"statusCode",
	"message"
})
@XmlRootElement(name = "errorResponse")
public class ErrorResponse {
	@XmlElement
	private int statusCode;
	@XmlElement
	private String message;
	
	public ErrorResponse() {
		
	}
	
	public ErrorResponse(int statusCode,String message) {
		this.statusCode=statusCode;
		this.message=message;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
