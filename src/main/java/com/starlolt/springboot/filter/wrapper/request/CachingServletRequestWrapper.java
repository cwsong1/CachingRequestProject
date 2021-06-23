package com.starlolt.springboot.filter.wrapper.request;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CachingServletRequestWrapper extends HttpServletRequestWrapper {
    private final Charset encoding;
    private final byte[] rawData;

    public CachingServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        String characterEncoding = request.getCharacterEncoding();
        if(characterEncoding == null) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(characterEncoding);

        try(InputStream inputStream = request.getInputStream()) {
            this.rawData = inputStream.readAllBytes();
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new ByteArrayServletInputStream(this.rawData);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }
}
