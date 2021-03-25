package QueryBuilder;

import java.util.HashMap;

public class QueryConstants {
    public static final HashMap<String, String> LOCATION = new HashMap<>();
    public static final HashMap<String, String> COMPANIES = new HashMap<>();

    static {
        LOCATION.put("Mumbai", "106164952");
        LOCATION.put("Pune", "103671728");
        LOCATION.put("Bangalore", "109710172");
    }

    static {
        COMPANIES.put("Amazon", "1586");
        COMPANIES.put("Google", "1441");
        COMPANIES.put("Microsoft", "1035");
        COMPANIES.put("BrowserStack", "2553488");
        COMPANIES.put("Dream11", "264559");
    }
}