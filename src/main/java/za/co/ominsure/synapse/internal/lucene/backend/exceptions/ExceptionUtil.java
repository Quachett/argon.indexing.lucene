package za.co.ominsure.synapse.internal.lucene.backend.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

public class ExceptionUtil {
	protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("za.co.ominsure.synapse.common.util.ExceptionUtil");
	public static String getStackTrace(Throwable t) {
		StringWriter sw=null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			String stackTrace = sw.toString();
			return stackTrace;
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, (e.getMessage() == null ? e.toString() : e.getMessage()), e);
		}
		finally {
			if (pw!=null) {
				try {
				pw.close();
				} catch (Exception e) {}
			}
			if (sw!=null) {
				try {
				sw.close();
				} catch (Exception e) {}
			}
		}
		return null;
	}
}
