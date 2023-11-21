package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;

@Setter @Getter
@ToString
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

    @ManyToOne // emp M: 1
    @JoinColumn(name = "dept_id") //fk 값 셋팅
    private Department department; // 사원은 부서에 속함

    // fk갖고있는 entity가 연관관계의주인



}
