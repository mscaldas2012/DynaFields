package com.msc.customFields.dataAccess;

import com.msc.customFields.CustomFieldDefList;
import com.msc.customFields.CustomFieldDefinition;
import com.msc.utils.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

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
@Repository
public class CustomFieldDefinitionDAOImpl implements CustomFieldDefinitionDAO {
    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = Logger.getLogger(CustomFieldDefinitionDAOImpl.class.getName());

    public List<CustomFieldDefinition> findByOwner(long onwerId) {
        return null;
    }

    public List<CustomFieldDefinition> findByOwnerAndClass(long ownerId, String clazz) {
        return null;
    }

    public List<CustomFieldDefinition> findByOnwerClassAndScope(long ownerId, String clazz, String scope) {
        return null;
    }

    public CustomFieldDefinition findByPrimaryKey(Long pk) {
        return entityManager.find(CustomFieldDefinition.class, pk);
    }

    public CustomFieldDefinition save(CustomFieldDefinition newDef) {
        Calendar txTime = Calendar.getInstance();
        this.prepareChildren(newDef, txTime);
        newDef.setLastModifiedDate(txTime);
        if (newDef.getPk() ==  null) {
            newDef.setCreatedDate(txTime);
            entityManager.persist(newDef);
            return newDef;
        } else {
            return entityManager.merge(newDef);
        }
    }

    private void prepareChildren(CustomFieldDefinition def, Calendar txTime) {
        if (CollectionUtils.isNotEmpty(def.getListValues())) {
            for (Iterator<CustomFieldDefList> it=def.getListValues().iterator(); it.hasNext();) {
                CustomFieldDefList listVal = it.next();
                listVal.setLastModifiedDate(txTime);
                if (listVal.isDeleted()) {
                    if (listVal.getPk() != null) {
                        listVal = entityManager.merge(listVal); //Gotta merge the Object before I can remove it.
                        entityManager.remove(listVal);
                    }
                    it.remove();
                }  else if (listVal.getPk() == null) {
                    listVal.setCreatedDate(txTime);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(def.getGroupColumns())) {
            for (Iterator<CustomFieldDefinition> it=def.getGroupColumns().iterator(); it.hasNext();) {
                CustomFieldDefinition defGroup = it.next();
                defGroup.setLastModifiedDate(txTime);
                if (defGroup.isDeleted()) {
                    if (defGroup.getPk() != null) {
                        defGroup = entityManager.merge(defGroup); //Gotta merge the Object before I can remove it.
                        entityManager.remove(defGroup);
                    }
                    it.remove();
                }  else if (defGroup.getPk() == null) {
                    defGroup.setCreatedDate(txTime);
                }
            }

        }
    }

    public void delete(CustomFieldDefinition def) {
        def = entityManager.merge(def); //Gotta merge the Object before I can remove it.
        entityManager.remove(def);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
