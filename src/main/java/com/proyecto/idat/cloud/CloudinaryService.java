package com.proyecto.idat.cloud;

import java.util.Map;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.proyecto.idat.exception.ResponseException;

@Service
public class CloudinaryService {
	Cloudinary cloudinary;
	
	private Map<String, String> valueMapping = new HashMap<>();

	public CloudinaryService() {
		valueMapping.put("cloud_name", CloudinaryCredentials.cloud_name);
		valueMapping.put("api_key", CloudinaryCredentials.api_key);
		valueMapping.put("api_secret",CloudinaryCredentials.api_secret);
		cloudinary = new Cloudinary(valueMapping);
	}
	
	
	public Map<?, ?> uploadFile(MultipartFile multipartFile) throws IOException {
		BufferedImage bImage = ImageIO.read(multipartFile.getInputStream());
		if(bImage == null) {
			 throw new ResponseException("Imagen not valid", HttpStatus.BAD_REQUEST);
		}
		
		File file  = convertToFile(multipartFile);
		Map<?,?>  resultMap  = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
		file.delete();
		return resultMap;
	}
	
	public Map<?, ?>   deleteFile(String id) throws IOException{
		Map<?, ?>  resultMap = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
		return resultMap;
	}
	

	private File convertToFile(MultipartFile multipartFile) throws IOException{
		File file = new File(multipartFile.getOriginalFilename());
		FileOutputStream fOutputStream = new FileOutputStream(file);
		fOutputStream.write(multipartFile.getBytes());
		fOutputStream.close();
		return file;
	}
}