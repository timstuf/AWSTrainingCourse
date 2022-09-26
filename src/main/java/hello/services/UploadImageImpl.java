package hello.services;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UploadImageImpl implements UploadImage {
	private final S3Storage s3Storage;

	@Override
	public String uploadCardDesignImages(Map<String, MultipartFile> files) {
		log.debug("Enter method uploadCardDesignImages. Parameters: files={}", files);
		files.forEach(s3Storage::uploadObject);

		log.debug("Method uploadCardDesignImages was finished.");
		return "Image was successfully uploaded";
	}
}
