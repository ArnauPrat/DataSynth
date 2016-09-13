package org.dama.datasynth.schnappi;

import org.dama.datasynth.common.Types;
import org.dama.datasynth.lang.dependencygraph.*;
import org.dama.datasynth.lang.dependencygraph.Literal;
import org.dama.datasynth.schnappi.ast.Ast;
import org.dama.datasynth.schnappi.ast.Number;
import org.dama.datasynth.schnappi.solver.Loader;
import org.dama.datasynth.schnappi.solver.Solver;

import java.util.*;

/**
 * Created by quim on 5/5/16.
 * The Chnappi compilers that creates a Schnappi program from a dependency graph
 */
public class Compiler extends DependencyGraphVisitor {

    private Map<String, Solver>     solversDB           = new TreeMap<String,Solver>( new Comparator<String>() {
            public int compare( String a, String b) {
            return a.compareToIgnoreCase(b);
        }
        });
    private Ast program             = new Ast();
    private Set<Long>             generatedVertices   = new HashSet<Long>();

    /**
     * Constructor
     * @param graph The dependency graph to build the Schnappi program from
     * @param dir The directory to locate the solvers
     */
    public Compiler(DependencyGraph graph, String dir){
        super(graph);
        loadSolvers(dir);
    }

    /**
     * Loads the solvers
     * @param dir The folder containing the solvers
     */
    private void loadSolvers(String dir){
        for(Solver s : Loader.loadSolvers(dir)) {
            this.solversDB.put(s.getSignature().getBindings().values().iterator().next(),s);
        }
    }


    /**
     * Synthetizes the Schnappi program
     */
    public void synthesizeProgram(){
        for(Entity v : graph.getEntities()) {
            visit(v);
        }

        for(Edge v : graph.getEdges()) {
            visit(v);
        }
    }

    /**
     * Solves a vertex
     * @param v The vertex to solve
     * @throws CompilerException
     */
    private void solveVertex(Vertex v) throws CompilerException {
        Solver s = this.solversDB.get(v.getType());
        if(s == null || !s.eval(graph,v)) throw new CompilerException(CompilerException.CompilerExceptionType.UNSOLVABLE_PROGRAM, "No solver for type "+v.getType());
        this.concatenateProgram(s.instantiate(graph,v));
    }

    /**
     * Concatenates an existing Ast to the current program ast
     * @param ast The ast to concatenate
     */
    private void concatenateProgram(Ast ast){
        List<org.dama.datasynth.schnappi.ast.Operation> statements = ast.getOperations();
        for(org.dama.datasynth.schnappi.ast.Operation statement : statements){
            this.program.addOperation(statement);
        }
    }

    /**
     * Gets the synthetized program Ast
     * @return The ast of the synthetized program
     */
    public Ast getProgram() {
        return this.program;
    }

    @Override
    public void visit(Entity entity) {
        if(!generatedVertices.contains(entity.getId())) {
            for(Vertex neighbor : graph.getNeighbors(entity)) {
                neighbor.accept(this);
            }
            generatedVertices.add(entity.getId());
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
            if (!attribute.getName().contains(".oid")) {
                for(Vertex neighbor : graph.getNeighbors(attribute)) {
                    neighbor.accept(this);
                }
                try {
                    solveVertex(attribute);
                } catch (CompilerException e) {
                    e.printStackTrace();
                }
            } else {
                String entityName = attribute.getName().substring(0,attribute.getName().indexOf("."));
                org.dama.datasynth.schnappi.ast.Parameters parameters = new org.dama.datasynth.schnappi.ast.Parameters(new Number(String.valueOf(graph.getEntity(entityName).getNumInstances()), Types.DataType.LONG));
                org.dama.datasynth.schnappi.ast.Function function = new org.dama.datasynth.schnappi.ast.Function("genids", parameters);
                org.dama.datasynth.schnappi.ast.Assign assign = new org.dama.datasynth.schnappi.ast.Assign(new org.dama.datasynth.schnappi.ast.Id(attribute.getName(),new Boolean(attribute.getIsTemporal())), function);
                this.program.addOperation(assign);
            }
            generatedVertices.add(attribute.getId());
        }
    }

    @Override
    public void visit(Edge edge) {
        if(!generatedVertices.contains(edge.getId())) {
            for(Vertex neighbor : graph.getNeighbors(edge)) {
                neighbor.accept(this);
            }
            try {
                solveVertex(edge);
            } catch (CompilerException e) {
                e.printStackTrace();
            }
            generatedVertices.add(edge.getId());
        }
    }

    @Override
    public void visit(Generator generator) {
        for(Vertex vertex : graph.getNeighbors(generator,"requires")) {
            vertex.accept(this);
        }
    }

    @Override
    public void visit(Literal literal) {
        throw new RuntimeException("Method visit Literal in compiler not implemented.");
    }
}
