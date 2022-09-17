package za.co.ominsure.synapse.internal.lucene.backend.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LuceneUtils {
    private static final String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final Gson gson = new GsonBuilder()
			.setDateFormat(ISO_8601_DATE_FORMAT)
			.setPrettyPrinting().create();
	public static final String WHERE_CLAUSE = " = ?";
	public static final int MAX_SEARCH = 100;
	
	//INTENDA CONSTANTS
	public static final String SEQ_NO = "SEQ_NO";
	public static final String LONG_DESC = "LONG_DESC";
	public static final String SEGMENT_NAME = "SEGMENT_NAME";
	public static final String SEGMENT_CODE = "SEGMENT_CODE";
	public static final String FAMILY_NAME = "FAMILY_NAME";
	public static final String FAMILY_CODE = "FAMILY_CODE";
	public static final String CLASS_NAME = "CLASS_NAME";
	public static final String CLASS_CODE = "CLASS_CODE";
	public static final String COMMODITY_NAME = "COMMODITY_NAME";
	public static final String COMMODITY_CODE = "COMMODITY_CODE";
	public static final String ITEM_CODE = "ITEM_CODE";
	public static final String STOCKCODE = "STOCKCODE";
	public static final String BRAND = "BRAND";
	public static final String SHORT_DESC = "SHORT_DESC";
	public static final String USERID = "USERID";
	public static final String TIMESTAMP = "TIMESTAMP";
	public static final String REMOVED = "REMOVED";

	public static Gson getGson() {
		return gson;
	}

}
