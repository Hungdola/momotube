package com.hungtran.youtubeclone.repository;

import com.hungtran.youtubeclone.dto.VideoDto;
import com.hungtran.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VideoRepository extends MongoRepository<Video, String> {
    public List<Video> findByTitleContainingIgnoreCase(
            String key1
    );

    List<Video> findByUserId(String userId);

    List<Video> findByUserIdIn(List<String> userIds);

    List<Video> findAllByOrderByViewCountDesc();

    List<Video> findByTheloaiVideo(String theloaiVideo);

}
