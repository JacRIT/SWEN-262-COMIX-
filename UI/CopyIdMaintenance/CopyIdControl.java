package UI.CopyIdMaintenance;

import java.util.LinkedList;
import java.util.List;

/**
 * CopyIdControl maintains a collection of all the CopyIdRecords created during the active session.
 * Only one CopyIdControl should be created per session.
 */
public class CopyIdControl {
    private List<CopyIdRecord> records;
    
    public CopyIdControl() {
        this.records = new LinkedList<>();
    }

    /**
     * Adds a new CopyIdRecord to CopyIdControl.
     * @param id the current id of the newly created CopyIdRecord
     * @return the newly created CopyIdRecord
     */

    public CopyIdRecord addRecord(int id) {
        CopyIdRecord record = new CopyIdRecord(id);
        this.records.add(record);
        return record;
    }

    /**
     * Checks if the id is contained in an existing CopyIdRecord and returns it if found.
     * If the id is not found, a new CopyIdRecord is created, added to this CopyIdControl, and returned.
     * @param id the id that will be contained in the return CopyIdRecord
     * @return a CopyIdRecord that contains the given id, either as the current id or an old id
     */
    public CopyIdRecord findOrAdd(int id) {
        CopyIdRecord record = null;
        for(CopyIdRecord cur : this.records) {
            if (cur.contains(id)) {
                record = cur;
                break;
            }
        }
        if (record == null) {
            record = this.addRecord(id);
        }
        return record;
    }
}
