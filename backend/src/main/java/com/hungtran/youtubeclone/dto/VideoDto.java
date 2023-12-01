package com.hungtran.youtubeclone.dto;

import com.hungtran.youtubeclone.model.Comment;
import com.hungtran.youtubeclone.model.TheloaiVideo;
import com.hungtran.youtubeclone.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private String id;
    private String title;
    private String description;
    private String userId;
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private TheloaiVideo theloaiVideo;
    private String thumbnailUrl;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
}






























