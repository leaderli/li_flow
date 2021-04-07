package com.leaderli.li.flow.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

	
}
