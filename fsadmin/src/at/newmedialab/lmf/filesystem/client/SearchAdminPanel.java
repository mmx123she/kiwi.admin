package at.newmedialab.lmf.filesystem.client;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.ext.json.JsonRepresentation;
import org.restlet.client.representation.Representation;
import org.restlet.client.resource.ClientResource;

import java.io.IOException;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class SearchAdminPanel extends VLayout {

    public SearchAdminPanel() {
        listCores();
    }



    private void listCores() {
        ClientResource coreClient = new ClientResource(Admin.SERVER_URL+"solr/cores");


        coreClient.setOnResponse(new Uniform() {
            @Override
            public void handle(Request request, Response response) {
                // Get the representation as an JsonRepresentation
                JsonRepresentation rep = new JsonRepresentation(response.getEntity());
                try {
                    JSONArray result = rep.getValue().isArray();
                    if(result != null) {
                        for(int i = 0; i<result.size(); i++) {
                            String coreName = result.get(i).isString().stringValue();
                            addCore(coreName);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        coreClient.get();
    }


    private void addCore(String name) {
        DynamicForm form = new DynamicForm();
        form.setIsGroup(true);
        form.setGroupTitle("Core "+name);
        form.setPadding(10);

        final TextAreaItem textArea = new TextAreaItem();
        textArea.setTitle("Program");
        textArea.setWidth(600);
        textArea.setHeight(200);
        form.setItems(textArea);

        this.addMember(form);


        ClientResource coreClient = new ClientResource(Admin.SERVER_URL+"solr/cores/"+name);
        coreClient.setOnResponse(new Uniform() {
             @Override
             public void handle(Request request, Response response) {
                 try {
                     String program = response.getEntity().getText();
                     textArea.setValue(program);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         });

         coreClient.get();

    }


}
