package com.study.jpa.chap01_basic.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값을 발동시키는 옵션 // id라는 컬럼의 전략을 발생시키는
    //변수명 idㄹ 하고 필드명 pro_id 하고 싶을때
    @Column(name = "prod_id")
    private long id;
    @Column(name= "prod_name", nullable = false, unique = true, length = 30)
    private String name;

    private int price;
    // private int price = 0 ; default 값 지정

    @Enumerated(EnumType.STRING)
    private Category category;
    @CreationTimestamp // Default :  current_timeStamp (= oracle 의 sysDate 랑 동일)
    @Column(updatable = false) // updatable : 수정의 여부 등록 시간 결정 되면 수정이 안되게 함 !
    private LocalDateTime createDate;
    @UpdateTimestamp // update될ㄸ ㅐ자동 으로 값 setting
    private LocalDateTime updateDate;

    public enum Category {
        FOOD, FASHION, ELECTRONIC

        // 0,1.2 로 인식하겠다
    }
}
