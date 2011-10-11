package at.newmedialab.lmf.admin.client;

import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public interface Configuration {

    /**
     * Get a displayable name for the configuration module
     * @return
     */
    public String getName();

    /**
     * Get a more detailed description for the configuration module
     * @return
     */
    public String getDescription();


    /**
     * Register all actions that are available in the configuration module. The method should call the
     * Admin.registerAction() method to carry out the registration.
     * @return
     */
    public void registerActions();


}
