package nsl.webmapia.game.common;

public enum NumberConstants {
    DEFAULT_QUERY_PAGE_SIZE(10);

    private final int value;

    NumberConstants(int value) {
        this.value = value;
    }

    public int get() {
        return this.value;
    }
}
