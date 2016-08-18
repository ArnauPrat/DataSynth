package org.dama.datasynth.lang;

import org.dama.datasynth.common.Types;
import java.util.*;

/**
 * Created by aprat on 10/04/16.
 */
public class Ast {

    /**
     * A node of the AST
     */
    public static class Node {

        /**
         * The name of the AST node
         */
        protected String           name       = null;

        /**
         * Class Constructor
         * @param name The name of the AST Node
         */
        public Node(String name) {
            this.name = name;
        }

        /**
         * Gets the name of the node
         * @return The name of the node
         */
        public String getName() {
            return name;
        }
    }

    /**
     * An attribute in the AST
     */
    public static class Attribute extends Node {

        /**
         * The data type of the attribute.
         */
        private Types.DATATYPE type;

        /**
         * The generator to generate this attribute
         */
        private Generator generator;

        /**
         * Class Constructor
         * @param name The name of the attribute.
         * @param type The data type of the attribute.
         */
        public Attribute(String name, Types.DATATYPE type, Generator generator) {
            super(name);
            this.type = type;
            this.generator = generator;
        }

        public Generator getGenerator() {
            return generator;
        }

        public Types.DATATYPE getType() {
            return type;
        }
    }

    /**
     * An entity in the AST
     */
    public static class Entity extends Node {

        /**
         * The list of attributes of the entity
         */
        protected List<Attribute>  attributes = new ArrayList<Attribute>();

        /**
         * The number of entities
         */
        protected Long numInstances;

        /**
         * Gets the number of entities
         * @return The number of entities
         */
        public Long getNumInstances() {
            return numInstances;
        }

        /** Class Constructor
         * @param name The name of the entity
         */
        public Entity(String name, Long numInstances) {
            super(name);
            this.numInstances = numInstances;
        }

        /**
         * Get the attributes of the entity
         * @return The attributes of the entity
         */
        public List<Attribute> getAttributes() { return attributes; }

        /**
         * Adds a new attribute to the entity
         * @param attribute The attribute to be added
         */
        public void addAttribute(Attribute attribute) { attributes.add(attribute);}


    }

    /**
     * A generator in the AST
     */
    public static class Generator extends Node {

        /**
         * The list of parameters of the init method of the generator.
         */
        private List<String> initParameters = new ArrayList<String>();

        /**
         * The list of parameters of the run method of the generator
         */
        private List<String> runParameters = new ArrayList<String>();

        /**
         * Class Constructor
         * @param name The name of the generator
         */
        public Generator(String name) {
            super(name);
        }

        /**
         * Gets the list of parameters of the run method for this generator
         */
        public List<String> getRunParameters() { return runParameters; }

        /**
         * Gets the list of parameters for the init method this generator
         */
        public List<String> getInitParameters() { return initParameters; }

        /**
         * Adds a run method parameter
         * @param parameter The parameter to add
         */
        public void addRunParameter(String parameter)  { runParameters.add(parameter);}

        /**
         * Adds an init method parameter
         * @param parameter The parameter to add
         */
        public void addInitParameter(String parameter)  { initParameters.add(parameter);}

    }

    /**
     * A generator in the AST
     */
    public static class Edge extends Node {
        private Entity origin;
        private Entity destination;
        private int[] cardinality;
        private List<Attribute> attributesOrigin;
        private List<Attribute> attributesTarget;
        private Generator generator;
        /**
         * Class Constructor
         * @param name The name of the generator
         */
        public Edge(String name) {
            super(name);
            this.cardinality = new int[2];
            this.attributesOrigin = new ArrayList<Attribute>();
            this.attributesTarget = new ArrayList<Attribute>();
        }

        public Edge(String name, Generator gen) {
            super(name);
            this.cardinality = new int[2];
            this.generator = gen;
            this.attributesOrigin = new ArrayList<Attribute>();
            this.attributesTarget = new ArrayList<Attribute>();
        }

        public Entity getOrigin() {
            return origin;
        }

        public void setOrigin(Entity origin) {
            this.origin = origin;
        }

        public Entity getDestination() {
            return destination;
        }

        public void setDestination(Entity destination) {
            this.destination = destination;
        }

        public int[] getCardinality() {
            return cardinality;
        }

        public void setCardinality(int cardinality, int i) {
            this.cardinality[i] = cardinality;
        }

        public void addAttributeOrigin(Attribute atr){
            this.attributesOrigin.add(atr);
        }
        public void addAttributeTarget(Attribute atr){
            this.attributesTarget.add(atr);
        }

        public List<Attribute> getAttributesOrigin() {
            return attributesOrigin;
        }

        public List<Attribute> getAttributesTarget() {
            return attributesTarget;
        }

        public List<Attribute> getAllAttributes(){
            ArrayList<Attribute> result = new ArrayList<>();
            result.addAll(this.attributesOrigin);
            result.addAll(this.attributesTarget);
            return result;
        }
        public String getGenerator(){ return generator.getName();}
    }

    /**
     * The list of entities of the AST 
     */
    private List<Entity> entities = new ArrayList<Entity>();

    /**
     * Gets the list of entities of the AST
     */
    public List<Entity> getEntities() { return entities; }

    /**
     * Adds a new entity into the AST
     */
    public void addEntity( Entity entity ) { entities.add(entity); }

    /**
     * The list of edges of the AST
     */
    private List<Edge> edges = new ArrayList<Edge>();

    /**
     * Adds a new edge into the AST
     */
    public void addEdge(Edge edg) { edges.add(edg); }
    /**
     * Gets the list of edges of the AST
     */
    public List<Edge> getEdges() { return edges; }

    /**
     * Adds a new edge into the AST
     */
    public void addEntity( Edge edg ) { edges.add(edg); }


    /**
     * Performs a semantic analysis over the AST.
     * Checks for valid parameter names for generators
     * @throws SemanticException
     */
    public void doSemanticAnalysis() throws SemanticException {
        Map<String,Set<String>> attributes = new HashMap<String,Set<String>>();
        for(Entity entity : entities ) {
            String entityName = entity.getName();
            Set<String> attributeNames = new TreeSet<String>();
            for( Attribute attribute : entity.getAttributes() ) {
                String attributeName = attribute.getName();
                if(attributeName.compareTo("oid") == 0) throw new SemanticException("Attribute name \"oid\" is reserved.");
                attributeNames.add(attributeName);
            }
            if(attributes.containsKey(entityName)) throw new SemanticException("Two entities with the same name: "+entityName+". Entity names must be unique");
            attributes.put(entityName,attributeNames);
        }

        for(Entity entity : entities ) {
            String entityName = entity.getName();
            for( Attribute attribute : entity.getAttributes() ) {
                Generator generator = attribute.getGenerator();
                for( String parameter : generator.getRunParameters()) {
                    if((parameter.compareTo("oid") != 0) && !attributes.get(entityName).contains(parameter)) {
                        throw new SemanticException("Entity "+entityName+" does not contain an attribute named "+parameter);
                    }
                }
            }
        }
    }
}
