package com.hungtran.youtubeclone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Document(value = "Video")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    @Id
    private String id;
    private String title;
    private  String description;
    private  String userId;
    private String author;
    private AtomicInteger likes = new AtomicInteger(0);//kiểu AtomicInteger thay thế cho 1 số nguyên, nó giống như bộ đếm
    private AtomicInteger disLikes = new AtomicInteger(0);
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private TheloaiVideo theloaiVideo;
    private AtomicInteger viewCount = new AtomicInteger(0);
    private String thumbnailUrl;
    private List<Comment> commentList = new CopyOnWriteArrayList<>();

    public void incrementLikes() {
        likes.incrementAndGet(); //điều này sẽ làm tăng lượt like
    }
    public void decrementLikes() {
        likes.decrementAndGet(); //điều này sẽ làm giảm lượt like
    }

    public void incrementDisLikes() {
        disLikes.incrementAndGet(); //điều này sẽ làm tăng lượt dislike
    }
    public void decrementDisLikes() {
        disLikes.decrementAndGet(); //điều này sẽ làm giảm lượt dislike
    }

    public void incrementViewCount() {
        viewCount.incrementAndGet();
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }
}

































