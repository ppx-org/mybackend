package com.planb.file.upload;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.fasterxml.jackson.databind.ObjectWriter.GeneratorSettings;
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import com.planb.common.conf.ExceptionEnum;
import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.Context;
import com.planb.common.util.StrUtils;

@RestController
@RequestMapping(ModuleConfig.FILE + "upload")
public class UploadController {

	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@PostMapping("uploadImg")
	public String uploadImg(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		if (file == null) {
			return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "上传的图片不能为空");
		}
		
		String originalFilename = file.getOriginalFilename();
		if (!StrUtils.hasText(originalFilename)) {
			return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "上传的文件名不能为空");
		}
		
		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
		
		File saveFile = new File("D:\\tmp\\uploadfile\\" + fileName);
		file.transferTo(saveFile);
		
		return "";
	}
	
}