package at.newmedialab.lmf.admin.client;

import at.newmedialab.lmf.admin.search.PathMappingAdminPanel;
import at.newmedialab.lmf.admin.search.SearchAdminPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class Admin implements EntryPoint {

    public static final String SERVER_URL="http://localhost:8080/LMF/";


    public void onModuleLoad() {
        createAdminOverview();
    }



    public void createAdminOverview() {
        RootPanel rootPanel = RootPanel.get("adminPanel");
        rootPanel.clear();

        TabSet tabSet = new TabSet();
        tabSet.setTabBarPosition(Side.TOP);
        tabSet.setWidth(800);
        tabSet.setHeight100();

        Tab tSearch = new Tab("Search");
        tSearch.setPane(new SearchAdminPanel());
        tabSet.addTab(tSearch);

        Tab tPath   = new Tab("Path Mappings");
        tPath.setPane(new PathMappingAdminPanel());
        tabSet.addTab(tPath);

        rootPanel.add(tabSet);

        /*
        final VLayout layout = new VLayout(20);
        layout.setWidth(800);
        rootPanel.add(layout);


        HTMLFlow greeting = new HTMLFlow();
        greeting.setContents("<h2>Admin Panel (Hello World!!!)</h2>");
        layout.addMember(greeting);

        layout.draw();
        */
    }
}
