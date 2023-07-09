package com.ecommerce.admin.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ecommerce.admin.AbstractExporter;
import com.ecommerce.common.entity.User;

public class UserExcelExporter extends AbstractExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public UserExcelExporter() {
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("users");
		XSSFRow row = sheet.createRow(0);
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		cellStyle.setFont(font);
		
		createdCell(row, 0, "User ID", cellStyle);
		createdCell(row, 1, "Email", cellStyle);
		createdCell(row, 2, "First Name", cellStyle);
		createdCell(row, 3, "Last Name", cellStyle);
		createdCell(row, 4, "Roles", cellStyle);
		createdCell(row, 5, "Enabled", cellStyle); 
	}

	private void createdCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		
		if(value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		
		cell.setCellStyle(style);
	}

	public void export(List<User> users, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/octet-stream", ".xlsx","users");

		writeHeaderLine();
		writeDataLine(users);
		
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	private void writeDataLine(List<User> users) {
		int rowIndex = 1;
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		cellStyle.setFont(font);
		
		for(User user : users) {
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex=0;	
			createdCell(row, columnIndex++, user.getId(),cellStyle);
			createdCell(row, columnIndex++, user.getEmail(),cellStyle);
			createdCell(row, columnIndex++, user.getFirstname(),cellStyle);
			createdCell(row, columnIndex++, user.getLastname(),cellStyle);
			createdCell(row, columnIndex++, user.getRoles().toString(),cellStyle);
			createdCell(row, columnIndex++, user.isEnabled(),cellStyle);
		}
	}

}
