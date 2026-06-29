package com.flexilant;

import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import lombok.extern.slf4j.Slf4j;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
public class AllureListener extends AllureTestNg {

   @Override
   public void onTestStart(ITestResult result) {
      super.onTestStart(result);
      log.info("Starting test: {}.{}", result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName());
   }

   @Override
   public void onTestSuccess(ITestResult result) {
      super.onTestSuccess(result);
      log.info("Passed test: {}.{}", result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName());
   }

   @Override
   public void onTestFailure(ITestResult result) {
      attachTestMetadata(result);
      attachThrowable(result);
      super.onTestFailure(result);
      log.error("Failed test: {}.{}", result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName(), result.getThrowable());
   }

   @Override
   public void onTestSkipped(ITestResult result) {
      attachTestMetadata(result);
      attachThrowable(result);
      super.onTestSkipped(result);
      log.warn("Skipped test: {}.{}", result.getTestClass().getRealClass().getSimpleName(),
            result.getMethod().getMethodName());
   }

   private void attachTestMetadata(ITestResult result) {
      String metadata = String.format(
            "Test: %s%nClass: %s%nMethod: %s%nDescription: %s%nParameters: %s",
            result.getName(),
            result.getTestClass().getName(),
            result.getMethod().getMethodName(),
            result.getMethod().getDescription(),
            formatParameters(result)
      );
      Allure.addAttachment("Test Metadata", "text/plain", metadata);
   }

   private void attachThrowable(ITestResult result) {
      Throwable throwable = result.getThrowable();
      if (throwable == null) {
         return;
      }
      Allure.addAttachment("Failure Stack Trace", "text/plain", getStackTrace(throwable));
   }

   private String formatParameters(ITestResult result) {
      Object[] parameters = result.getParameters();
      if (parameters == null || parameters.length == 0) {
         return "none";
      }
      return Arrays.stream(parameters)
            .map(param -> param == null ? "null" : param.toString())
            .collect(Collectors.joining(", "));
   }

   private String getStackTrace(Throwable throwable) {
      StringWriter stringWriter = new StringWriter();
      throwable.printStackTrace(new PrintWriter(stringWriter));
      return stringWriter.toString();
   }
}
