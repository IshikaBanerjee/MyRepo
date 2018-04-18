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

@Service(LaunchCode.class)
@SlingServlet(paths = {"/bin/launchcode"}, generateComponent = false)
@Component(label = "Dropdown Movie data provider", description = "This servlet provides launch code names in drop down",
        enabled = true, immediate = true, metatype = false)
public class LaunchCode extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchCode.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            JSONObject eachOption;
            JSONArray optionsArray = new JSONArray();
            String[] code = { "101BC", "XC456","AE81S",
                    "FGT123", "RT555", "DF010",
                    "SD345", "VG564","PO909" };
            String[] returnData = new String[code.length];

            for (int i = 0; i < code.length; i++) {
                eachOption = new JSONObject();
                returnData[i] = code[i];
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