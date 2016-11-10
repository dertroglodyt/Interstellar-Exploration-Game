/*
 * Copyright DerTroglodyt
 */
package de.hdc.commonlibrary.data;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The "glue" between a DataAtom and it's viewers / editors.
 * Handles "editing" the immutable DataAtoms and informing everyone of changes.
 *
 * @author dertroglodyt
 */
public class DataChangeManager {

    //Used only for deserialization.
    @Deprecated
    public DataChangeManager() {
        super();
        changed = false;
    }

    /**
     * Signals if the data was isChanged since creation of instance
     *
     * @return True if data was isChanged.
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * registers a listener to changes on the data of the object
     *
     * @param listener Listener to be added to ware.
     */
    public void addListener(DataChangeListener listener) {
        if (this.listener == null) {
            this.listener = new CopyOnWriteArraySet<>();
        }
        this.listener.add(listener);
    }

    /**
     * removes a listener to changes on the data of the object
     *
     * @param listener Listener to be deleted from ware.
     */
    public void removeListener(DataChangeListener listener) {
        if (this.listener == null) {
            return;
        }
        this.listener.remove(listener);
    }

    @Override
    public String toString() {
        return "DataModel{changed=" + changed + ", listener=" + listener + '}';
    }
    /**
     * Indicates if data has isChanged and needs saving.
     */
    private transient boolean changed;
    /**
     * Viewer or editor (IDataChangeListener) that is listening for changes.
     * Now allows for additions / deletions while notifying.
     * Lazy Initialization for reduced overhead.
     */
    private transient CopyOnWriteArraySet<DataChangeListener> listener;

    /**
     * Notifies the registered listeners of changes on the data of the object.
     *
     * @param sender The sending data model.
     */
    private synchronized void notifyListener(final IDataAtom sender) {
        if (this.listener == null) {
            return;
        }
        for (DataChangeListener l : listener) {
            if (!l.equals(sender)) {
                l.changeNotify(sender);
            }
        }
    }

}
