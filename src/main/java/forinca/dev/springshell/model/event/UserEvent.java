package forinca.dev.springshell.model.event;

public record UserEvent
    (String username,
     String password,
     String email)
{}
