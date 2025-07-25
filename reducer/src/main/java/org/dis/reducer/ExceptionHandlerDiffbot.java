package org.dis.reducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionHandlerDiffbot
{
   @ExceptionHandler(JsonProcessingException.class)
   public ResponseEntity<String> handleJsonProcessingError()
   {
      return ResponseEntity.internalServerError().body("Could not process JSON");
   }
}
