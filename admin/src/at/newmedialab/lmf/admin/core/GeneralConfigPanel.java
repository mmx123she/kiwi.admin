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

package at.newmedialab.lmf.admin.core;

import at.newmedialab.lmf.admin.client.Admin;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.ext.json.JsonRepresentation;
import org.restlet.client.resource.ClientResource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class GeneralConfigPanel extends VLayout {

    private ListGrid configurationGrid;

    private Admin admin;

    public GeneralConfigPanel(Admin admin) {
        super();
        this.admin = admin;
        initialiseConfigurationGrid();
    }

    public void initialiseConfigurationGrid() {
        setMembersMargin(20);
        setMargin(5);

        configurationGrid = new ListGrid();
        configurationGrid.setWidth100();
        //configurationGrid.setHeight100();
        configurationGrid.setShowFilterEditor(true);
        configurationGrid.setFilterOnKeypress(true);
        configurationGrid.setShowAllRecords(true);
        configurationGrid.setSortField("key");
        configurationGrid.setSortDirection(SortDirection.ASCENDING);
        configurationGrid.setAutoFitData(Autofit.VERTICAL);

        final ListGridField fieldKey = new ListGridField("key", "Configuration Key", 250);
        fieldKey.setRequired(true);
        fieldKey.setCanEdit(false);

        ListGridField fieldValue = new ListGridField("value", "Configuration Value");

        configurationGrid.setFields(fieldKey, fieldValue);
        configurationGrid.setCanEdit(true);
        configurationGrid.setEditEvent(ListGridEditEvent.CLICK);
        configurationGrid.setListEndEditAction(RowEndEditAction.NEXT);

        addMember(configurationGrid);

        IButton button = new IButton("Edit New");
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                fieldKey.setCanEdit(true);
                configurationGrid.startEditingNew();
            }
        });
        addMember(button);


        loadConfigurationData();
    }

    public void loadConfigurationData() {
        ClientResource coreClient = new ClientResource(admin.getServerUrl() + "config/list");


        coreClient.setOnResponse(new Uniform() {
            @Override
            public void handle(Request request, Response response) {
                // Get the representation as an JsonRepresentation
                JsonRepresentation rep = new JsonRepresentation(response.getEntity());

                ArrayList<ListGridRecord> records = new ArrayList<ListGridRecord>();

                try {
                    JSONObject result = rep.getValue().isObject();

                    // iterate over all keys in the result and create listGridRecords; distinguish between single-value
                    // and multi-value fields
                    for (String key : result.keySet()) {
                        ListGridRecord record = new ListGridRecord();
                        record.setAttribute("key", key);
                        JSONObject value = result.get(key).isObject();
                        if (value != null && value.get("value").isString() != null) {
                            record.setAttribute("value", value.get("value").isString().stringValue());
                        } else if (value != null && value.get("value").isArray() != null) {
                            StringBuilder builder = new StringBuilder();
                            JSONArray values = value.get("value").isArray();
                            ArrayList<String> valArray = new ArrayList<String>();
                            for (int i = 0; i < values.size(); i++) {
                                valArray.add(values.get(i).isString().stringValue());
                            }
                            record.setAttribute("value", valArray.toArray(new String[0]));
                        }
                        if (value != null && value.get("comment").isString() != null) {
                            record.setAttribute("comment", value.get("comment").isString().toString());
                        }
                        records.add(record);
                    }

                    configurationGrid.setData(records.toArray(new ListGridRecord[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        coreClient.get();

    }

}
