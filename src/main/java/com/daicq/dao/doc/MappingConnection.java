package com.daicq.dao.doc;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Document
public class MappingConnection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String MAPPING_KEY_PREFIX = "MAPPING::";

	@Id
	private String key;
	@Field
	private String mappingId;
	@Field
	private Long id;
	@Field
	private String type;
	@Field
	private List<Map<String, String>> connection;
	@Field
	private String mappingName;
	@Field
	private List<List<String>> sourceId;
	@Field
	private String description;
	@Field
	private String mappingType;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMappingId() {
		return mappingId;
	}

	public void setMappingId(String mappingId) {
		this.mappingId = mappingId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		this.mappingId = MappingDoc.getMappingKeyPrefix(id);
		this.key = MappingDoc.getMappingKeyPrefix(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Map<String, String>> getConnection() {
		return connection;
	}

	public void setConnection(List<Map<String, String>> connection) {
		this.connection = connection;
	}

	public String getMappingName() {
		return mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	public List<List<String>> getSourceId() {
		return sourceId;
	}

	public void setSourceId(List<List<String>> sourceId) {
		this.sourceId = sourceId;
	}

	public static String getMappingKeyPrefix(Long id) {
		return MAPPING_KEY_PREFIX + id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MappingConnection() {
		super();
	}

	public String getMappingType() {
		return mappingType;
	}

	public void setMappingType(String mappingType) {
		this.mappingType = mappingType;
	}

	public MappingConnection(String key, String mappingId, Long id, String type, List<Map<String, String>> connection, String mappingName, List<List<String>> sourceId, String description, String mappingType) {
		super();
		this.key = key;
		this.mappingId = mappingId;
		this.id = id;
		this.type = type;
		this.connection = connection;
		this.mappingName = mappingName;
		this.sourceId = sourceId;
		this.description = description;
		this.mappingType = mappingType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connection == null) ? 0 : connection.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((mappingId == null) ? 0 : mappingId.hashCode());
		result = prime * result + ((mappingName == null) ? 0 : mappingName.hashCode());
		result = prime * result + ((mappingType == null) ? 0 : mappingType.hashCode());
		result = prime * result + ((sourceId == null) ? 0 : sourceId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MappingConnection other = (MappingConnection) obj;
		if (connection == null) {
			if (other.connection != null)
				return false;
		} else if (!connection.equals(other.connection))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (mappingId == null) {
			if (other.mappingId != null)
				return false;
		} else if (!mappingId.equals(other.mappingId))
			return false;
		if (mappingName == null) {
			if (other.mappingName != null)
				return false;
		} else if (!mappingName.equals(other.mappingName))
			return false;
		if (mappingType == null) {
			if (other.mappingType != null)
				return false;
		} else if (!mappingType.equals(other.mappingType))
			return false;
		if (sourceId == null) {
			if (other.sourceId != null)
				return false;
		} else if (!sourceId.equals(other.sourceId))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
