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

package at.newmedialab.lmf.admin.client;

import at.newmedialab.lmf.admin.core.CoreConfiguration;
import at.newmedialab.lmf.admin.search.PathMappingAdminPanel;
import at.newmedialab.lmf.admin.search.SearchConfiguration;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.*;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class Admin implements EntryPoint {

    public static final String SERVER_URL = "http://localhost:8080/LMF/";


    private VStack bodyLayout;
    private HLayout mainLayout;


    private SectionStack navBar;


    private List<Configuration> modules;


    private HashMap<Configuration, VLayout> moduleMenus;


    private TabSet contentCanvas;


    public void onModuleLoad() {
        modules = new LinkedList<Configuration>();
        modules.add(new CoreConfiguration(this));
        modules.add(new SearchConfiguration(this));

        moduleMenus = new HashMap<Configuration, VLayout>();


        createAdminOverview();
    }


    public void createAdminOverview() {
        RootPanel rootPanel = RootPanel.get("adminPanel");
        rootPanel.clear();


        bodyLayout = new VStack();
        bodyLayout.setWidth100();
        bodyLayout.setAutoHeight();
        bodyLayout.setMinHeight(800);
        bodyLayout.setOverflow(Overflow.VISIBLE);
        bodyLayout.setMembersMargin(5);
        bodyLayout.setMargin(8);
        rootPanel.add(bodyLayout);

        createTitleBar();

        mainLayout = new HLayout();
        mainLayout.setMembersMargin(5);
//        mainLayout.setDragAppearance(TARGET);
//        mainLayout.setOverflow(Overflow.HIDDEN);
//        mainLayout.setCanDragResize(true);
//        mainLayout.setResizeFrom("L", "R");
//        mainLayout.setHeight("*");
        mainLayout.setOverflow(Overflow.VISIBLE);
        mainLayout.setWidth100();
        mainLayout.setHeight(630);
//        mainLayout.setAutoHeight();

        bodyLayout.addMember(mainLayout);

        createNavigationBar();
        createOverviewPanel();

        bodyLayout.draw();
    }


    private void createTitleBar() {
        HTMLFlow titleBar = new HTMLFlow("<h1>LMF Administration</h1>");
        titleBar.setHeight(60);
        titleBar.setWidth100();

        bodyLayout.addMember(titleBar);
    }


    private void createNavigationBar() {
        navBar = new SectionStack();
        navBar.setVisibilityMode(VisibilityMode.MULTIPLE);
        navBar.setWidth(250);
        navBar.setMinHeight(600);
        navBar.setCanDragResize(true);
        navBar.setResizeFrom("R");


        final Admin self = this;
        for (final Configuration cfg : modules) {
            SectionStackSection stack = new SectionStackSection(cfg.getName());
            stack.setExpanded(true);

            VLayout content = new VLayout();
            content.setMembersMargin(5);
            content.setMargin(5);
            moduleMenus.put(cfg, content);

            cfg.registerActions();


            stack.addItem(content);

            navBar.addSection(stack);
        }


        mainLayout.addMember(navBar);
    }


    private void createOverviewPanel() {
        TabSet tabSet = new TabSet();
        tabSet.setTabBarPosition(Side.TOP);


        Tab overview = new Tab("Overview");
        overview.setPane(new PathMappingAdminPanel());
        tabSet.addTab(overview);

        setContentPanel(tabSet);
    }


    /**
     * Register a action menu entry for the specified module.
     *
     * @param module
     * @param action
     */
    public void registerAction(final Configuration module, String label, ClickHandler action) {
        VLayout content = moduleMenus.get(module);

        if (content != null) {
            Label link = new Label(label);
            link.addStyleName("clickable");
            link.setHeight(20);

            link.addClickHandler(action);
            content.addMember(link);
        }

    }


    public void setContentPanel(TabSet canvas) {
        if (contentCanvas != null) {
            mainLayout.removeMember(contentCanvas);
        }
        contentCanvas = canvas;
        contentCanvas.setWidth("*");
        contentCanvas.setMinHeight(600);
        contentCanvas.setCanDragResize(true);
        contentCanvas.setResizeFrom("L");
        contentCanvas.setOverflow(Overflow.VISIBLE);
        contentCanvas.setPaneContainerOverflow(Overflow.VISIBLE);

        mainLayout.addMember(contentCanvas);
    }


    public String getServerUrl() {
        return SERVER_URL;
    }

}
