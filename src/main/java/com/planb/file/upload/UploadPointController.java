package com.planb.file.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
		
	private String DATE_FORMAT =  "yyyyMMdd";

	private static final Logger logger = LoggerFactory.getLogger(UploadPointController.class);
	
	
	@PostMapping("uploadPoint")
	public Map<String, Object> uploadPointFastFile(HttpServletRequest request, @RequestParam(value="file", required=true) MultipartFile file, 
			@RequestParam String fileName, @RequestParam long fileSize, @RequestParam long sliceLen, @RequestParam long sliceN, @RequestParam long lastModified,  @RequestParam long startIndex, 
									  @RequestParam String jobId) {
		
		String originalFilename = file.getOriginalFilename();
		if (!StrUtils.hasText(originalFilename)) {
			return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "上传的文件名不能为空");
		}
		String uploadType = "point";
		
		
//		String fileName = UUID.randomUUID().toString().replaceAll("-", "");
//		String suffix = "";
//		if (originalFilename.contains(".")) {
//			String[] item = originalFilename.split("\\.");
//			suffix = item[item.length - 1];
//		}
//		fileName = fileName + "." + suffix;
		
		String UPLOAD_PATH = "D:/tmp/uploadfile/";
		
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		String filePath = UPLOAD_PATH;
		String realPath = UPLOAD_PATH;
		
		String suffix = ".zip";
		String tempFileName = MD5Encoder.encode((fileName + fileSize + lastModified).getBytes()) + suffix;
		File tempFile = new File(realPath + tempFileName);
		if (!file.isEmpty()) {
			FileOutputStream fos = null;
			BufferedOutputStream stream = null;
			try {
				long fSize = tempFile.length();
				// 文件已经存在
				if (fSize >= fileSize) {
					// 复制
					try {
						Files.copy(tempFile.toPath(), new File(filePath).toPath());
					} catch (IOException e) {
						logger.error("异常",e);
						returnMap.put("status", "-1");
						return returnMap;
					}
					returnMap.put("status", "1");
					returnMap.put("result", fileName);
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
				fos = new FileOutputStream(tempFile, true);
				byte[] bytes = file.getBytes();
				stream = new BufferedOutputStream(fos);
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				logger.error("异常",e);
				returnMap.put("status", "-1");
				return returnMap;
			} finally {
				try {
					if (fos != null) fos.close();
					if (stream != null) stream.close();
				} catch (Exception e) {
					logger.error("异常",e);
				}
			}
		} else {
			// 上传的文件内容为空
			returnMap.put("status", "-2");
			return returnMap;
		}

		if (fileSize <= sliceLen * (sliceN + 1)) {
			// 最后一次, 复制
			// 删除多余的
			File[] filelist = new File(realPath).listFiles();
			for (File f : filelist) {
				if (f.getName().equals(tempFileName)) {
					continue;
				}
				if (f.getName().endsWith(".jar") || f.getName().endsWith(".zip")) {
					f.delete();
				}
			}
			
			// 复制
			try {
				Files.copy(tempFile.toPath(), new File(filePath).toPath());				
				returnMap.put("status", "-1");
				returnMap.put("result", fileName);
				return returnMap;
			} catch (IOException e) {
				logger.error("异常",e);
				returnMap.put("status", "-1");
				return returnMap;
			}
		}
		
		returnMap.put("status", "0");
		return returnMap;
	}
	
}