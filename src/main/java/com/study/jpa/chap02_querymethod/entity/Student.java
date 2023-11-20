package com.study.jpa.chap02_querymethod.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
@Setter // 실무적 측면에서 setter는 신중하게 선택할 것.
@Getter
            // 여러개 주고싶으면  ( of = {" id " , " name" }) 같아야 같은 객체
@ToString @EqualsAndHashCode(of = "id")// id 만 같으면 같은 객체
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_student")
public class Student {
    @Id
    @Column(name = "stu_id") // 실제 테이블 안의 컬럼명
    // uuid GenericGenerator 만들고, GeneratedValue 에 전달
    @GeneratedValue(generator = "uid")
    @GenericGenerator(strategy = "uuid", name="uid")
    private String id;

    @Column(name = "stu_name", nullable = false)
    private String name;

    private String city;

    private String major;
}
