package com.tts.android.common.databean;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Vina.Chiong
 *
 * @param <T>
 */
public class DataPackage<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2751636390718544274L;
	
	private List<T> data;
	
	private int total;

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
