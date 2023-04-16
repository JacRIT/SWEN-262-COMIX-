package UI.CopyIdMaintenance;

import java.util.HashSet;
import java.util.Set;

import Model.JavaObjects.Comic;

/**
 * CopyIdRecord maintains links between past versions of a copy's id and the current one.
 * This allows commands to be undone and redone even after doing a different command causes 
 * changes to a copy's id.
 */
public class CopyIdRecord {
    private int currentId;
    private Set<Integer> oldIds;

    /**
     * Creates an instance of a CopyIdRecord.
     * @param id the current id for the record to be started with.
     */
    public CopyIdRecord(int id) {
        this.currentId = id;
        this.oldIds = new HashSet<>();
    }

    /**
     * Moves the current id into the collection of old ids, and sets the new current id.
     * @param id the new current id
     */
    public void setCurrentId(int id) {
        this.oldIds.add(this.currentId);
        this.currentId = id;
    }

    /**
     * Checks whether the given id is in this record, either as the current id or an old id.
     * @param id the id the record is being checked for
     * @return true is the id is present in the record and false if not
     */
    public boolean contains(int id) {
        return (this.currentId == id) || (this.oldIds.contains(id));
    }

    /**
     * Sets the comic's copy id as this record's current id.
     * Assumes the given comic's copyId is in the record.
     * @param comic the comic whose copy id is being updated.
     */
    public void updateComic(Comic comic) {
        comic.setCopyId(this.currentId);
    }
}
