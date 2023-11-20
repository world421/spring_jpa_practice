package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

import java.rmi.server.ExportException;
import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01_basic.entity.Product.*;
import static com.study.jpa.chap01_basic.entity.Product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트 완료 후 롤백 ~!
@Rollback(false) // true 가 기본값,,
class ProductRepositoryTest {
    @Autowired // test는 autowired로 연결하깅
    ProductRepository productRepository;

    @BeforeEach // 테스트 메서드 전에 실행되는 메서드
    void insertDummyDate(){
        //given
        Product p1 = builder()
                .name("아이폰")
                .category(ELECTRONIC)
                .price(1000000)
                .build();
        Product p2 = builder()
                .name("탕수육")
                .category(FOOD)
                .price(1000000)
                .build();
        Product p3 = builder()
                .name("구두")
                .category(FASHION)
                .price(1000000)
                .build();
        Product p4 = builder()
                .name("쓰레기")
                .category(FOOD)
                .build();
        //when

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);

        //then
    }

    @Test
    @DisplayName("5번째 상품을 데이터베이스에 저장해야한다.")
    void testSave () {
        //given
        Product p5 = builder()
                .name("정장")
                .category(FASHION)
                .price(1000000)
                .build();
        //when
        Product saved = productRepository.save(p5);

        //then
        assertNotNull(saved);
    }
    @Test
    @DisplayName("id가 2번인 데이터를 삭제해아 한다")
    void testRemove() {
        //given
        long id = 2L;
        //when
        productRepository.deleteById(id);
        //then
    }

    @Test
    @DisplayName("상품 전체조회를 하면 상품의 개수가 4개여야 한다.")
    void testFindAll() {
        //given

        //when
        List<Product> products = productRepository.findAll();

        //then
        products.forEach(System.out::println);

        assertEquals(4, products.size());
    }

    @Test
    @DisplayName("3번 상품을 조회하면 상품명이 구두 일 것이다.")
    void testFindOne() {
        //given
        Long id = 3L;
        //when
        // Optional -> null check 자동으로
        Optional<Product> product = productRepository.findById(id);

        //then
        product.ifPresent(p -> {
            assertEquals("구두", p.getName());
        });
        Product foundProduct = product.get();
        assertNotNull(foundProduct);

        System.out.println("foundProduct = " + foundProduct);

    }

    @Test
    @DisplayName("2번 상품의 이름과 가격을 변경해야 한다.")
    void testModify() {
        //given
        Long id = 2L;
        String newName = "짜장면";
        int newPrice = 6000;
        //when
        //jpa 에서는 update 는 따로 메서드로 제공하지 않고,
        //조회한 후 setter로 변경하면 자동으로 update문이 나갑니다.
        // 변경후 save를 호출하세요. 그럼 update가 나갑니다.
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p-> {
            p.setName(newName);
            p.setPrice(newPrice);

            productRepository.save(p);
        });

        //then
        assertTrue(product.isPresent());
        Product p = product.get();
        assertEquals("짜장면", p.getName());

    }
}