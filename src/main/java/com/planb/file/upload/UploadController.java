package com.planb.file.upload;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.Context;
import com.planb.common.util.StrUtils;

@RestController
@RequestMapping(ModuleConfig.FILE + "upload")
public class UploadController {
	
	private String TEST_IMG_PAHT = "test_img"; 
	
	private String DATE_FORMAT =  "yyyyMMdd";

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
	
	@PostMapping("uploadTestImg")
	public String uploadTestImg(@RequestParam("file") MultipartFile file) throws Exception {
		return upload(TEST_IMG_PAHT, file);
	}


	private String upload(String uploadType, @RequestParam(value="file", required=true) MultipartFile file) throws Exception {
		
		String originalFilename = file.getOriginalFilename();
		if (!StrUtils.hasText(originalFilename)) {
			return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "上传的文件名不能为空");
		}
		
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		String suffix = "";
		if (originalFilename.contains(".")) {
			String[] item = originalFilename.split("\\.");
			suffix = item[item.length - 1];
		}
		fileName = fileName + "." + suffix;
		
		String dataStr = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		String UPLOAD_PATH = "D:/tmp/uploadfile/";
		if (!new File(UPLOAD_PATH).exists()) {
			String msg = "没有找到上传路径:" + UPLOAD_PATH + "，请检查配置";
			logger.error(msg);
			return Context.setException(ExceptionEnum.SYSYTEM_ERROR, msg); 
		}
		String filePath = UPLOAD_PATH + uploadType + "/" + dataStr + "/";
		File pathFile = new File(filePath);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		
		File saveFile = new File(filePath + "/" + fileName);
		file.transferTo(saveFile);
		
		return uploadType + "/" + dataStr + "/" + fileName;
	}
	
	public static void main(String[] args) {
		String passwordMd5 = DigestUtils.md5DigestAsHex("abc3fg".getBytes());
		System.out.println("xxxxxxxxxx001:" + passwordMd5);
	}
	
}