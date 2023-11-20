package ru.verpul.DTO;

import lombok.Data;

@Data
public class VKNewsPostDTO {

    private int postId;
    private String groupName;
    private String groupScreenName;
    private long sourceId;
    private String text;
    private int numberOfComments;
    private String postDate;
    private boolean hidden;
}
