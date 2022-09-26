package hello.controller;

import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import hello.services.UploadImage;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(path = "/api/images")
@RequiredArgsConstructor
public class ImageUploadController {

private final UploadImage uploadImage;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadAppSkinImages(MultipartHttpServletRequest request) {
		return uploadImage.uploadCardDesignImages(request.getFileMap());
	}
}
