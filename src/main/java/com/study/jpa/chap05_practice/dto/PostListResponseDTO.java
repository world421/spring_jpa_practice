package com.study.jpa.chap05_practice.dto;

import lombok.*;

import java.util.List;
@Setter @Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostListResponseDTO { // 게시물 리스트 응답 하는 dto

    private int count;  // 총게시물수
    private PageResponseDTO pageInfo; // 페이지 렌더링 정보 ,
    private List<PostDetailResponseDTO> posts; // 게시물 렌더링 정보 (하나의 게시물 상세 보기)

    //엔터티 응답용으로 사용 xx

}