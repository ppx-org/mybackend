package com.planb.file.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
public class UploadPointController {
		
//	private String DATE_FORMAT =  "yyyyMMdd";

	private static final Logger logger = LoggerFactory.getLogger(UploadPointController.class);
	
	
	@PostMapping("uploadPoint")
	public Map<String, Object> uploadPointFastFile(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
			@RequestParam String fileName, @RequestParam long fileSize, @RequestParam long sliceLen, @RequestParam long sliceN,
			@RequestParam long lastModified,  @RequestParam long startIndex) throws Exception {
		
		String originalFilename = file.getOriginalFilename();
		if (!StrUtils.hasText(originalFilename)) {
			return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "上传的文件名不能为空");
		}
//		String uploadType = "point";
		
		
//		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
//		String suffix = "";
//		if (originalFilename.contains(".")) {
//			String[] item = originalFilename.split("\\.");
//			suffix = item[item.length - 1];
//		}
//		fileName = fileName + "." + suffix;
		
		String UPLOAD_PATH = "D:/tmp/uploadfile/";
		if (!new File(UPLOAD_PATH).exists()) {
			String msg = "没有找到上传路径:" + UPLOAD_PATH + "，请检查配置";
			logger.error(msg);
			return Context.setException(ExceptionEnum.SYSYTEM_ERROR, msg); 
		}
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String filePath = UPLOAD_PATH + "/test.zip";
		String realPath = UPLOAD_PATH;
		
		String suffix = ".zip";
		String tempFileName = DigestUtils.md5DigestAsHex((fileName + fileSize + lastModified).getBytes()) + suffix;
		File tempFile = new File(realPath + tempFileName);
		
		// FileOutputStream fos = null;
//		BufferedOutputStream stream = null;
		
		
		try (FileOutputStream fos = new FileOutputStream(tempFile, true); 
				BufferedOutputStream stream = new BufferedOutputStream(fos);) {
			long fSize = tempFile.length();
			// 文件已经存在
			if (fSize >= fileSize) {
				returnMap.put("status", "exist");
				returnMap.put("result", tempFileName);
				return returnMap;
			}

			// 文件出异常，不能再续传，请重新上传，startIndex大于0说明已进入过该判断，跳过，补充完整分片内容
			if (fSize % sliceLen != 0 && startIndex <= 0) {
				returnMap.put("status", "2");
				// kmind_208，存着分片未完整保存，先把分片补完整
				long posiN = fSize / sliceLen;
				long startPoint = fSize % sliceLen;
				returnMap.put("posiN", posiN - 1);// 前一个完整分片序列
				returnMap.put("startPoint", startPoint);// 不完整分片已录长度
				return returnMap;
			}

			// 判断是否为正确的断点续传，不正确返回继点位置
			boolean ok = (tempFile.length() == sliceN * sliceLen);
			// startIndex大于0标识当前为补充完整分片内容，跳过
			if (!ok && startIndex <= 0) {
				long posiN = fSize / sliceLen;
				returnMap.put("status", "3");
				returnMap.put("posiN", posiN - 1);
				return returnMap;
			}

			// 继写文件
			byte[] bytes = file.getBytes();
			stream.write(bytes);
			stream.close();
		}
		
		if (fileSize <= sliceLen * (sliceN + 1)) {
			returnMap.put("status", "finish");
			returnMap.put("result", tempFileName);
			return returnMap;
		}
		
		returnMap.put("status", "continue");
		returnMap.put("result", tempFileName);
		return returnMap;
	}
	
}