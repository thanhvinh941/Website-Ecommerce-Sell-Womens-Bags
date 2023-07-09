package com.ecommerce.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.ecommerce.admin.AbstractExporter;
import com.ecommerce.common.entity.Product;

public class ProductCSVExport extends AbstractExporter{
	
	public void export(List<Product> list, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","products");

		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Product Id", "Name", "Alias", "Price", "Discount", "In Stock", "Created Time", "Updated Time"};
		String[] fieldMapping = { "id", "name", "alias", "price", "discountPercent", "inStock", "createdTime", "updatedTime" };

		csvBeanWriter.writeHeader(csvHeader);

		for (Product p : list) {
			csvBeanWriter.write(p, fieldMapping);
		}

		csvBeanWriter.close();
	}
}
