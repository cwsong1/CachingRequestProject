package com.starlolt.springboot.filter;

import com.starlolt.springboot.filter.wrapper.request.CachingServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class OneLineLoggingFilter implements javax.servlet.Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException  {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

        if(servletRequest.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            CachingServletRequestWrapper requestWrapper = new CachingServletRequestWrapper(httpServletRequest);
            chain.doFilter(requestWrapper, servletResponse);

            log.info("### [doFilter] getJsonRequestData:" + getJsonRequestData(requestWrapper));
        } else { // MediaType.APPLICATION_FORM_URLENCODED_VALUE
            chain.doFilter(servletRequest, servletResponse);

            log.info("### [doFilter] getUrlEncodedRequestData:" + getUrlEncodedRequestData(httpServletRequest));
        }
    }

    //WARN: 이렇게 inputStream을 직접 읽으면  컨트롤러에서 요청 데이터를 다시 읽을 수 없게 된다.
    private String getRequestParams(final ServletRequest servletRequest) throws IOException {
        InputStream inputStream = servletRequest.getInputStream();
        return new String(inputStream.readAllBytes(), servletRequest.getCharacterEncoding());
    }

    //Wrapper를 이용하여 읽으면 컨트롤러에서 요청 데이터를 다시 읽을 수 있다.
    private String getJsonRequestData(final CachingServletRequestWrapper requestWrapper) throws IOException {
        InputStream inputStream = requestWrapper.getInputStream();
        return new String(inputStream.readAllBytes(), requestWrapper.getCharacterEncoding());
    }

    //ContentCachingRequestWrapper 클래스를 이용 - x-www-form-urlencoded 형식 데이터는 해결 가능
    private String getUrlEncodedRequestData(final HttpServletRequest httpServletRequest) throws IOException {
        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(httpServletRequest);

        contentCachingRequestWrapper.getParameterMap(); //caching을 수행하기 위해 꼭 호출해준다.
        return new String(contentCachingRequestWrapper.getContentAsByteArray(), contentCachingRequestWrapper.getCharacterEncoding());
    }
}
