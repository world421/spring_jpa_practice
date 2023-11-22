package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PageResponseDTO;
import com.study.jpa.chap05_practice.dto.PostDetailResponseDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.ls.LSException;

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
}
