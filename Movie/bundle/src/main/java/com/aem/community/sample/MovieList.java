package com.aem.community.sample;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.io.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

@Service(MovieList.class)
@SlingServlet(paths = {"/bin/moviename"}, generateComponent = false)
@Component(label = "Dropdown Movie data provider", description = "This servlet provides list of movie names in drop down",
        enabled = true, immediate = true, metatype = false)
public class MovieList extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieList.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            JSONObject eachOption;
            JSONArray optionsArray = new JSONArray();
            String[] movies = { "Terminator", "Kicking & Screaming","Harold and Maude",
                    "Ratatouille", "10 Things I Hate About You", "American Beauty",
                    "The Dark Knight", "The Wolf of Wall Street","Mean Girls",
                    "Inception", "Life Is Beautiful", "No Country for Old Men" };
            String[] returnData = new String[movies.length];

            for (int i = 0; i < movies.length; i++) {
                eachOption = new JSONObject();
                returnData[i] = movies[i];
                eachOption.put("text", returnData[i]);
                eachOption.put("value", returnData[i]);
                optionsArray.put(eachOption);
            }

            JSONObject finalJsonResponse = new JSONObject();
            //Adding this finalJsonResponse object to showcase optionsRoot property functionality
            finalJsonResponse.put("root", optionsArray);

            response.getWriter().println(finalJsonResponse.toString());
        } catch (JSONException e) {
            LOGGER.error("Json Exception occured while adding data to JSON Object : ", e);
        } catch (IOException e) {
            LOGGER.error("IOException occured while getting Print Writer from SlingServletResponse : ", e);
        }
    }
}