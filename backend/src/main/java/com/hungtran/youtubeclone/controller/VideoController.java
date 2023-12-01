package com.hungtran.youtubeclone.controller;

import com.hungtran.youtubeclone.dto.CommentDto;
import com.hungtran.youtubeclone.dto.UploadVideoResponse;
import com.hungtran.youtubeclone.dto.VideoDto;
import com.hungtran.youtubeclone.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file")MultipartFile file){
        return videoService.uploadVideo(file);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(@RequestParam("file")MultipartFile file, @RequestParam("videoId") String videoId){
        return videoService.uploadThumbnail(file, videoId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
     public VideoDto editVideoMetadata(@RequestBody VideoDto videoDto) {
        return videoService.editVideo(videoDto);
    }

    @GetMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto getVideoDetails(@PathVariable String videoId) {
        return videoService.getVideoDetails(videoId);
    }

    @PostMapping("/{videoId}/like")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto likeVideo(@PathVariable String videoId) {
        return videoService.likeVideo(videoId);
    }

    @PostMapping("/{videoId}/dislike")
    @ResponseStatus(HttpStatus.OK)
    public VideoDto disLikeVideo(@PathVariable String videoId) {
        return videoService.disLikeVideo(videoId);
    }

    @PostMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@PathVariable String videoId, @RequestBody CommentDto commentDto) {
        videoService.addComment(videoId, commentDto);
    }

    @GetMapping("/{videoId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getAllComments(@PathVariable String videoId) {
        return videoService.getAllComments(videoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getAllVideos(@RequestParam(defaultValue = "") String searchKey) {
        return videoService.getAllVideos(searchKey);
    }

    @GetMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getListVideoHistory() {
        return videoService.getListVideoHistory();
    }

    @GetMapping("/liked")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getListVideoLiked() {
        return videoService.getListVideoLiked();
    }

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getVideosByUserId(@PathVariable String userId) {
        return videoService.getVideosByUserId(userId);
    }

    @DeleteMapping("/{videoId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVideoById(@PathVariable String videoId) {
        videoService.deleteVideoById(videoId);
    }

    @GetMapping("/byuserids")
    public List<VideoDto> getVideosByUserIds(@RequestParam List<String> userIds) {
        return videoService.getVideosByUserIds(userIds);
    }

    @GetMapping("/sorted-by-view-count")
    public List<VideoDto> getVideosSortedByViewCount() {
        return videoService.getAllVideosSortedByViewCount();
    }

    @GetMapping("/theloai/{theloaiVideo}")
    @ResponseStatus(HttpStatus.OK)
    public List<VideoDto> getVideosByTheloaiVideo(@PathVariable String theloaiVideo) {
        return videoService.getVideosByTheloaiVideo(theloaiVideo);
    }
}




























