package com.msc.customFields.dataAccess;

import com.msc.customFields.CustomFieldDefinition;

import java.util.List;

/**
 * Interface with data access layer contract for Custom Definition.
 *
 * <P>Date: Jan 5, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public interface CustomFieldDefinitionDAO {
    public List<CustomFieldDefinition> findByOwner(long onwerId);
    public List<CustomFieldDefinition> findByOwnerAndClass(long ownerId, String clazz);
	public List<CustomFieldDefinition> findByOnwerClassAndScope(long ownerId, String clazz, String scope) ;
    public CustomFieldDefinition findByPrimaryKey(Long pk);

    public CustomFieldDefinition save(CustomFieldDefinition newDef);
    public void delete(CustomFieldDefinition def);
}
