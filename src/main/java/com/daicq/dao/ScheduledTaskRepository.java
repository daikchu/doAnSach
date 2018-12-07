package com.daicq.dao;

import com.daicq.dao.doc.MySqlDoc;
import com.daicq.dao.doc.OhubDoc;
import com.daicq.service.BucketService;
import com.daicq.service.CommonService;
import com.daicq.service.OhubService;
//import com.daicq.util.FileUtil;
import com.daicq.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Functions for scheduled tasks
 * 
 * @author Mikkel Pichay
 */

@Component
public class ScheduledTaskRepository {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTaskRepository.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	private OhubService ohubService;
	@Autowired
	private BucketService bucketService;
	@Autowired
	private OhubRepository ohubRepository;
	@Autowired
	private CommonService commonService;
	@Autowired
	private MySqlRepository mySqlRepository;
	@Autowired
	private CustomRepository customRepository;
	private static String saveType = "";
	private static String exceptionMsg = "Exception message compiled : \n";
	private static final String TYPE = "CONNECTION";
	private static final String OHUB_TYPE = "OHUB-API";
	private static int exceptionCounter = 0;

	public String saveOhubDataToCouchBase(String operation, String status, boolean deleteExisting) throws Exception {
		getThreadInfo();
		log.info("UPDATING {} STARTED AT : {}", saveType, dateFormat.format(new Date()));
		String bucketName = bucketService.getBucketName();
		saveType = "OHUB-API-SAVED-CONNECTION";
		if (deleteExisting) {
			customRepository.deleteDocumentInCouchBaseByType(saveType);// delete existing ohub first
		}
		JSONArray ohubConnections = new JSONArray(customRepository.findDistinctDocument("id, link, countryId, status", TYPE, OHUB_TYPE, status).toString());
		JSONArray otherIdList = null;
		JSONArray filteredOhubConnections = filterConnections(ohubConnections, operation);

		for (int i = 0; i < filteredOhubConnections.length(); i++) {
			try {
				JSONObject connection = filteredOhubConnections.getJSONObject(i);
				OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connection.get("id").toString());

				if (ohubDoc != null) {
					System.err.println("Current OHUB connection : " + ohubDoc.getDocumentId());
					if (ohubDoc.getCountryId() != null) {
						String parameter = Util.getParameterByAPILink(ohubDoc.getLink());
						String countryId = String.join(",", ohubDoc.getCountryId());
						String fieldForMap = "id";
						String baseTable = "MYSQL_CONTACTS";
						String apiLink = ohubDoc.getLink();
						String active = apiLink.contains("/golden") || apiLink.contains("/campaignWaveSendings") ? "1" : "0";

						if (apiLink.contains("display/recommendations") || apiLink.contains("/loyaltyStatus") || apiLink.contains("/display/golden/operator")) {
							baseTable = "MYSQL_24";
							fieldForMap = "armstrong_2_customers_id";
						}

						MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, baseTable);
						JSONArray armstrongIds = new JSONArray();
						int targetOHUBBatch = 2500;

						if (Util.checkCampaignAPIByLink(apiLink) || apiLink.contains("/salesData")) {
							targetOHUBBatch = 1;
							if (otherIdList == null) {
								String baseConnection = "";
								String fields = "";
								if (Util.checkCampaignAPIByLink(apiLink)) {
									baseConnection = "OHUB_CAMPAIGN_WAVE_SENDINGS"; // make this code dynamic, find a way to always get saved connection in couchbase
									fields = "r.campaignWaveResponseId as id, r.cpSourceId";
								} else if (apiLink.contains("/salesData")) {
									baseConnection = "OHUB_WEBSHOP_ORDER"; // add logic to use this for other types of primary order data
									fields = "r.ordIntegrationId as id, r.cpSourceId";
								}
								otherIdList = new JSONArray(customRepository.getOHUBFieldDataInCouchBase(baseConnection, saveType, fields).toString());
							}
							armstrongIds = otherIdList;
						} else {
							armstrongIds = new JSONArray(commonService.generateSourceId(fieldForMap, mySqlDoc, mySqlDoc.getDatabaseTable(), countryId, active).toString());
						}

						if (apiLink.contains("/campaignWaveSendings")) {
							targetOHUBBatch = 1000;
						}

						log.info("Total id from armstrong : {}", armstrongIds.length());
						int ohubBatchCounter = 0;
						int batchCounter = 0;
						JSONArray temp_array_result = new JSONArray();

						// by batch call of OHUB API
						for (int j = 0; j < armstrongIds.length(); j++) {
							temp_array_result.put(armstrongIds.get(j));
							ohubBatchCounter++;
							if (j >= armstrongIds.length() - 1) {
								ohubBatchCounter = targetOHUBBatch;
							}
							if (ohubBatchCounter >= targetOHUBBatch) {
								JSONArray ohub_array_result = new JSONArray();
								ohub_array_result = ohubService.getOhubDataOfOhubConnection2(bucketName, ohubDoc, parameter, parameter, fieldForMap, temp_array_result);
								if (ohub_array_result != null && !ohub_array_result.toString().equals("[]")) {
									int targetQueryBatch = 1;
									int queryBatchCounter = 0;
									JSONArray query_array = new JSONArray();

									// by batch query for saving in couchbase
									for (int k = 0; k < ohub_array_result.length(); k++) {
										query_array.put(ohub_array_result.getJSONObject(k));
										String id = "";

										if (Util.checkCampaignAPIByLink(apiLink) || apiLink.contains("/salesData")) {
											String tempKey = "";
											String ohubKey = "";
											if (Util.checkCampaignAPIByLink(apiLink)) {
												tempKey = "id";
												ohubKey = "campaignWaveResponseId";
											} else if (apiLink.contains("/salesData")) {
												tempKey = "id";
												ohubKey = "ordIntegrationId";
											}
											id = compareTwoStringInJSONArray(temp_array_result, tempKey, ohub_array_result, ohubKey, apiLink);
										} else {
											id = ohub_array_result.getJSONObject(k).get(Util.getSourceIdKeyByLink(apiLink)).toString();
										}
										queryBatchCounter++;
										if (k >= ohub_array_result.length() - 1) {
											queryBatchCounter = targetQueryBatch;
										}
										if (queryBatchCounter >= targetQueryBatch) {
											if (query_array != null && !query_array.toString().equals("[]")) {
												try {
													String savedConnectionDocId = ohubDoc.getConnectionType() + "-SAVED-CONNECTION-" + ohubDoc.getDocumentId() + "-" + batchCounter;
													ohubService.upsertDocumentInCouchBase(bucketName, savedConnectionDocId, Util.formatDataToSend(ohubDoc.getDocumentId(), saveType, "result", query_array, dateFormat.format(new Date()), id));
													batchCounter++;
												} catch (Exception e) {
													System.err.println("An error occured while upserting data to couchbase.");
												}
											}
											queryBatchCounter = 0;
											query_array = new JSONArray();
										}
									}
								}
								temp_array_result = new JSONArray();
								ohubBatchCounter = 0;
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error("An error has occured : " + e);
				exceptionMsg += e + "\n";
				exceptionCounter++;
			}
		}
		return operationComplete();
	}

	public String operationComplete() {
		log.warn(exceptionMsg);
		String msg = "UPDATING " + saveType + " FINISHED AT : " + dateFormat.format(new Date()) + " WITH " + exceptionCounter + " ERROR(S).";
		log.info(msg);
		exceptionMsg = "Exception message compiled : \n";
		exceptionCounter = 0;
		return msg;
	}

	/*
	 * Use to check if a string exist in another source
	 * 
	 * @param tempArray, tempKey, ohubArray, ohubKey, apiLink
	 * 
	 * @return matched string result
	 */
	private String compareTwoStringInJSONArray(JSONArray tempArrayResult, String tempKey, JSONArray ohubArrayResult, String ohubKey, String link) {
		// find the suitable cpSourceId for campaignAPI saving
		String result = "";
		for (int i = 0; i < tempArrayResult.length(); i++) {
			for (int j = 0; j < ohubArrayResult.length(); j++) {
				String tempId = tempArrayResult.getJSONObject(i).get(tempKey).toString();
				String ohubId = ohubArrayResult.getJSONObject(j).get(ohubKey).toString();
				if (tempId.equals(ohubId)) {
					result = tempArrayResult.getJSONObject(i).get(Util.getSourceIdKeyByLink(link)).toString();
					break;
				}
			}
		}
		return result;
	}

///*	public String saveMappingData() {
//		getThreadInfo();
//		log.info("SAVING MAPPING DATA TO FILE STORAGE STARTED!");
//		try {
//			String bucketName = bucketService.getBucketName();
//			JSONArray mappingData = new JSONArray(customRepository.getAllMapsData().toString());
//			for (int i = 0; i < mappingData.length(); i++) {
//				for (int j = 0; j < 24; j++) {
//					try {
//						JSONObject maps = mappingData.getJSONObject(i);
//						String mappingName = maps.get("id").toString();
//						String countryId = String.valueOf(j + 1);
//
//						System.err.println("Current country id : " + countryId);
//
//						maps.remove("countryId"); // remove existing countryId
//						maps.put("countryId", new JSONArray().put(countryId));// replace with every countryId using the array
//						JSONArray inputArray = new JSONArray("[" + maps.toString() + "]");// force conversion of jsonObject to jsonArray
//						JSONArray jsonData = commonService.combineMultiApi(bucketName, inputArray.toString());// call the mapping API
//						FileUtil.saveJsonToTxt(jsonData, mappingName, countryId); // save json mapping result to a text file
//						FileUtil.convertJSONToExcel(jsonData, mappingName, countryId); // convert json to excel and save to file storage
//						if(maps.getString("connectionSource").contains("CUSTOMER")) {
//							FileUtil.saveJsonToTxt(commonService.compareArmstrongToOhub(bucketName, supermapperRecordInput(maps)), mappingName + "-SUPERMAPPERID", countryId);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						System.err.println("An error occured while saving mapping data : " + e);
//					}
//				}
//			}
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return operationComplete();
//	}*/

	public void getThreadInfo() {
		log.info("Scheduled task executed in thread : {}", Thread.currentThread().getName());
	}

	/*
	 * Filter connections to be saved
	 */
	private JSONArray filterConnections(JSONArray ohubConnections, String operation) {//optimize this code
		JSONArray filteredOhubConnections = new JSONArray();
		JSONArray filteredDummyConnection = new JSONArray();

		for (int i = 0; i < ohubConnections.length(); i++) {
			JSONObject connection = ohubConnections.getJSONObject(i);
			
			if (connection.has("link") && connection.has("id") && connection.has("countryId")) {
				JSONObject connectionDummy = new JSONObject();
				String conLink = connection.getString("link");
				connectionDummy.put("link", conLink);
				connectionDummy.put("id", connection.getString("id"));
				if (!filteredDummyConnection.toString().contains(connectionDummy.toString())) {
					if (operation.equals("non-campaignAPI")) {
						if (!Util.checkCampaignAPIByLink(conLink) && conLink.contains("display/campaignWaveSendings") && !conLink.contains("display/campaignWaveSendingsFull") 
								|| !Util.checkCampaignAPIByLink(conLink) && conLink.contains("display/primaryOrderData")) {
							filteredDummyConnection.put(connectionDummy);
							filteredOhubConnections.put(connection);
						}
					} else if (operation.equals("campaignOpens")) {
						if (Util.checkCampaignAPIByLink(conLink) && conLink.contains("campaign/opens")) {
							filteredDummyConnection.put(connectionDummy);
							filteredOhubConnections.put(connection);
						}
					} else if (operation.equals("campaignLinks")) {
						if (Util.checkCampaignAPIByLink(conLink) && conLink.contains("campaign/links")) {
							filteredDummyConnection.put(connectionDummy);
							filteredOhubConnections.put(connection);
						}
					} else if (operation.equals("campaignWaveResponse")) {
						if (Util.checkCampaignAPIByLink(conLink) && conLink.contains("display/campaignWaveBounceResponse")) {
							filteredDummyConnection.put(connectionDummy);
							filteredOhubConnections.put(connection);
						}
					} else if (operation.equals("salesData")) {
						if (conLink.contains("/salesData")) {
							filteredDummyConnection.put(connectionDummy);
							filteredOhubConnections.put(connection);
						}
					}
				} else {
					log.info("{} : {} is already in the array.", connection.get("id"), connectionDummy.getString("link"));
				}
			}
		}
		return filteredOhubConnections;
	}
	
	public JSONArray supermapperRecordInput(JSONObject maps) {
		JSONArray requestBody = new JSONArray();
		JSONObject object = new JSONObject();
		object.put("armstrongConnectionId", maps.get("connectionSource"));
		object.put("armstrong_key", maps.get("keySource"));
		object.put("armstrong_fields", new JSONArray().put("armstrong_2_customers_id").put("armstrong_2_customers_name"));
		object.put("countryId", maps.getJSONArray("countryId"));
		object.put("ohubConnectionId", maps.get("connectionTarget"));
		object.put("ohub_key", maps.get("keyTarget"));
		requestBody.put(object);
		return requestBody;
	}
}