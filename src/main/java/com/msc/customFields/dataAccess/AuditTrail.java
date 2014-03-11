package com.msc.customFields.dataAccess;

import java.util.Calendar;

/**
 * <p/>
 * </P>
 * <p/>
 * <p/>
 * <P>Date: Mar 30, 2006</P>
 *
 * @author <a href="mailto:mcq1@cdc.gov">Marcelo Caldas</a>
 */
public interface AuditTrail {
	public void setCreatedBy(String newValue);
	public String getCreatedBy();
	public void setCreatedDate(Calendar newValue);
	public Calendar getCreatedDate();

	public void setLastModifiedBy(String newValue);
	public String getLastModifiedBy();
	public void setLastModifiedDate(Calendar newValue);
	public Calendar getLastModifiedDate();

}
