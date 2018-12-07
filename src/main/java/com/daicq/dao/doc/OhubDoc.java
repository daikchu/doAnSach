package com.daicq.dao.doc;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Document
public class OhubDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String OHUB_KEY_PREFIX = "OHUB_API_";

	@Id
	private String documentId;
	@Field
	private String id;
	@Field
	private String name;
	@Field
	private String connectionType;
	@Field
	private String link;
	@Field
	private String method;
	@Field
	private List<String> dateCreated;
	@Field
	private String createdBy;
	@Field
	private String updatedBy;
	@Field
	private List<String> dateUpdated;
	@Field
	private String type;
	@Field
	private List<String> channels;
	@Field
	private String identifierKey;
	@Field
	private List<String> fields;
	@Field
	private Map<String, String> parameters;
	@Field
	private Map<String, String> otherParameters;
	@Field
	private Map<String, String> credentials;
	@Field
	private List<String> countryId;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.documentId = OhubDoc.getOhubKeyPrefix(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public List<String> getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(List<String> dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<String> getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(List<String> dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getChannels() {
		return channels;
	}

	public void setChannels(List<String> channels) {
		this.channels = channels;
	}

	public String getIdentifierKey() {
		return identifierKey;
	}

	public void setIdentifierKey(String identifierKey) {
		this.identifierKey = identifierKey;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getOtherParameters() {
		return otherParameters;
	}

	public void setOtherParameters(Map<String, String> otherParameters) {
		this.otherParameters = otherParameters;
	}

	public Map<String, String> getCredentials() {
		return credentials;
	}

	public void setCredentials(Map<String, String> otherParameters) {
		this.credentials = otherParameters;
	}

	public static String getOhubKeyPrefix(String id) {
		return OHUB_KEY_PREFIX + id;
	}

	public OhubDoc(String documentId, String id, String name, String connectionType, String link, String method, List<String> dateCreated, String createdBy, String updatedBy, List<String> dateUpdated, String type, List<String> channels, String identifierKey, List<String> fields, Map<String, String> parameters, Map<String, String> otherParameters, Map<String, String> credentials, List<String> countryId) {
		super();
		this.documentId = documentId;
		this.id = id;
		this.name = name;
		this.connectionType = connectionType;
		this.link = link;
		this.method = method;
		this.dateCreated = dateCreated;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.dateUpdated = dateUpdated;
		this.type = type;
		this.channels = channels;
		this.identifierKey = identifierKey;
		this.fields = fields;
		this.parameters = parameters;
		this.otherParameters = otherParameters;
		this.credentials = credentials;
		this.countryId = countryId;
	}

	public OhubDoc() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channels == null) ? 0 : channels.hashCode());
		result = prime * result + ((connectionType == null) ? 0 : connectionType.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((identifierKey == null) ? 0 : identifierKey.hashCode());
		result = prime * result + ((documentId == null) ? 0 : documentId.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((otherParameters == null) ? 0 : otherParameters.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((credentials == null) ? 0 : credentials.hashCode());
		result = prime * result + ((countryId == null) ? 0 : countryId.hashCode());
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
		OhubDoc other = (OhubDoc) obj;
		if (channels == null) {
			if (other.channels != null)
				return false;
		} else if (!channels.equals(other.channels))
			return false;
		if (connectionType == null) {
			if (other.connectionType != null)
				return false;
		} else if (!connectionType.equals(other.connectionType))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateUpdated == null) {
			if (other.dateUpdated != null)
				return false;
		} else if (!dateUpdated.equals(other.dateUpdated))
			return false;
		if (fields == null) {
			if (other.fields != null)
				return false;
		} else if (!fields.equals(other.fields))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identifierKey == null) {
			if (other.identifierKey != null)
				return false;
		} else if (!identifierKey.equals(other.identifierKey))
			return false;
		if (documentId == null) {
			if (other.documentId != null)
				return false;
		} else if (!documentId.equals(other.documentId))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (otherParameters == null) {
			if (other.otherParameters != null)
				return false;
		} else if (!otherParameters.equals(other.otherParameters))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (credentials == null) {
			if (other.credentials != null)
				return false;
		} else if (!credentials.equals(other.credentials))
			return false;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;

		return true;
	}

	public List<String> getCountryId() {
		return countryId;
	}

	public void setCountryId(List<String> countryId) {
		this.countryId = countryId;
	}

}