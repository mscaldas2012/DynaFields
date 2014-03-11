package com.msc.customFields;


import com.msc.customFields.dataAccess.AuditTrailImpl;

import javax.persistence.*;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 24, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 *
 *
 */
@Entity
@Table(name="CUSTOM_FIELD_DEF_LIST")
public class CustomFieldDefList extends AuditTrailImpl {
    @Id
    @Column(name="id" )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk ;

    @Column
    private String item;
    @Column
	private String code;
    @Column
	private String description;
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="owner_id", nullable=false, updatable=false)
    private CustomFieldDefinition owner;
    @Column
	private Long sequence;

    public CustomFieldDefList() {
    }

    public CustomFieldDefList(String item, String code, String description, Long sequence) {
        this.description = description;
        this.item = item;
        this.code = code;
        this.sequence = sequence;
    }

    /**
	 *  @return the label of this list value.
	 */
	public String getItem() {
		return this.item;
	}
	public void setItem(String newValue) {
		this.item = newValue;
	}
	/**
	 * @return A description for this value.
	 */
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String newValue) {
		this.description = newValue;
	}
	/**
	 * @return The sequence associated with this item.
	 */
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return The Custom field definition that owns this value
	 */
	public CustomFieldDefinition getOwner() {
		return owner;
	}
	public void setOwner(CustomFieldDefinition owner) {
		this.owner = owner;
	}
	/**
	 * @return The code associated with this item.
	 */
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String toString() {
        return "CustomFieldDefList" + "{item='" + item + '\'' + ", sequence=" + sequence + '}';
	}

    public Long getPk() {
        return pk;
    }

    public void setPk(Long pk) {
        this.pk = pk;
    }

}
