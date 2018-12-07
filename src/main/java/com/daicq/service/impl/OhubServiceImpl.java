package com.daicq.service.impl;

import com.daicq.dao.OhubCounterRepository;
import com.daicq.dao.OhubRepository;
import com.daicq.dao.doc.OhubDoc;
import com.daicq.service.OhubService;
import com.daicq.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * implementation for OhubService: process for Ohub data
 */
@Service
public class OhubServiceImpl implements OhubService {
	@Autowired
	OhubRepository ohubRepository;
	@Autowired
	OhubCounterRepository ohubCounterRepository;

	/**
	 * create new Ohub document into couchbase server
	 * 
	 * @Params: Ohub document
	 */
	@Override
	public OhubDoc create(OhubDoc ohubDoc, String couchbaseBooketName) {
		// TODO Auto-generated method stub
		List<OhubDoc> listOhubDoc = ohubRepository.findAllOhub(couchbaseBooketName);
		if (!Util.checkDuplicateOhub(listOhubDoc, ohubDoc)) {
			ohubDoc.setId(ohubCounterRepository.counter().toString());
			return ohubRepository.save(ohubDoc);
		}
		return null;
	}

	/**
	 * get all ohub document in couchbase server
	 * 
	 * @return: List ohub document
	 */
	@Override
	public List<OhubDoc> findAll(String couchbaseBooketName) {
		// TODO Auto-generated method stub
		return ohubRepository.findAllOhub(couchbaseBooketName);
	}

	/**
	 * create new Ohub document into couchbase server
	 * 
	 * @Params: Ohub document
	 */
	@Override
	public void createD(String couchbaseBooketName, String key, String value) {
		// TODO Auto-generated method stub
		ohubRepository.createOhubDocumentInCouchBase(couchbaseBooketName, key, value);
	}

	@Override
	public JSONArray getOhubDataOfOhubConnection(OhubDoc ohubDoc) {
		// TODO Auto-generated method stub
		JSONArray jsonArray_Result = new JSONArray();
		if (ohubDoc == null) {
			return jsonArray_Result;
		}
		String method = ohubDoc.getMethod();
		if (method.equals("GET")) {
			return Util.readRestfulAPIDemoGETmethod(ohubDoc);
		} else if (method.equals("POST")) {
			return Util.readRestfulAPIDemoPOSTmethod(ohubDoc);
		}
		return jsonArray_Result;
	}

