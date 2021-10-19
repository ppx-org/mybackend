package com.planb.file.upload;

import org.springframework.beans.factory.annotation.Value;

public class UploadConfig {
	
	@Value("${my.upload.path}")
	String UPLOAD_PATH;
	
	String TYPE_TEST_IMG = "test_img"; 
	
	String TYPE_POINT = "point"; 
	
	String DATE_FORMAT =  "yyyyMMdd";
	
	
}
