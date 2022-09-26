package hello.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


import hello.config.ImageUploadProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3StorageImpl implements S3Storage {

	private final AmazonS3 s3;
	private final ImageUploadProperties properties;

	@Autowired
	public S3StorageImpl(AmazonS3 amazonS3, ImageUploadProperties properties) {
		this.s3 = amazonS3;
		this.properties = properties;
	}

	@Override
	public String uploadObject(String keyName, MultipartFile mFile) {
		log.debug("Enter method uploadObject. Parameters: keyName={}", keyName);
		String newKeyName = addExtensionToFileName(keyName, mFile);
		File file = convertMultiPartToFile(mFile);
		String bucketName = properties.getS3Bucket();
		if(!s3.doesBucketExistV2(bucketName)) {
			log.error("Method uploadObject was finished. Result: exception");
			throw new RuntimeException("Bucket not found");
		}
		try {
			PutObjectRequest request = new PutObjectRequest(bucketName, newKeyName, file);
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(mFile.getContentType());
			request.setMetadata(metadata);
			s3.putObject(request);
		} catch(AmazonServiceException e) {
			log.error("Method uploadObject was finished. Result: exception {}", e.getMessage());
			throw new RuntimeException(e);
		}
		log.debug("Method uploadObject was finished. Result = {}", newKeyName);
		return newKeyName;
	}

	private String addExtensionToFileName(String fileName, MultipartFile file) {
		if(MediaType.IMAGE_PNG_VALUE.equals(file.getContentType())) {
			return fileName + ".png";
		} else if(MediaType.IMAGE_GIF_VALUE.equals(file.getContentType())) {
			return fileName + ".gif";
		} else if(MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())) {
			return fileName + ".jpeg";
		} else {
			log.error("Method addExtensionToFileName was finished. Result: exception - file type is not PNG or GIF.");
			throw new RuntimeException("Another extension");
		}
	}

	private File convertMultiPartToFile(MultipartFile file) {
		log.debug("Enter method convertMultiPartToFile. Parameters: file={}", file);
		if(Objects.isNull(file)) {
			throw new RuntimeException("File is null");
		}
		File convertedFile = new File("default");
		try(FileOutputStream fos = new FileOutputStream(convertedFile)) {
			fos.write(file.getBytes());
		} catch(IOException e) {
			log.error("Method convertMultiPartToFile was finished. Result: exception {}", e.getMessage());
			throw new RuntimeException(e);
		}
		log.debug("Method convertMultiPartToFile was finished. Result = {}", convertedFile);
		return convertedFile;
	}

}
