package jpa.board.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import jpa.board.entity.BoardFile;
import jpa.board.repository.BoardFileRepository;
import jpa.board.repository.FileRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("fileService")
@RequiredArgsConstructor
public class FileService {

    @Autowired Environment env;
    @Autowired FileService fileService;
    @Autowired FileRepository fileRepository;

    private final String UPLOAD_FILE_PATH = "common.public_upload_local_path";

    /** 파일 업로드 */
    public Map<String, Object> uploadFile(HttpServletRequest request, List<MultipartFile> multipartFile) throws IOException{

        //결과 Map
        Map<String, Object> result = new HashMap<String, Object>();

        //파일 시퀀스 리스트
        List<Long> fileIds = new ArrayList<Long>();

        String _filePath = String.valueOf(request.getParameter("filePath")).equals("null") ? env.getProperty(UPLOAD_FILE_PATH) : env.getProperty(UPLOAD_FILE_PATH)+String.valueOf(request.getParameter("filePath")+"/");

        try {
            if(multipartFile != null){
                // 파일이 있을때 탄다.
                if(multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {

                    for(MultipartFile file1 : multipartFile) {

                        String originalFileName = file1.getOriginalFilename();	//오리지날 파일명
                        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자
                        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

                        File targetFile = new File(_filePath + savedFileName);

                        //초기값으로 fail 설정
                        result.put("result", "FAIL");
                        jpa.board.entity.File file = new jpa.board.entity.File(originalFileName, savedFileName, _filePath, extension, file1.getSize(), file1.getContentType());

                        //파일 insert
                        Long fileId = fileService.insertFile(file);
                        log.info("fileId={}", fileId);

                        try {
                            InputStream fileStream = file1.getInputStream();
                            FileUtils.copyInputStreamToFile(fileStream, targetFile); //파일 저장
                            //배열에 담기
                            fileIds.add(fileId);
                            result.put("fileIdxs", fileIds.toString());
                            result.put("result", "OK");
                        } catch (Exception e) {
                            //파일삭제
                            FileUtils.deleteQuietly(targetFile);	//저장된 현재 파일 삭제
                            e.printStackTrace();
                            result.put("result", "FAIL");
                            break;
                        }
                    }
                }
            }

            // 파일 아무것도 첨부 안했을때 탄다.(게시판일때, 업로드 없이 글을 등록하는경우)
            else {
                result.put("result", "OK");
            }
        }catch(Exception e){
            e.printStackTrace();
            result.put("result", "FAIL");
        }

        return result;
    }

    /** 파일 저장 db */
    @Transactional
    public Long insertFile(jpa.board.entity.File file) {
        return fileRepository.save(file).getFileIdx();
    }
}
