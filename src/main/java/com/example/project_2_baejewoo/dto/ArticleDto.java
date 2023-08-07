package com.example.project_2_baejewoo.dto;

import com.example.project_2_baejewoo.entity.ArticleEntity;
import lombok.Data;


@Data
public class ArticleDto {

    private Long id;
    private String title;
    private String content;
    private boolean draft;
    private String delete_at;


}
