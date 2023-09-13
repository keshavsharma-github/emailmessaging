package com.keshav.emailmessaging.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keshav.emailmessaging.model.SuccessResponse;
import com.keshav.emailmessaging.service.EmailMessagingService;

//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;

/**
 * @author Keshav-Sharma 12/9/23
 */
@RestController
@RequestMapping("/file")
public class EmailMessagingController {
	@Autowired
	private EmailMessagingService emailMessagingService;

//	@ApiOperation(value = "Upload and Send Email", notes = "Upload a file and send an email with the details mentioned in file.")
//	@ApiResponses({ @ApiResponse(code = 200, message = "Email sent successfully"),
//			@ApiResponse(code = 400, message = "Bad request"),
//			@ApiResponse(code = 500, message = "Internal server error") })
	@PostMapping("/upload")
	public ResponseEntity<SuccessResponse> uploadAndSendEmail(
//			@ApiParam(value = "The file to be uploaded and used to sent as an email", required = true) 
			@RequestParam MultipartFile file)
			throws Exception {
		return ResponseEntity.ok(emailMessagingService.uploadAndSendEmail(file));
	}
}