package com.nhom2.admin.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.nhom2.admin.AbstractExporter;
import com.nhom2.common.entity.User;

public class UserCsvExporter extends AbstractExporter{

	public void export(List<User> lisUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","users");

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "User Id", "Email", "First Name", "Last Name", "Roles", "Enabled" };
		String[] fieldMapping = { "id", "email", "firstname", "lastname", "roles", "enabled" };

		csvBeanWriter.writeHeader(csvHeader);

		for (User user : lisUsers) {
			csvBeanWriter.write(user, fieldMapping);
		}

		csvBeanWriter.close();
	}
}
