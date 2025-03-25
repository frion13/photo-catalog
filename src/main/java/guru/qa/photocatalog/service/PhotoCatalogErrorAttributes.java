package guru.qa.photocatalog.service;

import guru.qa.photocatalog.controller.error.ApiError;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

public class PhotoCatalogErrorAttributes extends DefaultErrorAttributes {

    private final String apiVersion;

    public PhotoCatalogErrorAttributes(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> defaultMap = super.getErrorAttributes(webRequest, options);
        ApiError apiError = ApiError.fromAttributesMap(
                apiVersion,
                defaultMap
        );
        return apiError.toAttributesMap();
    }
}
