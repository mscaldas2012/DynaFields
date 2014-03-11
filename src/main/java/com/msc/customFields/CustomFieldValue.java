package com.msc.customFields;


import com.msc.utils.CollectionUtils;
import com.msc.utils.StringUtils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 26, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
//@Entity
public abstract class CustomFieldValue  {
    private String value;
    private Calendar dateValue;
    //The pk for the Custom Field definition of this value.
    private Long cfDefId;

    private Long sequence;

    private CustomFieldValue groupRow;
    protected Set cells;

	private Object owner;
    private Long pk;
    private boolean deleted;

    /**
     * @hibernate.id column="custom_field_data_id" generator-class="native"
     * @return The primary key of this instance.
     */
    public Long getPk() {
        return pk;
    }
    /**
     * @hibernate.property column="custom_field_definition_id" not-null="true" update="false"
     * @return The customFieldDefinition this value is associated with.
     */
    public Long getCfDefId() {
        return cfDefId;
    }
    public void setCfDefId(Long cfDefId) {
        this.cfDefId = cfDefId;
    }

    /**
     * @hibernate.property column="value"
     * @return the Value the user gave for this custom field.
     */
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * @hibernate.property column="date_value"
     * @return The value for date fields off this custom field.
     */
    public Calendar getDateValue() {
        return dateValue;
    }
    public void setDateValue(Calendar dateValue) {
        this.dateValue = dateValue;
    }
    /**
     * @hibernate.property column="sequence"
     * @return The sequence on which this value was answered.
     */
    public Long getSequence() {
        return sequence;
    }
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

//    public abstract PhindirObject getOwner() ;
//    public abstract void setOwner(PhindirObject newValue);
	public Object getOwner() {
		return this.owner;
	}
	public void setOwner(Object newValue) {
		this.owner =  newValue;
	}


    /**
     * hibernate.many-to-one column="group_row_id" class="gov.phindir.platform.common.customFields.transferObjects.CustomFieldValue" cascade="none" not-null="false"   insert="true" update="false"
     * @return The "row" this answer belongs to...
     */
    public CustomFieldValue getGroupRow() {
        return groupRow;
    }
    public void setGroupRow(CustomFieldValue groupRow) {
        this.groupRow = groupRow;
    }
//    public abstract Set getCells();
    /**
     * hibernate.set table="CUSTOM_FIELD_DATA" cascade="all" inverse="true" lazy="false" order-by="sequence"
     * hibernate.collection-key column="group_row_id" name="groupRow"
     * hibernate.collection-one-to-many class="gov.phindir.platform.common.customFields.transferObjects.CustomFieldValue"
     * return The values for all cells that belongs to this group.
     */
    public Set getCells() {
        return cells;
    }    
    public void setCells(Set cells) {
        this.cells = cells;
    }
    public void addCell(CustomFieldValue cell) {
        if (this.cells == null) {
            this.cells = new HashSet();
        }
        cell.setGroupRow(this);
        this.cells.add(cell);
    }

    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("CustomFieldValue");
        sb.append("{pk=").append(getPk());
        if (value != null) {
            sb.append(", value='").append(value).append('\'');
        }
        if (dateValue != null) {
            sb.append(", dateValue='").append(dateValue.getTime()).append('\'');
        }
        sb.append(", cfDefId=").append(cfDefId);
        sb.append(", sequence=").append(sequence);
        sb.append('}');
        return sb.toString();
    }
    public int hashCode() {
        int result = 0;
        if (this.getPk() != null) {
            result += this.getPk().hashCode();
        }
        if (this.cfDefId != null) {
            result += this.cfDefId.hashCode();
        }
        if (this.value != null) {
            result += this.value.hashCode();
        } else if (this.dateValue != null) {
            result += this.dateValue.hashCode();
        } else if (this.getCells() != null) {
            result += this.getCells().hashCode();
        }
        return result;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CustomFieldValue that = (CustomFieldValue) o;

        if (this.getCfDefId() == null || !this.getCfDefId().equals(that.getCfDefId())) {
            return false;
        }

        final int thisSize = this.getNonDeletedItemsSize();
        if (thisSize > 0) {
            if (that.getCells() == null || that.getNonDeletedItemsSize() != thisSize) {
                return false;
            }
            boolean theyAreEquals = true;
            for (Iterator iterator = this.getCells().iterator(); iterator.hasNext() && theyAreEquals;) {
                CustomFieldValue thisCell = (CustomFieldValue) iterator.next();
                CustomFieldValue thatCell = getCellForDefId(thisCell.getCfDefId(), that.getCells());
                if (thisCell.isDeleted()) {
                    theyAreEquals = thatCell == null;
                } else {
                    theyAreEquals = thisCell.equals(thatCell);
                }
            }
            return theyAreEquals;
        } else if (this.getDateValue() != null) {
            return this.getDateValue().equals(that.getDateValue());
        } else {
            return StringUtils.isSame(this.getValue(), that.getValue()) && (this.isDeleted()==that.isDeleted()) && CollectionUtils.isEmpty(that.getCells());
        }
    }
    public int getNonDeletedItemsSize() {
        int result = 0;
        if (CollectionUtils.isNotEmpty(this.getCells())) {
            boolean found = false;
            for (Iterator iterator = this.getCells().iterator(); iterator.hasNext() && !found;) {
                CustomFieldValue customFieldValue = (CustomFieldValue) iterator.next();
                result += customFieldValue.isDeleted()?0:1;
            }
        }
        return result;
    }
    private CustomFieldValue getCellForDefId(Long cfDefId, Set cells) {
        boolean found = false;
        CustomFieldValue fieldValue = null;
        for (Iterator iterator = cells.iterator(); iterator.hasNext() && !found;) {
            fieldValue = (CustomFieldValue) iterator.next();
            found = fieldValue.getCfDefId() != null && fieldValue.getCfDefId().equals(cfDefId) && !fieldValue.isDeleted();
        }
        if (found) {
            return fieldValue;
        } else {
            return null;
        }
    }

    public boolean isDeleted() {
        return deleted || (StringUtils.isEmpty(this.getValue()) && this.getDateValue() == null && getNonDeletedItemsSize() == 0);
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
