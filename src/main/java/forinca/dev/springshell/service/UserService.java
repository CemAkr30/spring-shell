package forinca.dev.springshell.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import forinca.dev.springshell.dao.domain.User;
import forinca.dev.springshell.dao.repository.UserRepository;
import forinca.dev.springshell.exception.UserAlreadyExistsException;
import forinca.dev.springshell.exception.UserNotFoundException;
import forinca.dev.springshell.model.event.LoginEvent;
import forinca.dev.springshell.model.event.RegisterEvent;
import forinca.dev.springshell.model.event.UserEvent;
import forinca.dev.springshell.util.ShellPrinter;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final ShellPrinter shellPrinter;
  private final ObjectMapper objectMapper;

  public UserService(UserRepository userRepository,
      ShellPrinter shellPrinter, ObjectMapper objectMapper) {
    this.userRepository = userRepository;
    this.shellPrinter = shellPrinter;
    this.objectMapper = objectMapper;
  }

  public boolean register(final RegisterEvent registerEvent)
      throws JsonProcessingException {
    boolean result = false;
    var newUser = new User();
    newUser.setUsername(registerEvent.username());
    newUser.setEmail(registerEvent.email());
    newUser.setPassword(registerEvent.password());

    Optional<User> userExists = userRepository.findByUsername(newUser.getUsername());

    if (userExists.isPresent()){
      throw new UserAlreadyExistsException("User already exists");
    }

    try {
        userRepository.save(newUser);
        result = true;
        shellPrinter.printSuccess(String.format(
            "User registered: {}", objectMapper.writeValueAsString(registerEvent)
        ));
    }catch (Exception e){
      shellPrinter.printError(String.format(
          "Error registering user: {}", e.getMessage()
      ));
      throw new RuntimeException("Error registering user");
    }

    return result;
  }

  public boolean login(final LoginEvent loginEvent)
      throws JsonProcessingException {
    boolean result = false;

    Optional<User> userExists = userRepository.findByUsernameAndPassword(
        loginEvent.username(),
        loginEvent.password()
    );

    if (userExists.isEmpty()){
      throw new UserNotFoundException("User not found");
    }

    result = true;
    shellPrinter.printSuccess(String.format(
        "User logged in: {}", objectMapper.writeValueAsString(loginEvent)
    ));

    return result;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public Optional<User> getUserById(Long id) {
    return userRepository.findById(id);
  }

  public boolean deleteUserById(Long id) {
    boolean result = false;
    Optional<User> userExists = userRepository.findById(id);
    if (userExists.isEmpty()) {
      throw new UserNotFoundException("User not found");
    }

    userRepository.deleteById(id);
    result = true;
    return result;
  }

  public boolean updateUserById(Long id, UserEvent userEvent) {
    boolean result = false;
    Optional<User> userExists = userRepository.findById(id);
    if (userExists.isEmpty()) {
      throw new UserNotFoundException("User not found");
    }

    User user =  userExists.get();
    user.setUsername(userEvent.username());
    user.setEmail(userEvent.email());
    user.setPassword(userEvent.password());

    userRepository.save(user);
    result = true;
    return result;
  }

  public boolean createUser(UserEvent userEvent){
    boolean result = false;
    var newUser = new User();
    newUser.setUsername(userEvent.username());
    newUser.setEmail(userEvent.email());
    newUser.setPassword(userEvent.password());

    try {
      userRepository.save(newUser);
      result = true;
      shellPrinter.printSuccess(String.format(
          "User created: {}", objectMapper.writeValueAsString(userEvent)
      ));
    }catch (Exception e){
      shellPrinter.printError(String.format(
          "Error creating user: {}", e.getMessage()
      ));
      throw new RuntimeException("Error creating user");
    }

    return result;
  }
}
