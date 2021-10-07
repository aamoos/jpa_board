package jpa.board.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jpa.board.util.RequestUtil;
import jpa.board.util.ResponseUtil;
import jpa.board.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class AttachDownloadView extends AbstractView {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public AttachDownloadView() {
        setContentType("applicaiton/download;charset=utf-8");
    }

    private void setDownloadFileName(String fileName, HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {

        fileName = StringUtils.encodeFileNm(fileName, RequestUtil.getBrowser(request));
        String headerValue = CacheControl.maxAge(365, TimeUnit.DAYS).getHeaderValue();

        response.setHeader("Cache-Control", headerValue);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
    }

    private void downloadFile(File downloadFile, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(downloadFile);

        try {
            FileCopyUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ioe) {
                logger.warn(ioe.getMessage(), ioe);
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException ioe) {
                logger.warn(ioe.getMessage(), ioe);
            }
        }
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                                           HttpServletResponse response) throws Exception {
        try {
            this.setResponseContentType(request, response);

            File downloadFile = (File) model.get("file");
            String filename = (String) model.get("filename");

            if (logger.isDebugEnabled()) {
                logger.debug("downloadFile {} {}", filename, downloadFile);
            }
            if (downloadFile.exists() == false) {
                ResponseUtil.alertAndBack(response, "파일이 없습니다.");
                return;
            }

            this.setDownloadFileName(filename, request, response);

            response.setContentLength((int) downloadFile.length());
            this.downloadFile(downloadFile, request, response);
        } catch (Exception e) {
            throw e;
        }
    }
}
