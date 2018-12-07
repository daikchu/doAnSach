package com.daicq.controller;

import com.daicq.dao.CustomRepository;
import com.daicq.dao.MySqlRepository;
import com.daicq.dao.OhubRepository;
import com.daicq.dao.ScheduledTaskRepository;
import com.daicq.dao.doc.MySqlDoc;
import com.daicq.dao.doc.OhubDoc;
import com.daicq.service.BucketService;
import com.daicq.service.CommonService;
import com.daicq.service.OhubService;
import com.daicq.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * process for ohub requests
 * 
 * @Author: To Thanh Luong
 */
@RestController
@RequestMapping("/ohub")
public class OhubController {
	@Autowired
	OhubService ohubService;
	@Autowired
	BucketService bucketService;
	@Autowired
	OhubRepository ohubRepository;
	@Autowired
	CustomRepository customRepository;
	@Autowired
	ScheduledTaskRepository scheduledTaskRepository;
	@Autowired
	CommonService commonService;
	@Autowired
	MySqlRepository mySqlRepository;

	/**
	 * create new ohub connection into couchbase
	 * 
	 * @param: ohub document return: String result message
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String create(@Valid @RequestBody OhubDoc ohubDoc) {
		String couchbaseBooketName = bucketService.getBucketName();
		String message = "";
		try {
			if (ohubService.create(ohubDoc, couchbaseBooketName) != null) {
				message = "New connection is created!";
			} else {
				message = "Connection is existed!";
			}
		} catch (Exception e) {
		}
		return message;
	}

	/**
	 * fill all couchbase document return: List document
	 */
	@RequestMapping(value = "/find/all", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<OhubDoc> findAll() {
		String couchbaseBooketName = bucketService.getBucketName();
		return ohubService.findAll(couchbaseBooketName);
	}

	/**
	 * get token return: String token
	 */
	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getToken() {
		return Util.getToken();
	}

	/**
	 * fill all fields of all document in couchbase return: List field
	 */
	@RequestMapping(value = "/find/allfieldsofallconnection", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getAllOhubFields() {
		String couchbaseBooketName = bucketService.getBucketName();
		// get all connection id by connection
		JSONArray result = new JSONArray();
		try {
			List<OhubDoc> list_OhubConnection = ohubService.findAll(couchbaseBooketName);
			int sizeOf_list_ConnectionOHUB = list_OhubConnection.size();
			for (int i = 0; i < sizeOf_list_ConnectionOHUB; i++) {

				String connectionId = list_OhubConnection.get(i).getDocumentId();
				JSONObject object = new JSONObject();
				object.put(connectionId, ohubService.getOhubFieldOfConnection(list_OhubConnection.get(i)));
				result.put(object);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * fill all fields of all document in couchbase by 'connectionId' parameter
	 * 
	 * @param: connectionId: connection id
	 * @return: List field
	 */
	@RequestMapping(value = "/find/allfieldsbyconnectionid", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public List<String> getOhubFieldsByCondition(@RequestParam("connectionId") String connectionId) {
		String couchbaseBooketName = bucketService.getBucketName();
		List<String> result = new ArrayList<>();
		try {
			OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(couchbaseBooketName, connectionId);
			return ohubService.getOhubFieldOfConnection(ohubDoc);
		} catch (Exception e) {
			result.add("get field error");
			return result;
		}
	}

	/**
	 * fill all fields of all document in couchbase by 'connectionId' parameter
	 * 
	 * @param: connectionId: connection id
	 * @return: List field
	 */
	@RequestMapping(value = "/getohubdatabyconnectionid", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public String getDataByconnectionid(@RequestParam("connectionId") String connectionId) {
		String bucketName = bucketService.getBucketName();
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketName, connectionId);
		return ohubService.getOhubDataOfOhubConnection(ohubDoc).toString();
	}

	/**
	 * update connection OHUB_API
	 * 
	 * @param key
	 * @param ohubDoc
	 * @return
	 */
	@RequestMapping(value = "/{key}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String update(@PathVariable String key, @Valid @RequestBody OhubDoc ohubDoc) {
		String mes = "";
		String bucketName = bucketService.getBucketName();
		try {
			if (ohubService.updateOhub(key, ohubDoc, bucketName) != null) {
				mes = "Connection is update!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			mes = "Update error";
		}
		return mes;
	}

	/**
	 * get token return: String token
	 * 
	 * @param connectionId (documentKey)
	 * @throws IOException
	 */
	@RequestMapping(value = "/getAccessToken", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getToken(@RequestParam(required = false, defaultValue = "OHUB::CREDENTIALS::123") String connectionId) throws IOException {
		OhubDoc ohubDoc = ohubRepository.findOhubDocumentInCouchBaseByDocumentKey(bucketService.getBucketName(), connectionId);
		return Util.getToken(new JSONObject(ohubDoc).getJSONObject("credentials"), "");//try to improve this using ohubDoc.getCredentials();
	}

	@RequestMapping(value = "/getOhubDataInCouchBase", method = RequestMethod.GET, produces = "json/application")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id using POST
	public String getOhubDataInCouchBase(@RequestParam("type") String type, @RequestParam("connectionId") String connectionId, @RequestParam("countryId") String countryId) throws Exception {
		return customRepository.getConnectionDocument("result", type, connectionId, "").toString();
	}

	@RequestMapping(value = "/runScheduledTask", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public String RunScheduledTask(@RequestParam("operation") String operation, @RequestParam("status") String status, @RequestParam("deleteExisting") boolean deleteExisting) throws Exception {
		return scheduledTaskRepository.saveOhubDataToCouchBase(operation, status, deleteExisting);
	}

/*	@RequestMapping(value = "/saveMappingToFileStorage", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void saveMappingToFileStorage() throws Exception {
		scheduledTaskRepository.saveMappingData();
	}*/

	@CrossOrigin(origins = Util.CORS)
	@RequestMapping(value = "/getOhubData", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getDataByconnectionidNew(@RequestBody String requestBodyStr) throws JSONException, Exception {
		try {
			String bucketName = bucketService.getBucketName();
			JSONArray input = new JSONArray(requestBodyStr);
			JSONArray ohubResult = ohubService.getOhubDataOfConnection(requestBodyStr, bucketName);
			JSONArray result = new JSONArray();

			if (input.getJSONObject(0).has("mapToArmstrong")) {
				String mapToArmstrong = input.getJSONObject(0).getString("mapToArmstrong");
				String armstrongField = input.getJSONObject(0).getString("armstrongField");
				String targetApiField = input.getJSONObject(0).getString("targetApiField");
				MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, mapToArmstrong);
				JSONArray barcodes = new JSONArray();

				for (int i = 0; i < ohubResult.length(); i++) {
					barcodes.put("'" + ohubResult.getJSONObject(i).getString(targetApiField) + "'");
				}

				JSONArray mapToResult = customRepository.getAllDataFromTableMySQLByCountryId(mySqlDoc, "mapBySource", armstrongField, "0", Util.convertJsonArrayIdToStringId(barcodes), "");
				result = commonService.combine2ApiForGetOHUB(mapToResult, armstrongField, ohubResult, targetApiField);
			} else {
				result = ohubResult;
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error : " + e);
			return null;
		}
	}
}