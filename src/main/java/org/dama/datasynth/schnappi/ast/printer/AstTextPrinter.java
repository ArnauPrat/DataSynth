package org.dama.datasynth.schnappi.ast.printer;

import org.dama.datasynth.DataSynth;
import org.dama.datasynth.schnappi.ast.*;
import org.dama.datasynth.schnappi.ast.Number;
import org.dama.datasynth.schnappi.ast.Visitor;
import org.dama.datasynth.schnappi.solver.Solver;

import java.util.logging.Logger;

/**
 * Created by aprat on 22/08/16.
 */
public class AstTextPrinter implements Visitor {

    private static final Logger logger= Logger.getLogger( DataSynth.class.getSimpleName() );


    @Override
    public void visit(Assign n) {
    }

    @Override
    public void visit(Binding n) {

    }

    @Override
    public void visit(Expression n) {

    }

    @Override
    public void visit(Function n) {

    }

    @Override
    public void visit(Parameters n) {

    }

    @Override
    public void visit(Signature n) {

    }

    @Override
    public void visit(Solver n) {

    }

    @Override
    public void visit(Operation n) {

    }

    @Override
    public void visit(Atomic n) {

    }

    @Override
    public void visit(Id n) {

    }

    @Override
    public void visit(StringLiteral n) {

    }

    @Override
    public void visit(Number n) {

    }
}