package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_dept")
public class Department {
    @Id
    // 값을 발동시키는 옵션 // id라는 컬럼의 전략을 발생시키는
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private long id;

    @Column(name = "dept_name", nullable = false)
    private String name;

}
