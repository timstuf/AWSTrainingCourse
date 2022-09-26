package hello.services;

import org.springframework.web.multipart.MultipartFile;

public interface S3Storage {

	/**
	 * Converts object to File and uploads it to specified location
	 *
	 * @param keyName key of the uploading object
	 * @param file object to be upload
	 * @return new key name with version
	 */
	String uploadObject(String keyName, MultipartFile file);

}
