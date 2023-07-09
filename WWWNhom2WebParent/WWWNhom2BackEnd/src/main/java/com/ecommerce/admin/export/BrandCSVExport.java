package com.ecommerce.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.ecommerce.admin.AbstractExporter;
import com.ecommerce.common.entity.Brand;
import com.ecommerce.common.entity.Product;

public class BrandCSVExport extends AbstractExporter{
	
	public void export(List<Brand> list, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","brands");

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Brand Id", "Name"};
		String[] fieldMapping = { "id", "name"};

		csvBeanWriter.writeHeader(csvHeader);

		for (Brand b : list) {
			csvBeanWriter.write(b, fieldMapping);
		}

		csvBeanWriter.close();
	}
}
