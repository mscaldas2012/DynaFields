package com.msc.customFields.manager;

import com.msc.cache.CacheManager;
import com.msc.customFields.dataAccess.CustomFieldDefinitionDAO;
import com.msc.customFields.CustomFieldDefGroupKey;
import com.msc.customFields.CustomFieldDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 5, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
@Service
public class CustomFieldDefinitionManagerImpl implements CustomFieldDefinitionManager {

    //IoC
    private CacheManager cacheManager;
    private CustomFieldDefinitionDAO dao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomFieldDefinition save(CustomFieldDefinition newValue) {
        newValue = dao.save(newValue);
        //Invalidate group cache for the OnwerID of the newly added CustomField.
        cacheManager.invalidate(newValue);
        return newValue;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(CustomFieldDefinition def) {
        dao.delete(def);
    }

    public Collection findByOwnerIdAndClass(Long ownerId, String clazz) throws Exception {
        CustomFieldDefGroupKey key = new CustomFieldDefGroupKey(ownerId, clazz);
		return cacheManager.getByGroup(key);
	}


    public CustomFieldDefinition findByPrimaryKey(long pk) throws Exception {
        return (CustomFieldDefinition) cacheManager.get(pk);
        //return dao.findByPrimaryKey(pk)       ;
    }
//	public long findUsagesOf(Long customFieldDefPk) throws ApplicationException, BusinessException {
//		return ((CustomFieldDefinitionDAO)this.getDAO()).getUsagesOf(customFieldDefPk);
//	}
	//Need to first delete the columns then, delete the group:

    public void setDao(CustomFieldDefinitionDAO dao) {
        this.dao = dao;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
