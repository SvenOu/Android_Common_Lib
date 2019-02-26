package com.tts.android.db;


public abstract class DbTemplateFactory {

	private static DbTemplate dbTemplate;
	
	public static DbTemplate getDbTemplate()
	{
		if (null == dbTemplate){
			synchronized(DaoSupport.class){
				if (null == dbTemplate) {
                    dbTemplate = new DbTemplate(DatabaseManager.getInstance().wdb());
                }
			}
		}
		
		return dbTemplate;
	}
}
