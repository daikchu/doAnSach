package com.daicq.service.impl;

import com.couchbase.client.java.Bucket;
import com.daicq.configuration.CouchbaseConfig;
import com.daicq.configuration.CouchbaseSetting;
import com.daicq.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * using get bucket detail connected
 */
@Service
public class BucketServiceImpl implements BucketService {
	@Autowired
	CouchbaseSetting couchbaseSetting;
	@Autowired
	CouchbaseConfig couchbaseConfig;

	@Override
	public String getBucketName() {
		return couchbaseSetting.getBucketName();
	}

	@Override
	public String getPassword() {
		return couchbaseSetting.getPassword();
	}

	@Override
	public Bucket getBucket() throws Exception {
		return couchbaseConfig.couchbaseClient();
	}
}
