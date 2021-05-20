package com.qtech.bubbleblaster.common.setable;

import com.qtech.bubbleblaster.common.Namespace;
import com.qtech.bubbleblaster.core.exceptions.IllegalCharacterException;

public class SetableNamespace extends Namespace {
    public SetableNamespace(String namespace) throws IllegalCharacterException {
        super(namespace);
    }

    @SuppressWarnings("EmptyMethod")
    public String getNamespace() {
        return super.getNamespace();
    }

    public void setNamespace(String namespace) {
        super.setNamespace(namespace);
    }
}
