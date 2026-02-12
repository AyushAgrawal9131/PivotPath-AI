package com.project.pivotPath.Ai.model;

import java.util.List;

public record SkillGapReport(
        int matchPercentage,
        List<String> matchedSkills,
        List<String> missingSkills,
        String overallFeedBack
) {
}

