package forinca.dev.springshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication(scanBasePackages = "forinca.dev.springshell")
public class SpringShellApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringShellApplication.class, args);
  }

}
