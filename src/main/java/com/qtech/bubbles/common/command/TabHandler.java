package com.qtech.bubbles.common.command;

import java.util.List;

public interface TabHandler {
    List<String> tabComplete(String[] args);
}
