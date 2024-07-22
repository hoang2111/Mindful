package com.example.meditationapp2_0;

import java.time.LocalDateTime;

public class SessionData {
    private String sessionType;
    private int duration;
    private LocalDateTime sessionDate;

    public SessionData(String sessionType, int duration, LocalDateTime sessionDate) {
        this.sessionType = sessionType;
        this.duration = duration;
        this.sessionDate = sessionDate;
    }

    public String getSessionType() {
        return sessionType;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getSessionDate() {
        return sessionDate;
    }
}