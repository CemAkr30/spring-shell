package forinca.dev.springshell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import forinca.dev.springshell.util.ShellReader;
import org.jline.reader.LineReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import forinca.dev.springshell.exception.ExceptionHandler;

@Configuration
public class ApplicationConfig
{
  @Bean
  ExceptionHandler exceptionHandler() {
    return new ExceptionHandler();
  }

  @Bean
  public ObjectMapper objectMapper(){
    return new ObjectMapper();
  }

  @Bean
  public ShellReader shellReader(@Lazy LineReader lineReader) {
    return new ShellReader(lineReader);
  }
}
