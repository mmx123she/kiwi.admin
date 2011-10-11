package at.newmedialab.lmf.admin.client;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public abstract class AbstractConfiguration implements Configuration {

    private Admin parent;

    protected AbstractConfiguration(Admin parent) {
        this.parent = parent;
    }

    public Admin getParent() {
        return parent;
    }

    public void setParent(Admin parent) {
        this.parent = parent;
    }
}
