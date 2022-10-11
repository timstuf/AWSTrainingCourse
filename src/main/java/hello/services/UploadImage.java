package hello.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImage {
	String uploadCardDesignImages(Map<String, MultipartFile> files);
}
