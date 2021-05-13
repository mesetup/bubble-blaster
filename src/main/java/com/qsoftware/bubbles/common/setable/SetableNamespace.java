package com.qsoftware.bubbles.common.setable;

import com.qsoftware.bubbles.common.Namespace;
import com.qsoftware.bubbles.core.exceptions.IllegalCharacterException;

public class SetableNamespace extends Namespace {
    public SetableNamespace(String namespace) throws IllegalCharacterException {
        super(namespace);
    }

    public String getNamespace() {
        return super.getNamespace();
    }

    public void setNamespace(String namespace) {
        super.setNamespace(namespace);
    }
}
