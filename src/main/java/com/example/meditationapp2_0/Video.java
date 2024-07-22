package com.example.meditationapp2_0;

public class Video {
    private String title;
    private String filePathOrUrl;
    private VideoTheme theme;

    public Video(String title, String filePathOrUrl, VideoTheme theme) {
        this.title = title;
        this.filePathOrUrl = filePathOrUrl;
        this.theme = theme;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePathOrUrl() {
        return filePathOrUrl;
    }

    public VideoTheme getTheme() {
        return theme;
    }

    @Override
    public String toString() {
        return title;
    }
}