package com.msc.customFields.dataAccess;

import javax.persistence.*;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * This code was written by Marcelo Caldas.
 * e-Mail: mscaldas@gmail.com
 * <p/>
 * \* Project: DynaFields
 * <p/>
 * Date: 3/8/14
 * <p/>
 * Enjoy the details of life.
 */
@MappedSuperclass
public abstract class AuditTrailImpl implements AuditTrail {
    private static final Logger logger = Logger.getLogger(AuditTrailImpl.class.getName());

    @Column
    private Calendar createdDate;
    @Column
    private Calendar lastModifiedDate;

    @Column
    private String createdBy;
    @Column
    private String lastModifiedBy;

    @Transient
    private boolean deleted;


    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Calendar createdDate) {
        this.createdDate = createdDate;
    }

    public Calendar getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Calendar lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
