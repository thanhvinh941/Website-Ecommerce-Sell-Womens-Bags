package com.nhom2.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.nhom2.admin.AbstractExporter;
import com.nhom2.common.entity.Customer;

public class CustomerCSVExport extends AbstractExporter{
	
	public void export(List<Customer> list, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","customers");

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "ID","Email", "Họ", "Tên", "Số điện thoại", "Giới tính", "Ngày Sinh"};
		String[] fieldMapping = { "id", "email", "firstname", "lastname", "phoneNumber", "gioiTinh", "ngaySinh"};
			
		csvBeanWriter.writeHeader(csvHeader);

		for (Customer c : list) {
			csvBeanWriter.write(c, fieldMapping);
		}

		csvBeanWriter.close();
	}
}
