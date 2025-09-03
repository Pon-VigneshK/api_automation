package org.op_ra.utils;

public class CurrentTestTracker {
    private static final ThreadLocal<String> CURRENT_TEST = new ThreadLocal<>();

    private CurrentTestTracker() {}

    public static String get() { return CURRENT_TEST.get(); }
    public static void set(String name) { CURRENT_TEST.set(name); }
    public static void clear() { CURRENT_TEST.remove(); }
}
