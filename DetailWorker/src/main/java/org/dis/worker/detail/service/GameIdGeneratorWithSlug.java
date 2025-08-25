package org.dis.worker.detail.service;

import com.github.slugify.Slugify;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameIdGeneratorWithSlug {
   // A curated list of common English stop words.
   private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
         "a", "an", "and", "the", "in", "on", "of", "for", "with", "to"
   ));

   // Tier 1: Keywords to be completely removed.
   // The pattern uses word boundaries (\b) to avoid matching substrings.
   private static final Pattern TIER_1_NOISE_PATTERN = Pattern.compile(
         "\\b(game of the year edition|goty|deluxe edition|gold edition|complete edition|ultimate edition|legendary edition|collectors edition|limited edition|standard edition)\\b",
         Pattern.CASE_INSENSITIVE
   );

   // Tier 2: Keywords to be standardized.
   private static final Map<Pattern, String> TIER_2_MODIFIER_MAP = Map.of(
         Pattern.compile("\\b(hd remaster|remastered)\\b", Pattern.CASE_INSENSITIVE), "remaster",
         Pattern.compile("\\b(vr edition)\\b", Pattern.CASE_INSENSITIVE), "vr",
         Pattern.compile("\\b(directors cut)\\b", Pattern.CASE_INSENSITIVE), "directors-cut"
         // Add more standardization rules as needed
   );

   // Pattern to find Roman numerals within a string.
   private static final Pattern ROMAN_NUMERAL_PATTERN = Pattern.compile(
         "\\b(M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3}))\\b",
         Pattern.CASE_INSENSITIVE
   );

   private static final Slugify SLUGIFY_INSTANCE = Slugify.builder()
         .lowerCase(true)
         .underscoreSeparator(false) // Use hyphens
         .build();

   public static String generate(String title) {
      if (title == null || title.trim().isEmpty()) {
         return "";
      }

      // Stages 1 & 5 (partial) are handled by the library, but we need pre-processing.
      // The library handles lowercasing, accent removal, and final slugging.
      // We still need to perform our custom logic in between.

      // Pre-process for Roman numerals and editions before slugification.
      String processedTitle = title;

      // Stage 2: Semantic Component Standardization
      processedTitle = standardizeNumerals(processedTitle);

      // Stage 3: Domain-Specific Noise Reduction
      processedTitle = reduceDomainNoise(processedTitle);

      // Stage 4: Core Title Isolation (Stop words can be removed here, or handled via custom replacements)
      processedTitle = isolateCoreTitle(processedTitle);

      // Final Slugification using the library
      // The library will handle remaining punctuation, whitespace collapsing, and trimming.
      return slugify(processedTitle);
   }
   // Helper for Stage 2
   private static String standardizeNumerals(String text) {
      Matcher matcher = ROMAN_NUMERAL_PATTERN.matcher(text);
      StringBuilder sb = new StringBuilder();
      while (matcher.find()) {
         String roman = matcher.group(1);
         if (!roman.isEmpty()) {
            int arabic = romanToArabic(roman.toUpperCase());
            matcher.appendReplacement(sb, String.valueOf(arabic));
         }
      }
      matcher.appendTail(sb);
      return sb.toString();
   }

   // Helper for Stage 3
   private static String reduceDomainNoise(String text) {
      // Remove Tier 1 noise
      String result = TIER_1_NOISE_PATTERN.matcher(text).replaceAll("");

      // Standardize Tier 2 modifiers
      for (Map.Entry<Pattern, String> entry : TIER_2_MODIFIER_MAP.entrySet()) {
         result = entry.getKey().matcher(result).replaceAll(entry.getValue());
      }
      return result;
   }

   // Helper for Stage 4
   private static String isolateCoreTitle(String text) {
      return Arrays.stream(text.split("\\s+"))
            .filter(word ->!STOP_WORDS.contains(word))
            .collect(Collectors.joining(" "));
   }

   // Helper for Stage 5
   private static String slugify(String text) {
      // Remove all punctuation
      String noPunctuation = text.replaceAll("\\p{Punct}", " ");
      // Replace multiple whitespace characters with a single hyphen
      String singleHyphen = noPunctuation.trim().replaceAll("\\s+", "-");
      // Remove leading/trailing hyphens
      return singleHyphen.replaceAll("^-+|-+$", "");
   }

   // Robust Roman to Arabic numeral converter
   private static int romanToArabic(String roman) {
      int result = 0;
      int lastValue = 0;
      for (int i = roman.length() - 1; i >= 0; i--) {
         char c = roman.charAt(i);
         int currentValue;
         switch (c) {
            case 'I': currentValue = 1; break;
            case 'V': currentValue = 5; break;
            case 'X': currentValue = 10; break;
            case 'L': currentValue = 50; break;
            case 'C': currentValue = 100; break;
            case 'D': currentValue = 500; break;
            case 'M': currentValue = 1000; break;
            default: return 0; // Invalid character
         }
         if (currentValue < lastValue) {
            result -= currentValue;
         } else {
            result += currentValue;
         }
         lastValue = currentValue;
      }
      return result;
   }
}
