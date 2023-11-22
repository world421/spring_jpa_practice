package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Tag(name="post API", description = "게시물 조회, 등록 및 수정, 삭제 api 입니다.")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {
    // 리소스: 게시물 (Post)  // 설계단계에서 약속하깅
    /*
        게시물 목록 조회: /posts            - GET, param:(page,size)
        게시물 개별 조회: /posts/{id}       - GET
        게시물 등록:     /posts            - POST, payload: (title, writer, content, hashTags)
        게시물 수정:     /posts            - PUT, PATCH , payload : (title, content, postNo)
        게시물 삭제:     /posts/{id}       - DELETE

        # PUT 은 새로 갈아끼우는 느낌
        # PATCH 소소하게 (?) 바꾸는거
       리턴되는값은 이거야 API 명세서는  ++ return 값
       {
            "writer": "맛집추천러",
            "title": "아 배고프다 ",
            "content": "방금 밥먹음 ",
            "hashTags": [
                "해람",
                "신촌맛집",
                "점메추"
            ],
             "regDate": "2023/11/22"
        }
     */

    private final PostService postService;

    @GetMapping
    public ResponseEntity<?> list(PageDTO pageDTO) {
        log.info("/api/v1/posts?page={}&size={}", pageDTO.getPage(), pageDTO.getSize());
        PostListResponseDTO dto =  postService.getPosts(pageDTO);

        return ResponseEntity.ok().body(dto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id ){
        log.info("/api/v1/posts/{} GET", id);

        try {
            PostDetailResponseDTO dto = postService.getDetail(id);
            return ResponseEntity.ok().body(dto);
        } catch (Exception e) { // 예외처리
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "게시물 작성", description = "게시물 작성을 담당하는 메서드 입니다.")
    @Parameters({
            @Parameter(name = "writer", description = "게시물의 작성자 이름을 쓰세여!", example = "김뽀삐", required = true),
            @Parameter(name = "title", description = "게시물의 제목을 쓰세여!", example = "글제목", required = true),
            @Parameter(name = "content", description = "게시물 내용 을 쓰세여!", example = "내용내용"),
            @Parameter(name = "hashTags", description = "해시태그를  작성 !", example = "['핳','ffu;']"),
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Validated @RequestBody PostCreateDTO dto,
            BindingResult result // 검증 에러 정보를 가진 객체
    ){
        log.info("/api/v1/posts POST!! - payload:{}", dto);

        if(dto == null ){
            return ResponseEntity.badRequest().body("등록 게시물 정보를 전달해 주세요!");
        }
        ResponseEntity<List<FieldError>> fieldErrors = getValidatedResult(result);
        if (fieldErrors != null) return fieldErrors;
        // 위에 존재하는 if 문을 건너뜀
        // -> dto 가 null 도 아니고 , 입력값 검증도 모두 통과함
        // -> service 에게 명령.
        try {
            PostDetailResponseDTO responseDTO = postService.insert(dto);
             return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity
                    .internalServerError()
                    .body(" 미안 서버터짐원인 : " +  e.getMessage());
        }
    }


    // 게시물 수정
    // 2가지 요청을 처리하고시프면
    @RequestMapping(method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<?> update(
            @Validated @RequestBody PostModifyDTO dto,
            BindingResult  result, // Valida의 결과
            HttpServletRequest request
    ){
        log.info("/api/v1/posts {} - payload: {} "
                , request.getMethod(), dto );

        ResponseEntity<List<FieldError>> fieldErrors = getValidatedResult(result);
        if(fieldErrors != null ) return fieldErrors;
        //fieldErrors 에 null 이 오면 문제가 없는거

        PostDetailResponseDTO responseDTO
                = postService.modify(dto);

        return ResponseEntity.ok().body(responseDTO);
    }

    // 입력값 검증 (Validation ) 의 결과를 처리해주는 전역 메서드
    private static ResponseEntity<List<FieldError>> getValidatedResult(BindingResult result) {
        if(result.hasErrors()){ // 입력값 검증 단계에서 문제가 있었다면 true
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err-> {
                log.warn("invalid client date - {} ", err.toString());
            });
            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        log.info("/api/v1/post/{} DELETE ",id );

        try {
            postService.delete(id);
            return ResponseEntity.ok("DEL SUCCESS!! ");
        }
        /*catch (SQLIntegrityConstraintViolationException e){
            return ResponseEntity.internalServerError()
                    .body("해시태그가 달린 게시물은 삭제 할 수 없습니다.");
        }*/
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }

    }



}















