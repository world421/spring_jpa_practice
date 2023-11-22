package com.study.jpa.chap05_practice.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class PageDTO {

    private int page;
    private int size;

    public PageDTO() {
        this.page = 1; // 기본값 세팅
        this.size = 10;
    }
}
