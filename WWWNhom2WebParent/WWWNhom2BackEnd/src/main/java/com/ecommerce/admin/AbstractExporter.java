package com.ecommerce.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;



public class AbstractExporter {
	public void setResponseHeader(HttpServletResponse response, String contenType, String extension,String prefix) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

		String timeStamp = dateFormat.format(new Date());
		String fileName = prefix + timeStamp + extension;

		response.setContentType(contenType);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

	}
}
