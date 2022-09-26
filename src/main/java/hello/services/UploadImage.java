package hello.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public interface UploadImage {
	String uploadCardDesignImages(Map<String, MultipartFile> files);
}
