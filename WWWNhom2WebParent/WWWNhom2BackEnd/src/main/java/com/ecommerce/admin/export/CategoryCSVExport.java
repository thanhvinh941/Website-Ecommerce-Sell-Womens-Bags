package com.ecommerce.admin.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.ecommerce.admin.AbstractExporter;
import com.ecommerce.common.entity.Category;
import com.ecommerce.common.entity.User;

import net.bytebuddy.implementation.bind.annotation.Super;

public class CategoryCSVExport extends AbstractExporter{

	public void export(List<Category> list, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		super.setResponseHeader(response, "text/csv", ".csv", "categories_");
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "Category ID","Category Name"};
		String[] fieldMapping = { "id","name" };

		csvBeanWriter.writeHeader(csvHeader);

		for (Category category : list) {
			category.setName(category.getName().replace("--"," "));
			csvBeanWriter.write(category, fieldMapping);
		}

		csvBeanWriter.close();
	}

}
