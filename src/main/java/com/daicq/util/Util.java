package com.daicq.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.daicq.dao.doc.MappingDoc;
import com.daicq.dao.doc.MySqlDoc;
import com.daicq.dao.doc.OhubDoc;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Util class for constants and generic methods
 * 
 * @author chu quang dai
 */

public class Util {
	public static final String CONNECTION_ID = "connectionId";
	public static final String TIMESTAMP = "TIMESTAMP";
	private static String regex = "^[1-9]\\d*$";
	public static final String TYPE = "type";
	public static final String NAME = "name";
	public static final String COUNTRY = "country";
	public static final String POST = "POST";
	public static final String GET = "GET";
	public static final String OHUB_API_TYPE = "OHUB-API";
	public static final String MYSQL_TYPE = "MYSQL";
	public static final String CORS = "http://supermapper.api.ufs-armstrong.com:8080";
	public static final String REGEX_INVALID_CHARS = "[^\\p{L} 0-9.,_=+@()<>':;{}#$%&*/?|]";

	public static boolean isNullOrEmpty(String param) {
		return param == null || param.trim().length() == 0;
	}

	public static boolean isNullOrEmpty(List<?> element) {
		return element == null || element.isEmpty();
	}

	public static boolean checkDuplicateOhub(List<OhubDoc> lstOhubDocs, OhubDoc ohubDoc) {
		for (OhubDoc ohubDocElement : lstOhubDocs) {
			if (ohubDocElement.equals(ohubDoc)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkDuplicateMySql(List<MySqlDoc> lstMysql, MySqlDoc mySqlDoc) {
		for (MySqlDoc mysqlDocElement : lstMysql) {
			if (mysqlDocElement.equals(mySqlDoc)) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkDuplicateMapping(List<MappingDoc> lstMappingDoc, MappingDoc mappingDoc) {
		for (MappingDoc mappingDocElement : lstMappingDoc) {
			if (mappingDocElement.equals(mappingDoc)) {
				return true;
			}
		}
		return false;
	}

	public static boolean validateInteger(String s) {
		return Pattern.matches(regex, s);
	}

	public static String getToken() {
		String token = "";
		try {
			URL url = new URL("http://sandbox.ufs.com/OviewWeb/ufs/access/token");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				return null;
				// throw new RuntimeException("Failed : HTTP error code : " +
				// conn.getResponseCode());

			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br.readLine()) != null) {
				token = output;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return token;
	}

	// For method GET
	public static JSONArray readRestfulAPIDemoGETmethod(OhubDoc ohubDoc) {
		String apiLink = ohubDoc.getLink();
		String paramValues = "";
		//put parameters
		for (String key : ohubDoc.getParameters().keySet()) {
			paramValues += "&" + key + "=" + ohubDoc.getParameters().get(key);
		}
		if (ohubDoc.getOtherParameters() != null) {
			for (String key : ohubDoc.getOtherParameters().keySet()) {
				paramValues += "&" + key + "=" + ohubDoc.getOtherParameters().get(key);
			}
		}
		String output = "";
		String result = "";

		try {
			String urlString = apiLink;
			if (!apiLink.contains("maps.googleapis.com")) {
				String token = getToken(null, "static");
				if (token.contains("token_error")) {
					return new JSONArray();
				}
				urlString += "?access_token=" + token;
			}

			URL url = new URL(urlString + paramValues);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				return null;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			while ((output = br.readLine()) != null) {
				result += output;
			}
			conn.disconnect();
			JSONObject jsonObject = new JSONObject(result);
			String keyContainData = "";

			if (!apiLink.contains("maps.googleapis.com")) {
				if (jsonObject.has("data")) {
					keyContainData = "data";
				} else if (jsonObject.has("cwBounceResponse")) {
					// process for campain
					keyContainData = "cwBounceResponse";
				}
				return jsonObject.getJSONArray(keyContainData);
			} else
				return new JSONArray().put(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSONArray readRestfulAPIDemoPOSTmethod(OhubDoc ohubDoc) {
		String apiLink = ohubDoc.getLink();
		String urlString = "";

		try {
			String token = getToken(null, "static");
			if (token.contains("token_error")) {
				return new JSONArray();
			}
			urlString = apiLink + "?access_token=" + token;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (ohubDoc.getOtherParameters() != null) {
			for (String key : ohubDoc.getOtherParameters().keySet()) {
				urlString += "&" + key + "=" + ohubDoc.getOtherParameters().get(key);
			}
		}
		// for map to get key and value
		JSONObject requestData = new JSONObject(ohubDoc.getParameters());
		byte[] postData = requestData.toString().getBytes(StandardCharsets.UTF_8);

		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setDoOutput(true);

			OutputStream wr = conn.getOutputStream();
			wr.write(postData);
			wr.flush();

			System.err.println(url);
			System.err.println("POST Response code : " + conn.getResponseCode());

			if (conn.getResponseCode() != 200) {
				/*
				 * throw new RuntimeException("Failed : HTTP error code : " +
				 * conn.getResponseCode());
				 */
				return null;
			}
			StringBuilder content;
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line;
			content = new StringBuilder();
			while ((line = in.readLine()) != null) {
				content.append(line);
				content.append(System.lineSeparator());
			}

			conn.disconnect();
			JSONObject jsonObject = new JSONObject(content.toString());
			String keyContainData = "";

			if (jsonObject.has("data")) {
				keyContainData = "data";
			} else if (jsonObject.has("cwBounceResponse")) {
				// process for campain
				keyContainData = "cwBounceResponse";
			}

			if (jsonObject.isNull(keyContainData)) {
				return new JSONArray();
			}
			return jsonObject.getJSONArray(keyContainData);

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}
	}

	public static String getTimestamp(String timestampValue) {
		if (timestampValue == null) {
			return "0000-00-00 00:00:00";
		} else {
			return timestampValue;
		}
	}

	public static JSONObject putJsonObjectToJsonObject(JSONObject source, JSONObject target) {
		Iterator<?> keys = source.keys();
		try {
			while (keys.hasNext()) {
				String key = (String) keys.next();
				target.put(key, source.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return target;
	}

	public static boolean isInteger(Object x) {
		if (x instanceof Integer) {
			return true;
		}
		return false;
	}

	public static String getToken(JSONObject jObjCredential, String operation) throws IOException {
		URL url = new URL("https://oviewapi.ufs.com/OviewWeb/ufs/oauth/token?");
		JSONObject jObjClient = new JSONObject();
		if (operation.equals("static")) {
			url = new URL("https://oviewapi.ufs.com/OviewWeb/ufs/oauth/token?" + "grant_type=password" + "&username=ARMSTRONG" + "&password=UFSSecretPassword?");
			jObjClient.put("clientId", "ARMSTRONG");
			jObjClient.put("clientSecret", "4b!wNr6m!Wf8");
		} else {
			url = new URL("https://oviewapi.ufs.com/OviewWeb/ufs/oauth/token?grant_type=" + jObjCredential.getString("grant_type") + "&username=" + jObjCredential.getString("username") + "&password=" + jObjCredential.getString("password"));
			jObjClient.put("clientId", jObjCredential.getString("clientId"));
			jObjClient.put("clientSecret", jObjCredential.getString("clientSecret"));
		}
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-type", "application/json");
		conn.setDoOutput(true);
		sendData(conn, jObjClient.toString());
		String tokenString = read(conn.getInputStream());

		JSONObject tokenObj = new JSONObject();
		if (tokenString.contains("access_token")) {
			tokenObj = new JSONObject(tokenString);
			if (tokenObj.has("access_token")) {
				return tokenObj.getString("access_token"); // validation for getting token
			} else {
				String errorMsg = "token_error : " + tokenString;
				System.err.println(errorMsg);
				return errorMsg;// error message
			}
		}
		return tokenString;
	}

	// Send POST request
	public static void sendData(HttpURLConnection con, String data) throws IOException {
		DataOutputStream wr = null;
		try {
			byte[] postData = data.toString().getBytes(StandardCharsets.UTF_8);
			wr = new DataOutputStream(con.getOutputStream());
			wr.write(postData);
			wr.flush();
			wr.close();
		} catch (IOException exception) {
			throw exception;
		} finally {
			closeQuietly(wr);
		}
	}

	// send GET request
	public static String read(InputStream is) throws IOException {
		BufferedReader in = null;
		String inputLine;
		StringBuilder body;
		try {
			in = new BufferedReader(new InputStreamReader(is));
			body = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				body.append(inputLine);
			}
			in.close();
			return body.toString();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			closeQuietly(in);
		}
	}

	// close connection
	protected static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		} catch (IOException e) {
			System.err.println("An error occured : " + e);
		}
	}

	// For method GET
	public static JSONArray readRestfulAPIDemoGETmethod(OhubDoc ohubDoc, String sourceId, String data) {
		String apiLink = ohubDoc.getLink();
		String paramValues = "";
		String idToCheck = "";
		JSONArray array_result = new JSONArray();

		for (String key : ohubDoc.getParameters().keySet()) {
			idToCheck = key + "=";
			if (!sourceId.contains(idToCheck)) {
				String value = ohubDoc.getParameters().get(key);
				// if parameter value is empty, dont include it in the URL
				if (!value.equals("") && value != null) {
					paramValues += "&" + key + "=" + value;
				}
			}
		}
		if (ohubDoc.getOtherParameters() != null) {
			for (String key : ohubDoc.getOtherParameters().keySet()) {
				String value = ohubDoc.getOtherParameters().get(key);
				// if parameter value is empty, dont include it in the URL
				if (!value.equals("") && value != null) {
					paramValues += "&" + key + "=" + value;
				}
			}
		}
		String output = "";
		String result = "";
		try {
			String token = getToken(null, "static");
			if (token.contains("token_error")) {
				return array_result;
			}
			URL url;

			// if data is empty, use the provided sourceId for the parameter.
			if (data.equals("") || data == null) {
				url = new URL(apiLink + "?access_token=" + token + "&" + sourceId + paramValues);
			} else {
				url = new URL(apiLink + "?access_token=" + token + "&" + data + paramValues);
			}

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			System.err.println("URL:" + url);
			System.err.println("GET Response code : " + conn.getResponseCode());

			if (conn.getResponseCode() != 200) {
				return array_result;
			}
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			while ((output = br.readLine()) != null) {
				result = output;
			}
			conn.disconnect();

			JSONObject jsonObject = new JSONObject(result);
			String keyContainData = "";

			if (jsonObject.has("data")) {
				keyContainData = "data";
			} else if (jsonObject.has("cwBounceResponse")) {
				keyContainData = "cwBounceResponse";
			}
			if (jsonObject.has("cwOpens")) {
				keyContainData = "cwOpens";
			} else if (jsonObject.has("cwClicks")) {
				keyContainData = "cwClicks";
			}

			if (jsonObject.isNull(keyContainData)) {
				return array_result;
			}

			if (apiLink.contains("/campaignWaveSendings") && !apiLink.contains("/campaignWaveSendingsFull")) {
				return getCWSDataFromResult(jsonObject.getJSONArray(keyContainData));
			} else if (apiLink.contains("/primaryOrderData")) {
				return filterPrimaryOrderDataFromResult(jsonObject.getJSONArray(keyContainData));
			} else if(apiLink.contains("/salesData")) {
				return salesDataFilter(jsonObject.getJSONArray(keyContainData));
			}

			return jsonObject.getJSONArray(keyContainData);
		} catch (Exception e) {
			e.printStackTrace();
			return array_result;
		}
	}

	// convert String to List
	public static List<String> convertStringtoList(String a) {
		List<String> lstConvert = new ArrayList<String>(Arrays.asList(a.split(",")));
		return lstConvert;
	}

	// convert String to Map
	public static Map<String, String> convertStringToMap(String a) {
		Map<String, String> mapConvert = new HashMap<String, String>();
		String[] pairs = a.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyValue = pair.split(":");
			mapConvert.put(keyValue[0], keyValue[1]);
		}
		return mapConvert;
	}

	public static JSONArray readRestfulAPIDemoPOSTmethod(OhubDoc ohubDoc, JSONObject requestDataObj, String parameter) {
		String apiLink = ohubDoc.getLink();
		String urlString = "";
		String paramValues = "";
		JSONArray array_result = new JSONArray();

		try {
			String token = getToken(null, "static");
			if (token.contains("token_error")) {
				return array_result;
			}
			urlString = apiLink + "?access_token=" + token;
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String key : ohubDoc.getParameters().keySet()) {
			String value = ohubDoc.getParameters().get(key);
			// if parameter value is empty, dont include it in the URL
			if (!value.equals("") && value != null) {
				paramValues += "&" + key + "=" + ohubDoc.getParameters().get(key);
			}
		}

		if (ohubDoc.getOtherParameters() != null) {
			for (String key : ohubDoc.getOtherParameters().keySet()) {
				String value = ohubDoc.getOtherParameters().get(key);
				// if parameter value is empty, dont include it in the URL
				if (!value.equals("") && value != null) {
					urlString += "&" + key + "=" + ohubDoc.getOtherParameters().get(key);
				}
			}
		}

		try {
			// for map to get key and value
			JSONObject requestData = new JSONObject(ohubDoc.getParameters());
			// if requestDataObj contains data, use it instead of the data from the connection parameter.
			if (requestDataObj != null) {
				// if requestDataObj has multiple ids, use it as a parameter.
				if (requestDataObj.has("ids")) {
					urlString += "&" + parameter + requestDataObj.getString("ids");
				} else {
					urlString += paramValues;
					requestData = requestDataObj; // if requestDataObj contains a requestBody, use it for request.
				}
			}

			byte[] postData = requestData.toString().getBytes(StandardCharsets.UTF_8);
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			conn.setDoOutput(true);

			OutputStream wr = conn.getOutputStream();
			wr.write(postData);
			wr.flush();

			System.err.println("URL:" + urlString);
			System.err.println("POST Response code : " + conn.getResponseCode());

			if (conn.getResponseCode() != 200) {
				return array_result;
			}

			StringBuilder content;
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String line;
			content = new StringBuilder();
			while ((line = in.readLine()) != null) {
				content.append(line);
				content.append(System.lineSeparator());
			}

			conn.disconnect();
			JSONObject jsonObject = new JSONObject(content.toString());
			String keyContainData = "";

			if (jsonObject.has("data")) {
				keyContainData = "data";
			} else if (jsonObject.has("cwBounceResponse")) {
				keyContainData = "cwBounceResponse";
			}
			if (jsonObject.has("cwOpens")) {
				keyContainData = "cwOpens";
			} else if (jsonObject.has("cwClicks")) {
				keyContainData = "cwClicks";
			}

			if (jsonObject.isNull(keyContainData)) {
				return array_result;
			}
			if (apiLink.contains("/campaignWaveSendings") && !apiLink.contains("/campaignWaveSendingsFull") && parameter.contains("campaign")) {
				return getCWSIdFromCWS(jsonObject.getJSONArray(keyContainData));
			} else if (apiLink.contains("/campaignWaveSendings") && !apiLink.contains("/campaignWaveSendingsFull")) {
				return getCWSDataFromResult(jsonObject.getJSONArray(keyContainData));
			} else if (apiLink.contains("/primaryOrderData")) {
				return filterPrimaryOrderDataFromResult(jsonObject.getJSONArray(keyContainData));
			} else if(apiLink.contains("/salesData")) {
				return salesDataFilter(jsonObject.getJSONArray(keyContainData));
			}

			return jsonObject.getJSONArray(keyContainData);
		} catch (Exception e) {
			e.printStackTrace();
			return array_result;
		}
	}

	public static JSONObject generateMultiplePOSTRequestData(JSONArray idList, String paramKey, String paramVal) {
		JSONObject requestData = new JSONObject();
		String idConcat = "";
		for (int i = 0; i < idList.length(); i++) {
			String id = idList.getJSONObject(i).getString(paramVal);
			if (!id.equals("") && id != null) {
				idConcat += id;
				if (i != idList.length() - 1) {
					idConcat += ",";
				}
			}
		}
		requestData.put("source", "ARMSTRONG");
		requestData.put(paramKey.replace("=", ""), idConcat);
		return requestData;
	}

	public static JSONObject generateMultipleGETRequestId(JSONArray idList, String paramVal) {
		JSONObject requestParameter = new JSONObject();
		String idConcat = "";
		for (int i = 0; i < idList.length(); i++) {
			String id = idList.getJSONObject(i).getString(paramVal);
			if (!id.equals("") && id != null) {
				idConcat += id;
				if (i != idList.length() - 1) {
					idConcat += ",";
				}
			}
		}
		requestParameter.put("ids", idConcat);
		return requestParameter;
	}

	public static JSONArray getAllCWResponseIdFromCWSendings(JSONArray cwResponse) {
		JSONArray cwResponseId = new JSONArray();
		for (int i = 0; i < cwResponse.length(); i++) {
			String id = cwResponse.getJSONObject(i).getString("campaignWaveResponseId");
			cwResponseId.put(id);
		}
		return cwResponseId;
	}

	public static JSONObject generateCWResponseIdFromCWSendings(JSONArray cwResponseIds) {
		JSONObject cwResponseId = new JSONObject();
		String idConcat = "";
		System.err.println("cwResponseids:" + cwResponseId);
		for (int i = 0; i < cwResponseIds.length(); i++) {
			String id = cwResponseIds.getString(i);
			if (!id.equals("") && id != null) {
				idConcat += id;
				if (i != cwResponseIds.length() - 1) {
					idConcat += ",";
				}
			}
		}
		cwResponseId.put("ids", idConcat);
		return cwResponseId;
	}

	public static String convertJsonArrayIdToStringId(JSONArray arrayString) {
		String idConcat = "";
		for (int i = 0; i < arrayString.length(); i++) {
			String id = arrayString.get(i).toString();
			if (!id.equals("") && id != null) {
				idConcat += id;
				if (i != arrayString.length() - 1) {
					idConcat += ",";
				}
			}
		}
		return idConcat;
	}

	/*
	 * This function will return the parameter to be used in fetching ohub data from
	 * ohub api
	 * 
	 * @return String parameter
	 */
	public static String getParameterByAPILink(String link) {
		String parameter = "sourceId=";
		if (link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/campaign/opens") || link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/campaign/links") || link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/campaignWaveBounceResponse"))
			parameter = "campaignWaveResponseId=";
		else if (link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/salesData"))
			parameter = "orderId=";
		else
			parameter = "sourceId=";

		return parameter;
	}

	public static String formatDataToSend(String connectionName, String type, String resultKey, JSONArray result, String dateTime, String id) {
		JSONObject data = new JSONObject();
		data.put("connectionId", connectionName);
		data.put("type", type);
		data.put(resultKey, result);
		data.put("dateCreated", dateTime);
		data.put("sourceId", id);
		return data.toString();
	}

	public static JSONArray getDataFromMultiplePOSTResult(JSONArray temp_array) {
		// loop for getting data from temp_array to jsonArray_result
		JSONArray jsonArray_result = new JSONArray();
		// loop for getting data from temp_array to jsonArray_result
		for (int i = 0; i < temp_array.length(); i++) {
			if (temp_array.getJSONArray(i) != null && temp_array.getJSONArray(i).length() > 0) {
				int size = temp_array.getJSONArray(i).length();
				for (int j = 0; j < size; j++) {
					if (temp_array.getJSONArray(i) != null || !temp_array.getJSONArray(i).toString().equals("[]")) {
						jsonArray_result.put(temp_array.getJSONArray(i).get(j));
					}
				}
			}
		}
		return jsonArray_result;
	}

	/*
	 * This function will get the required fields of campaignWaveSendings this
	 * fields will be used in mapping
	 * 
	 * @return array result
	 */
	public static JSONArray getCWSDataFromResult(JSONArray result) {
		JSONArray array_result = new JSONArray();
		for (int i = 0; i < result.length(); i++) {
			JSONObject resultObj = result.getJSONObject(i);
			JSONObject tempObj = new JSONObject();
			tempObj.put("campaignWaveResponseId", resultObj.getString("campaignWaveResponseId"));
			tempObj.put("campaignName", resultObj.get("campaignName").toString());
			tempObj.put("waveName", resultObj.get("waveName").toString());
			tempObj.put("channel", resultObj.get("channel").toString());
			tempObj.put("cwsDate", resultObj.get("cwsDate").toString());
			tempObj.put("cpSourceId", resultObj.get("cpSourceId").toString());
			tempObj.put("cpFirstName", resultObj.get("cpFirstName").toString());
			tempObj.put("cpLastName", resultObj.get("cpLastName").toString());
			array_result.put(tempObj);
		}
		return array_result;
	}

	/*
	 * check if the API links contains the link of one of the campaign APIs
	 * 
	 * @return boolean
	 */
	public static boolean checkCampaignAPIByLink(String link) {
		if (link.contains("/opens") || link.contains("/links") || link.contains("/campaignWaveBounceResponse")) {
			return true;
		}
		return false;
	}

	/*
	 * This function will get the required fields of primary order data these fields  will be used in mapping
	 * 
	 * @return array result
	 */
	public static JSONArray filterPrimaryOrderDataFromResult(JSONArray result) {
		JSONArray array_result = new JSONArray();
		for (int i = 0; i < result.length(); i++) {
			JSONObject resultObj = result.getJSONObject(i);
			JSONObject tempObj = new JSONObject();
			tempObj.put("sourceId", resultObj.get("sourceId").toString());
			tempObj.put("source", resultObj.get("source").toString());
			tempObj.put("cpSourceId", resultObj.get("cpSourceId").toString());
			tempObj.put("updatedAt", resultObj.get("updatedAt").toString());
			tempObj.put("createdAt", resultObj.get("createdAt").toString());
			tempObj.put("orderType", resultObj.get("orderType").toString());
			tempObj.put("wholesaler", resultObj.get("wholesaler").toString());
			tempObj.put("wholesalerId", resultObj.get("wholesalerId").toString());
			tempObj.put("orderEmailAddress", resultObj.get("orderEmailAddress").toString());
			tempObj.put("orderPhoneNumber", resultObj.get("orderPhoneNumber").toString());
			tempObj.put("orderMobilePhoneNumber", resultObj.get("orderMobilePhoneNumber").toString());
			tempObj.put("transactionDate", resultObj.get("transactionDate").toString());
			tempObj.put("orderAmount", resultObj.get("orderAmount").toString());
			tempObj.put("totalValueOrderCurrency", resultObj.get("totalValueOrderCurrency").toString());
			tempObj.put("totalValueOrderAmount", resultObj.get("totalValueOrderAmount").toString());
			tempObj.put("deliveryStreet", resultObj.get("deliveryStreet").toString());
			tempObj.put("campaignCode", resultObj.get("campaignCode").toString());
			tempObj.put("campaignName", resultObj.get("campaignName").toString());
			tempObj.put("deliveryHouseNumber", resultObj.get("deliveryHouseNumber").toString());
			tempObj.put("deliveryHouseNumberExt", resultObj.get("deliveryHouseNumberExt").toString());
			tempObj.put("deliveryPostCode", resultObj.get("deliveryPostCode").toString());
			tempObj.put("deliveryCity", resultObj.get("deliveryCity").toString());
			tempObj.put("deliveryState", resultObj.get("deliveryState").toString());
			tempObj.put("deliveryCountry", resultObj.get("deliveryCountry").toString());
			tempObj.put("deliveryPhone", resultObj.get("deliveryPhone").toString());
			tempObj.put("invoiceName", resultObj.get("invoiceName").toString());
			tempObj.put("invoiceStreet", resultObj.get("invoiceStreet").toString());
			tempObj.put("invoiceHouseNumber", resultObj.get("invoiceHouseNumber").toString());
			tempObj.put("invoiceHouseNumberExt", resultObj.get("invoiceHouseNumberExt").toString());
			tempObj.put("invoiceZipcode", resultObj.get("invoiceZipcode").toString());
			tempObj.put("invoiceCity", resultObj.get("invoiceCity").toString());
			tempObj.put("invoiceState", resultObj.get("invoiceState").toString());
			tempObj.put("invoiceCountry", resultObj.get("invoiceCountry").toString());
			tempObj.put("comments", resultObj.get("comments").toString());
			tempObj.put("vat", resultObj.get("vat").toString());
			tempObj.put("ordIntegrationId", resultObj.get("ordIntegrationId").toString());
			array_result.put(tempObj);
		}
		return array_result;
	}

	/*
	 * This function will return the id key based on api link
	 * 
	 * @return String key
	 */
	public static String getSourceIdKeyByLink(String link) {
		String key = "cpSourceId";
		if (link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/campaign/opens") || link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/campaign/links") || link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/campaignWaveBounceResponse"))
			key = "cpSourceId";
		else if (link.equals("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/salesData"))
			key = "cpSourceId";
		else if (link.contains("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/recommendations"))
			key = "opSourceId";
		else if (link.contains("https://oviewapi.ufs.com/OviewWeb/ufs/rest/display/newsLetterSubscriptions"))
			key = "nlSourceId";
		else
			key = "cpSourceId";

		return key;
	}

	/*
	 * This function will join the list of id from json array and combine it to one
	 * single string seperated with commas for example '12345', '67890'
	 * 
	 * @return String id
	 */
	public static String getIdFromSourceArray(JSONArray idList, String keyField) {
		List<String> newIdList = new ArrayList<String>();
		for (int i = 0; i < idList.length(); i++) {
			newIdList.add("'" + idList.getJSONObject(i).get(keyField).toString() + "'");
		}
		String id = String.join(",", newIdList);
		return id;
	}

	public static JSONArray checkMatchedChars(JSONObject sourceArr, String source, JSONObject targetArr, String target) throws JsonParseException, JsonMappingException, IOException {
		JSONArray newJSONData = new JSONArray();
		JSONObject sourceObject = new JSONObject();
		JSONObject targetObject = new JSONObject();
		JSONArray newSource = new JSONArray();
		JSONArray newTarget = new JSONArray();
		int counter = 0;

		String baseTable = "customers";
		String field = "armstrong_2_customers_name";

		if (source.toUpperCase().contains("CUSTOMER") && !source.toUpperCase().contains("GOOGLE_PLACES_API")) {
			baseTable = "customers";
			field = "armstrong_2_customers_name";
		} else if (source.toUpperCase().contains("CONTACT") && !source.toUpperCase().contains("GOOGLE_PLACES_API")) {
			baseTable = "contacts";
			field = "";
		} else if (source.toUpperCase().contains("CUSTOMER") && source.toUpperCase().contains("GOOGLE_PLACES_API")) {
			baseTable = "customers_google";
			field = "armstrong_2_customers_name";
		}

		for (String sourceKey : sourceArr.keySet()) {
			JSONObject filters = mergeFilter(baseTable);
			String sourceStr = sourceArr.get(sourceKey).toString();
			sourceStr = sourceStr.equals("null") || sourceStr.equals("") ? "-" : sourceStr.replaceAll(REGEX_INVALID_CHARS, "");

			String nameFilter = sourceKey.equals(field) ? "0_" : toAlphabetic(counter) + "_";
			for (String targetKey : targetArr.keySet()) {
				String targetStr = targetArr.get(targetKey).toString();
				targetStr = targetArr.get(targetKey).toString().equals("null") || targetArr.get(targetKey).toString().equals("") ? "-" : targetStr.replaceAll(REGEX_INVALID_CHARS, "");

				if (source.toUpperCase().contains("ARMSTRONG"))
					sourceObject.put("zzz_source", "armstrong");
				if(target.toUpperCase().contains("OHUB"))
					targetObject.put("zzz_source", "ohub");
				if(target.toUpperCase().contains("GOOGLE_PLACES"))
					targetObject.put("zzz_source", "google");
				
				for (String filterKey : filters.keySet()) {
					if (filterKey.equals(sourceKey) && targetKey.equals(filters.get(filterKey).toString())) {
						sourceObject.put(nameFilter + sourceKey, sourceStr);
						targetObject.put(nameFilter + sourceKey, targetStr);
					} else if (filterKey.equals(sourceKey) && filters.get(filterKey).equals("NO_FIELD")) {
						sourceObject.put(nameFilter + sourceKey, sourceStr);
						targetObject.put(nameFilter + sourceKey, "-");
					}
				}
			}
			counter++;
		}
		JSONArray delete = new JSONArray();
		for (String key : targetObject.keySet()) {
			String current = key.substring(0, 2);
			for (String key2 : targetObject.keySet()) {
				String current2 = key2.substring(0, 2);
				if (current.equals(current2) && targetObject.getString(key2).equals("-")) {
					if (!key.equals(key2)) {
						delete.put(key2);
					}
				}
			}
		}

		for (int i = 0; i < delete.length(); i++) {
			targetObject.remove(delete.getString(i));
		}

		JSONObject newObject = new JSONObject();
		newSource.put(objectMapper(sourceObject.toString()));
		newTarget.put(objectMapper(targetObject.toString()));
		newObject.put("\"" + source + "\"", newSource);
		newObject.put("\"" + target + "\"", newTarget);
		newJSONData.put(newObject);
		return newJSONData;
	}

	public static JSONObject mergeFilter(String baseConnection) {
		JSONObject filters = new JSONObject();

		if (baseConnection.equals("customers")) {
			filters.put("armstrong_2_customers_name", "name");
			filters.put("email", "emailAddress");
			filters.put("street_address", "street");
			filters.put("city", "city");
			filters.put("postal_code", "zipCode");
			filters.put("phone", "mobilePhoneNumber");
			filters.put("fax", "faxNumber");

			//filters.put("display_name", "NO_FIELD");
			filters.put("web_url", "NO_FIELD");
			filters.put("latitude", "NO_FIELD");
			filters.put("longitude", "NO_FIELD");
			filters.put("opening_hours", "NO_FIELD");
			filters.put("rating", "NO_FIELD");

			//filters.put("armstrong_1_customers_id", "wholesalerOperatorId");
			//filters.put("armstrong_2_customers_id", "sourceId");
		} else if (baseConnection.equals("contacts")) {
			
		} else if(baseConnection.equals("customers_google")) {
			filters.put("armstrong_2_customers_name", "name");
			filters.put("phone", "formatted_phone_number");
			filters.put("opening_hours", "opening_hours");
			filters.put("street_address", "formatted_address");
			filters.put("rating", "rating");
			filters.put("web_url", "website");
			filters.put("latitude", "lat");
			filters.put("longitude", "lng");
			filters.put("postal_code", "postal_code");
			
			filters.put("fax", "NO_FIELD");
			filters.put("email", "NO_FIELD");
			filters.put("city", "NO_FIELD");
		}
		return filters;
	}

	public static String toAlphabetic(int i) {
		if (i < 0) {
			return "-" + toAlphabetic(-i - 1);
		}

		int quot = i / 26;
		int rem = i % 26;
		char letter = (char) ((int) 'a' + rem);
		if (quot == 0) {
			return "" + letter;
		} else {
			return toAlphabetic(quot - 1) + letter;
		}
	}

	public static JSONArray getCWSIdFromCWS(JSONArray result) {
		JSONArray array_result = new JSONArray();
		for (int i = 0; i < result.length(); i++) {
			JSONObject resultObj = result.getJSONObject(i);
			JSONObject tempObj = new JSONObject();
			tempObj.put("campaignWaveResponseId", resultObj.get("campaignWaveResponseId"));
			tempObj.put("campaignName", resultObj.get("campaignName"));
			tempObj.put("cpFirstName", resultObj.get("cpFirstName"));
			tempObj.put("cpLastName", resultObj.get("cpLastName"));
			array_result.put(tempObj);
		}
		return array_result;
	}

	@SuppressWarnings("unchecked")
	public static String objectMapper(String jsonString) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		om.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		Map<String, Object> map;
		map = om.readValue(jsonString, HashMap.class);
		return om.writeValueAsString(map);
	}

	public static String getCountryNameById(int id) {
		final String countries[] = { "Malaysia", "Australia", "Hongkong", "South Africa", "Singapore", "Taiwan", "Thailand", "Philippines", "New Zealand", "Indonesia", "Vietnam", "Saudi Arabia", "United Arab Emirates", "Bahrain", "Qatar", "Oman", "Kuwait", "Egypt", "Myanmar", "Sri Lanka", "Pakistan", "Lebanon", "Maldives" };
		if(id == 0)
			return "";
		return countries[id - 1];
	}
	
	public static JSONArray salesDataFilter(JSONArray result) {
		JSONArray array_result = new JSONArray();
		for (int i = 0; i < result.length(); i++) {
			JSONObject resultObj = result.getJSONObject(i);
			JSONObject tempObj = new JSONObject();
			tempObj.put("quantity", resultObj.get("quantity"));
			tempObj.put("amount", resultObj.get("amount"));
			tempObj.put("unitPriceCurrency", resultObj.get("unitPriceCurrency"));
			tempObj.put("productName", resultObj.get("productName"));
			tempObj.put("itemType", resultObj.get("itemType"));
			tempObj.put("unit", resultObj.get("unit"));
			tempObj.put("countryCode", resultObj.get("countryCode"));
			tempObj.put("eanCode", resultObj.get("eanCode"));
			array_result.put(tempObj);
		}
		return array_result;
	}
	
	public static String checkNullString(Object str) {
		if(str.toString() == null || str.toString().equals("null"))
			return "";
		else
			return str.toString();
	}
	
	public static JSONArray createErrorMessage(String errorMsg) {
		JSONArray error = new JSONArray();
		error.put(new JSONObject().put("error", errorMsg));
		return error;
	}
	
	public static String generateSupermapperID(String lastSMID) {
		String supermapperId = "";
		int idCounter = lastSMID.equals("") ? 1 : Integer.valueOf(lastSMID.substring(4, 11));
		char currentChar = lastSMID.equals("") ? 'A' : (char)lastSMID.charAt(lastSMID.length() - 1);
		
		if(idCounter < 9999999 && !lastSMID.equals("")) {
			idCounter++;
		}
		else if(idCounter >= 9999999) { //fix possible error here like if the letter is already Z the next should be AA, AB, AC
			idCounter = 1;
			currentChar = (char)(currentChar + 1);
		}
		supermapperId = String.format("SMID%07d" + String.valueOf(currentChar), idCounter);
		System.err.println(supermapperId);
		return supermapperId;
	}
}
