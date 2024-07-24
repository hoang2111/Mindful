package com.example.meditationapp2_0;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StreakManager {

    private static final String STREAK_FILE = "streak_data.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private int currentStreak;
    private LocalDateTime streakStartDateTime;
    private LocalDateTime lastLoginDateTime;

    public StreakManager() {
        checkAndCreateStreakFile();
        loadStreakData();
    }

    private void checkAndCreateStreakFile() {
        File file = new File(STREAK_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(STREAK_FILE))) {
                    writer.write("0");
                    writer.newLine();
                    writer.write(LocalDateTime.now().minusDays(1).format(DATE_TIME_FORMATTER));
                    writer.newLine();
                    writer.write(LocalDateTime.now().minusDays(1).format(DATE_TIME_FORMATTER));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadStreakData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STREAK_FILE))) {
            currentStreak = Integer.parseInt(reader.readLine());
            String streakStartDateTimeString = reader.readLine();
            String lastLoginDateTimeString = reader.readLine();
            streakStartDateTime = (streakStartDateTimeString != null && !streakStartDateTimeString.isEmpty())
                    ? LocalDateTime.parse(streakStartDateTimeString, DATE_TIME_FORMATTER)
                    : LocalDateTime.now().minusDays(1); // Default to 1 day ago
            lastLoginDateTime = (lastLoginDateTimeString != null && !lastLoginDateTimeString.isEmpty())
                    ? LocalDateTime.parse(lastLoginDateTimeString, DATE_TIME_FORMATTER)
                    : LocalDateTime.now().minusDays(1); // Default to 1 day ago
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            currentStreak = 0;
            streakStartDateTime = LocalDateTime.now().minusDays(1);  // Default to 1 day ago
            lastLoginDateTime = LocalDateTime.now().minusDays(1); // Default to 1 day ago
        }
    }

    public void login() {
        LocalDateTime now = LocalDateTime.now();
        if (lastLoginDateTime != null) {
            if (now.toLocalDate().isAfter(lastLoginDateTime.toLocalDate())) {
                if (now.toLocalDate().isEqual(lastLoginDateTime.toLocalDate().plusDays(1))) {
                    currentStreak++;
                    System.out.println("Streak incremented to: " + currentStreak);
                } else {
                    currentStreak = 1;
                    streakStartDateTime = now;
                    System.out.println("Streak reset to: " + currentStreak);
                }
            }
        } else {
            currentStreak = 1;
            streakStartDateTime = now;
            System.out.println("Streak started at: " + streakStartDateTime.format(DATE_TIME_FORMATTER));
        }
        lastLoginDateTime = now;
        saveStreakData();
    }

    private void saveStreakData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STREAK_FILE))) {
            writer.write(String.valueOf(currentStreak));
            writer.newLine();
            writer.write(streakStartDateTime.format(DATE_TIME_FORMATTER));
            writer.newLine();
            writer.write(lastLoginDateTime.format(DATE_TIME_FORMATTER));
            System.out.println("Streak data saved. Current streak: " + currentStreak);
            System.out.println("Streak start time: " + streakStartDateTime.format(DATE_TIME_FORMATTER));
            System.out.println("Last login time: " + lastLoginDateTime.format(DATE_TIME_FORMATTER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public LocalDateTime getStreakStartDateTime() {
        return streakStartDateTime;
    }

    public LocalDateTime getLastLoginDateTime() {
        return lastLoginDateTime;
    }
}