//
//package com.daicq.util;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFCellStyle;
//import org.apache.poi.xssf.usermodel.XSSFFont;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.io.Resource;
//
//import java.io.*;
//import java.util.*;
//
///**
// * Custom Utilities: Functionalities for : JSON to HTML table JSON to CSV JSON
// * to Excel (xlsx) saving files to file storage merging multiple files in one
// * file
// *
// * @author Mikkel Pichay
// */
//
//public class FileUtil {
//
//	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);
//	private static List<String> headers = new ArrayList<String>();
//	public static final String USER_DIR = System.getProperty("user.dir");
//	public static final String UPLOAD_DIR = "/uploads/", MERGED_DIR = "MERGED/";
//	private static int headerCounter = 0;
//
//	private static String addTableContent(JSONArray jsonData) {
//		log.info("Adding table data...");
//		JSONObject object = null;
//		String row = "";
//		for (int d = 0; d < jsonData.length(); d++) {
//
//			JSONObject tempObject = jsonData.getJSONObject(d);
//			row += "<tr>";
//
//			for (int i = 0; i < headers.size(); i++) {
//
//				String header = headers.get(i);
//				if (tempObject.has(header)) {
//					object = tempObject.getJSONArray(header).getJSONObject(0);
//
//					for (String key : object.keySet()) {
//
//						if (tempObject.toString().equals("")) {
//							row += "<td align='center'> null </td>";
//						} else {
//							row += "<td align='center'>" + object.get(key) + "</td>";
//						}
//					}
//				}
//			}
//			row += "</tr>";
//		}
//		log.info("table data added.");
//		return row;
//	}
//
//	private static String addTableHeader(JSONArray jsonData, boolean isHTML) {
//		log.info("Adding table headers...");
//		String thead_source = "<thead class='thead-dark'>";
//		JSONObject object = null;
//		int counter = 0;
//		headers = new ArrayList<String>();
//
//		if (jsonData.getJSONObject(0) != null) {
//			object = jsonData.getJSONObject(0);
//		}
//		if (isHTML) {
//			thead_source += "<tr>";
//		}
//		for (String key : object.keySet()) {
//			headers.add(key);
//			if (counter < jsonData.length())
//				object = jsonData.getJSONObject(counter).getJSONArray(key).getJSONObject(0);
//			if (isHTML) {
//				thead_source += "<th class='headers' colspan='" + object.keySet().size() + "' style='font-weight: bold; text-align:left; color: black; \'>" + key + "</th>";
//			}
//			counter++;
//		}
//
//		if (isHTML) {
//			thead_source += "</tr>";
//		}
//		String properties = "<tr>";
//
//		for (int i = 0; i < headers.size(); i++) {
//			object = jsonData.getJSONObject(0).getJSONArray(headers.get(i)).getJSONObject(0); // we only need one so get the value in data[0]
//			for (String key : object.keySet()) {
//				properties += "<th class='headers' colspan='" + 1 + "' style='font-weight: bold; text-align:left; color: black; \'>" + key + "</th>";
//			}
//		}
//
//		properties += "</tr>";
//		thead_source += properties + "</thead>";
//		log.info("Table headers added.");
//		return thead_source;
//	}
//
//	public static String convertJSONToTable(JSONArray jsonData, boolean isHTML) {
//		log.info("Creating table from json...");
//		if (jsonData == null || jsonData.toString().equals("[]"))
//			return "";
//		String tableStr = "";
//		tableStr = addTableHeader(jsonData, isHTML) + addTableContent(jsonData);
//		log.info("Table created.");
//		return tableStr;
//	}
//
//	public static void convertHTMLTableToCSV(String htmlTable, String fileName, String countryId) throws IOException {
//		FileWriter writer = new FileWriter(USER_DIR + UPLOAD_DIR + fileName + "-" + countryId + ".csv");
//		Document doc;
//
//		try {
//			doc = Jsoup.parse("<html><table border='1' id = 'table'>" + htmlTable + "</table></html>");
//			Element table = doc.getElementById("table");
//			Elements rows = table.select("tr");
//
//			int counter = 0;
//			for (Element row : rows) {
//				Elements tds = counter > 0 ? row.select("td") : rows.select("th");
//				for (Element td : tds) {
//					writer.write(td.text().concat(", ").trim());
//					counter++;
//				}
//				writer.write("\n");
//			}
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void convertJSONToExcel(JSONArray jsonData, String fileName, String countryId) throws IOException {
//
//		if (jsonData == null || jsonData.toString().equals("[]")) {
//			return;
//		}
//
//		System.out.println("Creating excel file from json...");
//		XSSFWorkbook workbook = new XSSFWorkbook();
//		XSSFSheet sheet = workbook.createSheet("Supermapper Export Data");
//		String dir = USER_DIR + UPLOAD_DIR + getDirectoryPerCountry(countryId);
//
//		checkDirectory(dir);
//
//		try {
//			jsonData = duplicateJSONData(jsonData);
//			Row row;
//			Cell cell = null;
//			headers = new ArrayList<String>();
//			CellStyle style = null;
//			XSSFFont font = workbook.createFont();
//			JSONObject jsonHeader = null;
//			int counter = 0;
//			if (jsonData.getJSONObject(0) != null) {
//				jsonHeader = jsonData.getJSONObject(0);
//			}
//			int rowNum = 0;
//
//			for (String key : jsonHeader.keySet()) {
//				headers.add(key);
//				if (counter < jsonData.length())
//					jsonHeader = jsonData.getJSONObject(counter).getJSONArray(key).getJSONObject(0);
//				counter++;
//			}
//
//			Collections.sort(headers); //sort headers to follow loop for body format
//
//			font.setBold(true);
//			font.setFontHeightInPoints((short) 12);
//			style = workbook.createCellStyle();
//			style.setFont(font);
//			row = sheet.createRow(rowNum++);
//			int colNum1 = 0;
//
//			// Loop for headers
//			for (int i = 0; i < headers.size(); i++) {
//				// we only need one header so get the value in first row
//				jsonHeader = jsonData.getJSONObject(0).getJSONArray(headers.get(i)).getJSONObject(0);
//				for (String key : jsonHeader.keySet()) {
//					cell = row.createCell(colNum1++);
//					cell.setCellValue(key);
//					cell.setCellStyle(style);
//				}
//			}
//
//			JSONObject jsonBody = null;
//			// loop for body
//			for (int d = 0; d < jsonData.length(); d++) {
//				//loop through all JSONObjects inside the JSONArray
//				JSONObject jsonDataObj = jsonData.getJSONObject(d);
//				row = sheet.createRow(rowNum++);
//				int colNum = 0;
//				for (int i = 0; i < headers.size(); i++) {
//					String header = headers.get(i);
//					//check if the JSONObject contains the header
//					if (jsonDataObj.has(header)) {
//						for (int j = 0; j < jsonDataObj.getJSONArray(header).length(); j++) {
//							jsonBody = jsonDataObj.getJSONArray(header).getJSONObject(j);
//							// check if multiple OHUB result exists in the array.
//							if (i > 0 && j > 0 && jsonDataObj.getJSONArray(header).length() > 1) {//right side data
//								//process to duplicate data, for ex : there's 1 armstrong and 2 ohub.
//								JSONObject base = new JSONObject();
//								String baseHeader = headers.get(0);
//								base = jsonDataObj.getJSONArray(baseHeader).getJSONObject(0);// we only need one
//								row = sheet.createRow(rowNum++);
//								colNum = 0;
//								colNum = setCellValues(base, cell, row, jsonDataObj.toString(), colNum);
//							}
//							colNum = setCellValues(jsonBody, cell, row, jsonDataObj.toString(), colNum);//left side data
//						}
//					}
//				}
//			}
//			autoSizeColumns(workbook);
//		} catch (Exception e) {
//			System.err.println("JSON is empty. Nothing to save.");
//		}
//
//		try {
//			FileOutputStream outputStream = new FileOutputStream(dir + fileName + "-" + countryId + ".xlsx");
//			workbook.write(outputStream);
//			outputStream.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		System.out.println("Excel file created.");
//	}
//
//	public static String fileName(String fileName) {
//		// removes the countryId in the fileName of saved mapping data
//		for (int i = 0; i < 24; i++) {
//			String countryId = String.valueOf(i + 1);
//			String strToRemove = "-" + countryId;
//			if (fileName.contains(strToRemove)) {
//				fileName = fileName.replace(strToRemove, "");
//				break;
//			}
//		}
//		return fileName;
//	}
//
//	public static void autoSizeColumns(XSSFWorkbook workbook) {
//		int numberOfSheets = workbook.getNumberOfSheets();
//		for (int i = 0; i < numberOfSheets; i++) {
//			XSSFSheet sheet = workbook.getSheetAt(i);
//			if (sheet.getPhysicalNumberOfRows() > 0) {
//				Row row = sheet.getRow(0);
//				try {
//					Iterator<Cell> cellIterator = row.cellIterator();
//					while (cellIterator.hasNext()) {
//						Cell cell = cellIterator.next();
//						int columnIndex = cell.getColumnIndex();
//						sheet.autoSizeColumn(columnIndex);
//					}
//				} catch (Exception e) {
//					System.err.println("An error occured while auto sizing columns.");
//				}
//			}
//		}
//	}
//
//	public static void mergeExcelFiles(List<Resource> resource, String fileName) throws IOException {
//		String dir = USER_DIR + UPLOAD_DIR + MERGED_DIR;
//
//		checkDirectory(dir);
//
//		XSSFWorkbook book = new XSSFWorkbook();
//		XSSFSheet sheet = book.createSheet("MAPPING");
//		headerCounter = 0;
//
//		for (int i = 0; i < resource.size(); i++) {
//			XSSFWorkbook b = new XSSFWorkbook(resource.get(i).getInputStream());
//			for (int j = 0; j < b.getNumberOfSheets(); j++) {
//				copySheets(book, sheet, b.getSheetAt(j));
//			}
//		}
//
//		try {
//			System.err.println("Starting to merge excel files...");
//			writeFile(book, dir + fileName + ".xlsx");
//			System.err.println("Merging excel files completed.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	protected static void writeFile(XSSFWorkbook book, String file) throws Exception {
//		FileOutputStream out = new FileOutputStream(file);
//		book.write(out);
//		out.close();
//	}
//
//	private static void copySheets(XSSFWorkbook newWorkbook, XSSFSheet newSheet, XSSFSheet sheet) {
//		copySheets(newWorkbook, newSheet, sheet, false);
//	}
//
//	private static void copySheets(XSSFWorkbook newWorkbook, XSSFSheet newSheet, XSSFSheet sheet, boolean copyStyle) {
//		int newRownumber = newSheet.getLastRowNum();
//		int maxColumnNum = 0;
//		int indexToSub = 0;
//		int newRowNumToAdd = 0;
//		Map<Integer, XSSFCellStyle> styleMap = (copyStyle) ? new HashMap<Integer, XSSFCellStyle>() : null;
//
//		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
//			XSSFRow srcRow = null;
//			XSSFRow destRow = null;
//			if (i == 0 && headerCounter != 0) {
//				indexToSub = 1;// fix for blank rows between added excel files
//				newRowNumToAdd = 1;
//			} else {
//				headerCounter++;
//				destRow = newSheet.createRow((i - indexToSub) + newRownumber + newRowNumToAdd);
//				srcRow = sheet.getRow(i);
//
//				if (srcRow != null) {
//					copyRow(newWorkbook, sheet, newSheet, srcRow, destRow, styleMap, i);
//					if (srcRow.getLastCellNum() > maxColumnNum) {
//						maxColumnNum = srcRow.getLastCellNum();
//					}
//				}
//			}
//		}
//		for (int i = 0; i <= maxColumnNum; i++) {
//			int columnWidth = sheet.getColumnWidth(i);
//			if (((columnWidth / 256) > 255)) {
//				columnWidth = (255 * 256) / 256;
//			}
//			newSheet.setColumnWidth(i, columnWidth);
//		}
//	}
//
//	public static void copyRow(XSSFWorkbook newWorkbook, XSSFSheet srcSheet, XSSFSheet destSheet, XSSFRow srcRow, XSSFRow destRow, Map<Integer, XSSFCellStyle> styleMap, int index) {
//		CellStyle style = null;
//		XSSFFont font = newWorkbook.createFont();
//
//		if (index == 0) {
//			font.setBold(true);
//			font.setFontHeightInPoints((short) 12);
//			style = newWorkbook.createCellStyle();
//			style.setFont(font);
//		}
//
//		destRow.setHeight(srcRow.getHeight());
//		int j = srcRow.getFirstCellNum();
//		for (j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
//			XSSFCell oldCell = srcRow.getCell(j);
//			XSSFCell newCell = destRow.getCell(j);
//			if (oldCell != null) {
//				if (newCell == null) {
//					newCell = destRow.createCell(j);
//					if (index == 0) {
//						newCell.setCellStyle(style); // make the first row bold for header
//					}
//				}
//				copyCell(newWorkbook, oldCell, newCell, styleMap);
//			}
//		}
//	}
//
//	public static void copyCell(XSSFWorkbook newWorkbook, XSSFCell oldCell, XSSFCell newCell, Map<Integer, XSSFCellStyle> styleMap) {
//		if (styleMap != null) {
//			int stHashCode = oldCell.getCellStyle().hashCode();
//			XSSFCellStyle newCellStyle = styleMap.get(stHashCode);
//			if (newCellStyle == null) {
//				newCellStyle = newWorkbook.createCellStyle();
//				newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
//				styleMap.put(stHashCode, newCellStyle);
//			}
//			newCell.setCellStyle(newCellStyle);
//		}
//		switch (oldCell.getCellType()) {
//		case XSSFCell.CELL_TYPE_STRING:
//			newCell.setCellValue(oldCell.getRichStringCellValue());
//			break;
//		case XSSFCell.CELL_TYPE_NUMERIC:
//			newCell.setCellValue(oldCell.getNumericCellValue());
//			break;
//		case XSSFCell.CELL_TYPE_BLANK:
//			newCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
//			break;
//		case XSSFCell.CELL_TYPE_BOOLEAN:
//			newCell.setCellValue(oldCell.getBooleanCellValue());
//			break;
//		case XSSFCell.CELL_TYPE_ERROR:
//			newCell.setCellErrorValue(oldCell.getErrorCellValue());
//			break;
//		case XSSFCell.CELL_TYPE_FORMULA:
//			newCell.setCellFormula(oldCell.getCellFormula());
//			break;
//		default:
//			break;
//		}
//	}
//
//	public static void saveJsonToTxt(JSONArray jsonData, String fileName, String countryId) throws IOException {
//
//		if (jsonData == null || jsonData.toString().equals("[]")) {
//			return;
//		}
//
//		String dir = USER_DIR + UPLOAD_DIR + getDirectoryPerCountry(countryId);
//
//		checkDirectory(dir);
//
//		FileWriter file = null;
//		try {
//			System.out.println("Saving JSON to text file...");
//			file = new FileWriter(dir + fileName + "-" + countryId + ".txt");// create new file
//			String data = jsonData.toString();
//			if (fileName.contains("SUPERMAPPERID")) {
//				data = data.toString().replaceAll("\"", "").replaceAll("\\\\", "\"");
//			}
//			file.write(data);// save json to text file
//			System.out.println("Successfully saved JSON to text file.");
//		} catch (IOException e) {
//			System.err.println("An error occured while saving json to text file.");
//		} finally {
//			file.flush();
//			file.close();
//		}
//	}
//
//	@SuppressWarnings("resource")
//	public static String mergeJSONTextFiles(String fileName, String[] countryId) throws IOException {
//		System.err.println("Merging json files...");
//		JSONArray newJSONData = new JSONArray();
//		for (int i = 0; i < countryId.length; i++) {
//			String dir = USER_DIR + UPLOAD_DIR;
//
//			checkDirectory(dir);
//
//			try {
//				File file = new File(dir + getDirectoryPerCountry(countryId[i]) + fileName + "-" + countryId[i] + ".txt");
//				Scanner sc = new Scanner(file);
//				JSONArray oldData = null;
//				if (sc.hasNextLine()) {
//					oldData = new JSONArray(sc.nextLine()); //json is saved only in one line so we dont need loop
//				}
//				for (int j = 0; j < oldData.length(); j++) {
//					if (!fileName.contains("SUPERMAPPERID")) {
//						newJSONData.put(oldData.getJSONObject(j));
//					} else {
//						newJSONData.put(Util.objectMapper(oldData.getJSONObject(j).toString()));
//					}
//				}
//			} catch (Exception e) {
//				System.err.println("The json file with countryId '" + countryId[i] + "' does not exist! Skipping file.");
//			}
//		}
//		System.err.println("Merging json files completed.");
//		if (fileName.contains("SUPERMAPPERID")) {
//			return newJSONData.toString().toString().replaceAll("\"", "").replaceAll("\\\\", "\"");
//		} else {
//			return newJSONData.toString();
//		}
//	}
//
//	public static String getDirectoryPerCountry(String countryId) {
//		String directoryName = "";
//
//		if (countryId.equals("1")) {
//			directoryName = "1-MALAYSIA-MY";
//		}
//		if (countryId.equals("2")) {
//			directoryName = "2-AUSTRALIA-AU";
//		}
//		if (countryId.equals("3")) {
//			directoryName = "3-HONGKONG-HK";
//		}
//		if (countryId.equals("4")) {
//			directoryName = "4-SOUTH-AFRICA-ZA";
//		}
//		if (countryId.equals("5")) {
//			directoryName = "5-SINGAPORE-SG";
//		}
//		if (countryId.equals("6")) {
//			directoryName = "6-TAIWAN-TW";
//		}
//		if (countryId.equals("7")) {
//			directoryName = "7-THAILAND-TH";
//		}
//		if (countryId.equals("8")) {
//			directoryName = "8-PHILIPPINES-PH";
//		}
//		if (countryId.equals("9")) {
//			directoryName = "9-NEW-ZEALAND-NZ";
//		}
//		if (countryId.equals("10")) {
//			directoryName = "10-INDONESIA-ID";
//		}
//		if (countryId.equals("11")) {
//			directoryName = "11-VIETNAM-VN";
//		}
//		if (countryId.equals("12")) {
//			directoryName = "12-SAUDI-ARABIA-SA";
//		}
//		if (countryId.equals("13")) {
//			directoryName = "13-UNITED-ARAB-EMIRATES-UAE";
//		}
//		if (countryId.equals("14")) {
//			directoryName = "14-BAHRAIN-BH";
//		}
//		if (countryId.equals("15")) {
//			directoryName = "15-QATAR-QAT";
//		}
//		if (countryId.equals("16")) {
//			directoryName = "16-OMAN-OMN";
//		}
//		if (countryId.equals("17")) {
//			directoryName = "17-KUWAIT-KWT";
//		}
//		if (countryId.equals("18")) {
//			directoryName = "18-EGYPT-EG";
//		}
//		if (countryId.equals("19")) {
//			directoryName = "19-MYANMAR-MYR";
//		}
//		if (countryId.equals("20")) {
//			directoryName = "20-SRI-LANKA-LK";
//		}
//		if (countryId.equals("21")) {
//			directoryName = "21-PAKISTAN-PK";
//		}
//		if (countryId.equals("22")) {
//			directoryName = "22-LEBANON-LBN";
//		}
//		if (countryId.equals("23")) {
//			directoryName = "23-MALDIVES-MDV";
//		}
//		if (countryId.equals("24")) {
//			directoryName = "24-JORDAN-JOR";
//		}
//		return directoryName + "/";
//	}
//
//	/*
//	 * Checks if a directory is existing, creates a new directory if does not exist.
//	 */
//	private static void checkDirectory(String dir) {
//
//		try {
//			File directory = new File(dir);
//
//			if (!directory.exists()) {
//				directory.mkdir();
//			}
//		} catch (Exception e) {
//			System.err.println("An error occured while checking directory...");
//		}
//	}
//
//	private static int setCellValues(JSONObject body, Cell cell, Row row, String tempObjectStr, int colNum) {
//		for (String key : body.keySet()) {
//			cell = row.createCell(colNum++);
//			if (tempObjectStr.equals("")) {
//				cell.setCellValue("null");
//			} else {
//				cell.setCellValue(body.get(key).toString());
//			}
//		}
//		return colNum;
//	}
//
//	private static JSONArray duplicateJSONData(JSONArray jsonData) {//optimize the code later
//		JSONArray newData = new JSONArray();
//		List<String> header = new ArrayList<String>();
//
//		for (String key : jsonData.getJSONObject(0).keySet()) {
//			header.add(key);
//		}
//
//		for (int jsonDataIndex = 0; jsonDataIndex < jsonData.length(); jsonDataIndex++) {
//			JSONObject currentObj = jsonData.getJSONObject(jsonDataIndex);
//			for (int headerIndex = 0; headerIndex < header.size(); headerIndex++) {
//				if (headerIndex <= 0 && currentObj.getJSONArray(header.get(headerIndex)).length() > 1) {
//					JSONArray currentArray = currentObj.getJSONArray(header.get(headerIndex));
//					for (int currentArrIndex = 0; currentArrIndex < currentArray.length(); currentArrIndex++) {
//						JSONArray source = new JSONArray();
//						JSONArray target = new JSONArray();
//						source.put(currentArray.getJSONObject(currentArrIndex));
//						target.put(currentObj.getJSONArray(header.get(1)).getJSONObject(0));
//						JSONObject newJSONObject = new JSONObject();
//						newJSONObject.put(header.get(headerIndex), source);
//						newJSONObject.put(header.get(1), target);
//						newData.put(newJSONObject);
//					}
//				} else if (headerIndex <= 0 && currentObj.getJSONArray(header.get(headerIndex)).length() <= 1) {
//					JSONArray currentArray = currentObj.getJSONArray(header.get(headerIndex));
//					for (int currentArrIndex = 0; currentArrIndex < currentArray.length(); currentArrIndex++) {
//						JSONArray source = new JSONArray();
//						JSONArray target = new JSONArray();
//						source.put(currentArray.getJSONObject(currentArrIndex));
//						JSONArray targetArray = currentObj.getJSONArray(header.get(currentArrIndex + 1));
//						for (int targetArrayIndex = 0; targetArrayIndex < targetArray.length(); targetArrayIndex++) {
//							target.put(targetArray.getJSONObject(targetArrayIndex));
//						}
//						JSONObject newJSONObject = new JSONObject();
//						newJSONObject.put(header.get(headerIndex), source);
//						newJSONObject.put(header.get(currentArrIndex + 1), target);
//						newData.put(newJSONObject);
//					}
//				}
//			}
//		}
//
//		if (newData == null || newData.toString().equals("[]")) {
//			return jsonData;
//		} else {
//			return newData;
//		}
//	}
//}
