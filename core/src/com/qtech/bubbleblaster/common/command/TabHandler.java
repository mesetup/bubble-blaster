package com.qtech.bubbleblaster.common.command;

import java.util.List;

public interface TabHandler {
    List<String> tabComplete(String[] args);
}
