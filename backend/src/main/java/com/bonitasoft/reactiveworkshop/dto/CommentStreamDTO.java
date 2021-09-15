package com.bonitasoft.reactiveworkshop.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentStreamDTO {
    private String artistId;
    private String artistName;
    private String userName;
    private String comment;
}
