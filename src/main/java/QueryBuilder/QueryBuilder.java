package QueryBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class QueryBuilder extends QueryConstants {
    private final String query;

    private String[] keywords;
    private String[] company;
    private String[] location;

    public QueryBuilder(String query) {
        this.query = query;
    }

    public String generateUrl() {
        String[][] params = buildParams();

        this.keywords = params[1];
        this.company = params[2];
        this.location = params[3];

        String MAIN_URL = "https://www.linkedin.com/search/results/";

        MAIN_URL += params[0][0].toLowerCase() + "/";
        MAIN_URL += "?keywords=" + URLEncoder.encode(addKeywords(params[1]), StandardCharsets.UTF_8);
        MAIN_URL += !params[2][0].equals("") ? "&currentCompany=" + URLEncoder.encode(transformValues(params[2], COMPANIES), StandardCharsets.UTF_8) : "";
        MAIN_URL += !params[3][0].equals("") ? "&geoUrn=" + URLEncoder.encode(transformValues(params[3], LOCATION), StandardCharsets.UTF_8) : "";

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
        if (!values[0].equals("")) {
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
        return "";
    }

    public String getParamsSlug() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < company.length; i++) {
            if (company.length > 1) {
                builder.append(company[i]).append("_");
                if (i == company.length - 1)
                    builder.append(company[i]);
            } else
                builder.append(company[i]).append("_");
        }

        for (int i = 0; i < keywords.length; i++) {
            if (keywords.length > 1) {
                builder.append(keywords[i]).append("_");
                if (i == keywords.length - 1)
                    builder.append(keywords[i]);
            } else
                builder.append(keywords[i]).append("_");
        }

        for (int i = 0; i < location.length; i++) {
            if (location.length > 1) {
                builder.append(location[i]).append("_");
                if (i == location.length - 1)
                    builder.append(location[i]);
            } else
                builder.append(location[i]).append("_");
        }

        return builder.toString();
    }
}
