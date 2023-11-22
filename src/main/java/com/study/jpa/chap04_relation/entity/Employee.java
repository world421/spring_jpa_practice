package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;
import java.nio.MappedByteBuffer;

@Setter @Getter
// JPA 연관관계 매핑에서 연관관계 데이터는 toString 에서 제외해야 합니다.
@ToString(exclude = {"department"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "tbl_emp")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id") // 컬럼 이름
    private Long id;
    @Column(name = "emp_name", nullable = false)
    private String name;

    // EAGER : 항상 무조건 조인을 수행.(기본값)
    // LAZY : 필요한 경우에만 조인을 수행 ( 실무)
    @ManyToOne(fetch = FetchType.LAZY) // emp M: 1
    @JoinColumn(name = "dept_id") // fk 값 셋팅
    private Department department; // 사원은 부서에 속함

    // fk갖고있는 entity가 연관관계의 주인
    // 1 : M 에서 M 이 주인


}
