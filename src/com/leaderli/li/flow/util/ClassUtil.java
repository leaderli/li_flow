package com.leaderli.li.flow.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

public class ClassUtil {

	public static Type[] getGenericSuperclassActualTypeArguments(Class<?> klass) {
		ParameterizedType paramterizedType = (ParameterizedType) klass.getGenericSuperclass();
		Type[] klasses = paramterizedType.getActualTypeArguments();
		return klasses;
	}

	public static Class<?> getGenericSuperclassActualTypeArgument(Class<?> klass) {
		Type[] klasses = getGenericSuperclassActualTypeArguments(klass);
		Assert.isTrue(klasses != null && klasses.length == 1);
		try {
			return Class.forName(klasses[0].getTypeName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getGenericSuperclassActualTypeArgument(Object obj) {
		return (Class<T>) getGenericSuperclassActualTypeArgument(obj.getClass());
	}


	public static List<ParameterizedType> getAnsestorGenericsInterfaces(Class<?> obj) {

		List<ParameterizedType> result = new ArrayList<>();
		Type[] interfaces = obj.getGenericInterfaces();
		for(Type type:interfaces){
			if (type instanceof ParameterizedType) {
				result.add((ParameterizedType) type);
			} else if (type instanceof Class) {
				result.addAll(getAnsestorGenericsInterfaces((Class<?>) type));
			}
		}

		return result;
	}

	

}
