/*
 * Copyright (c) 2011, Salzburg NewMediaLab
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the KiWi Project nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package at.newmedialab.lmf.admin.search;

import at.newmedialab.lmf.admin.client.Admin;
import com.google.gwt.json.client.JSONArray;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.ext.json.JsonRepresentation;
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
        ClientResource coreClient = new ClientResource(Admin.SERVER_URL + "solr/cores");


        coreClient.setOnResponse(new Uniform() {
            @Override
            public void handle(Request request, Response response) {
                // Get the representation as an JsonRepresentation
                JsonRepresentation rep = new JsonRepresentation(response.getEntity());
                try {
                    JSONArray result = rep.getValue().isArray();
                    if (result != null) {
                        for (int i = 0; i < result.size(); i++) {
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
        form.setGroupTitle("Core " + name);
        form.setPadding(10);

        final TextAreaItem textArea = new TextAreaItem();
        textArea.setTitle("Program");
        textArea.setWidth(600);
        textArea.setHeight(200);
        form.setItems(textArea);

        this.addMember(form);


        ClientResource coreClient = new ClientResource(Admin.SERVER_URL + "solr/cores/" + name);
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
