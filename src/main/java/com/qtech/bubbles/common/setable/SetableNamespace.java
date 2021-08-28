package com.qtech.bubbles.common.setable;

import com.qtech.bubbles.common.Namespace;
import com.qtech.bubbles.core.exceptions.IllegalCharacterException;

/**
 * Same as {@link Namespace} but modifiable.
 *
 * @author Quinten
 * @since 1.0.0
 * @deprecated completely useless.
 */
@SuppressWarnings("ALL")
@Deprecated
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
