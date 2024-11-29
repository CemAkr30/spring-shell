package forinca.dev.springshell.model.event;

public record RegisterEvent
    (String username,
     String password,
     String email)
{}
