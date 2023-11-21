package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    // 엔터티들을 관리하는 역할을 수행하는 클래스(영속성컨텍스트를 관리함, Spring Data JAP에서만 사용하는게 x )
    // 영속성 컨텍스의 내의 내용들을  DB에 반영시키거나 비워내거나 수명을 관리할 수 있는 객체.

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("특정부서를 조회하면 해당 부서원들도 함께 조회되어야 한다.")
    void testFindDept() {
        //given
        Long id = 2L;
        //when
        Department department = departmentRepository.findById(id)
                .orElseThrow();
        //then
        System.out.println("\n\n\n");
        System.out.println("department = " + department);
        System.out.println("department.getEmployees = " + department.getEmployees());
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("Lazy 로딩과 Eager 로딩의 차이")
    void testLazyEager() {
        // 3번 사원 조회하고 싶은뎅 굳이 부서정보는 필요없당
        //given
        Long id = 3L;

        //when
        Employee employee = employeeRepository.findById(id).orElseThrow();

        //then
        System.out.println("\n\n\n");
        System.out.println("employee = " + employee);
        System.out.println("dept_name = " + employee.getDepartment().getName());
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("양방향 연관관계에서 연관데이터의 수정")
    void testChangeDept() {
        // 1번 사원의 부서를 1번부서에서 2번 부서로 변경해야 한다.
        //given
        Employee foundEmp = employeeRepository.findById(1L).orElseThrow();

        Department newDept = departmentRepository.findById(2L).orElseThrow();

        // 2번 -> 사원의 부서 정보를 업데이트 하면서, 부서에 대한 정보도 같이 업데이트.
        foundEmp.setDepartment(newDept); // 3번 사원  1번 부서로 변경
        newDept.getEmployees().add(foundEmp);
        // 아직 db는 반영되기 전

        employeeRepository.save(foundEmp);

        //1번 -> 변경 감지 (더티 체크) 후 변경된 내용을 즉시 반영하는 역할
        //entityManager.flush(); // dbㄹ 밀어내기
        //entityManager.clear(); // 영속성 컨텍스트 비우기 ( 비우지 않으면 컨텍스트 내의 정보를 참조하려함 )
        // =================================
        /*
             트랜잭션 끝나고 실행하려고 예약 함
              #1. save - commit - select
              #2. 변경된 내용 반영을 강제적으로 밀어넣자
              2번 부서 안에있는 employee 내용을 직접 수정해주자
         */
        //==================================
        //when

        //2번 부서 정볼를 조회해서 모든 사원을 보겠다.
        Department foundDept = departmentRepository.findById(2L).orElseThrow();

        System.out.println("\n\n\n");
        foundDept.getEmployees().forEach(System.out::println);
        System.out.println("\n\n\n");
        //then
    }

    @Test
    @DisplayName("N+1 문제 발생 예시")
    void testNPlus1Ex() {
        //given
        List<Department> departments = departmentRepository.findAll();

        //when
        departments.forEach(dept->{
            System.out.println("\n\n ======== 사원리스트 =========");
            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");
        });

        //then
    }

    @Test
    @DisplayName("N+1 문제 발생 예시")
    void testNPlus1Solution() {
        List<Department> departments = departmentRepository.findAllIncludesEmployees();

        departments.forEach(dept->{
            System.out.println("\n\n ======== 사원리스트 =========");
            List<Employee> employees = dept.getEmployees();
            System.out.println(employees);

            System.out.println("\n\n");
        });

    }
}