package com.daicq.dao.doc;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;

@Document
public class OhubBasicDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Field("id")
	private Long id;
	@Field("name")
	private String name;

	public OhubBasicDoc(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public OhubBasicDoc() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Ohub Basic [id=%s, name=%s]", id, name);
	}

}
