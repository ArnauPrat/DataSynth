package org.dama.datasynth.runtime;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.dama.datasynth.SparkEnv;
import org.dama.datasynth.common.Types;
import org.dama.datasynth.program.schnappi.ast.*;
import org.dama.datasynth.runtime.spark.MethodRef;
import org.dama.datasynth.runtime.spark.untyped.Function0Wrapper;
import org.dama.datasynth.runtime.spark.untyped.Function2Wrapper;
import org.dama.datasynth.runtime.spark.untyped.FunctionWrapper;
import org.dama.datasynth.runtime.spark.untyped.UntypedMethod;
import org.dama.datasynth.utils.Tuple;
import org.dama.datasynth.utils.TupleUtils;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by quim on 6/6/16.
 */
public class SchnappiInterpreter {
    private Map<String, JavaPairRDD<Long, Tuple>> rdds;
    private Map<String, Types.DATATYPE>  attributeTypes;
    private Map<String, Object> table;

    public SchnappiInterpreter() {
        rdds = new HashMap<String,JavaPairRDD<Long,Tuple>>();
        attributeTypes = new HashMap<String, Types.DATATYPE>();
        table = new HashMap<String, Object>();
    }
    public UntypedMethod execInit(FuncNode fn){
        String generatorName = fn.getChild(0).id;
        Generator generator = null;
        try {
            generator = (Generator)Class.forName(generatorName).newInstance();
        } catch (ClassNotFoundException cNFE) {
            cNFE.printStackTrace();
        } catch (InstantiationException iE) {
            iE.printStackTrace();
        } catch (IllegalAccessException iAE) {
            iAE.printStackTrace();
        } finally {
            //System.exit(1);
        }
        ArrayList<Object> params = new ArrayList<>();
        for(int i = 1; i < fn.children.size(); ++i) params.add(fn.getChild(i));
        UntypedMethod method = new UntypedMethod(generator,"initialize");
        method.invoke(params);
        return method;
        //return new MethodRef(generatorName, method);
    }
    public Object execAssig(AssigNode n) {
        return table.put(n.getChild(0).id, this.execExpr(n.getChild(1)));
    }
    public Object execExpr(Node n){
        return execAtom(n);
    }
    public Object execAtom(Node n){
        if(n instanceof AtomNode){
            return n.id;
        }else{
            return execFunc((FuncNode) n);
        }
    }
    public Object execFunc(FuncNode n){
        switch(n.type){
            case "map" : {
                return execMap(n);
            }
            case "union" : {
                return execUnion(n);
            }
            case "reduce" : {
                return execReduce(n);
            }
            case "genids" :{
                return execGenids(n);
            }
            default: {
                return null;
            }
        }
    }
    public JavaPairRDD<Long, Tuple> execMap(FuncNode fn) {
        Function<Tuple,Tuple> f = fetchFunction(fn.getChild(0).id, Integer.parseInt(fn.getChild(1).id));
        Object rd = fetchRDD(fn.getChild(1).id);
        JavaPairRDD<Long, Tuple> rdd = (JavaPairRDD<Long, Tuple>) rd;
        return rdd.mapValues(f);
    }
    public JavaPairRDD<Long, Tuple> execUnion(FuncNode fn){
        JavaRDD<Tuple2<Long, Tuple>> aux = SparkEnv.sc.emptyRDD();
        JavaPairRDD<Long,Tuple> result = JavaPairRDD.fromJavaRDD(aux);
        for(Node an : fn.children){
            Object rdd = execAtom(an);
            result.union((JavaPairRDD<Long,Tuple>)rdd);
        }
        result.reduceByKey(TupleUtils.join);
        return result;
    }
    public JavaPairRDD<Long, Tuple> execReduce(FuncNode fn) {
        Function<Tuple,Tuple> f = fetchFunction(fn.getChild(0).id, Integer.parseInt(fn.getChild(1).id));
        JavaRDD<Tuple2<Long, Tuple>> aux = SparkEnv.sc.emptyRDD();
        JavaPairRDD<Long,Tuple> result = JavaPairRDD.fromJavaRDD(aux);
        for(Node an : fn.children){
            Object rdd = execAtom(an);
            result.union((JavaPairRDD<Long,Tuple>)rdd);
        }
        /*new Function2<Long, Tuple, Tuple>() {
            @Override
            public Long call(Tuple a, Tuple b) throws Exception {
                Function2Wrapper<>
                return f(a,b);
            }
        };*/
        //result.reduceByKey(f);
        return result;
    }
    public JavaPairRDD<Long, Tuple> execEqjoin(FuncNode n){
        return null;
    }
    public JavaPairRDD<Long, Tuple> execGenids(FuncNode fn){
        ParamsNode pn = (ParamsNode) fn.getChild(0);
        int n = Integer.parseInt(pn.params.get(0));
        List<Long> init = new ArrayList<Long>();
        for(long i = 0; i < n; ++i) {
            init.add(i);
        }
        JavaRDD<Long> ids = SparkEnv.sc.parallelize(init);
        PairFunction<Long, Long, Tuple> f = (PairFunction<Long, Long, Tuple>) id -> new Tuple2<Long, Tuple>(id, new Tuple(id));
        JavaPairRDD<Long, Tuple> idss = ids.mapToPair(f);
        return idss;
    }
    private JavaPairRDD<Long, Tuple> fetchRDD(String str){
        return this.rdds.get(str);
    }
    private Function<Tuple, Tuple> fetchFunction(String generatorName, int numParams) {
        Generator generator = null;
        try {
            generator = (Generator)Class.forName(generatorName).newInstance();
        } catch (ClassNotFoundException cNFE) {
            cNFE.printStackTrace();
        } catch (InstantiationException iE) {
            iE.printStackTrace();
        } catch (IllegalAccessException iAE) {
            iAE.printStackTrace();
        } finally {
            //System.exit(1);
        }
        Function<Tuple, Tuple> fw = null;
        switch(numParams){
            case 0: {
                fw = new Function0Wrapper(generator, "run");
            }
            break;
            case 1: {
                fw = new FunctionWrapper(generator, "run");
            }
            break;
            case 2: {
                fw = new Function2Wrapper(generator, "run");
            }
            break;
            default:
                try {
                    throw new ExecutionException("Unsupported number of parameters");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        }
        return fw;
    }
    public Object fetchParameter(String param){
        if(table.get(param) != null) return table.get(param);
        else return param;
    }
}
