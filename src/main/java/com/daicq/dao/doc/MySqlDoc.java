package com.daicq.dao.doc;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document
public class MySqlDoc implements Serializable {

	/**
	 * doc using get mySql document
	 *
	 */
	private static final long serialVersionUID = 1L;
	protected static final String MYSQL_KEY_PREFIX = "MYSQL_";

	@Id
	private String documentId;
	@Field
	private String id;
	@Field
	private String name;
	@Field
	private String connectionType;
	@Field
	private String serverAddress;
	@Field
	private int port;
	@Field
	private String databaseName;
	@Field
	private String databaseUser;
	@Field
	private String databasePassword;
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
	private String databaseTable;
	@Field
	private List<String> fields;
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
		this.documentId = MySqlDoc.getMysqlKeyPrefix(id);
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

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
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

	public String getDatabaseTable() {
		return databaseTable;
	}

	public void setDatabaseTable(String databaseTable) {
		this.databaseTable = databaseTable;
	}

	public static String getMysqlKeyPrefix(String id) {
		return MYSQL_KEY_PREFIX + id;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public MySqlDoc(String documentId, String id, String name, String connectionType, String serverAddress, int port, String databaseName, String databaseUser, String databasePassword, List<String> dateCreated, String createdBy, String updatedBy, List<String> dateUpdated, String type, List<String> channels, List<String> fields, List<String> countryId) {
		super();
		this.documentId = documentId;
		this.id = id;
		this.name = name;
		this.connectionType = connectionType;
		this.serverAddress = serverAddress;
		this.port = port;
		this.databaseName = databaseName;
		this.databaseUser = databaseUser;
		this.databasePassword = databasePassword;
		this.dateCreated = dateCreated;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.dateUpdated = dateUpdated;
		this.type = type;
		this.channels = channels;
		this.fields = fields;
		this.countryId = countryId;
	}

	public MySqlDoc() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channels == null) ? 0 : channels.hashCode());
		result = prime * result + ((connectionType == null) ? 0 : connectionType.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((databaseName == null) ? 0 : databaseName.hashCode());
		result = prime * result + ((databasePassword == null) ? 0 : databasePassword.hashCode());
		result = prime * result + ((databaseTable == null) ? 0 : databaseTable.hashCode());
		result = prime * result + ((databaseUser == null) ? 0 : databaseUser.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateUpdated == null) ? 0 : dateUpdated.hashCode());
		result = prime * result + ((documentId == null) ? 0 : documentId.hashCode());
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((identifierKey == null) ? 0 : identifierKey.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		result = prime * result + ((serverAddress == null) ? 0 : serverAddress.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
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
		MySqlDoc other = (MySqlDoc) obj;
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
		if (databaseName == null) {
			if (other.databaseName != null)
				return false;
		} else if (!databaseName.equals(other.databaseName))
			return false;
		if (databasePassword == null) {
			if (other.databasePassword != null)
				return false;
		} else if (!databasePassword.equals(other.databasePassword))
			return false;
		if (databaseTable == null) {
			if (other.databaseTable != null)
				return false;
		} else if (!databaseTable.equals(other.databaseTable))
			return false;
		if (databaseUser == null) {
			if (other.databaseUser != null)
				return false;
		} else if (!databaseUser.equals(other.databaseUser))
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
		if (documentId == null) {
			if (other.documentId != null)
				return false;
		} else if (!documentId.equals(other.documentId))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		if (serverAddress == null) {
			if (other.serverAddress != null)
				return false;
		} else if (!serverAddress.equals(other.serverAddress))
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
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;

		return true;
	}

	public List<String> getCountryId() {
		return this.countryId;
	}

	public void setCountryId(List<String> countryId) {
		this.countryId = countryId;
	}
}
