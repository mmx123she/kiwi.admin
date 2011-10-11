package at.newmedialab.lmf.admin.core;

import at.newmedialab.lmf.admin.client.AbstractConfiguration;
import at.newmedialab.lmf.admin.client.Admin;
import at.newmedialab.lmf.admin.client.Configuration;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Configuration for the core admin module.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class CoreConfiguration extends AbstractConfiguration {

    private Admin parent;

    public CoreConfiguration(Admin parent) {
        super(parent);
    }

    /**
     * Get a displayable name for the configuration module
     *
     * @return
     */
    @Override
    public String getName() {
        return "LMF Core";
    }

    /**
     * Get a more detailed description for the configuration module
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Configuration tasks for the LMF Core system: general configuration, database, logging";
    }

    /**
     * List all actions that are available in the configuration module
     *
     * @return
     */
    @Override
    public void registerActions() {
        for(final String action : Arrays.asList("General", "Database", "Logging")) {
            getParent().registerAction(this,action, new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    SC.say(action + " clicked");
                }
            });
        }
    }


}
