package org.dama.datasynth.program.schnappi.ast;

import org.dama.datasynth.program.schnappi.ast.visitor.Visitor;

/**
 * Created by aprat on 22/08/16.
 */
public class Literal extends Any {

    public Literal(String value) {
        super(value);
    }

    public Literal(Literal literal) {
        super(literal.getValue());
    }

    @Override
    public Literal copy() {
        return new Literal(this);
    }

    @Override
    public java.lang.String toString() {
        return "<literal,"+value+">";
    }
}