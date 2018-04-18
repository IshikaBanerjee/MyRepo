package com.ishika.aem.impl;

import com.ishika.aem.*;
import com.day.cq.wcm.api.Page;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//Sling Imports

//This is a component so it can provide or consume services
@Component


@Service
public class QueryImpl implements Query {


    //Inject a Sling ResourceResolverFactory
    @Reference
    private ResourceResolverFactory resolverFactory;

    /** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    //@Override
    public String getJCRData(String location) {


        Map<String, Object> param = new HashMap<String, Object>();
        param.put(ResourceResolverFactory.SUBSERVICE, "datacug");


        try {


            //Get the title of the AEM web page at this specific location - assume its a value such as /content/geometrixx/en/services
            ResourceResolver resourceResolver = resolverFactory.getServiceResourceResolver(param);
            Resource res = resourceResolver.getResource(location);



            //Adapts the resource to another type - in this example to a     com.day.cq.wcm.api.page
            Page page = res.adaptTo(Page.class);

            String title = page.getTitle(); // Get the title of the web page


            //Write out the CUG Pages - the user datacug belongs to the UCG that has access to these pages
            Iterator<Page> it = page.listChildren() ;

            while (it.hasNext()) {

                String name = (String) it.next().getPath();
                log.info("**** CUG Child pages are: "+name);
            }


            return title ; // return title of the page
        }
        catch (Exception e)
        {
            e.printStackTrace()  ;
        }

        return null;
    }

}
