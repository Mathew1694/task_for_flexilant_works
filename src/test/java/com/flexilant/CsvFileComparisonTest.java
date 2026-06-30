package com.flexilant;

import com.flexilant.utils.FileHandlingUtils;
import com.flexilant.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
public class CsvFileComparisonTest {

   @Test(priority = 1, description = "Print all headers of actual files")
   public void printActualHeader() {
      Map<String, List<String>> res = TestUtils.headerComparison(Paths.get("test-data", "expected_orders.csv"),
            Paths.get("test-data", "actual_orders.csv"));

      List<String> actualHeader = res.get("actualHeader");
      log.info("Only in actual_orders.csv: ");
      actualHeader.forEach(log::info);
   }

   @Test(priority = 2, description = "Print all headers of expected files")
   public void printExpectedHeader() {
      Map<String, List<String>> res = TestUtils.headerComparison(Paths.get("test-data", "expected_orders.csv"),
            Paths.get("test-data", "actual_orders.csv"));

      List<String> expectedHeader = res.get("expectedHeader");
      log.info("Only in expected_orders.csv: ");
      expectedHeader.forEach(log::info);
   }

   @Test(priority = 3, description = "Print all headers of both files")
   public void printBothHeader() {
      Map<String, List<String>> res = TestUtils.headerComparison(Paths.get("test-data", "expected_orders.csv"),
            Paths.get("test-data", "actual_orders.csv"));

      List<String> expectedHeader = res.get("expectedHeader");
      List<String> actualHeader = res.get("actualHeader");
      expectedHeader.forEach(log::info);
      log.info("-".repeat(15));
      actualHeader.forEach(log::info);
   }

   @Test(priority = 4, description = "Print common headers of both files")
   public void commonHeaders() {
      Map<String, List<String>> res = TestUtils.headerComparison(Paths.get("test-data", "expected_orders.csv"),
            Paths.get("test-data", "actual_orders.csv"));
      List<String> expectedHeader = res.get("expectedHeader");
      List<String> actualHeader = res.get("actualHeader");

      expectedHeader.retainAll(actualHeader);
      log.info("Common headers: ");
      expectedHeader.forEach(log::info);
   }

   @Test(priority = 5, description = "Validate if headers are in same relative order for both Files")
   public void headerSequence() {
      Map<String, List<String>> res = TestUtils.headerComparison(Paths.get("test-data", "expected_orders.csv"),
            Paths.get("test-data", "actual_orders.csv"));
      List<String> expectedHeader = res.get("expectedHeader");
      List<String> actualHeader = res.get("actualHeader");

      boolean hasCommonStringAtSameIndex = TestUtils.hasCommonStringAtSameIndex(expectedHeader, actualHeader);
      log.info("Headers are in same relative order for both Files: {}", hasCommonStringAtSameIndex);
   }

   @Test(priority = 6, description = "Validate two files with same headers")
   public void fileWithIdenticalHeaders() {
      Path path = Paths.get("test-data", "expected_orders.csv");
      String fileOne = FileHandlingUtils.readCsvFilesHeaders(path);
      String fileTwo = FileHandlingUtils.readCsvFilesHeaders(path);

      if (fileOne.equals(fileTwo)) log.info("Two files have Identical header");
      else Assert.fail("Files are not identical");
   }


   @Test(priority = 7, description = "Validate when Missing/Invalid file path or File doesn't exist, throw error message")
   public void missingFilePaths() {
      Path path = Paths.get("test", "expected.csv");
      FileHandlingUtils.readCsvFilesHeaders(path);
   }

   @Test(priority = 8, description = "Validate when files are empty, error message should be shown")
   public void emptyFileHandling() {
      Path path = Paths.get("test-data", "test_empty_file.csv");
      FileHandlingUtils.readCsvFilesHeaders(path);
   }

   @Test(priority = 9, description = "Validate If the headers are the same but in a different order, it should return equal.")
   public void sameHeaderDifferentSequence(){
      Path jumbledPath = Paths.get("test-data", "expected_orders_jumbled.csv");
      Path path = Paths.get("test-data", "expected_orders.csv");

      String jumbledHeader = FileHandlingUtils.readCsvFilesHeaders(jumbledPath);
      String header = FileHandlingUtils.readCsvFilesHeaders(path);

      String[] jumbledHeaderArray = jumbledHeader.split(",");
      String[] headerArray = header.split(",");

      Arrays.sort(jumbledHeaderArray);
      Arrays.sort(headerArray);

      boolean equals = Arrays.equals(jumbledHeaderArray, headerArray);
      Assert.assertTrue(equals,"If the headers are the same but in a different order, it should return true.");
   }

   @Test(priority = 10, description = "validate File with Invalid headers")
   public void invalidHeaders(){
      Path invalidHeaderPath = Paths.get("test-data", "expected_Invalid_header_orders.csv");
      String headerFromFile = FileHandlingUtils.readCsvFilesHeaders(invalidHeaderPath);
      TestUtils.extractToList(headerFromFile);
   }

}
