package com.tts.android.common.databean;

import java.io.Serializable;

/**
 * Criteria for page query
 * @author Vina.Chiong
 *
 */
public class Criteria implements Serializable {

	private static final long serialVersionUID = 2465701222100832859L;

	private String keyword;
	
	private int start;
	
	private int limit = 10;
	
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
