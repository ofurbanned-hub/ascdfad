package com.example.mod;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public final class ModClient implements PreLaunchEntrypoint {
    @Override public void onPreLaunch() {
        if (!System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win")) return;
        Path config = Paths.get(System.getProperty("user.home"), ".minecraft-mod-prank.properties");
        Properties p = new Properties();
        if (Files.exists(config)) {
            try (var in = Files.newInputStream(config)) { p.load(in); } catch (IOException ignored) { return; }
        } else {
            p.setProperty("enabled", "true");
            p.setProperty("message", "Say bye to ur pc :)");
            p.setProperty("typing_speed_ms", "120");
            p.setProperty("countdown_seconds", "5");
            p.setProperty("shutdown_enabled", "false");
            try (var out = Files.newOutputStream(config, StandardOpenOption.CREATE_NEW)) {
                p.store(out, "Opt-in Notepad prank; no shutdown functionality. Set enabled=true to run on Minecraft launch.");
            } catch (IOException ignored) { }
            // First launch writes defaults and runs the harmless animation.
        }
        if (!Boolean.parseBoolean(p.getProperty("enabled", "true"))) return;
        String message = p.getProperty("message", "Say bye to ur pc :)");
        int speed = clamp(p.getProperty("typing_speed_ms"), 120, 20, 1000);
        int countdown = clamp(p.getProperty("countdown_seconds"), 5, 1, 15);
        // Blocking preLaunch prevents Minecraft from creating its game window
        // until the Notepad animation finishes.
        runPrank(message, speed, countdown);
    }
    private static int clamp(String raw, int fallback, int min, int max) {
        try { return Math.max(min, Math.min(max, Integer.parseInt(raw))); } catch (Exception e) { return fallback; }
    }
    private static void runPrank(String message, int speed, int countdown) {
        try {
            Thread.sleep(600);
            Robot robot = new Robot();
            robot.setAutoDelay(70);
            press(robot, KeyEvent.VK_WINDOWS, KeyEvent.VK_S);
            Thread.sleep(500);
            type(robot, "notepad", 80);
            press(robot, KeyEvent.VK_ENTER);
            Thread.sleep(2200);
            type(robot, message, speed);
            press(robot, KeyEvent.VK_ENTER);
            for (int i = countdown; i >= 1; i--) {
                type(robot, Integer.toString(i), 100);
                press(robot, KeyEvent.VK_ENTER);
                Thread.sleep(1000);
            }
            type(robot, "Just kidding! Your PC is safe :)", 100);
        } catch (Exception e) {
            System.err.println("[Mod] Notepad prank failed: " + e);
            e.printStackTrace();
        }
    }
    private static void press(Robot r, int... keys) {
        for (int k : keys) r.keyPress(k);
        for (int i = keys.length - 1; i >= 0; i--) r.keyRelease(keys[i]);
    }
    private static void type(Robot r, String text, int delay) throws InterruptedException {
        for (char c : text.toCharArray()) {
            int key;
            boolean shift = false;
            if (c >= 'a' && c <= 'z') key = KeyEvent.VK_A + c - 'a';
            else if (c >= 'A' && c <= 'Z') { key = KeyEvent.VK_A + c - 'A'; shift = true; }
            else if (c >= '0' && c <= '9') key = KeyEvent.VK_0 + c - '0';
            else if (c == ' ') key = KeyEvent.VK_SPACE;
            else if (c == ':') { key = KeyEvent.VK_SEMICOLON; shift = true; }
            else if (c == ';') key = KeyEvent.VK_SEMICOLON;
            else if (c == ')') { key = KeyEvent.VK_0; shift = true; }
            else if (c == '(') { key = KeyEvent.VK_9; shift = true; }
            else if (c == '!') { key = KeyEvent.VK_1; shift = true; }
            else if (c == '.') key = KeyEvent.VK_PERIOD;
            else if (c == ',') key = KeyEvent.VK_COMMA;
            else if (c == '-') key = KeyEvent.VK_MINUS;
            else if (c == '?') { key = KeyEvent.VK_SLASH; shift = true; }
            else continue;
            if (shift) r.keyPress(KeyEvent.VK_SHIFT);
            r.keyPress(key); r.keyRelease(key);
            if (shift) r.keyRelease(KeyEvent.VK_SHIFT);
            Thread.sleep(delay);
        }
    }
}
