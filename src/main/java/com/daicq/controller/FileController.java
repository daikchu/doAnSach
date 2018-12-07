package com.daicq.controller;

import com.daicq.service.BucketService;
import com.daicq.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * File upload/ download Controller
 *
 * @author Luong To Thanh
 */
@RestController
public class FileController {
	@Autowired
	BucketService bucketService;
	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private ServletContext servletContext;

	/*
	 * upload single file
	 * 
	 * @param: file return: filename, download url, type, size
	 */
/*	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		// set/get file name
		String fileName = fileStorageService.storeFile(file);
		// Set file download
		if (fileName == null || fileName == "File is not excel") {
			String fileDownloadUri = null;
			return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		} else {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(fileName).toUriString();
			// return
			return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
		}
	}*/

	/**
	 * upload multiple file
	 * 
	 * @param: file return: list filename, download url, type, size
	 */
/*	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}*/

	/**
	 * download content file
	 * 
	 * @param: file return: content
	 * @throws IOException
	 */
/*	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws IOException {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + FileUtil.fileName(resource.getFilename()) + "\"").body(resource);
	}*/

	/**
	 * @param fileName
	 * @return mess
	 */
	@RequestMapping(value = "/file/readExcelAndCreateSource/{fileName}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String readExcel(@PathVariable String fileName) {
		String bucketName = bucketService.getBucketName();
		String mes = "";
		mes = fileStorageService.readExcel(bucketName, fileName);
		return mes;
	}

	/*@RequestMapping(value = "/export", method = RequestMethod.GET)
	public ModelAndView exportRevisionsToExcel(ModelAndView modelAndView) {
		List<JSONArray> revList = (List<JSONArray>) servletContext.getAttribute("revisionsResponse");
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh_mm_ss");
		//String excelFileName = "Revisions_" + formatter.format(LocalDateTime.now()) + ".xlsx";
		SXSSFWorkbook wb = (new ExportRevisionResponseExcel()).exportExcel(new String[] { "REVISION ID", "CREATION DATE" }, revList);
		try {
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			wb.write(outByteStream);
			//byte[] outArray = outByteStream.toByteArray();
			//response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			//response.setContentLength(outArray.length);
			//response.setHeader("Expires:", "0"); // eliminates browser caching
			//response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);
			//OutputStream outStream = response.getOutputStream();
			//outStream.write(outArray);
			//outStream.flush();
			wb.dispose();
			((Closeable) wb).close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelAndView;
	}*/
}
