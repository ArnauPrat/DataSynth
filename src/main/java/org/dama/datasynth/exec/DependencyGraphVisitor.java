package org.dama.datasynth.exec;

import org.jgrapht.DirectedGraph;

import java.util.*;

/**
 * Created by aprat on 17/05/16.
 */
public abstract class DependencyGraphVisitor {

    public void visit(DependencyGraph dG) {
        DirectedGraph<Vertex,DEdge> graph = dG.getG();
        Set<Vertex> vertices = graph.vertexSet();
        for(Vertex v : vertices) {
            if(graph.outDegreeOf(v) == 0) {
               v.accept(this);
            }
        }
    }

    public abstract void visit(EntityTask entity);
    public abstract void visit(AttributeTask attribute);
    public abstract void visit(EdgeTask relation);
}