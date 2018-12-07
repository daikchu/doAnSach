package com.daicq.service;

import com.couchbase.client.java.Bucket;

public interface BucketService {
	String getBucketName();

	String getPassword();

	Bucket getBucket() throws Exception;
}
