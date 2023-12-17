package com.hungtran.youtubeclone.service;

import com.hungtran.youtubeclone.dto.CommentDto;
import com.hungtran.youtubeclone.dto.UploadVideoResponse;
import com.hungtran.youtubeclone.dto.VideoDto;
import com.hungtran.youtubeclone.model.Comment;
import com.hungtran.youtubeclone.model.User;
import com.hungtran.youtubeclone.model.Video;
import com.hungtran.youtubeclone.repository.UserRepository;
import com.hungtran.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final UserService userService;

    private final UserRepository userRepository;


    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        //upload file to AWS S3
        //Save video data to database
        String videoUrl =  s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);
        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        //find video by videoId
        var savedVideo = getVideoById(videoDto.getId());
        //Map the videoDto fields to video
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setUserId(videoDto.getUserId());
        savedVideo.setAuthor(videoDto.getAuthor());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());
        savedVideo.setTheloaiVideo(videoDto.getTheloaiVideo());
        //save video to the database
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        var savedVideo = getVideoById(videoId);

        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        videoRepository.save(savedVideo);
        return thumbnailUrl;
    }

    Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("cannot find video by id" + videoId));
    }

    public VideoDto getVideoDetails(String videoId) {
        Video savedVideo = getVideoById(videoId);

        increaseVideoCount(savedVideo);
        userService.addVideoToHistory(videoId);

        return mapToVideoDto(savedVideo);
    }

    private void increaseVideoCount(Video savedVideo) {
        savedVideo.incrementViewCount();
        videoRepository.save((savedVideo));
    }

    public VideoDto likeVideo(String videoId) {
        //get video by id
        Video videoById = getVideoById(videoId);

        //Increment like count
        //if user already liked video, then decrement like count
        //like - 0, dislike  - 0
        //like - 1, dislike - 0
        //like - 0, dislike - 0

        //like -0, dislike - 1
        //like - 1, dislike - 0

        //if user already disliked video, then increment like count and decrement dislike count

        if(userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
        } else if(userService.ifDisLikedVideo(videoId)) {
            videoById.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        } else {
            videoById.incrementLikes();
            userService.addToLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDto(videoById);
    }

    public VideoDto disLikeVideo(String videoId) {
//get video by id
        Video videoById = getVideoById(videoId);
        //Increment like count
        //if user already liked video, then decrement like count
        //like - 0, dislike  - 0
        //like - 1, dislike - 0
        //like - 0, dislike - 0

        //like -0, dislike - 1
        //like - 1, dislike - 0

        //if user already disliked video, then increment like count and decrement dislike count
        if(userService.ifDisLikedVideo(videoId)) {
            videoById.decrementDisLikes();
            userService.removeFromDisLikedVideos(videoId);
        } else if(userService.ifLikedVideo(videoId)) {
            videoById.decrementLikes();
            userService.removeFromLikedVideos(videoId);
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        } else {
            videoById.incrementDisLikes();
            userService.addToDisLikedVideos(videoId);
        }

        videoRepository.save(videoById);

        return mapToVideoDto(videoById);
    }

    public void addComment(String videoId, CommentDto commentDto) {
        Video video = getVideoById(videoId);
        Comment comment = new Comment();
        comment.setText(commentDto.getCommentText());
        comment.setAuthorId(commentDto.getAuthorId());
        video.addComment(comment);

        videoRepository.save(video);
    }

    private VideoDto mapToVideoDto(Video videoById) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(videoById.getVideoUrl());
        videoDto.setThumbnailUrl(videoById.getThumbnailUrl());
        videoDto.setId(videoById.getId());
        videoDto.setTitle(videoById.getTitle());
        videoDto.setDescription(videoById.getDescription());
        videoDto.setUserId(videoById.getUserId());
        videoDto.setAuthor(videoById.getAuthor());
        videoDto.setTags(videoById.getTags());
        videoDto.setVideoStatus(videoById.getVideoStatus());
        videoDto.setTheloaiVideo(videoById.getTheloaiVideo());
        videoDto.setLikeCount(videoById.getLikes().get());
        videoDto.setDislikeCount(videoById.getDisLikes().get());
        videoDto.setViewCount(videoById.getViewCount().get());
        return videoDto;
    }

    public List<CommentDto> getAllComments(String videoId) {
        Video video = getVideoById(videoId);
        List<Comment> commentList = video.getCommentList();

        return commentList.stream().map(this::mapToCommentDto).toList();
    }

    private CommentDto mapToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setCommentText(comment.getText());
        commentDto.setAuthorId(comment.getAuthorId());
        return commentDto;
    }

    public List<VideoDto> getAllVideos(String searchKey) {
        if(searchKey.equals("")){
            return videoRepository.findAll().stream().map(this::mapToVideoDto).toList();
        }
        else {
            return videoRepository.findByTitleContainingIgnoreCase(searchKey).stream().map(this::mapToVideoDto).toList();
        }
    }

    public List<VideoDto> getListVideoHistory() {
        User currentUser = userService.getCurrentUser();
        Iterable<Video> videos = videoRepository.findAllById(currentUser.getVideoHistory());
        return StreamSupport.stream(videos.spliterator(), false)
                .map(this::mapToVideoDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getListVideoLiked() {
        User currentUser = userService.getCurrentUser();
        Iterable<Video> videos = videoRepository.findAllById(currentUser.getLikedVideos());
        return StreamSupport.stream(videos.spliterator(), false)
                .map(this::mapToVideoDto)
                .collect(Collectors.toList());
    }

    public List<VideoDto> getVideosByUserId(String userId) {
        return videoRepository.findByUserId(userId).stream().map(this::mapToVideoDto).toList();
    }

    public void deleteVideoById(String videoId) {
        videoRepository.deleteById(videoId);
    }

    public List<VideoDto> getVideosByUserIds(List<String> userIds) {
        return videoRepository.findByUserIdIn(userIds).stream().map(this::mapToVideoDto).toList();
    }

    public List<VideoDto> getAllVideosSortedByViewCount() {
        return videoRepository.findAllByOrderByViewCountDesc().stream().map(this::mapToVideoDto).toList();
    }

    public List<VideoDto> getVideosByTheloaiVideo(String theloaiVideo) {
        return videoRepository.findByTheloaiVideo(theloaiVideo).stream().map(this::mapToVideoDto).toList();
    }


}
























