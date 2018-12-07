package com.daicq.dao.doc;

import java.util.List;

public class MapsObject {
	private String connectionSource;
	private String keyTarget;
	private List<String> sourceId;

	public String getConnectionSource() {
		return connectionSource;
	}

	public void setConnectionSource(String connectionSource) {
		this.connectionSource = connectionSource;
	}

	public String getKeyTarget() {
		return keyTarget;
	}

	public void setKeyTarget(String keyTarget) {
		this.keyTarget = keyTarget;
	}

	public List<String> getSourceId() {
		return sourceId;
	}

	public void setSourceId(List<String> sourceId) {
		this.sourceId = sourceId;
	}

}
