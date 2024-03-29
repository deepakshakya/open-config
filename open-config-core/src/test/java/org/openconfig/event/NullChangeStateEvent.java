package org.openconfig.event;

import org.openconfig.providers.ast.AbstractNode;
import org.openconfig.providers.ast.ComplexNode;

import java.util.Set;

/**
 * @author Richard L. Burton III
 */
public class NullChangeStateEvent implements ChangeStateEvent{
    
    public Set<String> getChangedPaths() {
        return null;
    }

    public ComplexNode getChangeState() {
        return null;
    }

    public ComplexNode getState() {
        return null;
    }

    public AbstractNode find(String property) {
        return null;
    }
}
