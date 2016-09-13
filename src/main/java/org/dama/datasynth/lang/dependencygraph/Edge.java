package org.dama.datasynth.lang.dependencygraph;

import org.dama.datasynth.common.Types;

/**
 * Created by quim on 4/27/16.
 * Represents an edge element in the dependency graph.
 */
public class Edge extends Vertex {

    /**
     * Constructor
     * @param name The name of the edge
     * @param direction The direction of the edge
     */
    public Edge(String name, Types.Direction direction) {
        super();
        properties.put("name",new PropertyValue(new Types.Id(name,false)));
        properties.put("direction",new PropertyValue(direction.getText()));
    }

    /**
     * Gets the name of the edge
     * @return The name of the edge
     */
    public String getName() {
        return properties.get("name").getValue();
    }

    /**
     * Gets the direction of the edge
     * @return The direction of the edge.
     */
    public Types.Direction getDirection() {
        return Types.Direction.fromString(properties.get("direction").getValue());
    }

    @Override
    public void accept(DependencyGraphVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString(){
        return "[" + getName() + ","+getDirection().toString()+","+getClass().getSimpleName()+"]";
    }
}
