package com.daicq.dao;

import com.daicq.dao.doc.OhubDoc;
import com.daicq.util.Util;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * interface OhubRepository
 *
 */
public interface OhubRepository extends CrudRepository<OhubDoc, String> {

	/**
	 * communicating with the couchbase server to get all ohub document
	 * 
	 * @return: List ohub document
	 */
	@Query("SELECT META(`#{[0]}`).id AS _ID, META(`#{[0]}`).cas AS _CAS, `#{[0]}`.* FROM `#{[0]}` WHERE `connectionType` = 'OHUB-API'")
	List<OhubDoc> findAllOhub(String couchbaseBooketName);

	/**
	 * communicating with the couchbase server to get ohub connection by current day
	 * 
	 * @param: current day
	 * @return: List ohub document
	 */
	@Query("SELECT META(`#{[0]}`).id AS _ID, META(`#{[0]}`).cas AS _CAS, `#{[0]}`.* FROM `#{[0]}` where `connectionType`='OHUB-API' and `operationHour`= '#{[1]}' and `operationTime`= '#{[2]}' AND ARRAY_LENGTH(operationFrequency) > 0 AND ANY #{[3]} IN operationFrequency SATISFIES #{[3]} END")
	List<OhubDoc> findConnectionWithDateTimeCondition(String couchbaseBooketName, String systemOperationHour, String systemOperationTime, String currentSystemDayOfWeek);

	/**
	 * communicating with the couchbase server to insert and update data from ohub
	 * api to couchbase server
	 * 
	 * @param: document key:value
	 */
	@Query("UPSERT INTO `#{[0]}` (KEY, VALUE) VALUES ('#{[1]}', #{[2]}) RETURNING *")
	void upSertDataFromOhubToCouchBase(String couchbaseBooketName, String documentKey, String documentValue);

	/**
	 * communicating with the couchbase server to insert data from ohub api to
	 * couchbase server
	 * 
	 * @param: document key:value
	 */
	@Query("INSERT INTO `#{[0]}` (KEY, VALUE) VALUES ('#{[1]}', #{[2]}) RETURNING *")
	void createOhubDocumentInCouchBase(String couchbaseBooketName, String key, String value);

	/**
	 * communicating with the couchbase server to get ohub document in couchbase by
	 * documentKey
	 * 
	 * @param: documentKey
	 * @return: ohub document
	 */
	@Query("SELECT META(`#{[0]}`).id AS _ID, META(`#{[0]}`).cas AS _CAS, `#{[0]}`.* from #{[0]} USE KEYS '#{[1]}' WHERE `connectionType` = '" + Util.OHUB_API_TYPE + "'")
	OhubDoc findOhubDocumentInCouchBaseByDocumentKey(String booketName, String documentKey);

	@Query("INSERT INTO `#{[0]}` (KEY, VALUE) VALUES ('#{[1]}', #{[2]}) RETURNING *")
	void insertWithoutDeleteInCouchBase(String couchbaseBooketName, String key, String value);

	/*
	 * get campaignWaveResponseId from saved campaignWaveSendings doc in couchbase
	 */
	@Query("SELECT r.campaignWaveResponseId FROM #{[0]} s UNNEST s.result r WHERE s.connectionId = '#{[1]}' and s.type = '#{[2]}'")
	List<String> getCampaignWaveResponseId(String bucketName, String connectionId, String type);
}