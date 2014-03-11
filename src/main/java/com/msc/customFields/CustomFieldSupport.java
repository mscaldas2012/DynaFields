package com.msc.customFields;

import java.util.Set;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Feb 2, 2007</P>
 *
 * @author <a href="mailto:mscaldas@gmail.com">Marcelo Caldas</a>
 */
public interface CustomFieldSupport {
    public Long getOwnerId();
    public String getClazz();
    public String getScope();

	public Set getCustomFields();

}
