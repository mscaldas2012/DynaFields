package com.msc.customFields.manager;

import com.msc.cache.CacheException;
import com.msc.cache.Cacheable;
import com.msc.cache.GroupCacheable;
import com.msc.cache.loader.CacheGroupLoaderPolicy;
import com.msc.customFields.dataAccess.CustomFieldDefinitionDAO;
import com.msc.customFields.CustomFieldDefGroupKey;
import com.msc.customFields.CustomFieldDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Jan 5, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public class CustomFieldDefinitionCacheLoader implements CacheGroupLoaderPolicy {

    //IoC
	private CustomFieldDefinitionDAO dao;

	public Collection<Cacheable> fetchByGroup(Object groupKey) throws CacheException {
        CustomFieldDefGroupKey gKey = (CustomFieldDefGroupKey) groupKey;
        List<CustomFieldDefinition> list = dao.findByOwnerAndClass(gKey.getOwnerId(), gKey.getClazz());
        Collection<Cacheable> result = new ArrayList<Cacheable>(list.size());
        result.addAll(list);
        return result;
	}
	public Cacheable fetchEntity(Object cacheKey) throws CacheException {
        return dao.findByPrimaryKey((Long) cacheKey);
    }
	//TODO::check how to implement this guy...
	public Collection<GroupCacheable> fetchGroups() throws CacheException {
        return null;
	}
	public Collection<Cacheable> fetchAll() throws CacheException {
		throw new CacheException("Invalid call. No way we can fetch all information for this cache!", "initialization.error");
	}

    public void setDao(CustomFieldDefinitionDAO dao) {
        this.dao = dao;
    }
}
