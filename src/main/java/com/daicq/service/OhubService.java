package com.daicq.service;

import com.daicq.dao.doc.OhubDoc;
import org.json.JSONArray;

import java.util.List;

/**
 * interface service for Ohub
 */
public interface OhubService {
	OhubDoc create(OhubDoc ohubDoc, String couchbaseBooketName);

	List<OhubDoc> findAll(String couchbaseBooketName);

	void createD(String couchbaseBooketName, String key, String value);

	public JSONArray getOhubDataOfOhubConnection(OhubDoc ohubDoc);

	public List<String> getOhubFieldOfConnection(OhubDoc ohubDoc);

	public OhubDoc updateOhub(String key, OhubDoc ohubDoc, String bucketName);

	public JSONArray getOhubDataOfOhubConnection(String bucketName, OhubDoc ohubDoc, String sourceId, String param, String keySource, JSONArray sourceId2);

	void upsertDocumentInCouchBase(String couchbaseBooketName, String key, String value);

	JSONArray getOhubDataOfOhubConnection2(String bucketName, OhubDoc ohubDoc, String sourceId, String param, String keySource, JSONArray idList);

	public String getCampaignWaveResponseId(String bucketName, String connectionId, String type);

	JSONArray getOhubDataOfConnection(String inputStr, String bucketName);
}
