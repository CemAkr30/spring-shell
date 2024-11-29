package forinca.dev.springshell.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import forinca.dev.springshell.dao.domain.User;
import forinca.dev.springshell.formatter.UserFormatter;
import forinca.dev.springshell.model.event.LoginEvent;
import forinca.dev.springshell.model.event.RegisterEvent;
import forinca.dev.springshell.model.event.UserEvent;
import forinca.dev.springshell.service.UserService;
import forinca.dev.springshell.util.ShellPrinter;
import forinca.dev.springshell.util.ShellReader;
import java.util.Collections;
import java.util.Optional;
import org.springframework.shell.command.annotation.Command;

@Command(group = "Auth Command")
public class AuthCommand
{
  private final UserService userService;
  private final UserFormatter userFormatter;
  private final ShellPrinter shellPrinter;
  private final ShellReader shellReader;

  public AuthCommand(
      UserService userService, UserFormatter userFormatter, ShellPrinter shellPrinter,
      ShellReader shellReader
  ) {
    this.userService = userService;
    this.userFormatter = userFormatter;
    this.shellPrinter = shellPrinter;
    this.shellReader = shellReader;
  }

  @Command(command = "register")
  public void register() throws JsonProcessingException {
    var username = shellReader.readLine("username");
    var email = shellReader.readLine("email");
    var password = shellReader.readLinePassword("pass");


    var registerEvent = new RegisterEvent(username,password,email);
    var result = userService.register(registerEvent);

    if (result)
      shellPrinter.printSuccess("successfully registered");
    else
      shellPrinter.printError("registration failed");
  }

  @Command(command = "login")
  public void login() throws JsonProcessingException {
    var username = shellReader.readLine("username");
    var password = shellReader.readLine("pass");


    var loginEvent = new LoginEvent(username,password);
    var result = userService.login(loginEvent);

    if (result)
      shellPrinter.printSuccess("successfully authenticated");
    else
      shellPrinter.printError("authentication failed");
  }

  @Command(command = "getAllUsers")
  public void getAllUsers() {
    var users = userService.getAllUsers();
    shellPrinter.print(userFormatter.convertToTable(users));
  }

  @Command(command = "deleteUserById")
  public void deleteUserById() {
    var id = shellReader.readLine("id");
    var result = userService.deleteUserById(Long.valueOf(id));
    if (result)
      shellPrinter.printSuccess("successfully deleted");
    else
      shellPrinter.printError("deletion failed");
  }

  @Command(command = "updateUserById")
  public void updateUserById() {
    var id = shellReader.readLine("id");
    var username = shellReader.readLine("username");
    var email = shellReader.readLine("email");
    var password = shellReader.readLine("pass");

    UserEvent userEvent = new UserEvent(username,password,email);

    var result = userService.updateUserById(
        Long.valueOf(id),
        userEvent
    );
    if (result)
      shellPrinter.printSuccess("successfully updated");
    else
      shellPrinter.printError("update failed");
  }

  @Command(command = "createUser")
  public void createUser() {
    var username = shellReader.readLine("username");
    var email = shellReader.readLine("email");
    var password = shellReader.readLine("pass");

    UserEvent userEvent = new UserEvent(username,password,email);

    var result = userService.createUser(userEvent);
    if (result)
      shellPrinter.printSuccess("successfully created");
    else
      shellPrinter.printError("creation failed");
  }

  @Command(command = "getUserById")
  public void getUserById(){
    var id = shellReader.readLine("id");
    Optional<User> user = userService.getUserById(Long.valueOf(id));

    shellPrinter.print(userFormatter.convertToTable(
        Collections.singletonList(user.get())
    ));
  }


  @Command(command = "logout")
  public void logout() {
    shellPrinter.printSuccess("successfully logged out");
  }
}
