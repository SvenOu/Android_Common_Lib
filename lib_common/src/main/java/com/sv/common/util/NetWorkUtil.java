package com.sv.common.util;

public class NetWorkUtil {
	/** Network type is unknown */
	public static final int NETWORK_TYPE_UNKNOWN = 0;
	/** Current network is GPRS */
	public static final int NETWORK_TYPE_GPRS = 1;
	/** Current network is EDGE */
	public static final int NETWORK_TYPE_EDGE = 2;
	/** Current network is UMTS */
	public static final int NETWORK_TYPE_UMTS = 3;
	/** Current network is CDMA: Either IS95A or IS95B*/
	public static final int NETWORK_TYPE_CDMA = 4;
	/** Current network is EVDO revision 0*/
	public static final int NETWORK_TYPE_EVDO_0 = 5;
	/** Current network is EVDO revision A*/
	public static final int NETWORK_TYPE_EVDO_A = 6;
	/** Current network is 1xRTT*/
	public static final int NETWORK_TYPE_1xRTT = 7;
	/** Current network is HSDPA */
	public static final int NETWORK_TYPE_HSDPA = 8;
	/** Current network is HSUPA */
	public static final int NETWORK_TYPE_HSUPA = 9;
	/** Current network is HSPA */
	public static final int NETWORK_TYPE_HSPA = 10;
	/** Current network is iDen */
	public static final int NETWORK_TYPE_IDEN = 11;
	/** Current network is EVDO revision B*/
	public static final int NETWORK_TYPE_EVDO_B = 12;
	/** Current network is LTE */
	public static final int NETWORK_TYPE_LTE = 13;
	/** Current network is eHRPD */
	public static final int NETWORK_TYPE_EHRPD = 14;
	/** Current network is HSPA+ */
	public static final int NETWORK_TYPE_HSPAP = 15;
	/** Unknown network class. {@hide} */
	public static final int NETWORK_CLASS_UNKNOWN = 0;
	/** Class of broadly defined "2G" networks. {@hide} */
	public static final int NETWORK_CLASS_2_G = 1;
	/** Class of broadly defined "3G" networks. {@hide} */
	public static final int NETWORK_CLASS_3_G = 2;
	/** Class of broadly defined "4G" networks. {@hide} */
	public static final int NETWORK_CLASS_4_G = 3;

	public static String getNetworkClass(int networkType) {
		Logger.i("getNetworkClass", "networkType:" + networkType);
	    switch (networkType) {
	        case NETWORK_TYPE_GPRS:
	        case NETWORK_TYPE_EDGE:
	        case NETWORK_TYPE_CDMA:
	        case NETWORK_TYPE_1xRTT:
	        case NETWORK_TYPE_IDEN:
	    return "2G";
	        case NETWORK_TYPE_UMTS:
	        case NETWORK_TYPE_EVDO_0:
	        case NETWORK_TYPE_EVDO_A:
	        case NETWORK_TYPE_HSDPA:
	        case NETWORK_TYPE_HSUPA:
	        case NETWORK_TYPE_HSPA:
	        case NETWORK_TYPE_EVDO_B:
	        case NETWORK_TYPE_EHRPD:
	        case NETWORK_TYPE_HSPAP:
	    return "3G";
	        case NETWORK_TYPE_LTE:
	    return "4G";
	        default:
	    return "UNKNOW-"+networkType;
	    }
	}

	public static void testRequest (int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			Logger.e("testRequest: ", e.getMessage());
		}
	}

}