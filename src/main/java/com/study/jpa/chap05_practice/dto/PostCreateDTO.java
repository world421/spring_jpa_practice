package com.study.jpa.chap05_practice.dto;

import com.study.jpa.chap05_practice.entity.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
@Setter @Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDTO {

    /*
     @ NotNull -> null 을 허용하지 않음. "", " " 은 허용
     @ NotEmpty  -> null , ""을 허용하지 않음. " " 은 허용
     @ NotBlank ->  null, "", " " 모두를 허용하지 않음
    */
    //공백을 허용하지 않겠당
    @NotBlank
    @Size(min = 2, max = 5)
    // 게시물 등록: payload: title, writer, content, hashTags
    private String writer;

    @NotBlank
    @Size(min = 1, max = 20)
    private String title;
    private String content;
    private List<String> hashTags;

    // dto 를 엔터티로 변환하는 메서드
    public Post toEntity(){
        return Post.builder()
                .writer(this.writer)
                .content(this.content)
                .title(this.title)
                // 해쉬태그는 여기서 넣는거 아님 !
                .build();
 // 빌더로 객체 생성하면 값을 안 넣어 주면  null ㄹ
    }

}
