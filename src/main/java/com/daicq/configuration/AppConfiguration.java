package com.daicq.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Application configuration class
 *
 * @author chu quang dai
 */

@EnableConfigurationProperties({ CouchbaseSetting.class /* other setting classes */ })
public class AppConfiguration {

}
