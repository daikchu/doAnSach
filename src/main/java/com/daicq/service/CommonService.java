package com.daicq.service;

import com.daicq.dao.doc.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * interface service for general
 */
public interface CommonService {
	List<String> findDocument(String type, String docType, String identifierKey) throws Exception;

	List<String> getLstOfSource(String type) throws Exception;

	List<String> findDocument(String type, String name) throws Exception;

	List<String> getLstIdFromNameCountryType(String type, String country, String name) throws Exception;

	JSONArray combineMultiApi(String couchbaseBooketName, String documentId) throws Exception;

	MappingDoc create(MappingDoc m);

	String checkConnectionMySQL(String bucketName, MySqlDoc mySqlDoc);

	String checkConnectionOhubApi(String bucketName, OhubDoc ohub);

	JSONArray groupConnection(String bucketName, String documentId) throws Exception;

	JSONArray groupConnectionbySourceId(String bucketName, String documentId) throws Exception;

	MappingConnection createMapCon(MappingConnection mappingConnection);

	List<String> findAllMapping() throws Exception;

	List<String> findMappingByName(String mappingName) throws Exception;

	MappingSource createMappingSource(MappingSource mappingSource);

	List<String> getDataByKey(String documentKey, String connectionType) throws Exception;

	public List<String> findMappingById(String mappingName) throws Exception;

	List<String> getAllDataMapping() throws Exception;

	//public String getMappingBySupMapId(String mappingId, String[] id) throws Exception;

	JSONArray getDocumentContent(String bucketName, String documentKey, String operation, String keySource, String countryId, String sourceId, String param, JSONArray sourceId2, String active) throws Exception;

	String getSourceId(String bucketName, String documentKey);

	JSONArray generateSourceId(String fields, MySqlDoc mySqlDoc, String baseTable, String countryId, String active) throws JSONException, Exception;

	JSONArray combineMultiApi2(String bucketName, String inputString) throws Exception;

	JSONArray getDocumentContent2(String bucketName, String connectionKey, String operation, String keySource, String countryId, String idList, String active) throws Exception;

	JSONArray combineMultiApiPaginated2(String bucketName, String inputString, int lastSourceIndex, int limit/*, boolean realTime*/) throws Exception;

	JSONArray compareArmstrongToOhub(String bucketName, JSONArray requestBody);

	JSONArray supermapperMerge(String bucketName, String requestBody);

	Object getDocumentData(String bucketName, String documentKey) throws Exception;

	JSONArray getGooglePlacesData(String bucketName, String requestData) throws IOException, InterruptedException, Exception;

	JSONArray combine2Api(JSONArray sourceArr, JSONArray targetArr, String connectionSource, String connectionTarget, String keySource, String keyTarget) throws Exception;

	JSONArray combine2ApiForGetOHUB(JSONArray sourceArray, String sourceKey, JSONArray targetArray, String targetKey);
}