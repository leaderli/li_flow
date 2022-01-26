package com.leaderli.li.flow.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TypeFinder {
	
	public static void find(Type type, TypeVisitor typeVisitor) {
	List<Type> find = new ArrayList<>();

	TypeVisitor fi= type1 -> {
		
		if(type1 instanceof Class){
			
		}

		return false;
	};
		typeVisitor.visitor(type);
	}

	public static void main(String[] args) {
		add(TypeFinder.class.getGenericSuperclass());

		add(TypeFinder.class.getGenericInterfaces());
	}

	private static void add(Type... types) {
		List<Type> find = new ArrayList<>();

		System.out.println(Arrays.toString(types));
	}
}
