package org.dama.datasynth.lang.dependencygraph.builder;

import org.dama.datasynth.lang.Ast;
import org.dama.datasynth.lang.AstVisitor;
import org.dama.datasynth.lang.dependencygraph.*;

import java.util.List;

/**
 * Created by aprat on 4/09/16.
 */
public class CrossDependenciesInserter extends AstVisitor<Ast.Node> {

    private DependencyGraph graph = null;


    public CrossDependenciesInserter() {
    }


    public void run(DependencyGraph graph, Ast ast) {
        this.graph = graph;
        for(Ast.Entity entity : ast.getEntities().values()) {
            entity.accept(this);
        }

        for(Ast.Edge edge : ast.getEdges().values()) {
            edge.accept(this);
        }
    }

    private void solveGeneratorOidDependency(Vertex vertex, Ast.Generator astGenerator, String generatorRelationName, String entityName) {
        List<Vertex> neighbors = graph.getNeighbors(vertex,generatorRelationName);
        Generator generator = (Generator)(neighbors.get(0));
        graph.addDependency(generator,graph.getAttribute(entityName+".oid"),"runParameter");
    }

    private void solveGeneratorDependencies(Vertex vertex, Ast.Generator astGenerator, String generatorRelationName) {
        List<Vertex> neighbors = graph.getNeighbors(vertex,generatorRelationName);
        Generator generator = (Generator)(neighbors.get(0));
        for(Ast.Atomic param : astGenerator.getRunParameters()) {
            Attribute attributeParameter = graph.getAttribute(param.getName());
            graph.addDependency(generator,attributeParameter,"runParameter");
        }
    }

    @Override
    public Ast.Entity visit(Ast.Entity astEntity) {
        for(Ast.Attribute astAttribute : astEntity.getAttributes().values()) {
            astAttribute.accept(this);
        }
        return astEntity;
    }

    @Override
    public Ast.Edge visit(Ast.Edge astEdge) {
        Edge edge = graph.getEdge(astEdge.getName());
        if(astEdge.getSourceCardinalityGenerator() != null) {
            solveGeneratorOidDependency(edge,astEdge.getSourceCardinalityGenerator(),"sourceCardinality",astEdge.getSource());
            solveGeneratorDependencies(edge,astEdge.getSourceCardinalityGenerator(),"sourceCardinality");
        }

        if(astEdge.getTargetCardinalityGenerator() != null) {
            solveGeneratorOidDependency(edge,astEdge.getSourceCardinalityGenerator(),"targetCardinality",astEdge.getSource());
            solveGeneratorDependencies(edge,astEdge.getTargetCardinalityGenerator(),"targetCardinality");
        }

        solveGeneratorOidDependency(edge,astEdge.getCorrellation(),"correllation",astEdge.getSource());
        if(astEdge.getTarget().compareTo(astEdge.getSource()) != 0) {
            solveGeneratorOidDependency(edge,astEdge.getCorrellation(),"correllation",astEdge.getTarget());
        }
        solveGeneratorDependencies(edge,astEdge.getCorrellation(),"correllation");
        return astEdge;
    }


    @Override
    public Ast.Attribute visit(Ast.Attribute astAttribute) {
        Attribute attribute = graph.getAttribute(astAttribute.getName());
        Entity entity = (Entity)graph.getIncomingNeighbors(attribute,"attribute").get(0);
        solveGeneratorOidDependency(attribute,astAttribute.getGenerator(),"generator",entity.getName());
        solveGeneratorDependencies(attribute,astAttribute.getGenerator(),"generator");
        return astAttribute;
    }

}