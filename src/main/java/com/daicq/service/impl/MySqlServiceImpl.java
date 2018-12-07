package com.daicq.service.impl;

import com.daicq.dao.CustomRepository;
import com.daicq.dao.MySqlCounterRepository;
import com.daicq.dao.MySqlRepository;
import com.daicq.dao.doc.MySqlDoc;
import com.daicq.service.BucketService;
import com.daicq.service.MySqlService;
import com.daicq.util.Util;
import com.daicq.util.UtilBase64;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

@Service
public class MySqlServiceImpl implements MySqlService {
	@Autowired
	MySqlRepository mySqlRepository;
	@Autowired
	MySqlCounterRepository counterRepository;
	@Autowired
	CustomRepository customRepository;
	@Autowired
	BucketService bucketService;
	/*
	 * @Autowired ConnectionUtil c;
	 */
	Connection con;
	Statement st;

	/**
	 * crete new MySQL document
	 * 
	 * @Params: MySQL document
	 */
	@Override
	public MySqlDoc create(MySqlDoc mysql, String coucbaseBooketName) {
		// TODO Auto-generated method stub
		List<MySqlDoc> listMysqlDoc = mySqlRepository.findAllMySqlConnection(coucbaseBooketName);
		if (!Util.checkDuplicateMySql(listMysqlDoc, mysql)) {
			mysql.setId(counterRepository.counter().toString());
			try {
				UtilBase64 td = new UtilBase64();
				String base64Password = mysql.getDatabasePassword();
				mysql.setDatabasePassword(td.encrypt(base64Password));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mySqlRepository.save(mysql);
		}
		return null;
	}

	/**
	 * process to get all MySQL document
	 * 
	 * @return: List MySQL document
	 */
	@Override
	public List<MySqlDoc> findAllMySqlConnection(String couchbaseCooketName) {
		// TODO Auto-generated method stub
		return mySqlRepository.findAllMySqlConnection(couchbaseCooketName);
	}

	@Override
	public JSONArray getMySqlDataBySqlConnection(MySqlDoc mySqlDoc) {
		JSONArray result = new JSONArray();
		if (mySqlDoc != null) {
			result = customRepository.getAllDataFromTableMySQL(mySqlDoc);
		}
		return result;
	}

	@Override
	public MySqlDoc findDocumentInCouchBaseByDocumentId(String booketName, String documentId) {
		// TODO Auto-generated method stub
		return mySqlRepository.findDocumentInCouchBaseByDocumentId(booketName, documentId);
	}

	@Override
	public MySqlDoc updateMySQL(String key, MySqlDoc mySqlDoc, String bucketName) {
		// TODO Auto-generated method stub
		final MySqlDoc mySqlExist = mySqlRepository.findDocumentInCouchBaseByDocumentId(bucketName, key);
		if (mySqlDoc.getName() != null) {
			mySqlExist.setName(mySqlDoc.getName());
		} else {
			mySqlExist.setName("");
		}
		//mySqlExist.setChannels(mySqlDoc.getChannels());
		mySqlExist.setConnectionType(mySqlDoc.getConnectionType());
		mySqlExist.setCreatedBy(mySqlDoc.getCreatedBy());
		mySqlExist.setDatabaseName(mySqlDoc.getDatabaseName());
		mySqlExist.setDatabasePassword(mySqlDoc.getDatabasePassword());
		mySqlExist.setDatabaseTable(mySqlDoc.getDatabaseTable());
		mySqlExist.setDatabaseUser(mySqlDoc.getDatabaseUser());
		//mySqlExist.setDateCreated(mySqlDoc.getDateCreated());
		mySqlExist.setDateUpdated(mySqlDoc.getDateUpdated());
		mySqlExist.setFields(mySqlDoc.getFields());
		mySqlExist.setIdentifierKey(mySqlDoc.getIdentifierKey());
		mySqlExist.setPort(mySqlDoc.getPort());
		mySqlExist.setServerAddress(mySqlDoc.getServerAddress());
		mySqlExist.setType(mySqlDoc.getType());
		mySqlExist.setUpdatedBy(mySqlDoc.getUpdatedBy());
		try {
			UtilBase64 td = new UtilBase64();
			String base64Password = mySqlExist.getDatabasePassword();
			mySqlExist.setDatabasePassword(td.encrypt(base64Password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mySqlRepository.save(mySqlExist);
	}
}
