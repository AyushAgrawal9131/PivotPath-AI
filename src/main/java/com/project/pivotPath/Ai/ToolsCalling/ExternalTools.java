package com.project.pivotPath.Ai.ToolsCalling;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExternalTools {

    // This matches the {"request": {"skillName": "Rust"}} structure exactly

    @Tool(description = "Fetches the most relevant video tutorial from the web")
    public String fetchWebTutorials(String skillName) {

        // Hardcoded "Best of YouTube" map for demo reliability
        Map<String, String> topVideos = Map.of(
                "rust", "https://www.youtube.com/watch?v=ms7uxf9798U",    // FreeCodeCamp
                "docker", "https://www.youtube.com/watch?v=pTFZFxd4hOI",  // Mosh
                "java", "https://www.youtube.com/watch?v=A74TOX803D0",    // AmigosCode
                "kubernetes", "https://www.youtube.com/watch?v=X48VuDVv0do", // TechWorld with Nana
                "spring boot", "https://www.youtube.com/watch?v=vtPkZShrpr0" // Java Guides
        );

        String finalUrl = topVideos.entrySet().stream()
                .filter(entry -> skillName.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("https://www.youtube.com/results?search_query=" + skillName.replace(" ", "+") + "+tutorial");

        String title = skillName + " Professional Tutorial";

        return String.format("COURSE_NAME: %s, COURSE_URL: %s", title, finalUrl);
    }
}
