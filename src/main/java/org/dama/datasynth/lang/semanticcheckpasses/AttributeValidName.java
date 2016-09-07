package org.dama.datasynth.lang.semanticcheckpasses;

import org.dama.datasynth.common.Types;
import org.dama.datasynth.lang.Ast;
import org.dama.datasynth.lang.AstVisitor;
import org.dama.datasynth.lang.SemanticException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by aprat on 6/09/16.
 */
public class AttributeValidName extends AstVisitor<Ast.Node> {

    private Set<String> observedAttributes = new HashSet<String>();

    public void check(Ast ast) {
        for(Ast.Entity entity : ast.getEntities().values()) {
            entity.accept(this);
        }
    }

    @Override
    public Ast.Node visit(Ast.Entity entity) {
        for(Ast.Attribute attribute : entity.getAttributes().values()) {
            attribute.accept(this);
        }
        return entity;
    }


    @Override
    public Ast.Node visit(Ast.Attribute attribute) {
        if(observedAttributes.add(attribute.getName()) == false) throw new SemanticException(SemanticException.SemanticExceptionType.ATTRIBUTE_NAME_REPEATED,attribute.getName());
        if(attribute.getName().contains("oid")) throw new SemanticException(SemanticException.SemanticExceptionType.ATTRIBUTE_NAME_OID,"");
        return attribute;
    }
}
