package trade.wayruha.hyperliquid.websocket;

public enum WSState {
    IDLE,
    DELAY_CONNECT,
    CONNECTED,
    CLOSED_ON_ERROR,
    CONNECTING
}
