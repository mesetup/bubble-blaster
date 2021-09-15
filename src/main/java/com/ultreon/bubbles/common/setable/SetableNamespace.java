package com.ultreon.bubbles.common.setable;

import com.ultreon.bubbles.common.Namespace;
import com.ultreon.bubbles.common.exceptions.IllegalCharacterException;

/**
 * Same as {@link Namespace} but modifiable.
 *
 * @author Qboi
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
