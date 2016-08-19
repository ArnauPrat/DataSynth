package org.dama.datasynth.exec;

import org.dama.datasynth.common.Types;
import org.dama.datasynth.lang.Ast;
import org.dama.datasynth.utils.traversals.Visitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aprat on 20/04/16.
 */
public class AttributeTask extends Vertex  implements ExecutableVertex{

    private Ast.Entity      entity          = null;
    private Ast.Attribute   attribute       = null;
    private String          generator       = null;
    private List<String>    runParameters   = new ArrayList<String>();
    private List<String>    initParameters  = new ArrayList<String>();

    /**
     * Class constructor
     * @param graph The dependency graph this vertex belongs to
     * @param entity The entity this task is generating something for
     * @param attribute The attribute this task is generating
     */
    public AttributeTask(DependencyGraph graph, Ast.Entity entity, Ast.Attribute attribute ) {
        super(graph, entity.getName()+"."+attribute.getName(),"attribute");
        this.entity = entity;
        this.attribute = attribute;
        this.generator = attribute.getGenerator().getName();
        for( String param : attribute.getGenerator().getRunParameters()) {
            this.runParameters.add(param);
        }

        for( String param : attribute.getGenerator().getInitParameters()) {
            this.initParameters.add(param);
        }
    }

    /**
     * Gets the generator of this attribute task
     * @return The name of the generator of this attribute task
     */
    public String getGenerator() {
        return generator;
    }


    /**
     * Gets the initialize method parameters
     * @return The list of parameter types of the initialize method
     */
    public List<String> getInitParameters() {
        return initParameters;
    }

    /**
     * Gets the entity this task is generating something for
     * @return The entity
     */
    public Ast.Entity getEntity() {
        return entity;
    }

    /**
     * Gets the name of the attribute this task is generating something for
     * @return The attribute
     */
    public String getAttributeName() {
        return attribute.getName();
    }

    /**
     * Gets the type of the attribute this task is generating something for
     * @return The attribute
     */
    public Types.DATATYPE getAttributeType() {
        return attribute.getType();
    }

    /**
     * Gets the parameters of the run method of the generator
     * @return The run parameters of the generator
     */
    public List<String> getRunParameters() {
        return runParameters;
    }


    @Override
    public void accept(Visitor visitor) {
        DependencyGraphVisitor  dGraphVisitor = (DependencyGraphVisitor)(visitor);
        dGraphVisitor.visit(this);
    }
}
