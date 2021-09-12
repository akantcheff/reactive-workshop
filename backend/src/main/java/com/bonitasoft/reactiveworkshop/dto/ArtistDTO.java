package com.bonitasoft.reactiveworkshop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArtistDTO {
    private String artistId;
    private String artistName;
    private String genre;
    private List<CommentDTO> comments;
}
