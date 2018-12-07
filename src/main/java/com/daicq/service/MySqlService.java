package com.daicq.service;

import com.daicq.dao.doc.MySqlDoc;
import org.json.JSONArray;

import java.util.List;

/**
 * interface service for MySql
 */
public interface MySqlService {

	MySqlDoc create(MySqlDoc mysql, String couchbaseBooketName);

	List<MySqlDoc> findAllMySqlConnection(String couchbaseBooketName);

	public JSONArray getMySqlDataBySqlConnection(MySqlDoc mySqlDoc);

	MySqlDoc findDocumentInCouchBaseByDocumentId(String booketName, String documentId);
	
	MySqlDoc updateMySQL(String key, MySqlDoc mySqlDoc, String bucketName);

}
