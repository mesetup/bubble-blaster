package com.qtech.bubbles.common.command.tabcomplete;

import com.qtech.bubbles.common.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public final class TabCompleter {
    private TabCompleter() {
        throw new UnsupportedOperationException("Not allowed to initialize TabCompleter.");
    }

    public static List<String> getStrings(String arg, String... strings) {
        ArrayList<String> list = new ArrayList<>();
        for (String str : strings) {
            addIfStartsWith(list, arg + " ", str);
        }
        return list;
    }

    public static List<String> getInts(String arg) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            list.add(arg + i);
        }
        return list;
    }

    public static List<String> getDecimals(String arg) {
        ArrayList<String> list = new ArrayList<>();
        list.add(arg + ".");
        for (int i = 0; i <= 9; i++) {
            list.add(arg + i);
        }
        return list;
    }

    @SuppressWarnings("UnusedReturnValue")
    private static List<String> addIfStartsWith(List<String> list, String arg, String startWith) {
        if (arg.startsWith(startWith)) {
            list.add(arg);
        }
        return list;
    }

    private static List<String> addIfStartsWith(List<String> list, ResourceLocation arg, String startWith) {
        if (arg.getPath().startsWith(startWith)) {
            list.add(arg.toString());
        } else if (arg.toString().startsWith(startWith)) {
            list.add(arg.toString());
        }
        return list;
    }
}
