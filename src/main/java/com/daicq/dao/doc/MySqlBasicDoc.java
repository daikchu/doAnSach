package com.daicq.dao.doc;

import com.couchbase.client.java.repository.annotation.Field;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;

@Document
public class MySqlBasicDoc implements Serializable {

	/**
	 * doc using get basic mySql document
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Field("id")
	private Long id;
	@Field("name")
	private String name;

	/*
	 * public MySqlBasicDoc(Long id, String name) { super(); this.id = id; this.name
	 * = name; }
	 */
	public MySqlBasicDoc() {
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
		return String.format("MYSQL Basic [id=%s, name=%s]", id, name);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MySqlBasicDoc other = (MySqlBasicDoc) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
