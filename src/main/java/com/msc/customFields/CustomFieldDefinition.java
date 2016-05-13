package com.msc.customFields;

import com.msc.customFields.dataAccess.AuditTrailImpl;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>This is the main class that Defines the metadata for a Custom Field!
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 5, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 *
 */
@Entity
@Table(name="CUSTOM_FIELD_DEFINITION")
//@PrimaryKeyJoinColumn(name="id")
public class CustomFieldDefinition extends AuditTrailImpl  {
    @Id
    @Column (name="id")
    @TableGenerator(
            name="cfDefinition",
            table="ID_GEN",
            pkColumnName="GEN_KEY",
            valueColumnName="GEN_VALUE",
            pkColumnValue="CFDEF_ID",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.TABLE, generator="cfDefinition")
    private Long pk ;

    /** The reason to have Custom fields is because different "owners" might have different data to collect. */
    //Owner is defined outside the scope of this framework, but is mandatory.
    @Column(name="form_id", nullable = false, updatable = false)
    private long formId;
    /** This property identifies which class this definition should be attached to. This is not for reflection purposes.
        It can be set to any value. It's used just as an identifier of which class this definition is associated with. */
    @Column(name="class", nullable = false)
    private String clazz;
    /** A general implementation for Scope: What is the scope of this definition?
       The scope should be defined outside the context of this implementation.
       We can save it as a identifier (number, OID) or we can save it as json object or name=value pairs.
       But it should represent a FK of sorts to a scope defined by the user of this framework. */
    @Column(name="scope")
    private String scope;

	/** The sequence of how the custom fields should be presented to the user. */
    @Column(name="sequence")
	private Long sequence = 0L;
	/** The name of this custom Field. It will be used as label on generic UIs */
    @Column(name="name", nullable = false)
	private String name;
	/**
	 * The type of this field. Currently we support String, Number, boolean, Date, and List.
	 */
    @Column(name="fieldType", nullable = false)
	private FieldType fieldType;
	/**
	 * Applied only to fieldTypes of NUMBER
	 * the number of decimal digits allowed if fieldType is Number.
	 * Set it to 0 for Longs, and greater than 0 for floats.
	 * (2 for money fields, for example)
	 */
    @Column(name="decimalDigits")
	private Long decimalDigits;
	/**
	 * Applied only to fieldTypes of NUMBER or DATEs
	 * The minimum value allowed for this field. It can hold either a number or a date.
	 * Dates are stored as Longs of milliseconds as in Date.getTime()
	 */
    @Column(name="minValueAllowed")
	private Double minValueAllowed;
	/**
	 * Applied only to fieldTypes of NUMBER or DATEs
	 * The maximum value allowed for this field. It can hold either a number or a date.
	 * Dates are stored as Longs of milliseconds as in Date.getTime()
	 */
    @Column(name="maxValueAllowed")
	private Double maxValueAllowed;
	/**
	 * Applied only to fieldTypes of STRING
	 * The minimum lenght of String fields.
	 */
    @Column(name="minLength")
	private Long minLength;
	/**
	 * Applied only to fieldTypes of STRING
	 * The maximum lenght of String fields.
	 */
    @Column(name="maxLength")
	private Long maxLength;
	/**
	 * Applied only to fieldTypes of STRING
	 * The regular expression this field should hold
	 */
    @Column(name="regExpression")
	private String regExpression;
	/**
	 * The minimum set of answers this custom field should require.
	 * Set it to 0 to make it not mandatory. 1 or greater for mandatory.
	 * (2 if you require at least two answers for this custom field.)
	 */
    @Column(name="minRequired", nullable = false)
	private Long minRequired;
	/**
	 * The maximum set of answers this custom field should allow.
	 * Set it to 1 if only one answer is allowed, -1 for as many as the user wants.
	 * (2 if you allow a maximum of two answers.)
	 *
	 * The minRequired and maxAllowed properties should allow you to set any kind of multiplicity:
	 * <UL>
	 * 		<LI>0..1 --> Not mandatory, maximum of one answer allowed (Most common)</LI>
	 * 		<LI>1..1 --> Mandatory and maximum of one answer allowed.</LI>
	 * 		<LI>0..0 --> Not mandatory, allow as many answers as needed.</LI>
	 * 		<LI>1..0 --> Mandatory and allow as many answers as needed.</LI>
	 * 		<LI>2..5 --> 2 answers mandatory, maximum of 5 allowed.</LI>
	 * </UL>
	 */
    @Column(name="maxAllowed", nullable = false)
	private Long maxAllowed;
	/**
	 * If fieldType is Enum, then it has to hold the list of
	 * values.
	 */
    @OneToMany (mappedBy = "owner", cascade= CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<CustomFieldDefList> listValues;

    //Self relationship for CF Definitions that belongs to Group.
    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="group_id", nullable=true, updatable=false)
	private CustomFieldDefinition belongsToGroup;

