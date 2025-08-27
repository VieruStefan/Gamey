package org.dis.worker.detail.service;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class GameIdGenerator {
   private static final Set<String> STOP_WORDS = Set.of(
         "a", "an", "and", "the", "of", "in", "on", "for", "with", "to", "i", "ii", "iii", "iv", "v"
   );

   private static final Pattern EDITION_PATTERN = Pattern.compile(
         "\\s+(game of the year|goty|collector's|collectors|deluxe|ultimate|standard|limited|remastered|remake|hd|vr|edition|bundle|part)",
         Pattern.CASE_INSENSITIVE
   );

   private GameIdGenerator() {}

   public static String generateGameId(String title) {
      if (title == null || title.isBlank()) {
         return "";
      }

      String normalized = title.toLowerCase();
      normalized = EDITION_PATTERN.matcher(normalized).replaceAll("");
      normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
      normalized = normalized.replaceAll("[^a-z0-9\\s]", "");
      normalized = Arrays.stream(normalized.split("\\s+"))
            .filter(word -> !STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
      normalized = normalized.trim().replaceAll("\\s+", "-");

      return normalized;
   }
}