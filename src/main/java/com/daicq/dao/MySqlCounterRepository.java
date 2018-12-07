package com.daicq.dao;

import com.couchbase.client.java.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MySqlCounterRepository {
	private static final long INITIAL_COUNTER_VALUE = 1;
	private static final String MYSQL_COUNTER_KEY = "MYSQL_counter";

	@Autowired
	private Bucket bucket;

	public Long counter() {
		return bucket.counter(MYSQL_COUNTER_KEY, 1, INITIAL_COUNTER_VALUE).content();
	}

	public void inc() {
		bucket.counter(MYSQL_COUNTER_KEY, 1, INITIAL_COUNTER_VALUE);
	}

	public void dec() {
		bucket.counter(MYSQL_COUNTER_KEY, -1, INITIAL_COUNTER_VALUE);
	}

	public Long getValue() {
		return bucket.counter(MYSQL_COUNTER_KEY, 0).content();
	}
}
