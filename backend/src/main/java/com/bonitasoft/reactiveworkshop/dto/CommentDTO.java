package com.bonitasoft.reactiveworkshop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CommentDTO {
    @JsonIgnore
    private String artist;
    private String userName;
    private String comment;
}
