package com.example.meditationapp2_0;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoLibraryManager {
    private List<Video> userUploadedVideos;
    private List<Video> preSelectedVideos;

    public VideoLibraryManager() {
        this.userUploadedVideos = new ArrayList<>();
        this.preSelectedVideos = new ArrayList<>();
        initializePreSelectedVideos();
    }

    private void initializePreSelectedVideos() {
        preSelectedVideos.add(new Video("Sleep Meditation1", "https://ia601906.us.archive.org/10/items/sample-video_202407/sample%20video.mp4", VideoTheme.SLEEP));
        preSelectedVideos.add(new Video("Eye Exercise1", "https://ia601905.us.archive.org/23/items/y-2mate.com-effective-eyes-exercises-to-restore-vision-heal-your-eyesight-1080p/y2mate.com%20-%20Effective%20eyes%20exercises%20to%20restore%20vision%20Heal%20your%20eyesight_1080p.mp4", VideoTheme.EYE_EXERCISE));
        preSelectedVideos.add(new Video("sleep", "https://ia803202.us.archive.org/10/items/y-2mate.com-5-minute-meditation-you-can-do-anywhere-1080p/y2mate.com%20-%205Minute%20Meditation%20You%20Can%20Do%20Anywhere_1080p.mp4", VideoTheme.SLEEP));
        preSelectedVideos.add(new Video("Eye Exercise2", "https://ia601905.us.archive.org/15/items/y-2mate.com-daily-exercise-get-rid-of-eye-strain-and-improve-vision-naturally-1080p/y2mate.com%20-%20Daily%20exercise%20get%20rid%20of%20eye%20strain%20and%20improve%20vision%20naturally_1080p.mp4", VideoTheme.EYE_EXERCISE));
        preSelectedVideos.add(new Video("Eye Exercise3", "https://ia600308.us.archive.org/19/items/y-2mate.com-5-eye-yoga-exercises-to-do-in-the-morning-1080p/y2mate.com%20-%205%20Eye%20Yoga%20Exercises%20To%20Do%20In%20The%20Morning_1080p.mp4", VideoTheme.EYE_EXERCISE));
        preSelectedVideos.add(new Video("sleep3", "https://ia601906.us.archive.org/8/items/y-2mate.com-5-minute-long-deep-breaths-1080p/y2mate.com%20-%205%20Minute%20Long%20Deep%20Breaths_1080p.mp4", VideoTheme.SLEEP));
    }

    public void addUserUploadedVideo(Video video) {
        userUploadedVideos.add(video);
    }

    public List<Video> getVideosByTheme(VideoTheme theme) {
        return preSelectedVideos.stream().filter(video -> video.getTheme() == theme).collect(Collectors.toList());
    }

    public List<Video> getUserUploadedVideos() {
        return userUploadedVideos;
    }

    public List<Video> getAllVideos() {
        List<Video> allVideos = new ArrayList<>();
        allVideos.addAll(preSelectedVideos);
        allVideos.addAll(userUploadedVideos);
        return allVideos;
    }
}