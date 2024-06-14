package api_implementations.endpoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UriBuilder {

    private final List<String> uriPath = new ArrayList<>();

    public UriBuilder() {
        uriPath.add(UriPaths.ROOT_DEFAULT);
    }

    public UriBuilder(final String customRootPath) {
        uriPath.add(customRootPath);
    }

    public UriBuilder addPath(Object path) {
        uriPath.add(path.toString());

        return this;
    }

    public UriBuilder addPaths(Object... paths) {
        Arrays.stream(paths).forEach(path -> uriPath.add(path.toString()));

        return this;
    }

    public UriBuilder addPathParam(Object pathParam) {
        uriPath.add(String.format("{%s}", pathParam));

        return this;
    }

    public UriBuilder addPathParams(Object... pathParams) {
        Arrays.stream(pathParams).forEach(path -> uriPath.add(String.format("{%s}", path)));

        return this;
    }

    public String buildPath() {
        StringBuffer result = new StringBuffer();
        uriPath.forEach(path -> result.append("/").append(path));
        if (result.toString().startsWith("/")) {
            result.deleteCharAt(0);
        }

        return result.toString();
    }
}
