package jpa.board.service;

import jpa.board.entity.Api;
import jpa.board.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApiService {

    private final ApiRepository apiRepository;

    /**
     * api 리스트 화면
     * @param page
     * @param keyword
     * @return
     */
    public Page<Api> list(int page, String keyword){

        //keyword가 없을경우
        if(keyword.isEmpty()){
            return apiRepository.findAllByUseYn(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "apiIdx")), "Y");
        }

        //keyword가 있을경우
        else{
            return apiRepository.findAllByDomainUrlContainingIgnoreCaseAndUseYn(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "apiIdx")), keyword, "Y");
        }
    }

    /**
     * api 등록
     * @param api
     * @return
     */
    public Long insertApi(Api api){
        //board 게시판 테이블 insert
        Long apiIdx = savePost(api);
        api.setApiIdx(apiIdx);
        return apiIdx;
    }

    /**
     * api 저장
     * @param api
     * @return
     */
    public Long savePost(Api api){
        return apiRepository.save(api).getApiIdx();
    }

    /**
     * api 수정화면
     * @param apiIdx
     * @return
     */
    public Api getDetail(Long apiIdx){
        Optional<Api> optional = apiRepository.findById(apiIdx);
        if(optional.isPresent()){
            Api api = optional.get();
            return api;
        }
        else{
            Api api = new Api();
            return api;
        }
    }

    public void deleteApi(List<String> apiIdxArray){
        for(int i=0; i<apiIdxArray.size(); i++) {
            String apiIdx = apiIdxArray.get(i);
            Optional<Api> optional = apiRepository.findById(Long.parseLong(apiIdx));
            if(optional.isPresent()){
                Api api = optional.get();
                api.setUseYn("N");
                apiRepository.save(api);
            }
            else{
                throw new NullPointerException();
            }
        }
    }

    /**
     * api 사용가능한 도메인인지 check후 사용가능하면 데이터 리턴
     * @param api
     * @return
     */
    public Map<String, Object> checkApiDomain(Api api){
        Map<String, Object> result = new HashMap<String, Object>();
        log.info("domainUrl={}, apiKey={}", api.getDomainUrl(), api.getApiKey());
        Api checkApi = apiRepository.findByDomainUrlAndApiKeyAndUseYn(api.getDomainUrl(), api.getApiKey(), "Y");
        //해당하는 항목이 없음
        if(checkApi != null){
            result.put("resultCode", "Success");
            result.put("resultMsg", "해당 도메인은 api가 사용이 가능합니다.");
        }

        else{
            result.put("resultCode", "Fail");
            result.put("resultMsg", "해당 도메인은 api가 사용이 불가능합니다.");
        }

        return result;
    }

}
