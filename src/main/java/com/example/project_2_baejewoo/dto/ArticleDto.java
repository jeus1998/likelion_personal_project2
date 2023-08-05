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

    public static ArticleDto fromEntity(ArticleEntity entity){
        ArticleDto dto = new ArticleDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDraft(entity.isDraft());
        dto.setDelete_at(entity.getDelete_at());
        return dto;
    }

}
