package jpa.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import jpa.board.common.AttachDownloadView;
import jpa.board.repository.FileRepository;
import jpa.board.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/* 파일 controller */
@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileRepository fileRepository;

    /** 멀티파일 업로드 */
    @PostMapping(value={"/file-upload"})
    @ResponseBody
    public Map<String, Object> fileUpload(
            @RequestParam(value = "article_file", required = false) List<MultipartFile> multipartFile
            , HttpServletRequest request) throws IOException {

        log.info("파일 컨트롤러 진입");
        return fileService.uploadFile(request, multipartFile);
    }

    /** 파일 다운로드
     * @throws UnsupportedEncodingException */
    @GetMapping(value = {"/file-download/{fileIdx}"})
    @ResponseBody
    public void downloadFile(HttpServletResponse res, @PathVariable Long fileIdx) throws UnsupportedEncodingException {

        //파일 조회
        jpa.board.entity.File fileInfo = fileRepository.findByFileIdx(fileIdx);

        //파일 경로
        Path saveFilePath = Paths.get(fileInfo.getLogiPath() + File.separator + fileInfo.getLogiNm());

        //해당 경로에 파일이 없으면
        if(!saveFilePath.toFile().exists()) {
            throw new RuntimeException("file not found");
        }

        res.setHeader("Content-Disposition", "attachment; filename=\"" +  URLEncoder.encode((String) fileInfo.getOrigNm(), "UTF-8") + "\";");
        res.setHeader("Content-Transfer-Encoding", "binary");
        res.setHeader("Content-Type", "application/download; utf-8");
        res.setHeader("Pragma", "no-cache;");
        res.setHeader("Expires", "-1;");

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(saveFilePath.toFile());
            FileCopyUtils.copy(fis, res.getOutputStream());
            res.getOutputStream().flush();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                fis.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    //Controller
    //첨부파일 image 보여주는 api
    @GetMapping(value = {"/api/img/print/{idx}"})
    public ModelAndView getPublicImage(@PathVariable("idx") Long idx, HttpServletResponse res) {
        System.out.println("타니?");
        jpa.board.entity.File file = fileRepository.findByFileIdx(idx);
        System.out.println(file.getLogiPath()+file.getLogiNm());
        File initialFile = new File(file.getLogiPath()+file.getLogiNm());

        ModelAndView mav = new ModelAndView();
        try {
            mav.setView(new AttachDownloadView());
            mav.addObject("file", initialFile);
            mav.addObject("filename", "filename");
            res.setContentType( "image/gif" );
        }

        catch (Exception e) {
            e.getStackTrace();
        }
        return mav;
    }
}
