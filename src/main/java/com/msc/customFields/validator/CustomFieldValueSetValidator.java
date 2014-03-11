package com.msc.customFields.validator;

import com.msc.customFields.CustomFieldDefinition;
import com.msc.customFields.CustomFieldSupport;
import com.msc.customFields.CustomFieldValue;
import com.msc.customFields.FieldType;
import com.msc.customFields.manager.CustomFieldDefinitionManager;
import com.msc.customFields.validator.ValidationException;
import com.msc.utils.CollectionUtils;
import com.msc.utils.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * <P>Date: Feb 2, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public class CustomFieldValueSetValidator {
	private static final String CUSTOM_FIELD_DEF_CACHE = "CUSTOM_FIELD_DEF_CACHE";
	private static final String ERROR_MSG_MINIMUM_REQUIRED_NOT_PROVIDED = "Field {0} require at least {1} value(s).";
	private static final String ERROR_MSG_MAX_ALLOWED_EXCEEDED = "Field {0} allows a maximum of {1} value(s).";
    private static final String ERROR_MSG_INVALID_CUSTOM_FIELD = "Unable to find Custom Field definition for {0}";

    //IoC
    CustomFieldDefinitionManager manager ;

    public void validate(CustomFieldSupport owner) throws Exception {
        //Get all definitions for this object:
//        Cacheable groupKey = new CustomFieldDefGroupKey(owner.getOwnerId(), owner.getClazz());
        Collection cfDefs = manager.findByOwnerIdAndClass(owner.getOwnerId(), owner.getClazz());
        //Validate each object individually:
        if (CollectionUtils.isNotEmpty(owner.getCustomFields())) {
            //CustomFieldValueValidator cfValidator =
            CustomFieldDefinition def;
            for (Object o : owner.getCustomFields()) {
                CustomFieldValue field = (CustomFieldValue) o;
                def = this.getDefFor(field.getCfDefId(), cfDefs);
                if (def == null) {
                    throw new ValidationException(ERROR_MSG_INVALID_CUSTOM_FIELD, field.getCfDefId().toString());
                }
                if (def.getScope() != null && !StringUtils.isSame(def.getScope(), owner.getScope())) {
                    //Object subtype no longer matches... delete orphan...
                    field.setDeleted(true);
                }
            }
        }
        //Validate Multiplicity:
        if (CollectionUtils.isNotEmpty(cfDefs)) {
            for (Object cfDef1 : cfDefs) {
                CustomFieldDefinition cfDef = (CustomFieldDefinition) cfDef1;
                if (StringUtils.isSame(owner.getScope(), cfDef.getScope())) {
                    checkMinAndMax(cfDef, owner.getCustomFields());
                    if (FieldType.GROUP.equals(cfDef.getFieldType())) {
                        //validate cells:
                        for (Object o : cfDef.getGroupColumns()) {
                            CustomFieldDefinition columnDef = (CustomFieldDefinition) o;
                            if (CollectionUtils.isNotEmpty(owner.getCustomFields())) {
                                for (Object o1 : owner.getCustomFields()) {
                                    CustomFieldValue group = (CustomFieldValue) o1;
                                    //set the deleted flag independent of a value being set:
                                    //If this group is the same of the def group we're validating...
                                    if ((group.getCfDefId().equals(cfDef.getPk()) && !group.isDeleted())) {
                                        group.setDeleted(group.isDeleted() || group.getNonDeletedItemsSize() == 0);
                                        if (!group.isDeleted()) {
                                            checkMinAndMax(columnDef, group.getCells());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private CustomFieldDefinition getDefFor(Long cfDefId, Collection cfDefs) {
        boolean found = false;
        CustomFieldDefinition def =  null;
        for (Iterator iterator = cfDefs.iterator(); iterator.hasNext() && !found;) {
            def = (CustomFieldDefinition) iterator.next();
            found = def.getPk().equals(cfDefId);
        }
        if (found) {
            return def;
        }
        return null;
    }

    private void checkMinAndMax(CustomFieldDefinition cfDef, Collection fieldValues) throws ValidationException {
        int foundCounter = 0;
        if ((cfDef.getMinRequired() > 0) || (cfDef.getMaxAllowed() > 0)) {
            if (CollectionUtils.isNotEmpty(fieldValues)) {
                for (Object fieldValue1 : fieldValues) {
                    CustomFieldValue fieldValue = (CustomFieldValue) fieldValue1;
                    foundCounter += (cfDef.getPk().equals(fieldValue.getCfDefId()) && !fieldValue.isDeleted()) ? 1 : 0;
                }
            } else if (cfDef.getMinRequired() > 0) {
                    throw new ValidationException(ERROR_MSG_MINIMUM_REQUIRED_NOT_PROVIDED, cfDef.getName(), cfDef.getMinRequired().toString());
            }
        }
        if (cfDef.getMinRequired() > 0 && foundCounter < cfDef.getMinRequired()) {
            throw new ValidationException(ERROR_MSG_MINIMUM_REQUIRED_NOT_PROVIDED, cfDef.getName(), cfDef.getMinRequired().toString());
        }
        if (cfDef.getMaxAllowed() > 0 && foundCounter > cfDef.getMaxAllowed()) {
            throw new ValidationException(ERROR_MSG_MAX_ALLOWED_EXCEEDED, cfDef.getName(), cfDef.getMaxAllowed().toString());
        }
    }

}
