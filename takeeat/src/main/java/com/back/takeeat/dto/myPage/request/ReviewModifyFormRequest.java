package com.back.takeeat.dto.myPage.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewModifyFormRequest {

    private Long reviewId;
    private int rating;
    private String content;
    private List<MultipartFile> file;

    private int imageModifyFlag;
}
