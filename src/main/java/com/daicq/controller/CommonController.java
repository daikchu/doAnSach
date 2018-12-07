package com.daicq.controller;

import com.daicq.dao.CustomRepository;
import com.daicq.dao.MySqlRepository;
import com.daicq.dao.OhubRepository;
import com.daicq.dao.doc.*;
import com.daicq.service.BucketService;
import com.daicq.service.CommonService;
import com.daicq.service.MySqlService;
import com.daicq.service.OhubService;
import com.daicq.util.Util;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

//import com.daicq.util.FileUtil;

/**
 * process for common requests.
 * 
 * @Author: To Thanh Luong
 */

@RestController
@RequestMapping("/common")
@Component
public class CommonController {
	@Autowired
	MySqlService mySqlService;
	@Autowired
	OhubService ohubService;
	@Autowired
	CommonService commonService;
	@Autowired
	BucketService bucketService;
	@Autowired
	MySqlRepository mySqlRepository;
	@Autowired
	OhubRepository ohubRepository;
	@Autowired
	CustomRepository customRepository;
	@Autowired
	FileController fileController;

	/**
	 * Get documents in Couchbase which contain 'type' and 'docType' params
	 * 
	 * @Params: type, docType
	 * @Return: list of record.
	 */
	@RequestMapping(value = "/find/document", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String findDocument(@RequestParam("type") String type, @RequestParam("docType") String docType, @RequestParam("identifierValue") String identifierValue) {
		String msg = "";
		try {
			if (Util.isNullOrEmpty(type)) {
				msg = "Type is required!";
			} else if (Util.isNullOrEmpty(docType)) {
				msg = "docType is required!";
			} else {
				msg = commonService.findDocument(type, docType, identifierValue).toString();
			}
		} catch (Exception e) {
			return "Error!";
		}
		return msg;
	}

	/**
	 * @author Luong To Thanh function: get list name of Source by 'type' parameter
	 * @param: type
	 * @return: List name
	 */
	@RequestMapping(value = "/getNameOfSource", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getNameOfSource(@RequestParam("type") String type) {
		String message = "";
		try {
			List<String> documentList = commonService.getLstOfSource(type);
			message = documentList.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * function: get list document in Couchbase by 'type' and 'name' parameters
	 * 
	 * @param: type, name
	 * @return: List documents
	 */
	@RequestMapping(value = "/find/documentByType", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String findDocumentByType(@RequestParam("type") String type, @RequestParam("name") String name) {
		String msg = "";
		try {
			if (Util.isNullOrEmpty(type)) {
				msg = "Type is required!";
			} else if (Util.isNullOrEmpty(name)) {
				msg = "Name is required!";
			} else {
				msg = commonService.findDocument(type, name).toString();
			}
		} catch (Exception e) {
			return "Error!";
		}
		return msg;
	}

	/**
	 * function: get list ids in Couchbase by 'name', 'country' and 'type'
	 * parameters
	 * 
	 * @param: type, name, country
	 * @return: List ids
	 */
	@RequestMapping(value = "/getLstId", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getLstIdFromNameTypeCountry(@RequestParam("name") String name, @RequestParam("country") String country, @RequestParam("type") String type) {
		String msg = "";
		try {
			if (Util.isNullOrEmpty(type)) {
				msg = "Type is required!";
			} else if (Util.isNullOrEmpty(name)) {
				msg = "Name is required!";
			} else if (Util.isNullOrEmpty(country)) {
				msg = "Country is required!";
			} else {
				msg = commonService.getLstIdFromNameCountryType(name, country, type).toString();
			}
		} catch (Exception e) {
			return "Error!";
		}
		return msg;
	}

	/**
	 * mapping combineMultiApi
	 * 
	 * @param inputString
	 * @return
	 */
	@RequestMapping(value = "/find/combineMultiApi", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public String combineMultiApi(@RequestBody String inputString) {
		String couchbaseBooketName = bucketService.getBucketName();
		String msg = "";
		try {
			msg = commonService.combineMultiApi(couchbaseBooketName, inputString).toString();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error : " + e;
		}
		return msg;
	}

	/**
	 * create mapping combineMultiAPI
	 * 
	 * @param m
	 * @return
	 */
	@RequestMapping(value = "mappingObj", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String createMap(@Valid @RequestBody MappingDoc m) {
		commonService.create(m);
		return "success";
	}

	/**
	 * check MYSQL connection
	 * 
	 * @param mySqlDoc
	 * @return
	 */
	@RequestMapping(value = "/checkMySQL", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String checkConnectionMySQL(@Valid @RequestBody MySqlDoc mySqlDoc) {
		String bucketName = bucketService.getBucketName();
		String msg = "";
		try {
			msg = commonService.checkConnectionMySQL(bucketName, mySqlDoc);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error!";
		}
		return msg;
	}

	/**
	 * check ohub_api connection
	 * 
	 * @param ohubDoc
	 * @return
	 */
	@RequestMapping(value = "/checkOhubAPI", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String checkConnectionOhub(@Valid @RequestBody OhubDoc ohubDoc) {
		String bucketName = bucketService.getBucketName();
		String msg = "";
		try {
			msg = commonService.checkConnectionOhubApi(bucketName, ohubDoc);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error!";
		}
		return msg;
	}

	/**
	 * group connection
	 * 
	 * @param inputString
	 * @return
	 */
	@RequestMapping(value = "/group/groupConnection", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String groupConnection(@RequestBody String inputString) {
		String bucketName = bucketService.getBucketName();
		String msg = "";
		try {
			msg = commonService.groupConnection(bucketName, inputString).toString();
		} catch (Exception e) {
			msg = "Error!";
		}
		return msg;
	}

	/**
	 * group by source Id
	 * 
	 * @param inputString
	 * @return String messenger
	 */
	@RequestMapping(value = "/group/groupBySourceId", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String groupBySourceId(@RequestBody String inputString) {
		String bucketName = bucketService.getBucketName();
		String msg = "";
		try {
			msg = commonService.groupConnectionbySourceId(bucketName, inputString).toString();
		} catch (Exception e) {
			msg = "Error!";
		}
		return msg;
	}

	/**
	 * create new mapping connection for groupBySourceId
	 * 
	 * @param
	 * @return String mapping success
	 */
	@RequestMapping(value = "mappingConnection", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String createMapConnection(@Valid @RequestBody MappingConnection m) {
		commonService.createMapCon(m);
		return "success";
	}

	/**
	 * @return
	 */
	@RequestMapping(value = "findAllMapping", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String findAllMapping() {
		String mes = "";
		try {
			if (commonService.findAllMapping().size() > 0) {
				mes = commonService.findAllMapping().toString();
			} else {
				mes = "null";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mes = "error";
		}
		return mes;
	}

	/**
	 * get mapping by name
	 * 
	 * @param mappingName
	 * @return String messenger
	 */
	@RequestMapping(value = "/getMappingbyName", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getMappingbyName(@RequestParam("mappingName") String mappingName) {
		String mes = "";
		try {
			mes = commonService.findMappingByName(mappingName).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mes = "error";
		}
		return mes;
	}

	@RequestMapping(value = "mappingSource", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String createMappingSource(@Valid @RequestBody MappingSource m) {
		commonService.createMappingSource(m);
		return "success";
	}

	@RequestMapping(value = "/getConnectionByKey", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getConnectionByKey(@RequestParam("key") String key, @RequestParam("connectionType") String connectionType) {
		String mes = "";
		try {
			mes = commonService.getDataByKey(key, connectionType).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mes = "error";
		}
		return mes;
	}

	@RequestMapping(value = "/getMappingbyId", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getMappingbyId(@RequestParam("mappingId") String mappingId) {
		String mes = "";
		try {
			mes = commonService.findMappingById(mappingId).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mes = "error";
		}
		return mes;
	}

	@RequestMapping(value = "/getAllMapping", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getAllMapping() {
		String mes = "";
		try {
			mes = commonService.getAllDataMapping().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mes = "error";
		}
		return mes;
	}

	/**
	 * mapping combineMultiApi2
	 * 
	 * @param inputString
	 * @return
	 */
	@RequestMapping(value = "/find/combineMultiApi2", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public String combineMultiApi2(@RequestBody String inputString) {
		String bucketName = bucketService.getBucketName();
		String msg = "";
		try {
			msg = commonService.combineMultiApi2(bucketName, inputString).toString();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error: " + e;
		}
		return msg;
	}

	/**
	 * mapping combineMultiApi
	 * 
	 * @param inputString
	 * @return
	 */
	@RequestMapping(value = "/find/combineMultiApiPaginated2", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public String combineMultiApiPaginated2(@RequestParam("lastSourceIndex") String lastSourceIndex, @RequestParam int limit, /*, @RequestParam("realTime") boolean realTime,*/ @RequestBody String inputString) {
		String couchbaseBooketName = bucketService.getBucketName();
		String msg = "";
		int lastIndex = 0;
		if (lastSourceIndex.equals("END_OF_RESULT")) {
			return new JSONArray().toString();
		} else {
			try {
				lastIndex = Integer.parseInt(lastSourceIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			msg = commonService.combineMultiApiPaginated2(couchbaseBooketName, inputString, lastIndex, limit/*, realTime*/).toString();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "Error : " + e;
		}
		return msg;
	}

	/**
	 * @throws IOException
	 */
	/*@RequestMapping(value = "/download/file", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public ResponseEntity<Resource> combineMultiApiSaveToStorage(@RequestParam("fileName") String fileName, @RequestParam("countryId") String[] id, HttpServletRequest request) throws IOException {

		ResponseEntity<Resource> resource = null;
		List<Resource> resourceList = new ArrayList<Resource>();

		for (int i = 0; i < id.length; i++) {
			String country = id[i].toString();
			try {
				String dir = FileUtil.USER_DIR + FileUtil.UPLOAD_DIR + FileUtil.getDirectoryPerCountry(country);
				resource = fileController.downloadFile(dir + fileName + "-" + country + ".xlsx", request);//add unique id to prevent errors when same user is accessing the file or delete file after download
				resourceList.add(resource.getBody());
			} catch (Exception e) {
				System.err.println("File not found in upload directory. File skipped.");
			}
		}
		FileUtil.mergeExcelFiles(resourceList, fileName);
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileController.downloadFile(FileUtil.MERGED_DIR + fileName + ".xlsx", request);
	}*/

	/**
	 * Save JSON to text file
	 * @throws IOException
	 */
/*	@RequestMapping(value = "/find/combineMultiApiJsonFromTxt", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	// get fields by condition: connection id
	public String combineMultiApiJsonFromTxt(@RequestParam("fileName") String fileName, @RequestParam("countryId") String[] id) throws IOException {
		String jsonString = "";
		try {
			jsonString = FileUtil.mergeJSONTextFiles(fileName, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonString;
	}*/

	/**
	 * generate data for supermapperId
	 * 
	 * @return JSON String
	 * @throws Exception
	 * @throws JSONException
	 */
	@RequestMapping(value = "display/supermapperIdRecords", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String supermapperId(@RequestBody String requestBody) throws JSONException, Exception {
		String bucketName = bucketService.getBucketName();
		try {
			JSONArray body = new JSONArray(requestBody);
			return commonService.compareArmstrongToOhub(bucketName, body).toString().replaceAll("\"", "").replaceAll("\\\\", "\"");
		} catch (Exception e) {
			return "An error has occured while fetching supermapperId records: " + e;
		}
	}

	/**
	 * merge data from armstrong to ohub etc.
	 * 
	 * @return JSON String
	 * @throws Exception
	 * @throws JSONException
	 */
	@RequestMapping(value = "/supermapperMerge", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String supermapperMerge(@RequestBody String requestBody) throws JSONException, Exception {
		String bucketName = bucketService.getBucketName();
		try {
			return commonService.supermapperMerge(bucketName, requestBody).toString().replaceAll("\"", "").replaceAll("\\\\", "\"");
		} catch (Exception e) {
			return "An error has occured while merging : " + e;
		}
	}

	@RequestMapping(value = "/googlePlaceSearch", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getGoogleMapData(@RequestBody String requestBody) throws JSONException, Exception {
		String bucketName = bucketService.getBucketName();
		try {
			return commonService.getGooglePlacesData(bucketName, requestBody).toString().replaceAll("\"", "").replaceAll("\\\\", "\"");
		} catch (Exception e) {
			e.printStackTrace();
			return Util.createErrorMessage(e.getMessage()).toString();
		}
	}
}
