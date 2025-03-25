package guru.qa.photocatalog.controller.error;

import java.util.List;
import java.util.Map;

public class ApiError {
    private final String apiVersion;
    private final Error error;

    public ApiError(String apiVersion, Error error) {
        this.apiVersion = apiVersion;
        this.error = error;
    }

    public ApiError(String apiVersion,
                    String code,
                    String message,
                    String domain,
                    String reason) {
        this.apiVersion = apiVersion;
        this.error = new Error(
                code,
                message,
                List.of(
                        new ErrorItem(
                                domain,
                                reason,
                                message
                        )
                )
        );
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public Error getError() {
        return error;
    }

    public record Error(String code, String message, List<ErrorItem> errors) {
    }

    public record ErrorItem(String domain, String reason, String message) {
    }

    public static ApiError fromAttributesMap(String apiversion, Map<String, Object> attributesMap) {
        return new ApiError(
                apiversion,
                ((Integer) attributesMap.get("status")).toString(),
                (String) attributesMap.getOrDefault("error", "No message found"),
                (String) attributesMap.getOrDefault("domain", "No path found"),
                (String) attributesMap.getOrDefault("error", "No message found")
        );
    }

    public Map<String, Object> toAttributesMap() {
        return Map.of(
                "apiversion", apiVersion,
                "error", error
        );
    }
}
