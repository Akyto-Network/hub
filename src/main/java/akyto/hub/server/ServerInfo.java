package akyto.hub.server;

import lombok.Getter;

@Getter
public class ServerInfo {

    private int playerCount;
    private boolean isWhitelisted;
    private boolean isOnline;

    public ServerInfo(int playerCount, boolean isWhitelisted, boolean isOnline) {
        this.playerCount = playerCount;
        this.isWhitelisted = isWhitelisted;
        this.isOnline = isOnline;
    }
}
