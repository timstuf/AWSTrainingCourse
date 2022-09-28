package hello.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize("hasAnyRole('S3')")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String uploadAppSkinImages(MultipartHttpServletRequest request) {
		return uploadImage.uploadCardDesignImages(request.getFileMap());
	}
}
