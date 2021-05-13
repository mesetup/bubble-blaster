package com.qsoftware.bubbles.common.interfaces;

import com.qsoftware.bubbles.core.exceptions.IllegalCharacterException;

public interface NamespaceHolder {
    String getNamespace();

    void setNamespace(String namespace) throws IllegalCharacterException;
}
