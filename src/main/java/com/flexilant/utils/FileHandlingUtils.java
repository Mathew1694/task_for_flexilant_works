package com.flexilant.utils;

import com.flexilant.exceptions.ValueNotFoundExceptions;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class FileHandlingUtils {

   private FileHandlingUtils() {
   }

   public static String readCsvFilesHeaders(Path path) {
      try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
         String line = br.readLine();
         if (line == null) throw new ValueNotFoundExceptions("Empty file, No contents present in file.");
         else return line;
      } catch (FileNotFoundException er) {
         throw new ValueNotFoundExceptions("File not found in the specified folder path.");
      }catch (IOException e){
         e.printStackTrace();
         log.error("Error Reading csv File...", e);
      }
      return null;
   }

}