    @OneToMany (mappedBy="belongsToGroup", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<CustomFieldDefinition> groupColumns;

    @Column(name="description")
	private String description;
    @Column(name="defaultValue")
	private String defaultValue;
    @Column(name="displayFormat")
	private int displayFormat;

    public static final int TABLE_FORMAT = 1;
	public static final int FORM_FORMAT = 2;

    public long getFormId() {
        return formId;
    }

    public void setFormId(long formId) {
        this.formId = formId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }

	/**
	 * @return The name of this custom field. It must be unique per Scope.
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return The type of this field - String, Number, Boolean, Date, etc.
	 */
	public FieldType getFieldType() {
		return fieldType;
	}
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	/**
	 * @return The number of decimal digits allowed for this custom field, if fieldType is NUMBER.
	 */
	public Long getDecimalDigits() {
		return decimalDigits;
	}
	public void setDecimalDigits(Long decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	/**
	 * @return the sequence in which this custom field should be presented
	 */
	public Long getSequence() {
		return sequence;
	}
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return The minimum number of answers needed for this custom field. 0 is not mandatory, 1 or greater is
	 *         mandatory.
	 */
	public Long getMinRequired() {
		return minRequired;
	}
	public void setMinRequired(Long minRequired) {
		this.minRequired = minRequired;
	}
	/**
	 * @return The maximum number allowed of answers for this custom field. -1 represents unlimited!
	 */
	public Long getMaxAllowed() {
		return maxAllowed;
	}
	public void setMaxAllowed(Long maxAllowed) {
		this.maxAllowed = maxAllowed;
	}
	/**
	 * @return The maximum length allowed for this custom field. Valid for String type objects.
	 */
	public Long getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}
	/**
	 * @return the minimum length allowed for this custom field. Valid for String type objects.
	 */
	public Long getMinLength() {
		return minLength;
	}
	public void setMinLength(Long minLength) {
		this.minLength = minLength;
	}
	/**
	 * @return the Maximun value allowed for this custom field. Valid for Numbers and date fields.
	 */
	public Double getMaxValueAllowed() {
		return maxValueAllowed;
	}
	public void setMaxValueAllowed(Double maxValueAllowed) {
		this.maxValueAllowed = maxValueAllowed;
	}
	/**
	 * @return The minimum value allowed for this custom field. Valid for number and date fields.
	 */
	public Double getMinValueAllowed() {
		return minValueAllowed;
	}
	public void setMinValueAllowed(Double minValueAllowed) {
		this.minValueAllowed = minValueAllowed;
	}

	/**
	 * @return The regular expression to validate this field against.
	 */
	public String getRegExpression() {
		return regExpression;
	}
	public void setRegExpression(String regExpression) {
		this.regExpression = regExpression;
	}
	/**
	 * hibernate.set table="CUSTOM_FIELD_LIST" cascade="all"  inverse="true" lazy="false" order-by="sequence"
	 * hibernate.collection-key column="custom_field_definition_id"
	 * hibernate.collection-one-to-many class="gov.phindir.platform.common.customFields.transferObjects.CustomFieldDefList"
	*/
	public Set<CustomFieldDefList> getListValues() {
		return listValues;
	}
	public void setListValues(Set<CustomFieldDefList> listValues) {
		this.listValues = listValues;
	}
	public void addListValue(CustomFieldDefList newValue) {
		if (this.listValues == null) {
			this.listValues = new HashSet<CustomFieldDefList>();
		}
		newValue.setOwner(this);
		this.listValues.add(newValue);
	}
	public Object getGroupKey() {
        return getScope();
		//return new CustomFieldDefGroupKey(objectType, ownerId);
	}

    //Todo:: Why size here? maybe something is wrong on GroupCacheable Interface...

    public long size() {
        return 0;
    }

    public Object getCacheKey() {
		return this.getPk();
	}

//	public static CustomFieldDefGroupKey generateGroupKey(String objectTypeAppCode, Long applicationId) {
//		return new CustomFieldDefGroupKey(objectTypeAppCode, applicationId);
//	}
	/**
	 * @return The description for this field definition.
	 */
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The default value to be assigned to this custom field.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * hibernate.many-to-one column="group_id" class="gov.phindir.platform.common.customFields.transferObjects.CustomFieldDefinition" cascade="none" not-null="false"   insert="true" update="false"
	 * @return If this Custom Field Definition belongs to a Group, the CFDef that represents the group.
	 */
	public CustomFieldDefinition getBelongsToGroup() {
		return belongsToGroup;
	}
	public void setBelongsToGroup(CustomFieldDefinition belongsToGroup) {
		this.belongsToGroup = belongsToGroup;
	}
	/**
	 * hibernate.set table="CUSTOM_FIELD_DEFINITION" cascade="all" inverse="true" lazy="false" order-by="sequence"
	 * hibernate.collection-key column="group_id" name="belongsToGroup"
	 * hibernate.collection-one-to-many class="gov.phindir.platform.common.customFields.transferObjects.CustomFieldDefinition"
	 * @return The list of Columns that belong to a specific Group.
	 */
	public Set<CustomFieldDefinition> getGroupColumns() {
		return groupColumns;
	}
	public void setGroupColumns(Set<CustomFieldDefinition> groupColumns) {
		this.groupColumns = groupColumns;
	}
	public void addGroupColumn(CustomFieldDefinition column) {
		if (this.groupColumns == null) {
			this.groupColumns = new HashSet<CustomFieldDefinition>();
		}
		column.setBelongsToGroup(this);
		this.groupColumns.add(column);
	}

	/**
	 * hibernate.property column="display_format"
	 * @return The display format for a given group.
	 */
	public int getDisplayFormat() {
		return displayFormat;
	}
	public void setDisplayFormat(int displayFormat) {
		this.displayFormat = displayFormat;
	}

    public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CustomFieldDefinition");
		sb.append("{pk=").append(this.getPk());
        sb.append(", owner='").append(this.getFormId()).append('\'');
        sb.append(", Class='").append(clazz).append('\'');
        sb.append(", scope=").append(this.getScope());
		sb.append(", name='").append(name).append('\'');
		sb.append(", sequence=").append(sequence);
		sb.append(", fieldType=").append(fieldType);
		if (fieldType != null) {
			if (fieldType == FieldType.STRING) {
				sb.append(", minLength=").append(minLength);
				sb.append(", maxLength=").append(maxLength);
				sb.append(", regExpression='").append(regExpression).append('\'');
			} else if (fieldType == FieldType.NUMBER) {
				sb.append(", decimalDigits=").append(decimalDigits);
				sb.append(", minValueAllowed=").append(minValueAllowed);
				sb.append(", maxValueAllowed=").append(maxValueAllowed);
			} else if (fieldType == FieldType.LIST) {
				sb.append(", listValues=").append(listValues);
			} else if (fieldType == FieldType.DATE) {
				sb.append(", minValueAllowed=").append(minValueAllowed);
				sb.append(", maxValueAllowed=").append(maxValueAllowed);
			}
		}
		sb.append(", minRequired=").append(minRequired);
		sb.append(", maxAllowed=").append(maxAllowed);
		sb.append('}');
		return sb.toString();
	}

    public Long getPk() {
        return pk;
    }
    public void setPk(Long pk) {
        this.pk = pk;
    }
}

