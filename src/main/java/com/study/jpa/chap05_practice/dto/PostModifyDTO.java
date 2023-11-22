package com.study.jpa.chap05_practice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostModifyDTO {
    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    private String content;
    @NotNull // Long type //  공백, 빈문자열이 들어올수 없는 타입은 notnull 로 선언
    @Builder.Default
    private Long postNo = 0L;
}

