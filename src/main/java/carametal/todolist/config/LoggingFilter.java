package carametal.todolist.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class LoggingFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    var requestWrapper = new ContentCachingRequestWrapper(request);
    var responseWrapper = new ContentCachingResponseWrapper(response);
    filterChain.doFilter(requestWrapper, responseWrapper);
    logRequest(requestWrapper);
    logResponse(responseWrapper);
  }

  private void logRequest(ContentCachingRequestWrapper request) {
    var builder = new StringBuilder();
    builder.append("URI:" + request.getRequestURI() + ";");
    builder.append("Query:" + request.getQueryString() + ";");
    builder.append("RequestBody:" + request.getContentAsString() + ";");
    logger.info(builder.toString());
  }

  private void logResponse(ContentCachingResponseWrapper response) throws IOException {
    logger.info(
        "ResponseBody:" + new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    response.copyBodyToResponse();
  }
}
