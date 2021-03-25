package QueryBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class QueryBuilder extends QueryConstants {
    private String query;

    public QueryBuilder(String query) {
        this.query = query;
    }

    public String generateUrl() {
        String[][] params = buildParams();
        String MAIN_URL = "https://www.linkedin.com/search/results/";

        MAIN_URL += params[0][0].toLowerCase() + "/";
        MAIN_URL += "?currentCompany=" + URLEncoder.encode(transformValues(params[2], COMPANIES), StandardCharsets.UTF_8);
        MAIN_URL += "&geoUrn=" + URLEncoder.encode(transformValues(params[3], LOCATION), StandardCharsets.UTF_8);
        MAIN_URL += "&keywords=" + URLEncoder.encode(addKeywords(params[1]), StandardCharsets.UTF_8);

        return MAIN_URL;
    }

    private String cleanParams(String p) {
        String filtered = p.replaceAll("[\\[\\]]", "");
        filtered = filtered.replaceAll("(\\s*)(,)(\\s*)", ",");
        return filtered;
    }

    private String[][] buildParams() {
        String[][] filteredParams = new String[4][];
        String[] params = this.query.split("\\s+->\\s+");

        for (int i = 0; i < filteredParams.length; i++)
            filteredParams[i] = cleanParams(params[i]).split(",");

        return filteredParams;
    }

    private String addKeywords(String[] keywords) {
        StringBuilder intermediateURL = new StringBuilder();

        for (int i = 0; i < keywords.length; i++) {
            if (i != keywords.length - 1) {
                intermediateURL.append(keywords[i]).append(" ");
            } else intermediateURL.append(keywords[i]);
        }

        return intermediateURL.toString();
    }

    private String transformValues(String[] values, HashMap<String, String> MAP) {
        StringBuilder intermediateURL = new StringBuilder("[");

        for (int i = 0; i < values.length; i++) {
            intermediateURL
                    .append("\"")
                    .append(MAP.get(values[i]))
                    .append("\"");
            if (i != values.length - 1)
                intermediateURL.append(",");
        }

        return intermediateURL.append("]").toString();
    }
}
