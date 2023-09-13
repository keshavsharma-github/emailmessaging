package com.keshav.emailmessaging.service;

import java.io.IOException;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keshav.emailmessaging.model.EmailDetails;
import com.keshav.emailmessaging.model.SuccessResponse;

/**
 * @author Keshav-Sharma 12/9/23
 */
@Service
public class EmailMessagingService {
	private static Logger LOGGER = LogManager.getLogger(EmailMessagingService.class);
	@Autowired
	private EmailService emailService;
	
	public SuccessResponse uploadAndSendEmail(MultipartFile file) throws IOException {
		SuccessResponse response = new SuccessResponse();
		try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
			int numberOfSheet = 1;
			for (int i = 0; i < numberOfSheet; i++) {
				// Getting the Sheet at index i
				Sheet sheet = workbook.getSheetAt(i);
				LOGGER.info("Sheet Name => {}", sheet.getSheetName());
				// Create a DataFormatter to format and get each cell's value as String
				DataFormatter dataFormatter = new DataFormatter();
				// 1. You can obtain a rowIterator and columnIterator and iterate over them
				LOGGER.info("Iterating over Rows and Columns using Iterator");
				Iterator<Row> rowIterator = sheet.rowIterator();
				boolean skipRow = true;
				String emailTo = null;
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if(skipRow) {
						skipRow = false;
					}else {
						int column = 1;
						EmailDetails emailDTO = new EmailDetails();
						// Now let's iterate over the columns of the current row
						Iterator<Cell> cellIterator = row.cellIterator();
						while (cellIterator.hasNext()) {
							Cell cell = cellIterator.next();
							String cellValue = dataFormatter.formatCellValue(cell);
							if(column == 1) {
								emailTo = cellValue;
							}else {
								mapEmailDTO(emailDTO, cellValue, column);
							}
							++column;
							LOGGER.info("cellValue: {}", cellValue);
						}
						emailService.sendEmail(emailTo, emailDTO);
					}
				}
			}
		}
		response.setMessage("Email Sent to all successfully.");
		response.setSuccess(true);
		return response;
	}

	private void mapEmailDTO(EmailDetails emailDTO, String cellValue, int column) {
		switch (column) {
		case 2:
			emailDTO.setName(cellValue);
			break;
		case 3:
			emailDTO.setSalary(cellValue);
			break;
		default:
			LOGGER.info("Incorrect column: {}", column);
			break;
		}
	}
}