package com.daicq.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.*;
import com.couchbase.client.java.query.dsl.Expression;
import com.daicq.dao.doc.MySqlDoc;
import com.daicq.dao.doc.OhubDoc;
import com.daicq.service.BucketService;
import com.daicq.service.OhubService;
import com.daicq.util.ConnectionUtil;
import com.daicq.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.*;

@Repository
public class CustomRepository {
	@Autowired
	BucketService bucketService;
	@Autowired
	MySqlRepository mySqlRepository;
	@Autowired
	OhubRepository ohubRepository;
	@Autowired
	OhubService ohubService;

	/**
	 * Get documents in Couchbase which contain 'connectionId' param
	 * 
	 * @Params: connectionId: connection Id
	 * 
	 * @Return: list of documents containing 'connectionId'.
	 */
	public List<String> findDocument(String connectionId) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		List<String> listDocument = new ArrayList<String>();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(i(bucketName)).where(x(Util.CONNECTION_ID).eq(s(connectionId)))));
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	/**
	 * Get documents in Couchbase which contain 'type' and 'docType' params
	 * 
	 * @Params: type, docType
	 * @Return: list String result
	 */
	public List<String> findDocument(String type, String docType) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s(type))).and(x("docType").eq(s(docType))))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public Map<String, String> getIdentifier(List<String> connectionIds) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		Map<String, String> identifierMap = new HashMap<String, String>();
		for (String connectionId : connectionIds) {
			N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "identifierKey")).from(bucketName).useKeys(s(connectionId))));
			for (N1qlQueryRow row : result) {
				JSONObject jo = new JSONObject(row.toString());
				String identifierKey = jo.getString("identifierKey");
				identifierMap.put(connectionId, identifierKey);
			}
		}
		return identifierMap;
	}

	public String findDocument(String type, String docType, String connectionId, String primaryKey, String primaryValue) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		String foundDocument = "";
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s(type))).and(x("docType").eq(s(docType))).and(x("connectionId").eq(s(connectionId))).and((x(primaryKey).eq(s(primaryValue))).or((x(primaryKey).eq(primaryValue)))))));
		for (N1qlQueryRow row : result) {
			foundDocument = row.toString();
		}
		return foundDocument;
	}

	public List<String> findNameofSource(String type) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(Expression.path(Expression.i(bucketName), "name")).from(Expression.i(bucketName)).where(Expression.x(Util.TYPE).eq(Expression.s(type)))));

		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public List<String> findDocumentByType(String type, String name) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		List<String> foundDocument = new ArrayList<String>();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "ids")).from(bucketName).where((x("lower(type)").eq(s(type.toLowerCase()))).and(x("lower(name)").eq(s(name.toLowerCase()))))));
		for (N1qlQueryRow row : result) {
			foundDocument.add(row.toString());
		}
		return foundDocument;
	}

	/**
	 * Get field of table in MySQL database
	 * 
	 * @Params: couchbaseBooketName: name of booket. mySqlDoc1 : MySQL object that
	 *          contains information to find connection.
	 * @Return: list of field in a specific table which is describe in connection
	 *          found.
	 */

	public ArrayList<String> getField(String couchbaseBooketName, MySqlDoc mySqlDoc) throws SQLException {
		Connection conn = null;
		Statement st = null;
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		ArrayList<String> columns = new ArrayList<String>();
		try {
			conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
			st = conn.createStatement();

			String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + databaseName + "' AND TABLE_NAME = '" + tableName + "';";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				columns.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return columns;
	}

	/**
	 * check existing identifier column in MySQL datasource
	 * 
	 * @Params: MySQL database name, table name, identifier column
	 * @Return: checking result (true or false)
	 */
	public int checkExistMySqlIdentifierColumnName(MySqlDoc mySqlDoc, Connection con) {
		try {
			/* st = (Statement) con.createStatement(); */
			String databaseName = mySqlDoc.getDatabaseName();
			String databaseTable = mySqlDoc.getDatabaseTable();
			String identifierColumnName = mySqlDoc.getIdentifierKey();
			Statement st = con.createStatement();
			String sql_getColumn = "SELECT COUNT(*) AS numberOfColumn FROM information_schema.COLUMNS  WHERE TABLE_SCHEMA = '" + databaseName + "' AND TABLE_NAME = '" + databaseTable + "' AND COLUMN_NAME = '" + identifierColumnName + "'";
			st.executeQuery(sql_getColumn);
			ResultSet rs = st.executeQuery(sql_getColumn);
			int result = 0;
			while (rs.next()) {
				result = rs.getInt("numberOfColumn");
			}
			return result;
		} catch (SQLException e) {
			return 0;
		}
	}

	/**
	 * get Sum of record of table in MySQL datasource
	 * 
	 * @Params: MySQL database name, table name, identifier column name
	 * @Return: sum of record
	 */
	public int getSumRowsOfTableMySql(String databaseName, String tableName, Connection con) {
		int result_sum = 0;
		try {
			/* st = (Statement) con.createStatement(); */
			Statement st = con.createStatement();
			String sql_getSUM = "SELECT COUNT(*) from " + databaseName + "." + tableName + "";
			ResultSet rs = st.executeQuery(sql_getSUM);
			while (rs.next()) {
				result_sum = rs.getInt("COUNT(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result_sum;
	}

	/**
	 * get sum record of MySQL datasource by DB name, table name, column name and
	 * value check
	 * 
	 * @Params: DB name, table name, column name, value
	 * @Return: sum of record
	 */
	public int getOneRecordOfTableMySql(String databaseName, String tableName, String culumnName, String valueCheck, Connection con) {
		int result_sum = 0;
		try {
			/* st = (Statement) con.createStatement(); */
			Statement st = con.createStatement();
			String sql_getSUM = "select count(*) from " + databaseName + "." + tableName + " where " + tableName + "." + culumnName + " = '" + valueCheck + "'";
			ResultSet rs = st.executeQuery(sql_getSUM);
			// int i=1;
			while (rs.next()) {
				result_sum = rs.getInt("count(*)");
			}
			return result_sum;
		} catch (SQLException e) {
			return 1; // to not create;
		}
	}

	/**
	 * connect to MySQL datasource and get all data of table by table name
	 * 
	 * @Params: MySQL database name, table name
	 * @Return: list MySQL data
	 */
	// get data from mySQL
	public JSONArray getAllDataFromTableMySQL(MySqlDoc mySqlDoc) {
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		JSONArray jsonArray_result = new JSONArray();
		PreparedStatement st = null;
		Connection conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
		try {
			String sql = "SELECT * FROM " + databaseName + "." + tableName;
			st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				jsonArray_result.put(getJsonObjectOneMySqlRow(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException ignore) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
			}
		}
		return jsonArray_result;
	}

	/**
	 * get json object is a mysql record => because if value in mysql table is null,
	 * then passer to json error, so it need process
	 * 
	 * @Params: ResultSet: resultSet get data from mysql table
	 * @Return: json object
	 */
	public JSONObject getJsonObjectOneMySqlRow(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int numOfColumns = rsmd.getColumnCount();
		JSONObject jsonObjectResult = new JSONObject();
		for (int i = 1; i <= numOfColumns; i++) {
			String columnName = rsmd.getColumnName(i);
			if (rsmd.getColumnTypeName(i).equals(Util.TIMESTAMP)) {
				jsonObjectResult.put(columnName, Util.getTimestamp(rs.getString(columnName)));
			} else {
				if (rs.getString(columnName) == null) {
					jsonObjectResult.put(columnName, "");
				} else {
					jsonObjectResult.put(columnName, rs.getString(columnName));
				}
			}
		}
		return jsonObjectResult;
	}

	/**
	 * get list record of mysql table by one value condition that had connected from
	 * other function
	 * 
	 * @Params: columnNameCondition : column to set condition valueCondition: value
	 *          of column name.
	 * @Return: list mysql records
	 */
	// get data by condition
	public JSONArray getListMySqlRecordByValueCondition(Connection con, String tbName, String columnNameCondition, String valueCondition) {
		String sql = "SELECT * FROM " + tbName + " WHERE " + columnNameCondition + " = '" + valueCondition + "'";
		JSONArray jsonArray_listResult = new JSONArray();
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				jsonArray_listResult.put(getJsonObjectOneMySqlRow(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray_listResult;
	}

	// get data SourceId from mySQL
	public JSONArray getDataSourceIdFromTableMySQL(MySqlDoc mySqlDoc, String keyTarget, List<String> keySource) {
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		JSONArray jsonArray_result = new JSONArray();
		Connection conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
		try {
			for (int i = 0; i < keySource.size(); i++) {
				String sql = "SELECT * FROM " + databaseName + "." + tableName + " WHERE " + keyTarget + " = " + keySource.get(i);
				// String sql = "SELECT * FROM " + databaseName + "." + tableName + "WHERE" +
				// keyTarget + "IN" + keySource;
				PreparedStatement st = conn.prepareStatement(sql);
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					jsonArray_result.put(getJsonObjectOneMySqlRow(rs));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("data get by Mysql" + jsonArray_result);
		return jsonArray_result;
	}

	/**
	 * @param mySqlDoc
	 * @return mess
	 */
	public String checkConnectionMySql(MySqlDoc mySqlDoc) {
		String mess = "";
		if (ConnectionUtil.getMysqlConnectionNotEncode(mySqlDoc) != null) {
			mess = "connection valid";
		} else {
			mess = "connection invalid";
		}
		;
		return mess;
	}

	/**
	 * @param ohubDoc
	 * @return mess
	 */
	public String checkConnectionOhub(OhubDoc ohubDoc) {
		String mess = "";
		if ((Util.readRestfulAPIDemoGETmethod(ohubDoc) != null) || Util.readRestfulAPIDemoPOSTmethod(ohubDoc) != null) {
			mess = "connection valid";
		} else {
			mess = "connection invalid";
		}
		return mess;
	}

	public JSONArray getDataBySourceId(MySqlDoc mySqlDoc, String keyTarget, String sourceId) {
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		JSONArray jsonArray_result = new JSONArray();
		Connection conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
		try {
			Statement st = conn.createStatement();
			String sql = "SELECT * FROM " + databaseName + "." + tableName + " where " + keyTarget + " in " + sourceId;
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				jsonArray_result.put(getJsonObjectOneMySqlRow(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray_result;
	}

	public JSONArray getDataSourceIdFromMySQL(MySqlDoc mySqlDoc, String keyTarget, String keySource, Connection conn) {
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		JSONArray jsonArray_result = new JSONArray();
		try {
			String sql = "SELECT * FROM " + databaseName + "." + tableName + " WHERE " + keyTarget + " = " + keySource;
			System.err.println(sql);
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				jsonArray_result.put(getJsonObjectOneMySqlRow(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray_result;
	}

	public List<String> findAllMapping() throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s("MAPPING"))))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public List<String> findMappingByName(String mappingName) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s("MAPPING")).and((x("mappingName").eq(s(mappingName))))))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public List<String> findMappingId(String mappingId) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s("MAPPING")).and((x("mappingId").eq(s(mappingId))))))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public List<String> findConnectionByKey(String key, String connectionType) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		//		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName)
		//				.where((x("connectionType").eq(s(connectionType)).useKeys(s(key))))));
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).useKeys(s(key)).where(x("connectionType").eq(s(connectionType)))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public List<String> getAllDataMapping() throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s("MAPPING"))))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public JSONArray getDataByCountryId(String fields, MySqlDoc mySqlDoc, String baseTable, String countryId, String active) throws Exception {
		Connection conn = null;
		Statement st = null;
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		JSONArray jsonArray_result = new JSONArray();
		String sql = "";
		try {
			conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
			st = conn.createStatement();

			if (baseTable.equals("customers")) {
				fields = fields.isEmpty() ? "armstrong_2_customers_id" : fields;
			} else if (baseTable.equals("products")) {
				fields = fields.isEmpty() ? "id" : fields;
			} else if (baseTable.equals("salespersons")) {
				fields = fields.isEmpty() ? "armstrong_2_salespersons_id" : fields;
			} else if (baseTable.equals("contacts")) {
				fields = fields.isEmpty() ? "id" : fields;
			}
			
			String activeStat = " ";
			if(active.equals("1")) {
				activeStat = " active = 1 AND ";
			}

			sql = "SELECT DISTINCT " + fields + " FROM " + databaseName + "." + tableName + " WHERE" + activeStat + "country_id IN (" + countryId + ")";
			//System.err.println("QUERY BY COUNTRY ID:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				jsonArray_result.put(getJsonObjectOneMySqlRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				st.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return jsonArray_result;
	}

	public String findDocumentByKey(String key, String operation) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		List<String> listDocument = new ArrayList<String>();
		N1qlQueryResult result;
		if (operation.equals("superMapper"))
			result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).where((x("type").eq(s("SUPERMAPPERID")).and((x("mappingId").eq(s(key))))))));
		else
			result = bucket.query(N1qlQuery.simple(select(path(i(bucketName), "*")).from(bucketName).useKeys(s(key))));
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument.toString();
	}

	public JSONArray getAllDataFromTableMySQLByCountryId(MySqlDoc mySqlDoc, String operation, String idField, String countryId, String idListStr, String active) throws SQLException {
		String databaseName = mySqlDoc.getDatabaseName();
		String tableName = mySqlDoc.getDatabaseTable();
		JSONArray jsonArray_result = new JSONArray();
		Connection conn = ConnectionUtil.getMysqlConnection(mySqlDoc);
		PreparedStatement st = null;
		if (operation.equals("mapBySource")) {
			try {
				JSONArray id_resultArray = new JSONArray(getDataByCountryId(idField, mySqlDoc, tableName, countryId, active).toString());
				JSONArray fields_resultArray = new JSONArray(mySqlDoc.getFields());
				List<String> ids = new ArrayList<String>();

				for (int i = 0; i < id_resultArray.length(); i++) {
					ids.add("'" + id_resultArray.getJSONObject(i).getString(idField).toString() + "'");
				}

				String idToFind = ids.toString().replace("[", "").replace("]", "");
				String fields = fields_resultArray.toString().replace("[", "").replace("]", "").replaceAll("\"", "");
				
				if(!idListStr.equals("")) {
					idToFind = idListStr;
				}
				String activeStat = " ";
				if(active.equals("1")) {
					activeStat = " active = 1 AND ";
				}
				
				String sql = "SELECT " + fields + " FROM " + databaseName + "." + tableName + " WHERE" + activeStat  + "" + idField + " IN (" + idToFind + ")";
				//System.err.println("QUERY GET DATA IN TABLE : " + sql);
				st = conn.prepareStatement(sql);
				ResultSet rs = null;
				try {
					rs = st.executeQuery();
					while (rs.next()) {
						jsonArray_result.put(getJsonObjectOneMySqlRow(rs));
					}
					st.close();
					conn.close();
				} finally {
					if (rs != null) {
						rs.close();
					}
				}
			} catch (Exception e) {
				System.err.println("An error occured while executing mysql query : " + e);
				return Util.createErrorMessage(e.getMessage());
			} finally {
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		}
		return jsonArray_result;
	}

	/**
	 * Get documents in Couchbase which contain 'type' and 'connectionType' params
	 * 
	 * @Params: type, connectionType
	 * @Return: list String result
	 */
	public List<String> findDistinctDocument(String fields, String type, String connectionType, String status) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();

		N1qlQueryResult result = bucket.query(N1qlQuery.simple(Select.selectDistinct(path(i(bucketName), fields)).from(bucketName).where((x("type").eq(s(type))).and(x("connectionType").eq(s(connectionType)).and(x("status").eq(s(status)))))), 60, TimeUnit.MINUTES);

		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public void deleteDocumentInCouchBaseByType(String type) throws Exception {
		System.err.println("Removing existing record from couchbase with type : " + type);
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		bucket.query(N1qlQuery.simple(Delete.deleteFrom(path(i(bucketName))).where(x("type").eq(s(type)))), 1, TimeUnit.HOURS);
	}

	public JSONArray getConnectionDocument(String field, String type, String connectionId, String idList) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(Select.selectDistinct(path(i(bucketName), field)).from(bucketName)
				.where((x("type").eq(s(type)))
						.and(x("connectionId").eq(s(connectionId)))
						.and(x("sourceId")
								.in("[" + idList + "]")))), 60, TimeUnit.MINUTES);//.limit(5000) add logic here

		JSONArray array_result = new JSONArray();
		for (N1qlQueryRow row : result) {
			JSONObject rowObject = new JSONObject(row.toString());
			array_result.put(rowObject.getJSONArray("result").getJSONObject(0));
		}
		return array_result;
	}

	/*
	 * get campaignWaveResponseId in campaignWaveSendings document in couchbase
	 */
	public List<String> getOHUBFieldDataInCouchBase(String connectionId, String type, String fields) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple("SELECT DISTINCT " + fields + " FROM " + bucketName + " s UNNEST s.result r WHERE " + "s.connectionId = '" + connectionId + "' and " + "s.type = '" + type + "'"), 60, TimeUnit.MINUTES);

		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public String findConnectionDocByBaseTable(String baseTable) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(Select.selectDistinct(path(i(bucketName), "id")).from(bucketName).where((x("databaseTable").eq(s(baseTable))))), 5, TimeUnit.MINUTES);
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
			break;
		}
		JSONArray array = new JSONArray(listDocument.toString());
		String str = array.getJSONObject(0).get("id").toString();// we only need one, so get the first array value
		return str;
	}

	public List<String> getConnectionDocument2(String field, String type, String connectionId, String id, String idList) throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		String statement = "SELECT r.* FROM `" + bucketName + "` s UNNEST s.result r WHERE s.connectionId = '" + connectionId + "' AND s.type = '" + type + "' AND s.sourceId IN [" + idList + "]";
		//System.err.println(N1qlQuery.simple(Select.select("r.*").from(bucketName + " s").unnest((x("s.result r"))).where((x("s.connectionId").eq(s(connectionId)).and((x("s.type").eq(s(type)).and((x("r." + id).in(s("[" + idList + "]"))))))))));

		N1qlQueryResult result = bucket.query(N1qlQuery.simple(statement), 60, TimeUnit.MINUTES);
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}

	public List<String> getAllMapsData() throws Exception {
		String bucketName = bucketService.getBucketName();
		Bucket bucket = bucketService.getBucket();
		N1qlQueryResult result = bucket.query(N1qlQuery.simple(Select.select("m.*, s.id").from(bucketName + " s").unnest((x("s.maps m"))).where((x("s.type").eq(s("MAPPING"))).or(x("s.type").eq(s("MAPPING_INACTIVE"))))));
		List<String> listDocument = new ArrayList<String>();
		for (N1qlQueryRow row : result) {
			listDocument.add(row.toString());
		}
		return listDocument;
	}
}
