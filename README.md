# Mod — Notepad countdown prank

This Fabric client mod uses a blocking `preLaunch` entrypoint to show the Notepad typing prank before Minecraft opens its main window. Windows only. It **does not shut down Windows**.

## Build
Upload the CONTENTS of this folder to your GitHub repository, keeping `.github/workflows/build.yml` and `src/main/...` intact. Run GitHub Actions > Build Fabric Mod. Uses Java 21 and Gradle 8.12.

## Run
Use a Java 21 Minecraft instance. Minecraft 1.21.1 is the build target; 1.21.11 has not been tested.

On first launch, the mod creates `%USERPROFILE%\.minecraft-mod-prank.properties` and runs the animation. Set `enabled=false` to disable it. Edit `message`, `typing_speed_ms`, and `countdown_seconds` as needed. `shutdown_enabled=false` is a reserved, nonfunctional setting: this build does not contain any Windows shutdown action.

The Start search / Notepad focus sequence depends on Windows desktop settings; it may not work on all systems.
