package com.back.takeeat.dto.myPage.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReviewFormRequest {

    private Long orderId;
    private int rating;
    private String content;
    private List<MultipartFile> file;
}
