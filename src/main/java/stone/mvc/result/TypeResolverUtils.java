/**
 * 
 */
package stone.mvc.result;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author langhsu
 *
 */
public class TypeResolverUtils {
	
	/**
     * Returns raw class for given <code>type</code>. Use this method with both regular and generic types.
     *
     * @param type the type to convert
     * @return the closest class representing the given <code>type</code>
     * @see #getRawType(java.lang.reflect.Type, Class)
     */
    public static Class<?> getRawType(Type type) {
        return getRawType(type, null);
    }

    /**
     * Returns raw class for given <code>type</code> when implementation class is known
     * and it makes difference.
     * @see #resolveVariable(java.lang.reflect.TypeVariable, Class)
     */
    public static Class<?> getRawType(Type type, Class<?> implClass) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            return getRawType(pType.getRawType(), implClass);
        }
        if (type instanceof WildcardType) {
            WildcardType wType = (WildcardType) type;

            Type[] lowerTypes = wType.getLowerBounds();
            if (lowerTypes.length > 0) {
                return getRawType(lowerTypes[0], implClass);
            }

            Type[] upperTypes = wType.getUpperBounds();
            if (upperTypes.length != 0) {
                return getRawType(upperTypes[0], implClass);
            }

            return Object.class;
        }
        if (type instanceof GenericArrayType) {
            Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
            Class<?> rawType = getRawType(genericComponentType, implClass);

            return Array.newInstance(rawType, 0).getClass();
        }
        if (type instanceof TypeVariable) {
            TypeVariable<?> varType = (TypeVariable<?>) type;
            if (implClass != null) {
                Type resolvedType = resolveVariable(varType, implClass);
                if (resolvedType != null) {
                    return getRawType(resolvedType, null);
                }
            }
            Type[] boundsTypes = varType.getBounds();
            if (boundsTypes.length == 0) {
                return Object.class;
            }
            return getRawType(boundsTypes[0], implClass);
        }
        return null;
    }
    
    /**
     * Resolves <code>TypeVariable</code> with given implementation class.
     */
    public static Type resolveVariable(TypeVariable<?> variable, Class<?> implClass) {
        Class<?> rawType = getRawType(implClass, null);

        int index = ArrayUtils.indexOf(rawType.getTypeParameters(), variable);
        if (index >= 0) {
            return variable;
        }

        Class<?>[] interfaces = rawType.getInterfaces();
        Type[] genericInterfaces = rawType.getGenericInterfaces();

        for (int i = 0; i <= interfaces.length; i++) {
            Class<?> rawInterface;
            if (i < interfaces.length) {
                rawInterface = interfaces[i];
            } else {
                rawInterface = rawType.getSuperclass();
                if (rawInterface == null) {
                    continue;
                }
            }
            Type resolved = resolveVariable(variable, rawInterface);
            if (resolved instanceof Class || resolved instanceof ParameterizedType) {
                return resolved;
            }

            if (resolved instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) resolved;
                index = ArrayUtils.indexOf(rawInterface.getTypeParameters(), typeVariable);
                if (index < 0) {
                    throw new IllegalArgumentException("Can't resolve type variable:" + typeVariable);
                }
                Type type = i < genericInterfaces.length ? genericInterfaces[i] : rawType.getGenericSuperclass();
                if (type instanceof Class) {
                    return Object.class;
                }
                if (type instanceof ParameterizedType) {
                    return ((ParameterizedType) type).getActualTypeArguments()[index];
                }
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
        return null;
    }
}
