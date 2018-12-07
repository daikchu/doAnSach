package com.daicq.service;

//import com.daicq.configuration.FileStorageProperties;
import com.daicq.dao.CustomRepository;
import com.daicq.dao.doc.MySqlDoc;
import com.daicq.dao.doc.OhubDoc;
import com.daicq.util.Util;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

@Service
public class FileStorageService {
	//private final Path fileStorageLocation;

	public final static String FILE_FALSE = "File is not excel";
	public final static String FILE_NULL = "File null";
	public final static String LINK = "uploads/";
	@Autowired
	MySqlService mySqlService;
	@Autowired
	OhubService ohubService;
	@Autowired
	CustomRepository customRepository;

//	@Autowired
//	public FileStorageService(FileStorageProperties fileStorageProperties) {
//		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
//		try {
//			Files.createDirectories(this.fileStorageLocation);
//		} catch (Exception ex) {
//			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
//		}
//	}
/*
	public String storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		if (file.getOriginalFilename() != null) {
			if (file.getOriginalFilename().endsWith(".xlsx") || file.getOriginalFilename().endsWith(".xls")) {
				try {
					// Check if the file's name contains invalid characters
					if (fileName.contains("..")) {
						throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
					}

					// Copy file to the target location (Replacing existing file with
					// the same name)
					//Path targetLocation = this.fileStorageLocation.resolve(fileName);
					//Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

					return fileName;
				} catch (IOException ex) {
					throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
				}
			} else {
				return FILE_FALSE;
			}
		} else {
			return FILE_NULL;
		}
	}*/

/*	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}*/

	/**
	 * @param bucketName
	 * @param fileNameExcel
	 * @return mess
	 */
	public String readExcel(String bucketName, String fileNameExcel) {
		String mes = "";
		try {
			// FileInputStream excelFile = new FileInputStream(new File("uploads" +
			// "source_input.xlsx"));
			// link static file
			FileInputStream excelFile = new FileInputStream(new File("LINK_STATICS" + fileNameExcel));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			Row firstRow = iterator.next();
			Cell firstCell = firstRow.getCell(0);
			// print first cell
			System.out.println(firstCell.getStringCellValue());
			// List<MySqlDoc> lstOfMySQL = new ArrayList<>();
			// List<OhubDoc> lstOfOhub = new ArrayList<>();
			// make object data by row
			// check if first row is MYSQL
			if (firstCell.getStringCellValue().equals("MYSQL")) {
				while (iterator.hasNext()) {
					Row currentRow = iterator.next();
					MySqlDoc m = new MySqlDoc();
					m.setConnectionType(currentRow.getCell(0).getStringCellValue());
					m.setCreatedBy(currentRow.getCell(1).getStringCellValue());
					m.setDatabaseName(currentRow.getCell(2).getStringCellValue());
					m.setDatabasePassword(currentRow.getCell(3).getStringCellValue());
					m.setDatabaseTable(currentRow.getCell(4).getStringCellValue());
					m.setDatabaseUser(currentRow.getCell(5).getStringCellValue());
					m.setDateCreated(Util.convertStringtoList(currentRow.getCell(6).getStringCellValue()));
					m.setDateUpdated(Util.convertStringtoList(currentRow.getCell(7).getStringCellValue()));
					m.setFields(Util.convertStringtoList(currentRow.getCell(8).getStringCellValue()));
					m.setIdentifierKey(currentRow.getCell(9).getStringCellValue());
					m.setPort(Integer.parseInt(currentRow.getCell(10).getStringCellValue()));
					m.setServerAddress(currentRow.getCell(11).getStringCellValue());
					m.setType(currentRow.getCell(12).getStringCellValue());
					m.setUpdatedBy(currentRow.getCell(13).getStringCellValue());
					m.setName(currentRow.getCell(14).getStringCellValue());
					m.setChannels(Util.convertStringtoList(currentRow.getCell(15).getStringCellValue()));
					// can check connection MYSQL before create
					// if (ConnectionUtil.getMysqlConnectionNotEncode(m) != null) {
					mySqlService.create(m, bucketName);
					mes = "create MYSQL successs";
					// }
					// lstOfMySQL.add(m);
				}
				// check if first row is OHUB-API
			} else if (firstCell.getStringCellValue().equals("OHUB-API")) {
				while (iterator.hasNext()) {
					Row currentRow = iterator.next();
					OhubDoc o = new OhubDoc();
					o.setConnectionType(currentRow.getCell(0).getStringCellValue());
					o.setCreatedBy(currentRow.getCell(1).getStringCellValue());
					o.setDateUpdated(Util.convertStringtoList(currentRow.getCell(2).getStringCellValue()));
					o.setFields(Util.convertStringtoList(currentRow.getCell(3).getStringCellValue()));
					o.setIdentifierKey(currentRow.getCell(4).getStringCellValue());
					o.setLink(currentRow.getCell(5).getStringCellValue());
					o.setMethod(currentRow.getCell(6).getStringCellValue());
					o.setName(currentRow.getCell(7).getStringCellValue());
					o.setOtherParameters(Util.convertStringToMap(currentRow.getCell(8).getStringCellValue()));
					o.setParameters(Util.convertStringToMap(currentRow.getCell(9).getStringCellValue()));
					o.setType(currentRow.getCell(10).getStringCellValue());
					o.setUpdatedBy(currentRow.getCell(11).getStringCellValue());
					o.setChannels(Util.convertStringtoList(currentRow.getCell(12).getStringCellValue()));
					o.setCredentials(Util.convertStringToMap(currentRow.getCell(13).getStringCellValue()));
					// can check connection OHUB-API before create
					// if(Util.readRestfulAPIDemoGETmethod(o) != null) {
					// ohubService.create(o, bucketName);
					// }
					ohubService.create(o, bucketName);
					// lstOfOhub.add(o);
					mes = "create OHUB-API successs";
				}
			} else {
				System.out.println("Could not read file");
			}
			excelFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			mes = "file not found";
		} catch (IOException e) {
			e.printStackTrace();
			mes = "error exception";
		}
		return mes;
	}
}
