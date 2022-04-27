package com.nhom2.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.nhom2.admin.AbstractExporter;
import com.nhom2.common.entity.Brand;
import com.nhom2.common.entity.Order;
import com.nhom2.common.entity.Product;

public class OrderCSVExport extends AbstractExporter{
	
	public void export(List<Order> list, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","brands");

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Order Id", "Creted Time", "Total"};
		String[] fieldMapping = { "id", "updatedTime", "total"};

		csvBeanWriter.writeHeader(csvHeader);

		for (Order b : list) {
			csvBeanWriter.write(b, fieldMapping);
		}

		csvBeanWriter.close();
	}
}
