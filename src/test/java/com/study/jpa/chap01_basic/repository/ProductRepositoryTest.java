package com.study.jpa.chap01_basic.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
@SpringBootTest
@Transactional // 테스트 완료 후 롤백 ~!
@Rollback(false) // true 가 기본값,,
class ProductRepositoryTest {
    @Autowired // test는 autowired로 연결하깅
    ProductRepository productRepository;

}