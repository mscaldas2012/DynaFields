package com.msc.customFields.manager;

import com.msc.customFields.CustomFieldDefinition;

import java.util.Collection;

/**
 * This code was written by Marcelo Caldas.
 * e-Mail: mscaldas@gmail.com
 * <p/>
 * \* Project: DynaFields
 * <p/>
 * Date: 3/7/14
 * <p/>
 * Enjoy the details of life.
 */
public interface CustomFieldDefinitionManager {
    public Collection findByOwnerIdAndClass(Long ownerId, String clazz) throws Exception;

    public CustomFieldDefinition save(CustomFieldDefinition newValue);
    public void delete(CustomFieldDefinition def);

    public CustomFieldDefinition findByPrimaryKey(long pk) throws Exception;
}
