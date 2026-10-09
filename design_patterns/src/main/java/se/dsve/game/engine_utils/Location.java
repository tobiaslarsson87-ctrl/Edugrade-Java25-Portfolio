package se.dsve.game.engine_utils;

public enum Location {
    FORREST("You are in a forrest. It's dark and scary. You hear a sound from the bushes."),
    CAVE("You enter a cave. You hear a sound from the darkness."),
    CASTLE("You are in front of a castle. You hear a sound from the other side of the gate."),
    HELL("You are in hell. It's burning everywhere and smells like death. You hear a sound from the darkness.");

    private final String description;

    Location(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
