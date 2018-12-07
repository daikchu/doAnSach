package com.daicq.controller;

import com.daicq.dao.ScheduledTaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

/**
 * Scheduled task controller
 * 
 * @author Mikkel Pichay
 */
@Component
public class ScheduledTaskController {
//	@Autowired
	//private ScheduledTaskRepository scheduledTaskRepository;
	private final Logger log = LoggerFactory.getLogger(ScheduledTaskController.class);
/*	private final static String TIME_ZONE = "Asia/Singapore";

	*//*
	 * Cron job for saving OHUB data to couchbase "0 0 0 * * ?" set to 9pm (21:00)
	 *//*
	@Scheduled(cron = "0 0 21 * * *", zone = TIME_ZONE)
	public void getUpdateOhubConnectionInCouchBase() throws Exception {
		getTimezoneInfo();
		String status = "active";
		String operation = "non-campaignAPI";
		boolean deleteExisting = false;
		scheduledTaskRepository.saveOhubDataToCouchBase(operation, status, deleteExisting);
	}

	*//*
	 * Cron job for saving campaignOpens set to 12am (0:00)
	 *//*
	@Scheduled(cron = "0 1 0 * * *", zone = TIME_ZONE)
	public void getUpdateOhubConnectionCampaignOPENS() throws Exception {
		getTimezoneInfo();
		String status = "active";
		String operation = "campaignOpens";
		boolean deleteExisting = false;
		scheduledTaskRepository.saveOhubDataToCouchBase(operation, status, deleteExisting);
	}

	*//*
	 * Cron job for saving campaignLinks set to 12am (0:00)
	 *//*
	@Scheduled(cron = "0 2 0 * * *", zone = TIME_ZONE)
	public void getUpdateOhubConnectionCampaignLINKS() throws Exception {
		getTimezoneInfo();
		String status = "active";
		String operation = "campaignLinks";
		boolean deleteExisting = false;
		scheduledTaskRepository.saveOhubDataToCouchBase(operation, status, deleteExisting);
	}

	*//*
	 * Cron job for saving campaignWaveResponse
	 * "0 1 1 * * *" set to 12am (0:00)
	 *//*
	@Scheduled(cron = "0 3 0 * * *", zone = TIME_ZONE)
	public void getUpdateOhubConnectionCampaignWAVERESPONSE() throws Exception {
		getTimezoneInfo();
		String status = "active";
		String operation = "campaignWaveResponse";
		boolean deleteExisting = false;
		scheduledTaskRepository.saveOhubDataToCouchBase(operation, status, deleteExisting);
	}

	*//*
	 * Cron job for saving OHUB (salesData) set to 12am (0:00)
	 *//*
	@Scheduled(cron = "0 4 0 * * *", zone = TIME_ZONE)
	public void getUpdateOhubConnectionSalesData() throws Exception {
		getTimezoneInfo();
		//run SalesData after saving documents with PrmimaryOrderData
		String status = "active";
		String operation = "salesData";
		boolean deleteExisting = false;
		scheduledTaskRepository.saveOhubDataToCouchBase(operation, status, deleteExisting);
	}

	*//*
	 * Cron job for saving mapping set to 4am (4:00)
	 *//*
	@Scheduled(cron = "0 0 4 * * *", zone = TIME_ZONE)
	public void saveMappingToFileStorage() throws Exception {
		getTimezoneInfo();
		scheduledTaskRepository.saveMappingData();
	}

	private void getTimezoneInfo() {
		log.info("Scheduled task executed with current timezone : {} with default timezone : {}.", TIME_ZONE, TimeZone.getDefault().toZoneId());
	}*/
}