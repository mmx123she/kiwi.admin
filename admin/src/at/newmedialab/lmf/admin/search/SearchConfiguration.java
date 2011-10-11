package at.newmedialab.lmf.admin.search;

import at.newmedialab.lmf.admin.client.AbstractConfiguration;
import at.newmedialab.lmf.admin.client.Admin;
import at.newmedialab.lmf.admin.client.Configuration;
import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.ext.json.JsonRepresentation;
import org.restlet.client.resource.ClientResource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class SearchConfiguration extends AbstractConfiguration {

    private Admin parent;

    public SearchConfiguration(Admin parent) {
        super(parent);
    }



    /**
     * Get a displayable name for the configuration module
     *
     * @return
     */
    @Override
    public String getName() {
        return "LMF Search";
    }

    /**
     * Get a more detailed description for the configuration module
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Configurations for the LMF Semantic Search component. Allows modification and addition of search cores.";
    }

    /**
     * List all actions that are available in the configuration module
     *
     * @return
     */
    @Override
    public void registerActions() {
        ClientResource coreClient = new ClientResource(Admin.SERVER_URL+"solr/cores");

        final SearchConfiguration self = this;
        coreClient.setOnResponse(new Uniform() {
            @Override
            public void handle(Request request, Response response) {
                // Get the representation as an JsonRepresentation
                JsonRepresentation rep = new JsonRepresentation(response.getEntity());
                try {
                    JSONArray result = rep.getValue().isArray();
                    if(result != null) {
                        for(int i = 0; i<result.size(); i++) {
                            final String coreName = result.get(i).isString().stringValue();
                            getParent().registerAction(self,"Core "+coreName, new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    SC.say("Core "+coreName+" selected");
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        coreClient.get();
    }

 }
