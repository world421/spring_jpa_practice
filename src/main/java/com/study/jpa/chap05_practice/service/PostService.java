package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.expression.ExpressionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ls.LSException;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // jpa 레파지토리는 트랜잭션 단위로 동작하기 때문에 작성해주세요!
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;

    public PostListResponseDTO getPosts(PageDTO dto) {

        // Pageable 객체 생성 (PageRequest 사용)
        Pageable pageable
                = PageRequest.of(
                dto.getPage() - 1,
                dto.getSize(),
                Sort.by("createDate").descending()
        );
        // 데이터베이스에서 게시물 목록 조회

        //응답하려는 객체 타입에 맞게 가공해서
        Page<Post> posts = postRepository.findAll(pageable);

        // 게시물 정보만 꺼내기
        List<Post> postList = posts.getContent();

        // 게시물 정보를 DTO의 형태에 맞게 변환(stream을 이용하여 객체마다 일괄 처리)
        List<PostDetailResponseDTO> detailList
                = postList.stream()
                            .map(post -> new PostDetailResponseDTO(post))
                            .collect(Collectors.toList());
        // db에서 조회한 정보를 JSON 형태에 맞는 DTO로 변환
        //  -> PostListResponseDTO


        return PostListResponseDTO.builder()
                .count(detailList.size())  // 총게시물수가 아니라 조회된 게시물의 수
                .pageInfo(new PageResponseDTO(posts)) /// 페이지 정보가 담긴 객체를 DTO에게 전달해서 그쪽에서 처리하게함.
                .posts(detailList)
                .build();

    }

    public PostDetailResponseDTO getDetail(Long id) throws Exception {

        Post postEntity = postRepository.findById(id)
                                        // 예외만들기, 예외발생하면 실행중지후 throws 발생
                .orElseThrow(() -> new RuntimeException(id + "번 게시물이 존재하지 않습니다.!"));
        return new PostDetailResponseDTO(postEntity);
    }


    public PostDetailResponseDTO insert(PostCreateDTO dto)
        throws Exception{
        // 게시물 저장 !  아직 해시태그 는 insert되지않ㅡㅁ)
        Post saved = postRepository.save(dto.toEntity());

        // 해시태그 저장 !
        List<String> hashTags = dto.getHashTags();
        if(hashTags != null && !hashTags.isEmpty()){
            hashTags.forEach(ht->{
                HashTag savedTag = hashTagRepository.save(
                        HashTag.builder()
                                .tagName(ht)
                                .post(saved)
                                .build()
                );

                /*
                    Post Entity는 DB에 save를 진행할 때 HashTag에 대한 내용을 갱신하지 않습니다.
                    HashTag Entity는 따로 save를 진행합니다. (테이블이 각각 나뉘어 있음)
                    HashTag는 양방향 맵핑이 되어있는 연관관계의 주인이기 때문에 save를 진행할 때 Post를 전달하므로
                    DB와 Entity와의 상태가 동일하지만,
                    Post는 HashTag의 정보가 비어있는 상태입니다.
                    Post Entity에 연관관계 편의 메서드를 작성하여 HashTag의 내용을 동기화 해야
                    추후에 진행되는 과정에서 문제가 발생하지 않습니다.
                    (Post를 화면단으로 return -> HashTag들도 같이 가야 함. -> 직접 갱신)
                    (Post를 다시 SELECT 해서 가져온다??? -> 의미없는 행동.(insert는 트랜잭션 종료 후 진행))
                 */

                saved.addHashTag(savedTag);
            });

        }
        
        
        return new PostDetailResponseDTO(saved);
    }
}
