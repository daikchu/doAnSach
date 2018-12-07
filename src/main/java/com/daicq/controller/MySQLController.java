package com.daicq.controller;

import com.daicq.dao.CustomRepository;
import com.daicq.dao.MySqlRepository;
import com.daicq.dao.doc.MySqlDoc;
import com.daicq.service.BucketService;
import com.daicq.service.CommonService;
import com.daicq.service.MySqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/mysql")
@Component
public class MySQLController {
	@Autowired
	MySqlService mySqlService;
	@Autowired
	BucketService bucketService;
	@Autowired
	CommonService commonService;
	@Autowired
	CustomRepository customRepository;
	@Autowired
	MySqlRepository mySqlRepository;

	/**
	 * create new connection mysql
	 * 
	 * @param: MySqlDoc return: String
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public String create(@Valid @RequestBody MySqlDoc mySqlDoc) {
		String couchbaseBooketName = bucketService.getBucketName();
		// return mySqlService.create(mySqlDoc);
		try {
			if (mySqlService.create(mySqlDoc, couchbaseBooketName) != null) {
				return "New connection is created!";
			} else {
				return "Connection is existed!";
			}
		} catch (Exception e) {
			return "Error!";
		}
	}

	/**
	 * find all connection mysql return: List<MySqlDoc>
	 */
	@RequestMapping(value = "/find/allmysqlconnection", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<MySqlDoc> findAll() {
		String couchbaseBooketName = bucketService.getBucketName();
		return mySqlService.findAllMySqlConnection(couchbaseBooketName);
	}

	/**
	 * get field connection mysql
	 * 
	 * @param: databaseName, databaseTable, connectionType, port return: String
	 *         result
	 */
	@RequestMapping(value = "/getField", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<String> getField(@RequestParam("connectionId") String connectionId) {
		String bucketName = bucketService.getBucketName();
		// public void getField() {
		List<String> result = new ArrayList<>();
		try {
			MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionId);

			// store field of table in a list
			return customRepository.getField(bucketName, mySqlDoc);
		} catch (Exception e) {
			result.add("get field error");
			return result;
		}
	}

	/**
	 * get table data in mysql datasource by 'connectionId' parameter
	 * 
	 * @param: connectionId return: String result
	 */
	@RequestMapping(value = "/getTableDataByConnectionId", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getTableDataFromMySqlDBByConnectionId(@RequestParam("connectionId") String connectionId) {
		String bucketName = bucketService.getBucketName();
		try {
			MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionId);
			return mySqlService.getMySqlDataBySqlConnection(mySqlDoc).toString();
		} catch (Exception e) {
			return "Error!";
		}

	}

	/**
	 * update connection MySQL
	 * 
	 * @param key
	 * @param mySql
	 * @return
	 */
	@RequestMapping(value = "/{key}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String update(@PathVariable String key, @Valid @RequestBody MySqlDoc mySql) {
		String bucketName = bucketService.getBucketName();
		String mes = "";
		try {
			if (mySqlService.updateMySQL(key, mySql, bucketName) != null) {
				mes = "Connection is update!";
			} else {
				mes = "Connection is existed!";
			}
		} catch (Exception e) {
			mes = "error";
		}
		return mes;
	}

	/**
	 * get specific data by countryId
	 * 
	 * @param: connectionId, get (the data to be retrieved), countryId
	 * @return JSON String
	 */
	@RequestMapping(value = "/getDataByCountryId", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String getData(@RequestParam("connectionId") String connectionId, @RequestParam("baseTable") String baseTable, @RequestParam("countryId") String countryId, @RequestParam("active") String active) {
		String bucketName = bucketService.getBucketName();
		try {
			MySqlDoc mySqlDoc = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, connectionId);
			return customRepository.getDataByCountryId("", mySqlDoc, baseTable, countryId, active).toString();
		} catch (Exception e) {
			return "Error!";
		}
	}
}
