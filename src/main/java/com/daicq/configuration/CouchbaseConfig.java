package com.daicq.configuration;

import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.query.Consistency;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Class that connects to Couchbase
 *
 * @author chu quang dai
 */
@Component
@Configuration
@EnableCouchbaseRepositories(basePackages = { "com.daicq.dao" })
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

	protected static final Logger logger = LoggerFactory.getLogger(CouchbaseConfig.class);

	@Autowired
	private CouchbaseSetting couchbaseSetting;

	@Override
	protected List<String> getBootstrapHosts() {
		logger.info("Registering host '{}' for couchbase cluster", couchbaseSetting.getHostName());
		return Arrays.asList(couchbaseSetting.getHostName());
	}

	@Override
	public String getBucketName() {
		logger.info("Opening bucket '{}'", couchbaseSetting.getBucketName());
		return couchbaseSetting.getBucketName();
	}

	@Override
	public String getBucketPassword() {
		return couchbaseSetting.getPassword();
	}

	@Override
	protected CouchbaseEnvironment getEnvironment() {
		DefaultCouchbaseEnvironment.builder().connectTimeout(600000) // by
																		// default
																		// 5 sec
																		// (5000
																		// ms)
				.queryTimeout(400000) // by default 75 sec (75000 ms)
				.maxRequestLifetime(1000000)
				.autoreleaseAfter(120000)
			//	.keepAliveTimeout(1000000)
				.socketConnectTimeout(450000)
				.build(); // by default 1 sec (1000 ms)
		return super.getEnvironment();
	}

	@Override
	public Consistency getDefaultConsistency() {
		// By default, READ_YOUR_OWN_WRITES
		// Values: READ_YOUR_OWN_WRITES, STRONGLY_CONSISTENT, UPDATE_AFTER,
		// EVENTUALLY_CONSISTENT
		return Consistency.READ_YOUR_OWN_WRITES;
	}

	@Override
	public String typeKey() {
		// By default, this attribute is named "_class".
		// Spring Data automatically adds to each document an attribute
		// containing the full class name of the entity.
		// This field is the one used by N1QL queries to filter only documents
		// corresponding to the repository’s entity.
		return "type";
	}
}
