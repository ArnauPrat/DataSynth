package org.dama.datasynth.program.schnappi;

import org.dama.datasynth.lang.dependencygraph.*;
import org.dama.datasynth.lang.dependencygraph.Literal;
import org.dama.datasynth.program.Ast;
import org.dama.datasynth.program.schnappi.ast.*;
import org.dama.datasynth.program.schnappi.ast.Number;
import org.dama.datasynth.program.solvers.Loader;
import org.dama.datasynth.program.solvers.Solver;

import java.util.*;

/**
 * Created by quim on 5/5/16.
 */
public class Compiler extends DependencyGraphVisitor {

    private Map<String, Solver>     solversDB           = new TreeMap<String,Solver>( new Comparator<String>() {
            public int compare( String a, String b) {
            return a.compareToIgnoreCase(b);
        }
        });
    private Ast                     program             = new Ast();
    private Set<String>             generatedVertices   = new HashSet<String>();

    public Compiler(DependencyGraph graph, String dir){
        super(graph);
        loadSolvers(dir);
    }

    private void loadSolvers(String dir){
        for(Solver s : Loader.loadSolvers(dir)) {
            this.solversDB.put(s.getSignature().getSource(),s);
        }
    }

    public void synthesizeProgram(){
        for(Vertex v : graph.getEntities()) {
            v.accept(this);
        }
    }

    private void solveVertex(Vertex v) throws CompilerException {
        Solver s = this.solversDB.get(v.getType());
        if(s == null) throw new CompilerException("Unsolvable program. No solver for type "+v.getType());
        this.concatenateProgram(s.instantiate(v));
    }

    private void solveEdge(DirectedEdge e) throws CompilerException {
        /*Solver s = this.solversDB.get(e.getSignature());
        if(s == null) throw new CompilerException("Unsolvable program");
        */
        //this.concatenateProgram(s.instantiate(e));
        //cool stuff happening here
        //this.merge(solversDB.get(e.getSignature()).instantiate(e.getSource(), e.getTarget()));
        // this.program.appendSomeStuffSomePlace(
    }

    private void concatenateProgram(Ast p){
        List<Operation> statements = p.getStatements();
        for(Operation statement : statements){
            this.program.addStatement(statement);
        }
    }

    /*private void addIncomingCartesianProduct(Vertex v, DependencyGraph g, String suffix){
        Set<DirectedEdge> edges = g.outgoingEdgesOf(v);
        Parameters parameters = new Parameters();
        long index = 0;
        for(DirectedEdge e : edges){
            parameters.addParam( new Id(graph.getEdgeTarget(e).getId()+".filtered["+index+"]"));
            ++index;
        }
        Function function = new Function("cartesian", parameters);
        Assign assign = new Assign(new Id(v.getId() + suffix),function);
        this.program.addStatement(assign);
    }*/

    /*private void addFilters(Edge v, DependencyGraph g){
        Set<DirectedEdge> edges = g.outgoingEdgesOf(v);
        long index = 0;
        for(DirectedEdge e : edges){
            addFilter(v, v.getAttributesByName(graph.getEdgeTarget(e).getId()), graph.getEdgeTarget(e).getId(), index);
            ++index;
        }
    }*/

    /*private void addFilter(Edge v, List<Attribute> attrs, String entityName, long ind){
        Parameters parameters = new Parameters();
        for(Attribute attr : attrs){
            parameters.addParam(new Id(attr.getId()));
        }
        Function function = new Function("filter",parameters);
        Assign assign = new Assign(new Id(entityName + ".filtered["+ind+"]"),function);
        this.program.addStatement(assign);
    }*/

    public Ast getProgram() {
        return this.program;
    }

    public void setProgram(Ast program) {
        this.program = program;
    }

    private void merge(Solver solver){
        program.merge(solver.getOperations());
    }

    @Override
    public void visit(Entity entity) {
        if(!generatedVertices.contains(entity.getId())) {
            for(Vertex neighbor : graph.getNeighbors(entity)) {
                neighbor.accept(this);
            }
            generatedVertices.add(entity.getName());
            try {
                solveVertex(entity);
            } catch (CompilerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void visit(Attribute attribute) {
        if(!generatedVertices.contains(attribute.getId())) {
            for(Vertex neighbor : graph.getNeighbors(attribute)) {
                neighbor.accept(this);
            }
            if (!attribute.getAttributeName().contains(".oid")) {
                try {
                    solveVertex(attribute);
                } catch (CompilerException e) {
                    e.printStackTrace();
                }
            } else {
                String entityName = attribute.getAttributeName().substring(0,attribute.getAttributeName().indexOf(".")-1);
                Parameters parameters = new Parameters(new Number(String.valueOf(graph.getEntity(entityName).getNumInstances())));
                Function function = new Function("genids", parameters);
                Assign assign = new Assign(new Id(attribute.getAttributeName()), function);
                this.program.addStatement(assign);
            }
            generatedVertices.add(attribute.getAttributeName());
        }
    }

    @Override
    public void visit(Edge edge) {
        if(!generatedVertices.contains(edge.getId())) {
            for(Vertex neighbor : graph.getNeighbors(edge)) {
                neighbor.accept(this);
            }
            /*addFilters(edge, graph);
            addIncomingCartesianProduct(edge, graph, ".input");
            */
            try {
                solveVertex(edge);
            } catch (CompilerException e) {
                e.printStackTrace();
            }
            generatedVertices.add(edge.getName());
        }
    }

    @Override
    public void visit(Generator generator) {
        throw new CompilerException("Method visit Generator in compiler not implemented.");
    }

    @Override
    public void visit(Literal literal) {
        throw new CompilerException("Method visit Literal in compiler not implemented.");
    }
}
