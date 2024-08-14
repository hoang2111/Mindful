package com.example.meditationapp2_0;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VideoLibraryManager {
    private static final String USER_UPLOADED_VIDEOS_FILE = "userUploadedVideos.json";
    private List<Video> userUploadedVideos;
    private List<Video> preSelectedVideos;

    public VideoLibraryManager() {
        this.userUploadedVideos = new ArrayList<>();
        this.preSelectedVideos = new ArrayList<>();
        initializePreSelectedVideos();
        loadUserUploadedVideos();
    }

    private void initializePreSelectedVideos() {
        preSelectedVideos.add(new Video("Eye Exercise 1", "https://od.lk/s/NDhfNTkwMjEwNjFf/y2mate.com%20-%205%20Eye%20Yoga%20Exercises%20To%20Do%20In%20The%20Morning_1080p.mp4", VideoTheme.EYE_EXERCISE));
        preSelectedVideos.add(new Video("Eye Exercise 2", "https://od.lk/s/NDhfNTkwMjEwMDNf/y2mate.com%20-%20Effective%20eyes%20exercises%20to%20restore%20vision%20Heal%20your%20eyesight_1080p.mp4", VideoTheme.EYE_EXERCISE));
        preSelectedVideos.add(new Video("Eye Exercise 3", "https://od.lk/s/NDhfNTg5ODY3OTNf/y2mate.com%20-%20Daily%20exercise%20get%20rid%20of%20eye%20strain%20and%20improve%20vision%20naturally_1080p.mp4", VideoTheme.EYE_EXERCISE));
        preSelectedVideos.add(new Video("Light Exercise 1", "https://od.lk/s/NDhfNTkwMjEzNjJf/y2mate.com%20-%20Fast%20Workout%20for%20the%20Office%203%20MINUTES%20no%20equipment_1080p.mp4", VideoTheme.GENERAL));
        preSelectedVideos.add(new Video("Light Exercise 2", "https://od.lk/s/NDhfNTkwMjEwODhf/y2mate.com%20-%20Stretch%20Break%20%20Stretches%20at%20Your%20Desk%20%207%20min_1080p.mp4", VideoTheme.GENERAL));
        preSelectedVideos.add(new Video("Light Exercise 3", "https://od.lk/s/NDhfNTkwMjEzNTZf/y2mate.com%20-%205%20minute%20Office%20Stretch_1080p.mp4", VideoTheme.GENERAL));
        preSelectedVideos.add(new Video("Meditation 1", "https://od.lk/s/NDhfNTkwMjEzODJf/sample%20video.mp4", VideoTheme.SLEEP));
        preSelectedVideos.add(new Video("Meditation 2", "https://od.lk/s/NDhfNTkwMjEzODVf/y2mate.com%20-%205%20Minute%20Guided%20Morning%20Meditation%20for%20Positive%20Energy%20_1080p.mp4", VideoTheme.SLEEP));
        preSelectedVideos.add(new Video("Meditation 3", "https://od.lk/s/NDhfNTkwMjEzODZf/y2mate.com%20-%205Minute%20Meditation%20You%20Can%20Do%20Anywhere_1080p.mp4", VideoTheme.SLEEP));
    }

    public void addUserUploadedVideo(Video video) {
        userUploadedVideos.add(video);
        preSelectedVideos.add(video);
        saveUserUploadedVideos();
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

    public void saveUserUploadedVideos() {
        try (Writer writer = new FileWriter(USER_UPLOADED_VIDEOS_FILE)) {
            new Gson().toJson(userUploadedVideos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserUploadedVideos() {
        File file = new File(USER_UPLOADED_VIDEOS_FILE);
        if (!file.exists()) {
            return; // File does not exist, so there's nothing to load
        }
        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Video>>() {}.getType();
            userUploadedVideos = new Gson().fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (com.google.gson.JsonSyntaxException e) {
            e.printStackTrace();
            // In case of JSON syntax error, start with an empty list
            userUploadedVideos = new ArrayList<>();
        }
    }
}