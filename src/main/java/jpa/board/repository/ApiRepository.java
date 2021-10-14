package jpa.board.repository;

import jpa.board.entity.Api;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRepository extends CrudRepository<Api, Long> {

    //useYn 조건 find
    Page<Api> findAllByUseYn(Pageable pageable, String useYn);

    //useYn boardTitle 조건
    Page<Api> findAllByDomainUrlContainingIgnoreCaseAndUseYn(Pageable pageable, String boardTitle, String useYn);

    //api를 사용할수 있는 도메인인지 체크
    Api findByDomainUrlAndApiKeyAndUseYn(String domainUrl, String apiKey, String useYn);

}
