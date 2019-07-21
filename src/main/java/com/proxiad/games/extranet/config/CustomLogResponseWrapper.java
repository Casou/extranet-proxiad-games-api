package com.proxiad.games.extranet.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.TeeOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

@Slf4j
public class CustomLogResponseWrapper extends HttpServletResponseWrapper {

    TeeServletOutputStream teeStream;

    PrintWriter teeWriter;

    ByteArrayOutputStream bos;

    public CustomLogResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    public String getContent() throws IOException {
        return bos == null ? "" : bos.toString();
    }

    @Override
    public PrintWriter getWriter() throws IOException {

        if (this.teeWriter == null) {
            this.teeWriter = new PrintWriter(new OutputStreamWriter(getOutputStream()));
        }
        return this.teeWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {

        if (teeStream == null) {
            bos = new ByteArrayOutputStream();
            teeStream = new TeeServletOutputStream(getResponse().getOutputStream(), bos);
        }
        return teeStream;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (teeStream != null) {
            teeStream.flush();
            System.err.println("teeStream flush");
        }
        if (this.teeWriter != null) {
            this.teeWriter.flush();
            System.err.println("teeWriter flush");
        }
    }

    public class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream(OutputStream one, OutputStream two) {
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
            this.targetStream.write(arg0);
        }

        public void flush() throws IOException {
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
            return bos != null;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}
