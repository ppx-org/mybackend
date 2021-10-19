package com.planb.file.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;

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

@RestController
@RequestMapping(ModuleConfig.FILE + "upload")
public class UploadPointController extends UploadConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadPointController.class);
	
	
	@PostMapping("uploadPoint")
	public Point uploadPointFastFile(HttpServletRequest request, @RequestParam("file") MultipartFile file, 
			@RequestParam String fileName, @RequestParam long fileSize, @RequestParam long sliceLen, @RequestParam long sliceN,
			@RequestParam long lastModified,  @RequestParam long startIndex) throws Exception {
				
		String suffix = "";
		if (fileName.contains(".")) {
			String[] item = fileName.split("\\.");
			suffix = item[item.length - 1];
		}
		
		if (!new File(UPLOAD_PATH).exists()) {
			String msg = "没有找到上传路径:" + UPLOAD_PATH + "，请检查配置";
			logger.error(msg);
			return Context.setException(ExceptionEnum.SYSYTEM_ERROR, msg); 
		}
		
		String dataStr = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		String filePath = UPLOAD_PATH + TYPE_POINT + "/" + dataStr + "/";
		File pathFile = new File(filePath);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		
		String tempFileName = DigestUtils.md5DigestAsHex((fileName + fileSize + lastModified).getBytes()) + "." + suffix;
		File tempFile = new File(filePath + tempFileName);
		
		try (FileOutputStream fos = new FileOutputStream(tempFile, true);
				BufferedOutputStream stream = new BufferedOutputStream(fos);) {
			long fSize = tempFile.length();
			// 文件已经存在
			if (fSize >= fileSize) {
				return new Point("exist", tempFileName);
			}

			// 文件出异常，不能再续传，请重新上传，startIndex大于0说明已进入过该判断，跳过，补充完整分片内容
			if (fSize % sliceLen != 0 && startIndex <= 0) {
				// 存着分片未完整保存，先把分片补完整
				long posiN = fSize / sliceLen;
				long startPoint = fSize % sliceLen;
				Point point = new Point("fileError", null);
				point.setPosiN(posiN - 1);// 前一个完整分片序列
				point.setStartPoint(startPoint);// 不完整分片已录长度
				return point;
			}

			// 判断是否为正确的断点续传，不正确返回继点位置
			boolean ok = (tempFile.length() == sliceN * sliceLen);
			// startIndex大于0标识当前为补充完整分片内容，跳过
			if (!ok && startIndex <= 0) {
				long posiN = fSize / sliceLen;
				Point point = new Point("partContinue", null);
				point.setPosiN(posiN - 1);
				return point;
			}

			// 继写文件
			byte[] bytes = file.getBytes();
			stream.write(bytes);
			stream.close();
		}
		
		if (fileSize <= sliceLen * (sliceN + 1)) {
			return new Point("finish", tempFileName);
		}
		
		return new Point("continue", tempFileName);
	}
	
}