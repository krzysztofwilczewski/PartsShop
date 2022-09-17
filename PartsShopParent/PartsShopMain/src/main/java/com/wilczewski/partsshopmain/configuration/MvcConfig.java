package com.wilczewski.partsshopmain.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        directory("../category-images", registry);
        directory("../brand-images", registry);
        directory("../manufacturer-images", registry);
        directory("../product-images", registry);
        directory("../site-logo", registry);
    }

    private void directory(String pathPattern, ResourceHandlerRegistry registry){
        Path path = Paths.get(pathPattern);
        String absolutePath = path.toFile().getAbsolutePath();

        String logicalPath = pathPattern.replace("../", "") + "/**";

        registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
    }

}
