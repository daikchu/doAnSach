package com.daicq.dao;

import com.daicq.dao.doc.MySqlDoc;
import com.daicq.util.Util;
import org.json.JSONObject;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * interface MySqlRepository
 *
 */
public interface MySqlRepository extends CrudRepository<MySqlDoc, String> {

	@Query("SELECT META(`#{[0]}`).id AS _ID, META(`#{[0]}`).cas AS _CAS, `#{[0]}`.* FROM `#{[0]}` WHERE `connectionType` = '" + Util.MYSQL_TYPE + "'")
	List<MySqlDoc> findAllMySqlConnection(String couchbaseBooketName);

	// select using key id
	@Query("SELECT META(`#{[0]}`).id from `#{[0]}` USE KEYS '#{[1]}'")
	MySqlDoc findMySqlDataInCouchBaseWithIdentifierKey(String couchbaseBooketName, String identifier);

	@Query("UPSERT INTO `#{[0]}` (KEY, VALUE) VALUES ('#{[1]}', #{[2]}) RETURNING *")
	void upsertMySqlRecordToCouchBase(String couchbaseBooketName, String documentKey, JSONObject jsonObjectValue);

	@Query("SELECT META(`#{[0]}`).id AS _ID, META(`#{[0]}`).cas AS _CAS, `#{[0]}`.* from #{[0]} USE KEYS '#{[1]}' WHERE `connectionType` = '" + Util.MYSQL_TYPE + "'")
	MySqlDoc findDocumentInCouchBaseByDocumentId(String booketName, String documentKey);

	@Query("SELECT META(`#{[0]}`).id AS _ID, META(`#{[0]}`).cas AS _CAS, `#{[0]}`.* from `#{[0]}` WHERE `serverAddress` = '#{[1]}' AND `databaseName` = '#{[2]}' AND `port`=#{[3]} AND `databaseTable` = '#{[4]}' AND `connectionType` = '#{[5]}' ")
	MySqlDoc findMySqlconnectionByServerAddress_DbName_DbTable_Port_ConnectionType(String bucketName, String serverAddress, String databaseName, int port, String databaseTable, String connectionType);

}