	@Override
	public List<String> getOhubFieldOfConnection(OhubDoc ohubDoc) {
		// TODO Auto-generated method stub
		List<String> resultS = new ArrayList<String>();
		JSONArray jsonArray = new JSONArray();
		jsonArray = getOhubDataOfOhubConnection(ohubDoc);
		try {
			if (jsonArray.length() != 0) {
				Iterator<?> keys = jsonArray.getJSONObject(0).keys();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					resultS.add(key);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultS;
	}

	@Override
	public OhubDoc updateOhub(String key, OhubDoc ohubDoc, String bucketName) {
		// TODO Auto-generated method stub
		final OhubDoc ohubExist = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, key);
		//ohubExist.setChannels(ohubDoc.getChannels());
		ohubExist.setConnectionType(ohubDoc.getConnectionType());
		ohubExist.setCreatedBy(ohubDoc.getCreatedBy());
		ohubExist.setDateUpdated(ohubDoc.getDateUpdated());
		ohubExist.setFields(ohubDoc.getFields());
		ohubExist.setIdentifierKey(ohubDoc.getIdentifierKey());
		ohubExist.setLink(ohubDoc.getLink());
		ohubExist.setMethod(ohubDoc.getMethod());
		if (ohubDoc.getName() != null) {
			ohubExist.setName(ohubDoc.getName());
		} else {
			ohubExist.setName("");
		}
		ohubExist.setOtherParameters(ohubDoc.getOtherParameters());
		ohubExist.setParameters(ohubDoc.getParameters());
		ohubExist.setType(ohubDoc.getType());
		ohubExist.setUpdatedBy(ohubDoc.getUpdatedBy());
		return ohubRepository.save(ohubExist);
	}

	public JSONArray getOhubDataOfOhubConnection(String bucketName, OhubDoc ohubDoc, String sourceId, String param, String keySource, JSONArray idList) {
		JSONArray jsonArray_result = new JSONArray();

		if (ohubDoc == null) {
			return jsonArray_result;
		}

		String method = ohubDoc.getMethod();
		String link = ohubDoc.getLink();
		JSONObject data = new JSONObject();

		JSONArray postGet_result = new JSONArray();
		JSONArray ids = new JSONArray();
		JSONArray temp_array = new JSONArray();
		int counter = 0;
		int targetCount = method.equals("GET") ? 100 : 1;

		if (!param.equals("campaignWaveResponseId=") && sourceId.equals(param) && method.equals("POST")) {
			if (!ohubDoc.getLink().contains("/campaignWaveSendings"))
				targetCount = 5000;
			else
				targetCount = 1000;
		} else if (param.equals("campaignWaveResponseId=") && method.equals("POST") && sourceId.equals(param)) {
			targetCount = 1;// for campaignWaveResponse APIs
		}

		if (link.contains("campaign/links") || link.contains("campaign/opens") || link.contains("display/campaignWaveBounceResponse")) {
			System.err.println("Getting waveSendings API data for opens/links....");
			OhubDoc ohubDocWaveSendings = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, "OHUB_CAMPAIGN_WAVE_SENDINGS");

			for (int index = 0; index < idList.length(); index++) {
				try {
					ids.put(idList.get(index));
					counter++;
					if (index >= idList.length() - 1) {
						counter = targetCount;
					}
					if (counter >= targetCount) {
						data = Util.generateMultiplePOSTRequestData(ids, "sourceId=", keySource);
						postGet_result = Util.readRestfulAPIDemoPOSTmethod(ohubDocWaveSendings, data, param);

						if (postGet_result != null)
							temp_array.put(postGet_result);
						ids = new JSONArray();
						counter = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			jsonArray_result = Util.getDataFromMultiplePOSTResult(temp_array);
			idList = Util.getAllCWResponseIdFromCWSendings(jsonArray_result);//override idList for campaign opens
		}

		if (method.equals("GET") || method.equals("POST")) {
			//if no sourceId/cwResponseId exists in connection, create multiple GET request parameter ids and generate result using GET or POST.
			if (sourceId.equals(param) && method.equals("GET") || param.equals("campaignWaveResponseId=") && method.equals("POST") && sourceId.equals(param) || !param.equals("campaignWaveResponseId=") && sourceId.equals(param) && idList.length() > 20000) {
				for (int index = 0; index < idList.length(); index++) {
					try {
						ids.put(idList.get(index));//put the per batch id in a new array.
						counter++;
						if (index >= idList.length() - 1) {
							counter = targetCount;//number of ids per API call
						}
						if (counter >= targetCount) {
							if (method.equals("GET")) {
								data = Util.generateMultipleGETRequestId(ids, keySource);//jsonString for multiple ids
								postGet_result = Util.readRestfulAPIDemoGETmethod(ohubDoc, sourceId, param + data.getString("ids"));
							} else if (method.equals("POST")) {
								if (param.equals("campaignWaveResponseId=") && method.equals("POST") && sourceId.equals(param)) {
									data = Util.generateCWResponseIdFromCWSendings(ids);//jsonString for multiple ids
									postGet_result = Util.readRestfulAPIDemoPOSTmethod(ohubDoc, data, param);
								} else if (!param.equals("campaignWaveResponseId=") && sourceId.equals(param)) {
									data = Util.generateMultiplePOSTRequestData(ids, param, keySource);//jsonString for multiple ids
									postGet_result = Util.readRestfulAPIDemoPOSTmethod(ohubDoc, data, param);
								}
							}

							if (postGet_result != null)//check if the result from GET or POST is not null before adding to result array.
								temp_array.put(postGet_result);
							ids = new JSONArray();
							counter = 0;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				jsonArray_result = Util.getDataFromMultiplePOSTResult(temp_array);
				return jsonArray_result;
			}
		}
		//if sourceId/cwResponseId exists in connection, generate result using GET.
		if (method.equals("GET") && !sourceId.equals(param)) {
			return Util.readRestfulAPIDemoGETmethod(ohubDoc, sourceId, "");
		} else if (method.equals("POST") && idList.length() <= 20000) {
			//if sourceId/cwResponseId does not exists in connection, create multiple requestData and generate result using POST.
			if (!param.equals("campaignWaveResponseId=") && sourceId.equals(param)) {
				data = Util.generateMultiplePOSTRequestData(idList, param, keySource);
				return Util.readRestfulAPIDemoPOSTmethod(ohubDoc, data, param);
			}
			//if sourceId/cwResponseId exists in connection, generate result using POST.
			else if (!sourceId.equals(param)) {
				return Util.readRestfulAPIDemoPOSTmethod(ohubDoc, null, param);
			}
		}
		return jsonArray_result;
	}

	@Override
	public void upsertDocumentInCouchBase(String couchbaseBooketName, String key, String value) {
		ohubRepository.upSertDataFromOhubToCouchBase(couchbaseBooketName, key, value);
	}

	@Override
	public JSONArray getOhubDataOfOhubConnection2(String bucketName, OhubDoc ohubDoc, String sourceId, String param, String keySource, JSONArray idList) {
		JSONArray jsonArray_result = new JSONArray();

		if (ohubDoc == null) {
			return jsonArray_result;
		}

		String method = ohubDoc.getMethod();
		JSONObject data = new JSONObject();

		//if sourceId/cwResponseId exists in connection, generate result using GET.
		if (method.equals("GET")) {
			data = Util.generateMultipleGETRequestId(idList, keySource);//jsonString for multiple ids
			return Util.readRestfulAPIDemoGETmethod(ohubDoc, sourceId, param + data.getString("ids"));
		} else if (method.equals("POST")) {
			//if sourceId/cwResponseId does not exists in connection, create multiple requestData and generate result using POST.
			if (param.equals("campaignWaveResponseId=")) {
				data = Util.generateMultipleGETRequestId(idList, keySource);//use this function for both GET and POST when setting the campaignWaveResponseId for opens/links/campaignWaveResponse
			} else if (!param.equals("campaignWaveResponseId=")) {
				data = Util.generateMultiplePOSTRequestData(idList, param, keySource);
			}
			return Util.readRestfulAPIDemoPOSTmethod(ohubDoc, data, param);
		}
		return jsonArray_result;
	}

	@Override
	public String getCampaignWaveResponseId(String bucketName, String connectionId, String type) {
		return ohubRepository.getCampaignWaveResponseId(bucketName, connectionId, type).toString();
	}

	@Override
	public JSONArray getOhubDataOfConnection(String inputStr, String bucketName) {
		JSONArray result = new JSONArray();

		try {
			JSONArray input = new JSONArray(inputStr);
			JSONObject inputObj = input.getJSONObject(0);
			String targetApi = inputObj.getString("targetApi");
			JSONObject parameters = inputObj.getJSONObject("parameters");

			OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, targetApi);
			String method = ohubDoc.getMethod();
			String apiLink = ohubDoc.getLink();
			String strIgnore = method.equals("POST") || Util.checkCampaignAPIByLink(ohubDoc.getLink()) ? "source" : "----------";
			JSONObject requestDataObj = new JSONObject();
			Map<String, String> parametersMap = new HashMap<String, String>();

			if(apiLink.contains("/salesData"))
				strIgnore = "source";
			
			for (String key : parameters.keySet()) {
				Object object = parameters.get(key);
				String str = "";

				if (object instanceof JSONArray) {
					str = Util.convertJsonArrayIdToStringId((JSONArray) object);
				} else {
					str = object.toString();
				}
				if (!key.contains(strIgnore)) {
					parametersMap.put(key, str);
				} else if (key.contains(strIgnore)) {
					requestDataObj.put(key, str);
				}
			}
			
			ohubDoc.setParameters(parametersMap);
			
			if (Util.checkCampaignAPIByLink(apiLink) || apiLink.contains("/salesData")) {
				String ohubTarget = "OHUB_CAMPAIGN_WAVE_SENDINGS";
				String parameterOps = "campaign";
				String idToGet = "campaignWaveResponseId";

				if (apiLink.contains("/salesData")) {
					ohubTarget = "OHUB_WEBSHOP_ORDER";
					parameterOps = "sales";
					idToGet = "ordIntegrationId";
				}

				OhubDoc ohubOptional = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, ohubTarget);
				JSONArray otherIds = Util.readRestfulAPIDemoPOSTmethod(ohubOptional, requestDataObj, parameterOps);
				JSONArray tempo = new JSONArray();

				for (int i = 0; i < otherIds.length(); i++) {
					String keyCode = apiLink.contains("/salesData") ? "orderId" : "campaignWaveResponseId";
					
					if (method.equals("GET")) {
						tempo = Util.readRestfulAPIDemoGETmethod(ohubDoc, "", keyCode + "=" + otherIds.getJSONObject(i).getString(idToGet));
					} else if (method.equals("POST")) {
						requestDataObj = new JSONObject();
						requestDataObj.put("ids", otherIds.getJSONObject(i).getString(idToGet));
						tempo = Util.readRestfulAPIDemoPOSTmethod(ohubDoc, requestDataObj, keyCode + "=");
					}
					for (int j = 0; j < tempo.length(); j++) {
						JSONObject campaignObj = tempo.getJSONObject(j);
						
						if (apiLink.contains("/salesData")) {
							campaignObj.put("sourceId", Util.checkNullString(otherIds.getJSONObject(i).get("sourceId")));
							campaignObj.put("transactionDate", Util.checkNullString(otherIds.getJSONObject(i).get("transactionDate")));
							campaignObj.put("orderAmount", Util.checkNullString(otherIds.getJSONObject(i).get("orderAmount")));
							result.put(campaignObj);
						}
						if (Util.checkCampaignAPIByLink(apiLink)) {
							campaignObj.put("campaignName", Util.checkNullString(otherIds.getJSONObject(i).get("campaignName")));
							campaignObj.put("cpFirstName", Util.checkNullString(otherIds.getJSONObject(i).get("cpFirstName")));
							campaignObj.put("cpLastName", Util.checkNullString(otherIds.getJSONObject(i).get("cpLastName")));
							result.put(campaignObj);
						}
					}
				}
			} else {
				if (method.equals("GET")) {
					result = Util.readRestfulAPIDemoGETmethod(ohubDoc, "", "");
				} else if (method.equals("POST")) {
					requestDataObj.put("source", "ARMSTRONG");
					result = Util.readRestfulAPIDemoPOSTmethod(ohubDoc, requestDataObj, "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = result.put("An error occured : " + e);
		}
		return result;
	}
}
