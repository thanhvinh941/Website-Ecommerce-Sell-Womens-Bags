package com.ecommerce.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()) {
			Path filePath = uploadPath.resolve(fileName.replace(" ", ""));
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new IOException("Could not save file:  " + fileName,e);
		}
	}
	
	public static void clearDir(String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file ->{
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					}catch (Exception e) {					}
				}
				
			});
		} catch (Exception e) {

		}
	}

	public static void removeDir(String categoryDir) {
		clearDir(categoryDir);
		try {
			Files.delete(Paths.get(categoryDir));
		} catch (Exception e) {
			
			// TODO: handle exception
		}
	}
}
