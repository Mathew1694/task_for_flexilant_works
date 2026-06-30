package com.flexilant.utils;

import com.flexilant.exceptions.ValueNotFoundExceptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
public class TestUtils {
   private TestUtils() {
   }

   public static Map<String, List<String>> headerComparison(Path expected, Path actual) {
      String expectedHeader = StringUtils.deleteWhitespace(FileHandlingUtils.readCsvFilesHeaders(expected));
      String actualHeader = StringUtils.deleteWhitespace(FileHandlingUtils.readCsvFilesHeaders(actual));

      if (isNullOrBlank(expectedHeader)|| isNullOrBlank(actualHeader))
         throw new ValueNotFoundExceptions("Empty Files or No Values present.");

      List<String> expectedHeaderList = extractToList(expectedHeader);
      List<String> actualHeaderList = extractToList(actualHeader);
      return Map.of("expectedHeader", expectedHeaderList, "actualHeader", actualHeaderList);
   }

   public static boolean isNullOrBlank(String s) {
      return s == null || s.isBlank();
   }

   public static boolean hasCommonStringAtSameIndex(List<String> expectedHeader, List<String> actualHeader) {
      for (String value : expectedHeader) {
         if (actualHeader.contains(value) &&
               expectedHeader.indexOf(value) == actualHeader.indexOf(value)) {
            return true;
         }
      }
      return false;
   }

   public static List<String> extractToList(String s){
      if(isNullOrBlank(s))throw new IllegalArgumentException("Input cannot be null or Empty");
      String[] split = s.split(",");
      return Arrays.stream(split)
            .map(String::trim)
            .map(x-> {
               String sanitized = x.replaceAll("[^a-zA-Z]", "");
               if(sanitized.isBlank()) throw new IllegalArgumentException("Sanitized header is either empty or not valid.");
               return sanitized;
            })
            .collect(Collectors.toCollection(() -> new LinkedList<String>()));
   }

}
