package com.example.meditationapp2_0;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StreakManager {

    private static final String STREAK_FILE = "streak_data.txt";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private int currentStreak;
    private LocalDateTime lastLoginDateTime;
    private LocalDateTime lastLogoutDateTime;

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
            String lastLoginDateString = reader.readLine();
            String lastLogoutDateString = reader.readLine();
            lastLoginDateTime = (lastLoginDateString != null && !lastLoginDateString.isEmpty())
                    ? LocalDateTime.parse(lastLoginDateString, DATE_TIME_FORMATTER)
                    : LocalDateTime.now().minusDays(1); // Default to 1 day ago
            lastLogoutDateTime = (lastLogoutDateString != null && !lastLogoutDateString.isEmpty())
                    ? LocalDateTime.parse(lastLogoutDateString, DATE_TIME_FORMATTER)
                    : LocalDateTime.now().minusDays(1); // Default to 1 day ago
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            currentStreak = 0;
            lastLoginDateTime = LocalDateTime.now().minusDays(1);  // Default to 1 day ago
            lastLogoutDateTime = LocalDateTime.now().minusDays(1); // Default to 1 day ago
        }
    }

    public void login() {
        lastLoginDateTime = LocalDateTime.now();
        System.out.println("Logged in at: " + lastLoginDateTime.format(DATE_TIME_FORMATTER));
        updateStreak();
    }

    public void logout() {
        LocalDateTime now = LocalDateTime.now();
        lastLogoutDateTime = now;
        System.out.println("Attempting to logout at: " + now.format(DATE_TIME_FORMATTER));
        saveStreakData();
    }

    private void updateStreak() {
        LocalDateTime now = LocalDateTime.now();
        if (lastLogoutDateTime != null) {
            if (now.isAfter(lastLogoutDateTime.plusDays(1))) {
                if (now.isBefore(lastLogoutDateTime.plusDays(2))) {
                    currentStreak++;
                    System.out.println("Streak incremented to: " + currentStreak);
                } else {
                    currentStreak = 1;
                    System.out.println("Streak reset to: " + currentStreak);
                }
            }
        } else {
            currentStreak = 1;
            System.out.println("Streak reset to: " + currentStreak);
        }
        saveStreakData();
    }

    private void saveStreakData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STREAK_FILE))) {
            writer.write(String.valueOf(currentStreak));
            writer.newLine();
            writer.write(lastLoginDateTime.format(DATE_TIME_FORMATTER));
            writer.newLine();
            writer.write(lastLogoutDateTime.format(DATE_TIME_FORMATTER));
            System.out.println("Streak data saved. Current streak: " + currentStreak);
            System.out.println("Last login time: " + lastLoginDateTime.format(DATE_TIME_FORMATTER));
            System.out.println("Last logout time: " + lastLogoutDateTime.format(DATE_TIME_FORMATTER));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCurrentStreak() {
        return currentStreak;
    }
}