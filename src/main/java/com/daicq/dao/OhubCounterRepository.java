package com.daicq.dao;

import com.couchbase.client.java.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OhubCounterRepository {
	private static final long INITIAL_COUNTER_VALUE = 1;
	private static final String OHUB_COUNTER_KEY = "OHUB_API_counter";

	@Autowired
	private Bucket bucket;

	public Long counter() {
		return bucket.counter(OHUB_COUNTER_KEY, 1, INITIAL_COUNTER_VALUE).content();
	}

	public void inc() {
		bucket.counter(OHUB_COUNTER_KEY, 1, INITIAL_COUNTER_VALUE);
	}

	public void dec() {
		bucket.counter(OHUB_COUNTER_KEY, -1, INITIAL_COUNTER_VALUE);
	}

	public Long getValue() {
		return bucket.counter(OHUB_COUNTER_KEY, 0).content();
	}
}
