package api_implementations.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.commons.lang3.Range;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestResponseFilter implements Filter {

    private final static Range<Integer> RANGE_SUCCESS = Range.between(200, 299);

    private final static Range<Integer> RANGE_ERROR = Range.between(400, 599);

    private final Logger log = LogManager.getLogger(RequestResponseFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {

        Response response = filterContext.next(requestSpec, responseSpec);

        int responseCode = response.statusCode();
        String requestMethod = requestSpec.getMethod();
        String requestURI = requestSpec.getURI();
        String responseBody = response.getBody().print();

        if (RANGE_SUCCESS.contains(responseCode)) {
            //Response status 2xx
            log.info("Request: {} {} {}", requestMethod, requestURI, responseCode);
            log.error("Response: {}", responseBody);
        } else if (RANGE_ERROR.contains(responseCode)) {
            //Response status 4xx or 5xx
            log.info("Request: {} {} {}", requestMethod, requestURI, responseCode);
            log.error("Response: {}", responseBody);
        } else {
            //Response status 1xx or 3xx
            log.info("Request: {} {} {}", requestMethod, requestURI, responseCode);
            log.error("Response: {}", responseBody);
        }

        return response;
    }
}
