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

import at.newmedialab.lmf.admin.client.AbstractConfiguration;
import at.newmedialab.lmf.admin.client.Admin;
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
        ClientResource coreClient = new ClientResource(Admin.SERVER_URL + "solr/cores");

        final SearchConfiguration self = this;
        coreClient.setOnResponse(new Uniform() {
            @Override
            public void handle(Request request, Response response) {
                // Get the representation as an JsonRepresentation
                JsonRepresentation rep = new JsonRepresentation(response.getEntity());
                try {
                    JSONArray result = rep.getValue().isArray();
                    if (result != null) {
                        for (int i = 0; i < result.size(); i++) {
                            final String coreName = result.get(i).isString().stringValue();
                            getParent().registerAction(self, "Core " + coreName, new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    SC.say("Core " + coreName + " selected");
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
