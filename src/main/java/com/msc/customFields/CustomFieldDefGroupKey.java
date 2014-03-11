package com.msc.customFields;

import com.msc.cache.Cacheable;
import com.msc.utils.StringUtils;

/**
 * <P>Date: Jan 5, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public class CustomFieldDefGroupKey implements Cacheable {
	private Long ownerId;
    private String clazz;

	public CustomFieldDefGroupKey(Long ownerId, String clazz) {
		this.clazz = clazz;
		this.ownerId = ownerId;
	}

	public Long getOwnerId() {
		return ownerId;
	}
	public String getClazz() {
		return clazz;
	}

	public boolean equals(Object object) {
		if (!(object instanceof CustomFieldDefGroupKey)) {
			return false;
		}
		CustomFieldDefGroupKey param = (CustomFieldDefGroupKey) object;
		return (StringUtils.isSame(this.clazz, param.getClazz()))
				&& ((this.ownerId != null) && this.ownerId.equals(param.getOwnerId()) ||
                    this.ownerId == null && param.getOwnerId() == null);
	}

	public int hashCode() {
		return this.toString().hashCode();
	}

	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("CFDefGroupKey");
		sb.append("{appId=").append(ownerId);
		sb.append(", clazz=").append(clazz);
		sb.append('}');
		return sb.toString();
	}
	public Object getCacheKey() {
		return this.hashCode();
	}
}
