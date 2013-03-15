package de.fisp.anwesenheit.core.domain;

public class AddBewilligungCommand {
    private long antragId;
    private String benutzerId;

    public AddBewilligungCommand(long antragId, String benutzerId) {
        super();
        this.antragId = antragId;
        this.benutzerId = benutzerId;
    }

    public long getAntragId() {
        return antragId;
    }

    public String getBenutzerId() {
        return benutzerId;
    }
}
