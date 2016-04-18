package org.dama.datasynth.common;

import org.dama.datasynth.runtime.Generator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * Created by aprat on 17/04/16.
 */
public class Types {

    public enum DATATYPE {
        INTEGER(Integer.class),
        LONG(Long.class),
        STRING(String.class),
        FLOAT(Float.class),
        DOUBLE(Double.class),
        BOOLEAN(Boolean.class);

        private Class typeData;

        DATATYPE(Class typeData) {
            this.typeData = typeData;
        }

        public String getText() {
            return typeData.getSimpleName();
        }

        public static DATATYPE fromString(String text) {
            if (text != null) {
                for (DATATYPE b : DATATYPE.values()) {
                    if (text.equalsIgnoreCase(b.getText())) {
                        return b;
                    }
                }
            }
            return null;
        }
    }

    public static Generator Generator(String name) {
        try {
            return (Generator) Class.forName(name).newInstance();
        } catch(ClassNotFoundException cNFE) {
            cNFE.printStackTrace();
        } catch(InstantiationException iE) {
            iE.printStackTrace();
        } catch(IllegalAccessException iAE) {
            iAE.printStackTrace();
        }
        return null;
    }

    public static Method GetMethod(Generator generator, String methodName, List<DATATYPE> parameterTypes) throws CommonException {
        Method[] methods = generator.getClass().getMethods();
        for(Method m : methods) {
            String mName = m.getName();
            if(mName.compareTo(methodName)==0) {
                boolean match = true;
                if(m.getParameters().length != parameterTypes.size())
                    continue;
                int index = 0;
                for(Parameter param : m.getParameters()) {
                    String paramType = param.getType().getSimpleName();
                    if(paramType.compareTo(parameterTypes.get(index).getText()) != 0) {
                        match = false;
                        break;
                    }
                    index++;
                }
                if(match) return m;
            }
        }
        String paramsString = new String();
        for(DATATYPE param : parameterTypes) {
            paramsString = paramsString+","+param.getText();
        }
        throw new CommonException("Method "+methodName+" with paramters "+parameterTypes.size()+" parameters <"+paramsString+"> not found");
    }
}
