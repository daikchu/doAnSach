package com.daicq.service.impl;

import com.daicq.dao.*;
import com.daicq.dao.doc.*;
import com.daicq.service.CommonService;
import com.daicq.service.OhubService;
import com.daicq.util.ConnectionUtil;
import com.daicq.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * implementation of interface commonService: process for data general
 */
@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	CustomRepository customRepository;
	@Autowired
	OhubRepository ohubRepository;
	@Autowired
	MySqlRepository mySqlRepository;
	@Autowired
	MappingRepository mappingRepository;
	@Autowired
	MappingCounterRepository mappingCounter;
	@Autowired
	OhubService ohubService;
	@Autowired
	MappingConnectionRepository mappingConRepository;
	@Autowired
	MappingSourceRepository mappingSourceRepository;

	/**
	 * get document in couchbase by 'type', 'docType', 'identifierValue' params
	 * 
	 * @param: type, docType, identifier value
	 * @return: list String result
	 */
	@Override
	public List<String> findDocument(String type, String docType, String identifierValue) throws Exception {
		List<String> foundDocumentList = customRepository.findDocument(type, docType);
		List<String> connectionIds = new ArrayList<String>();
		String connectionId = "";
		for (String documentJson : foundDocumentList) {
			JSONObject jo = new JSONObject(documentJson);
			connectionId = jo.getString(Util.CONNECTION_ID);
			if (!connectionIds.contains(connectionId)) {
				connectionIds.add(connectionId);
			}
		}
		Map<String, String> identifierMap = customRepository.getIdentifier(connectionIds);
		List<String> foundDocument = new ArrayList<String>();
		for (String key : identifierMap.keySet()) {
			String document = customRepository.findDocument(type, docType, key, identifierMap.get(key), identifierValue);
			if (!Util.isNullOrEmpty(document)) {
				foundDocument.add(document);
			}
		}
		return foundDocument;
	}

	/**
	 * get list of source by 'type' param
	 * 
	 * @param: type
	 * @return: list String result
	 */
	@Override
	public List<String> getLstOfSource(String type) throws Exception {
		// TODO Auto-generated method stub
		List<String> foundDocumentList = customRepository.findNameofSource(type);
		List<String> modifiedDocumentList = new ArrayList<String>();
		for (String documentJson : foundDocumentList) {
			JSONObject jo = new JSONObject(documentJson);
			jo.remove(Util.TYPE);
			modifiedDocumentList.add(jo.toString());
			// modifiedDocumentList.add(jo.getJSONArray("name").toString());
		}
		return modifiedDocumentList;
	}

	/**
	 * get document in couchbase by 'type', 'name' params
	 * 
	 * @param: type, name
	 * @return: list String ids
	 */
	@Override
	public List<String> findDocument(String type, String name) throws Exception {
		List<String> foundDocumentList = customRepository.findDocumentByType(type, name);
		List<String> ids = new ArrayList<String>();
		for (String documentJson : foundDocumentList) {
			JSONObject jo = new JSONObject(documentJson);
			JSONArray ja_data = jo.getJSONArray("ids");
			String id = "";
			int sizeOf_Ja_data = ja_data.length();
			if (sizeOf_Ja_data > 0) {
				for (int i = 0; i < sizeOf_Ja_data; i++) {
					JSONObject jo2 = ja_data.getJSONObject(i);
					id = jo2.getString("id");
					if (!Util.isNullOrEmpty(id)) {
						ids.add(id);
					}
				}
			}
		}
		return ids;
	}

	/**
	 * get list ids from couchbase document by 'name', 'country' and 'type' params
	 * 
	 * @param: name, country, type
	 * @return: list String result
	 */
	@Override
	public List<String> getLstIdFromNameCountryType(String name, String country, String type) throws Exception {
		// TODO Auto-generated method stub
		List<String> findIds = customRepository.findDocumentByType(type, name);
		List<String> lstId = new ArrayList<String>();
		for (String documentJson : findIds) {
			JSONObject jo = new JSONObject(documentJson);
			JSONArray ja_data = jo.getJSONArray("ids");
			String id = "";
			int sizeOf_Ja_data = ja_data.length();
			if (sizeOf_Ja_data > 0) {
				for (int i = 0; i < sizeOf_Ja_data; i++) {
					JSONObject jo2 = ja_data.getJSONObject(i);
					if (country.equals(jo2.getString("country"))) {
						id = jo2.getString("id");
						if (!Util.isNullOrEmpty(id)) {
							lstId.add(id);
						}
					}
				}
			}
		}
		return lstId;
	}

	@Override
	public MappingDoc create(MappingDoc m) {
		// TODO Auto-generated method stub
		m.setId(mappingCounter.counter());
		m.setType("MAPPING");
		if (m.getDescription() == null) {
			m.setDescription(m.getMaps().toString());
		}
		return mappingRepository.save(m);
	}

	@Override
	public JSONArray combineMultiApi(String bucketName, String inputString) throws Exception {
		JSONArray jsonArray = new JSONArray(inputString);
		JSONArray aggArray = new JSONArray();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jo2 = jsonArray.getJSONObject(i);
			String connectionSource = jo2.getString("connectionSource");
			String connectionTarget = jo2.getString("connectionTarget");
			String mappingType = jo2.getString("mappingType");
			String keySource = jo2.getString("keySource");
			String keyTarget = jo2.getString("keyTarget");
			String baseTable = jo2.getString("baseTable");
			JSONArray sourceContent = new JSONArray();
			JSONArray targetContent = new JSONArray();
			JSONArray idList = null;
			JSONArray countryId_array = jo2.getJSONArray("countryId");
			String param = "sourceId=";
			String countryId = "";
			String keyField = keySource;
			String active = "0";

			boolean checkOhub = getSourceId(bucketName, connectionTarget) != null && !getSourceId(bucketName, connectionTarget).equals("");
			MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionSource);
			OhubDoc ohubDoc = null;
			String apiLink = "";
			String id = "";

			if (mappingType.equals("mapBySource")) {
				countryId = Util.convertJsonArrayIdToStringId(countryId_array);
				ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connectionTarget);

				if (ohubDoc != null) {
					apiLink = ohubDoc.getLink();
					param = Util.getParameterByAPILink(ohubDoc.getLink());
					active = apiLink.contains("/golden") || apiLink.contains("/campaignWaveSendings") ? "1" : "0";

					if (checkOhub) {
						targetContent = getDocumentContent(bucketName, connectionTarget, mappingType, "", "", param + getSourceId(bucketName, connectionTarget), param, idList, active);
					}
				}
				if (ohubDoc == null || !checkOhub) {
					if (mySqlDoc == null) {
						mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, customRepository.findConnectionDocByBaseTable(baseTable));
						keyField = "id"; //use id instead of keySource if mapping to ohub-ohub or ohub-mysql to prevent null exceptions.
					}
					idList = new JSONArray(generateSourceId(keyField, mySqlDoc, baseTable, countryId, active).toString());
					id = Util.getIdFromSourceArray(idList, keyField);
					if (Util.checkCampaignAPIByLink(apiLink) || apiLink.contains("/salesData") || apiLink.contains("/primaryOrderData")/* || !realTime */) {
						System.err.println("NON-REAL TIME MAPPING");
						targetContent = getDocumentContent2(bucketName, connectionTarget, mappingType, keySource, countryId, id, active);//API to get data from couchbase/non-real time
					} else {
						System.err.println("REAL TIME MAPPING");
						System.err.println("CombineMultiApi total ids:" + idList.length());
						targetContent = getDocumentContent(bucketName, connectionTarget, mappingType, keyField, countryId, param + getSourceId(bucketName, connectionTarget), param, idList, active);
					}
				}
				System.err.println(connectionTarget + " in CombineMultiApi target length : " + targetContent.length());
			}
			if (i == 0) {
				if (Util.checkCampaignAPIByLink(apiLink) || mySqlDoc != null || apiLink.contains("/salesData") || apiLink.contains("/primaryOrderData")/* || !realTime*/) {
					sourceContent = getDocumentContent2(bucketName, connectionSource, mappingType, keySource, countryId, id, active);//API to get data from couchbase/non-real time
				} else {
					sourceContent = getDocumentContent(bucketName, connectionSource, mappingType, keySource, countryId, null, param, idList, active);
				}
				System.err.println(connectionSource + " in CombineMultiApi source length : " + sourceContent.length());
				aggArray = combine2Api(sourceContent, targetContent, connectionSource, connectionTarget, keySource, keyTarget);
			} else {
				aggArray = appendNextTarget(aggArray, targetContent, connectionSource, connectionTarget, keySource, keyTarget);
			}
		}
		return aggArray;
	}

	@Override
	public JSONArray combine2Api(JSONArray sourceArr, JSONArray targetArr, String connectionSource, String connectionTarget, String keySource, String keyTarget) throws Exception {
		JSONArray aggArray = new JSONArray();

		if (targetArr == null || sourceArr == null)
			return aggArray;
		if (targetArr.toString().equals("[]") || sourceArr.toString().equals("[]"))
			return aggArray;
		if (!sourceArr.get(0).toString().contains(keySource) || !targetArr.get(0).toString().contains(keyTarget)) {
			return aggArray;
		}

		System.err.println("targetArr length : " + targetArr.length());
		System.err.println("sourceArr length : " + sourceArr.length());

		for (int j = 0; j < sourceArr.length(); j++) {
			JSONObject jSource = sourceArr.getJSONObject(j);
			String keySourceValue = jSource.getString(keySource);
			JSONObject newJsonObject = new JSONObject();
			JSONArray newJsonArraySource = new JSONArray();
			JSONArray newJsonArrayTarget = new JSONArray();
			for (int k = 0; k < targetArr.length(); k++) {
				JSONObject jTarget = targetArr.getJSONObject(k);
				String keyTargetValue = jTarget.getString(keyTarget);
				if (keySourceValue.equals(keyTargetValue)) {
					newJsonArrayTarget.put(jTarget);
				}
			}
			if (newJsonObject != null && !newJsonObject.toString().equals("[]") && newJsonArrayTarget != null && !newJsonArrayTarget.toString().equals("[]")) {
				newJsonArraySource.put(jSource);
				newJsonObject.put(connectionSource, newJsonArraySource);
				newJsonObject.put(connectionTarget, newJsonArrayTarget);
				aggArray.put(newJsonObject);
			}
		}
		return aggArray;
	}

	public JSONArray appendNextTarget(JSONArray sourceArr, JSONArray targetArr, String connectionSource, String connectionTarget, String keySource, String keyTarget) throws Exception {
		for (int i = 0; i < sourceArr.length(); i++) {
			JSONObject jSource = sourceArr.getJSONObject(i);
			JSONArray sourceElementArr = new JSONArray();
			sourceElementArr = jSource.getJSONArray(connectionSource);
			JSONArray newJsonArray = new JSONArray();
			for (int j = 0; j < sourceElementArr.length(); j++) {
				JSONObject sourceElementObj = sourceElementArr.getJSONObject(j);
				for (int k = 0; k < targetArr.length(); k++) {
					JSONObject targetElementObj = targetArr.getJSONObject(k);

					if (sourceElementObj.has(keySource) && targetElementObj.has(keyTarget)) {
						if (sourceElementObj.getString(keySource).equals(targetElementObj.getString(keyTarget))) {
							// check duplicate target json object
							boolean flag_duplicate = false;
							for (int index = 0; index < newJsonArray.length(); index++) {
								if (newJsonArray.getJSONObject(index).equals(targetElementObj)) {
									flag_duplicate = true;
									break;
								}
							}
							if (flag_duplicate == false) {
								newJsonArray.put(targetElementObj);
							}

						}

					}
				}
			}
			jSource.put(connectionTarget, newJsonArray);
		}
		return sourceArr;
	}

	public JSONArray getDocumentContent(String bucketName, String documentKey) throws Exception {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, documentKey);
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, documentKey);

		JSONArray jsonArray = new JSONArray();
		if (ohubDoc != null) {
			jsonArray = ohubService.getOhubDataOfOhubConnection(ohubDoc);
		} else if (mySqlDoc != null) {
			// get content of mysql as json array
			jsonArray = customRepository.getAllDataFromTableMySQL(mySqlDoc);
		}
		return jsonArray;
	}

	@Override
	public String checkConnectionMySQL(String bucketName, MySqlDoc mySqlDoc) {
		// TODO Auto-generated method stub
		String mes = "";
		if (mySqlDoc == null) {
			mes = "Connection not found";
		} else {
			mes = customRepository.checkConnectionMySql(mySqlDoc);
		}
		return mes;
	}

	@Override
	public String checkConnectionOhubApi(String bucketName, OhubDoc ohub) {
		// TODO Auto-generated method stub
		String mes = "";
		if (ohub == null) {
			mes = "Connection not found";
		} else {
			mes = customRepository.checkConnectionOhub(ohub);
		}
		return mes;
	}

	public JSONArray getSelectedRecord(String bucketName, String connectionSource, String keyTarget, JSONArray sourceIdArr) throws Exception {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connectionSource);
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionSource);

		JSONArray jsonArray = new JSONArray();
		if (ohubDoc != null) {
			JSONArray ohubArr = new JSONArray();
			ohubArr = ohubService.getOhubDataOfOhubConnection(ohubDoc);
			int ohubArrLength = ohubArr.length();
			int sourceIdLength = sourceIdArr.length();
			if (ohubArrLength > 0) {
				if (sourceIdLength > 0) {
					for (int j = 0; j < sourceIdLength; j++) {
						String sourceId = "";
						if (Util.isInteger(sourceIdArr.get(j))) {
							sourceId = String.valueOf(sourceIdArr.getInt(j));
						} else {
							sourceId = sourceIdArr.getString(j);
						}
						for (int i = 0; i < ohubArrLength; i++) {
							JSONObject jsonObject = ohubArr.getJSONObject(i);
							if (jsonObject.getString(keyTarget).equals(sourceId)) {
								jsonArray.put(jsonObject);
							}
						}
					}
				}
			}
		} else if (mySqlDoc != null) {
			// get content of mysql as json array
			String sourceIdStr = sourceIdArr.toString().replace("[", "(").replace("]", ")");
			jsonArray = customRepository.getDataBySourceId(mySqlDoc, keyTarget, sourceIdStr);
		}
		return jsonArray;
	}

	@Override
	public JSONArray groupConnection(String bucketName, String inputString) throws Exception {
		JSONObject jsonObject = new JSONObject(inputString);
		JSONArray jsonArray = jsonObject.getJSONArray("maps");
		JSONArray bigJsonArray = new JSONArray();
		if (jsonArray.length() > 0) {
			JSONObject firstConnection = jsonArray.getJSONObject(0);
			JSONArray sourceIdArr = firstConnection.getJSONArray("sourceId");
			for (int i = 0; i < sourceIdArr.length(); i++) {
				String sourceId = "";
				if (Util.isInteger(sourceIdArr.get(i))) {
					sourceId = String.valueOf(sourceIdArr.getInt(i));
				} else {
					sourceId = sourceIdArr.getString(i);
				}
				JSONObject newObj = new JSONObject();
				for (int j = 0; j < jsonArray.length(); j++) {
					JSONObject jo2 = jsonArray.getJSONObject(j);
					String connectionSource = jo2.getString("connectionSource");
					String keyTarget = jo2.getString("keyTarget");
					JSONArray selectedData = getSelectedRecord(bucketName, connectionSource, keyTarget, sourceIdArr);
					if (selectedData.length() > 0) {
						JSONArray collectedArr = new JSONArray();
						for (int k = 0; k < selectedData.length(); k++) {
							JSONObject record = selectedData.getJSONObject(k);
							if (record.getString(keyTarget).equals(sourceId)) {
								collectedArr.put(record);
								if (!newObj.has(connectionSource)) {
									newObj.put(connectionSource, collectedArr);
								}
							} else {
								newObj.put(connectionSource, collectedArr);
							}
						}
					} else {
						newObj.put(connectionSource, new JSONArray());
					}
				}
				bigJsonArray.put(newObj);
			}
		}
		return bigJsonArray;
	}

	@Override
	public JSONArray groupConnectionbySourceId(String bucketName, String documentId) throws Exception {
		// TODO Auto-generated method stub
		JSONObject jsonObject = new JSONObject(documentId);
		JSONArray jsonArrayCon = jsonObject.getJSONArray("connection");
		JSONArray jsonArraySourceId = jsonObject.getJSONArray("sourceId");
		JSONArray bigJsonArray = new JSONArray();
		int lengthSource = jsonArraySourceId.length();
		for (int i = 0; i < lengthSource; i++) {
			JSONArray indexString = jsonArraySourceId.getJSONArray(i);
			int idSt = indexString.length();
			JSONObject newObj = new JSONObject();
			for (int index = 0; index < idSt; index++) {
				// JSONObject jo1 = indexString.getJSONObject(index);
				String sourceIdString = indexString.getString(index);
				JSONObject jo2 = jsonArrayCon.getJSONObject(index);
				String connectionSource = jo2.getString("connectionSource");
				String keyTarget = jo2.getString("keyTarget");
				JSONArray jsonData = getRecordBySource(bucketName, connectionSource, keyTarget, sourceIdString);
				int datajs = jsonData.length();
				// JSONObject newObj = new JSONObject();
				if (datajs > 0) {
					JSONArray collectedArr = new JSONArray();
					int jsdata = jsonData.length();
					for (int k = 0; k < jsdata; k++) {
						JSONObject record = jsonData.getJSONObject(k);
						if (record.getString(keyTarget).equals(sourceIdString)) {
							collectedArr.put(record);
							if (!newObj.has(connectionSource)) {
								newObj.put(connectionSource, collectedArr);
							}
						} else {
							newObj.put(connectionSource, collectedArr);
						}
					}
				} else {
					newObj.put(connectionSource, new JSONArray());
				}
				// objData.put(newObj);
			}
			bigJsonArray.put(newObj);
		}
		return bigJsonArray;
	}

	public JSONArray getRecordBySource(String bucketName, String connectionSource, String keyTarget, String sourceId) throws Exception {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connectionSource);
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionSource);
		JSONArray jsonArray = new JSONArray();
		if (ohubDoc != null) {
			JSONArray ohubArr = new JSONArray();
			ohubArr = ohubService.getOhubDataOfOhubConnection(ohubDoc);
			int ohubArrLength = ohubArr.length();
			if (ohubArrLength > 0) {
				for (int i = 0; i < ohubArrLength; i++) {
					JSONObject jsonObject = ohubArr.getJSONObject(i);
					if (jsonObject.getString(keyTarget).equals(sourceId)) {
						jsonArray.put(jsonObject);
					}
				}
			}
		} else if (mySqlDoc != null) {
			Connection conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
			String a = '"' + sourceId + '"';
			jsonArray = customRepository.getDataSourceIdFromMySQL(mySqlDoc, keyTarget, a, conn);
		}
		return jsonArray;
	}

	@Override
	public MappingConnection createMapCon(MappingConnection mappingConnection) {
		// TODO Auto-generated method stub
		mappingConnection.setId(mappingCounter.counter());
		mappingConnection.setType("MAPPING");
		if (mappingConnection.getDescription() == null) {
			mappingConnection.setDescription(mappingConnection.getConnection().toString());
		}
		return mappingConRepository.save(mappingConnection);
	}

	@Override
	public List<String> findAllMapping() throws Exception {
		// TODO Auto-generated method stub
		return customRepository.findAllMapping();
	}

	@Override
	public List<String> findMappingByName(String mappingName) throws Exception {
		// TODO Auto-generated method stub
		return customRepository.findMappingByName(mappingName);
	}

	@Override
	public MappingSource createMappingSource(MappingSource mappingSource) {
		// TODO Auto-generated method stub
		mappingSource.setId(mappingCounter.counter());
		mappingSource.setType("MAPPING");
		if (mappingSource.getDescription() == null) {
			mappingSource.setDescription(mappingSource.getMaps().toString());
		}
		return mappingSourceRepository.save(mappingSource);
	}

	@Override
	public List<String> getDataByKey(String documentKey, String connectionType) throws Exception {
		// TODO Auto-generated method stub
		List<String> lstDataConnection = customRepository.findConnectionByKey(documentKey, connectionType);
		return lstDataConnection;
	}

	@Override
	public List<String> findMappingById(String mappingName) throws Exception {
		// TODO Auto-generated method stub
		return customRepository.findMappingId(mappingName);
	}

	@Override
	public List<String> getAllDataMapping() throws Exception {
		// TODO Auto-generated method stub
		return customRepository.getAllDataMapping();
	}

	@Override
	public JSONArray getDocumentContent(String bucketName, String documentKey, String operation, String keySource, String countryId, String sourceId, String param, JSONArray idList, String active) throws Exception {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, documentKey);
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, documentKey);
		JSONArray jsonArray = new JSONArray();

		if (operation.equals("mapBySource")) {
			if (ohubDoc != null) {
				jsonArray = ohubService.getOhubDataOfOhubConnection(bucketName, ohubDoc, sourceId, param, keySource, idList);
			} else if (mySqlDoc != null) {
				if (param.contains("byCountryId")) {
					String idListStr = "'" + Util.convertJsonArrayIdToStringId(idList) + "'";
					jsonArray = customRepository.getAllDataFromTableMySQLByCountryId(mySqlDoc, operation, keySource, countryId, idListStr, active);
				} else {
					jsonArray = customRepository.getAllDataFromTableMySQLByCountryId(mySqlDoc, operation, keySource, countryId, "", active);
				}
			}
		}
		return jsonArray;
	}

	@Override
	public String getSourceId(String bucketName, String documentKey) {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, documentKey);
		String value = "";

		if (ohubDoc != null) {
			String link = ohubDoc.getLink();
			String key = Util.getParameterByAPILink(link).replace("=", "");
			if (ohubDoc.getParameters().get(key) != null) {
				value = ohubDoc.getParameters().get(key);
			}
		}
		return value;
	}

	@Override
	public JSONArray generateSourceId(String fields, MySqlDoc mySqlDoc, String baseTable, String countryId, String active) throws JSONException, Exception {
		JSONArray ids = new JSONArray(customRepository.getDataByCountryId(fields, mySqlDoc, baseTable, countryId, active).toString());
		return ids;
	}

	/*@Override
	public String getMappingBySupMapId(String mappingId, String[] id) throws Exception {
		return customRepository.getMappingBySupMapId(mappingId, id);
	}*/

	@Override
	public JSONArray combineMultiApi2(String bucketName, String inputString) throws Exception {
		JSONArray jsonArray = new JSONArray(inputString);
		JSONArray aggArray = new JSONArray();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jo2 = jsonArray.getJSONObject(i);
			String connectionSource = jo2.getString("connectionSource");
			String connectionTarget = jo2.getString("connectionTarget");
			String mappingType = jo2.getString("mappingType");
			String keySource = jo2.getString("keySource");
			String keyTarget = jo2.getString("keyTarget");
			JSONArray sourceContent = new JSONArray();
			JSONArray targetContent = new JSONArray();
			String countryId = "";
			String active = "0";

			if (mappingType.equals("mapBySource")) {
				countryId = Util.convertJsonArrayIdToStringId(jo2.getJSONArray("countryId"));
				targetContent = getDocumentContent2(bucketName, connectionTarget, mappingType, keySource, countryId, "", active);//make it dynamic for mysql-mysql, ohub-ohub, mysql-ohub, ohub-mysql

				if (i == 0) {
					sourceContent = getDocumentContent2(bucketName, connectionSource, mappingType, keySource, countryId, "", active);
					aggArray = combine2Api(sourceContent, targetContent, connectionSource, connectionTarget, keySource, keyTarget);
				} else {
					aggArray = appendNextTarget(aggArray, targetContent, connectionSource, connectionTarget, keySource, keyTarget);
				}
			}
		}
		return aggArray;
	}

	@Override
	public JSONArray getDocumentContent2(String bucketName, String connectionKey, String operation, String keySource, String countryId, String idList, String active) throws Exception {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connectionKey);
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionKey);
		JSONArray jsonArray = new JSONArray();

		if (ohubDoc != null) {
			jsonArray = new JSONArray(customRepository.getConnectionDocument("result", "OHUB-API-SAVED-CONNECTION", connectionKey, idList).toString());//add parameter id Field?
		} else if (mySqlDoc != null) {
			jsonArray = customRepository.getAllDataFromTableMySQLByCountryId(mySqlDoc, operation, keySource, countryId, "", active);
		}
		return jsonArray;
	}

	@Override
	public JSONArray combineMultiApiPaginated2(String bucketName, String inputString, int lastSourceIndex, int limit/*, boolean  realTime*/) throws Exception {
		JSONArray jsonArray = new JSONArray(inputString);
		JSONArray aggArray = new JSONArray();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jo2 = jsonArray.getJSONObject(i);
			String connectionSource = jo2.getString("connectionSource");
			String connectionTarget = jo2.getString("connectionTarget");
			String mappingType = jo2.getString("mappingType");
			String keySource = jo2.getString("keySource");
			String keyTarget = jo2.getString("keyTarget");
			String baseTable = jo2.getString("baseTable");
			JSONArray sourceContent = new JSONArray();
			JSONArray targetContent = new JSONArray();
			JSONArray idList = null;
			JSONArray countryId_array = jo2.getJSONArray("countryId");
			String param = "sourceId=";
			String countryId = "";
			String keyField = keySource;
			String active = "0";

			boolean checkOhub = getSourceId(bucketName, connectionTarget) != null && !getSourceId(bucketName, connectionTarget).equals("");
			MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionSource);
			OhubDoc ohubDoc = null;
			String apiLink = "";
			String id = "";

			if (mappingType.equals("mapBySource")) {
				countryId = Util.convertJsonArrayIdToStringId(countryId_array);
				ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connectionTarget);

				if (ohubDoc != null) {
					apiLink = ohubDoc.getLink();
					param = Util.getParameterByAPILink(ohubDoc.getLink());
					active = apiLink.contains("/golden") || apiLink.contains("/campaignWaveSendings") ? "1" : "0";

					if (checkOhub) {
						targetContent = getDocumentContent(bucketName, connectionTarget, mappingType, "", "", param + getSourceId(bucketName, connectionTarget), param, idList, active);
					}
				}
				if (ohubDoc == null || !checkOhub) {
					if (mySqlDoc == null) {
						mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, customRepository.findConnectionDocByBaseTable(baseTable));
						keyField = "id"; //use id instead of keySource if mapping to ohub-ohub or ohub-mysql to prevent null exceptions.
					}
					idList = new JSONArray(generateSourceId(keyField, mySqlDoc, baseTable, countryId, active).toString());
					id = Util.getIdFromSourceArray(idList, keyField);
					if (Util.checkCampaignAPIByLink(apiLink) || apiLink.contains("/salesData")/* || !realTime */) {
						System.err.println("NON-REAL TIME MAPPING");
						targetContent = getDocumentContent2(bucketName, connectionTarget, mappingType, keySource, countryId, id, active);//API to get data from couchbase/non-real time
					} else {
						System.err.println("REAL TIME MAPPING");
						System.err.println("COMBINE API PAGINATED idList:" + idList.length());
						targetContent = getDocumentContent(bucketName, connectionTarget, mappingType, keyField, countryId, param + getSourceId(bucketName, connectionTarget), param, idList, active);
					}
				}
				System.err.println(connectionTarget + " in combine1 target length : " + targetContent.length());
			}
			if (i == 0) {
				if (Util.checkCampaignAPIByLink(apiLink) || mySqlDoc != null || apiLink.contains("/salesData")/* || !realTime */) {
					sourceContent = getDocumentContent2(bucketName, connectionSource, mappingType, keySource, countryId, id, active);//API to get data from couchbase/non-real time
				} else {
					sourceContent = getDocumentContent(bucketName, connectionSource, mappingType, keySource, countryId, null, param, idList, active);
				}
				System.err.println(connectionSource + " in COMBINE API PAGINATED source length : " + sourceContent.length());
				aggArray = combine2ApiPaginated2(sourceContent, targetContent, connectionSource, connectionTarget, keySource, keyTarget, lastSourceIndex, limit);
			} else {
				aggArray = appendNextTarget(aggArray, targetContent, connectionSource, connectionTarget, keySource, keyTarget);
			}
		}
		return aggArray;
	}

	public JSONArray combine2ApiPaginated2(JSONArray sourceArr, JSONArray targetArr, String connectionSource, String connectionTarget, String keySource, String keyTarget, int lastSourceIndex, int resultLimit) throws Exception {
		JSONArray aggArray = new JSONArray();

		if (targetArr == null || sourceArr == null)
			return aggArray;
		if (targetArr.toString().equals("[]") || sourceArr.toString().equals("[]"))
			return aggArray;
		if (!sourceArr.get(0).toString().contains(keySource) || !targetArr.get(0).toString().contains(keyTarget)) {
			return aggArray;
		}

		for (int j = lastSourceIndex; j < sourceArr.length(); j++) {
			JSONObject jSource = sourceArr.getJSONObject(j);
			String keySourceValue = jSource.getString(keySource);
			JSONObject newJsonObject = new JSONObject();
			JSONArray newJsonArraySource = new JSONArray();
			JSONArray newJsonArrayTarget = new JSONArray();

			if (aggArray.length() >= resultLimit || j + 1 >= sourceArr.length()) {
				String value = "";
				JSONObject msgObj = new JSONObject();
				if (j + 1 >= sourceArr.length()) {
					value = "END_OF_RESULT";
				} else {
					lastSourceIndex = j;
					value = String.valueOf(lastSourceIndex);
				}
				msgObj.put("lastSourceIndex", value);
				aggArray.put(msgObj);
				break;
			}

			for (int k = 0; k < targetArr.length(); k++) {
				JSONObject jTarget = targetArr.getJSONObject(k);
				String keyTargetValue = jTarget.getString(keyTarget);
				if (keySourceValue.equals(keyTargetValue)) {
					newJsonArrayTarget.put(jTarget);
					if (newJsonArrayTarget.length() > 0)
						break;
				}
			}
			if (newJsonObject != null && !newJsonObject.toString().equals("[]") && newJsonArrayTarget != null && !newJsonArrayTarget.toString().equals("[]")) {
				newJsonArraySource.put(jSource);
				newJsonObject.put(connectionSource, newJsonArraySource);
				newJsonObject.put(connectionTarget, newJsonArrayTarget);
				aggArray.put(newJsonObject);
			}
		}
		return aggArray;
	}

	@Override
	public JSONArray compareArmstrongToOhub(String bucketName, JSONArray body) {
		JSONArray supermapperData = new JSONArray();

		if (body == null || body.toString().equals("[]")) {
			return supermapperData;
		}
		try {
			System.err.println("SAVING SUPERMAPPERID DATA");
			String armstrongConnectionId = body.getJSONObject(0).getString("armstrongConnectionId");
			String ohubConnectionId = body.getJSONObject(0).getString("ohubConnectionId");
			String countryId = Util.convertJsonArrayIdToStringId(body.getJSONObject(0).getJSONArray("countryId"));
			String armstrong_key = body.getJSONObject(0).getString("armstrong_key");
			String ohub_key = body.getJSONObject(0).getString("ohub_key");

			MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, armstrongConnectionId);
			OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, ohubConnectionId);
			String baseTable = mySqlDoc.getDatabaseTable();
			String apiLink = ohubDoc.getLink();
			String active = apiLink.contains("/golden") || apiLink.contains("/campaignWaveSendings") ? "1" : "0";
			String armstrong_fields = Util.convertJsonArrayIdToStringId(new JSONArray(mySqlDoc.getFields()));

			JSONArray armstrongIds = new JSONArray(generateSourceId("", mySqlDoc, baseTable, countryId, active).toString());
			JSONArray ids = new JSONArray();
			for (int j = 0; j < armstrongIds.length(); j++) {
				ids.put(armstrongIds.get(j));
			}
			JSONArray armstrongData = customRepository.getDataByCountryId(armstrong_fields, mySqlDoc, baseTable, countryId, active);
			JSONArray ohubData = ohubService.getOhubDataOfOhubConnection2(bucketName, ohubDoc, ohub_key, ohub_key, armstrong_key, ids);

			for (int i = 0; i < armstrongData.length(); i++) {
				JSONObject data = new JSONObject();
				JSONObject armstrongDataObj = armstrongData.getJSONObject(i);
				String armstrong_keyValue = armstrongDataObj.get(armstrong_key).toString();
				boolean empty = true;
				boolean hasOhub = false;

				if (ohubData != null) {
					for (int j = 0; j < ohubData.length(); j++) {
						JSONObject ohubDataObj = ohubData.getJSONObject(j);
						String ohub_keyValue = ohubDataObj.get(ohub_key).toString();

						if (armstrong_keyValue.equals(ohub_keyValue)) {
							hasOhub = true;
							try {
								String str = Util.checkMatchedChars(armstrongDataObj, armstrongConnectionId, ohubDataObj, ohubConnectionId).toString().replaceAll("\"", "").replaceAll("\\\\", "\"");//try to remove this later is possible
								JSONArray array = new JSONArray(str);
								data = array.getJSONObject(0).getJSONArray(armstrongConnectionId).getJSONObject(0);
								if (!data.toString().equals("[]") && data != null) {
									empty = false;
								}
							} catch (Exception e) {
								e.printStackTrace();
								System.err.println("Unknown character cannot be formatted! skipping record.");
							}
							break;
						}
					}
				}
				if (!empty) {
					data.put("01_ohub", hasOhub);
					data.put("02_leverage", false);
					data.put("03_id", armstrong_keyValue);
					supermapperData.put(Util.objectMapper(data.toString()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("An error occured while processing supermapper id records : " + e);
		}
		System.err.println("SAVING SUPERMAPPERID COMPLETED.");
		return supermapperData;
	}

	@Override
	public JSONArray supermapperMerge(String bucketName, String requestBody) {
		JSONArray array_result = new JSONArray();
		try {
			JSONArray body = new JSONArray(requestBody);
			JSONObject bodyObj = body.getJSONObject(0);

			String sourceId = bodyObj.getString("sourceId");
			String connectionSource = bodyObj.getString("connectionSource");
			String keySource = bodyObj.getString("keySource");
			String connectionTarget = bodyObj.getString("connectionTarget");
			String mappingType = "mapBySource";
			String countryId = "null";

			JSONArray sourceContent = new JSONArray();
			JSONArray targetContent = new JSONArray();
			OhubDoc ohubDoc = (OhubDoc) getDocumentData(bucketName, connectionTarget);

			String param = Util.getParameterByAPILink(ohubDoc.getLink());
			String apiLink = ohubDoc.getLink();
			String active = apiLink.contains("/golden") || apiLink.contains("/campaignWaveSendings") ? "1" : "0";

			JSONArray idList = new JSONArray();
			idList.put(new JSONObject().put(keySource, sourceId));
			sourceContent = getDocumentContent(bucketName, connectionSource, mappingType, keySource, countryId, keySource, "byCountryId", new JSONArray().put(sourceId), active);
			targetContent = getDocumentContent(bucketName, connectionTarget, mappingType, keySource, countryId, param + getSourceId(bucketName, connectionTarget), param, idList, active);

			if (sourceContent.toString().equals("[]") || sourceContent == null || targetContent.toString().equals("[]") || targetContent == null) {
				return array_result;
			}

			array_result = Util.checkMatchedChars(sourceContent.getJSONObject(0), connectionSource, targetContent.getJSONObject(0), connectionTarget);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("An error occured while merging supermapper record." + e);
			return array_result.put(e);
		}
		return array_result;
	}

	@Override
	public Object getDocumentData(String bucketName, String documentKey) throws Exception {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, documentKey);
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, documentKey);

		if (ohubDoc != null) {
			return ohubDoc;
		} else if (mySqlDoc != null) {
			return mySqlDoc;
		}
		return null;
	}

	@Override
	public JSONArray getGooglePlacesData(String bucketName, String requestBody) throws Exception {
		final String API_KEY = "AIzaSyCr0DlPoC0Oqm3U177o54m9m5GPkDNJOoI";
		final String SEARCH_FIELDS = "place_id";
		final String[] PLACE_ID_FIELDS = { "opening_hours/weekday_text", "formatted_address", "address_component", "geometry", "name", "rating", "website", "formatted_phone_number" };

		JSONArray result = new JSONArray();
		JSONArray body = new JSONArray(requestBody);
		String armstrongTable = body.getJSONObject(0).getString("armstrongTable");
		String armstrongField = body.getJSONObject(0).getString("armstrongField");
		final String API_NAME = "GOOGLE_PLACES_API";
		String sourceId = body.getJSONObject(0).getString("sourceId");
		JSONArray countries = body.getJSONObject(0).has("countryId") ? body.getJSONObject(0).getJSONArray("countryId") : new JSONArray().put("0");
		MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, armstrongTable);

		String armstrong_input = "";
		String place_id = "";

		for (int countryIndex = 0; countryIndex < countries.length(); countryIndex++) {
			JSONArray armstrongData = new JSONArray(customRepository.getAllDataFromTableMySQLByCountryId(mySqlDoc, "mapBySource", armstrongField, "0", "'" + sourceId + "'", "").toString());

			for (int armstrongIndex = 0; armstrongIndex < armstrongData.length(); armstrongIndex++) {
				JSONObject armstrongObject = armstrongData.getJSONObject(armstrongIndex);
				String country = Util.getCountryNameById(Integer.valueOf(countries.getString(countryIndex)));
				armstrong_input = armstrongObject.getString("armstrong_2_customers_name") + " " + armstrongObject.getString("street_address") + " " + armstrongObject.getString("postal_code") + " " + country;
				armstrong_input = armstrong_input.replaceAll("#", "").replaceAll(" ", "%20").replaceAll(Util.REGEX_INVALID_CHARS, "");
				String findPlaceAPILink = new String("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + armstrong_input + "&inputtype=textquery&fields=" + SEARCH_FIELDS + "&key=" + API_KEY);

				OhubDoc ohubDoc = new OhubDoc();
				ohubDoc.setLink(findPlaceAPILink);
				ohubDoc.setMethod("GET");
				ohubDoc.setParameters(new HashMap<String, String>());
				ohubDoc.setOtherParameters(new HashMap<String, String>());
				JSONArray armstrongResult = Util.readRestfulAPIDemoGETmethod(ohubDoc);

				if (!armstrongResult.toString().contains("error_message") && armstrongResult != null) {
					if (armstrongResult.getJSONObject(0).has("candidates") && !armstrongResult.getJSONObject(0).getJSONArray("candidates").toString().equals("[]")) {
						JSONObject container = new JSONObject();
						container.put(mySqlDoc.getDocumentId(), new JSONArray().put(armstrongObject));
						JSONArray candidates = armstrongResult.getJSONObject(0).getJSONArray("candidates");
						JSONArray googleResults = new JSONArray();
						for (int candidateIndex = 0; candidateIndex < candidates.length(); candidateIndex++) {
							JSONObject candid_search_result = candidates.getJSONObject(candidateIndex);

							if (googleResults.length() > 0) {
								break;
							}

							if (candid_search_result.has("place_id")) {
								place_id = candid_search_result.getString("place_id");//check multiple result
								String placeDetailLink = new String("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&key=" + API_KEY + "&fields=" + String.join(",", PLACE_ID_FIELDS));

								ohubDoc.setLink(placeDetailLink);
								JSONObject api_result = new JSONArray(Util.readRestfulAPIDemoGETmethod(ohubDoc).toString()).getJSONObject(0).getJSONObject("result");

								JSONObject geometry = api_result.getJSONObject("geometry");
								api_result.put("lat", geometry.getJSONObject("location").get("lat"));
								api_result.put("lng", geometry.getJSONObject("location").get("lng"));

								JSONArray addressComponents = api_result.getJSONArray("address_components");
								for (int aci = 0; aci < addressComponents.length(); aci++) {
									JSONObject adrObject = addressComponents.getJSONObject(aci);
									if (adrObject.toString().contains("postal_code")) {
										api_result.put("postal_code", adrObject.getString("short_name"));
										break;
									}
								}

								for (int mfi = 0; mfi < PLACE_ID_FIELDS.length; mfi++) {
									if (!api_result.has(PLACE_ID_FIELDS[mfi])) {
										api_result.put(PLACE_ID_FIELDS[mfi], "-");
									}
								}

								if (!api_result.has("postal_code")) {
									api_result.put("postal_code", "-");
								}

								if (api_result.has("opening_hours")) {
									JSONArray weekday = api_result.getJSONObject("opening_hours").getJSONArray("weekday_text");
									api_result.remove("opening_hours");
									api_result.put("opening_hours", Util.convertJsonArrayIdToStringId(weekday));
								} else {
									api_result.put("opening_hours", "-");
								}

								if (candidates.length() > 1) {
									if (api_result.has("address_components")) {
										if (api_result.has("postal_code")) {
											String postalCode = api_result.get("postal_code").toString();
											if (postalCode.equals(armstrongObject.get("postal_code")) || postalCode.equals(armstrongObject.get("postal_code"))) {
												googleResults.put(api_result);
												break;
											}
										}
									}
								} else {
									googleResults.put(api_result);
								}
							}
						}
						container.put(API_NAME, googleResults);
						result.put(container);

						if (result.length() >= 1 || armstrongIndex >= armstrongData.length()) {
							JSONArray temp = new JSONArray(Util.checkMatchedChars(result.getJSONObject(0).getJSONArray(armstrongTable).getJSONObject(0), armstrongTable + API_NAME, result.getJSONObject(0).getJSONArray(API_NAME).getJSONObject(0), API_NAME).toString().replaceAll("\"", "").replaceAll("\\\\", "\""));
							JSONObject newObject = new JSONObject();
							newObject.put("\"" + API_NAME + "\"", new JSONArray().put(Util.objectMapper(temp.getJSONObject(0).getJSONArray(API_NAME).get(0).toString())));
							result = new JSONArray();
							result.put(newObject);
							return result;
						}
					}
				} //Thread.sleep((1000));
			}
		}
		return result;
	}

	@Override
	public JSONArray combine2ApiForGetOHUB(JSONArray sourceArray, String sourceKey, JSONArray targetArray, String targetKey) {
		JSONArray result = new JSONArray();

		for (int i = 0; i < sourceArray.length(); i++) {
			JSONObject sourceObj = sourceArray.getJSONObject(i);
			JSONObject combinedResult = new JSONObject();

			for (int j = 0; j < targetArray.length(); j++) {

				JSONObject targetObj = targetArray.getJSONObject(j);
				if (targetObj.get(targetKey).equals(sourceObj.get(sourceKey))) {
					for (String sourceKeys : sourceObj.keySet()) {
						combinedResult.put(sourceKeys, Util.checkNullString(sourceObj.get(sourceKeys)));
					}
					for (String targetKeys : targetObj.keySet()) {
						combinedResult.put(targetKeys, Util.checkNullString(targetObj.get(targetKeys)));
					}
					if (combinedResult != null && !combinedResult.toString().equals("[]"))
						result.put(combinedResult);
					combinedResult = new JSONObject();
				}
			}
		}

		return result;
	}
}
