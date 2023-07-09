package com.ecommerce.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		Path userPhotosDir = Paths.get("user-photos");
		String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/user-photos/**")
			.addResourceLocations("file:/" + userPhotosPath + "/");
		
		Path categoryImagesDir = Paths.get("../categories-images");
		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/categories-images/**")
			.addResourceLocations("file:/" + categoryImagesPath + "/");
		
		Path brandImagesDir = Paths.get("../brands-images");
		String brandImagesPath = brandImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/brands-images/**")
			.addResourceLocations("file:/" + brandImagesPath + "/");
		
		Path productImagesDir = Paths.get("../products-images");
		String productImagesPath = productImagesDir.toFile().getAbsolutePath();
		registry.addResourceHandler("/products-images/**")
			.addResourceLocations("file:/" + productImagesPath + "/");
	}

	
}
