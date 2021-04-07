package com.leaderli.li.flow.editor.factory;

import org.eclipse.gef.requests.CreationFactory;

public class GenericsCreationFactory<T> implements CreationFactory{
	private Class<T> klass ;
	public GenericsCreationFactory(Class<T> klass){
		this.klass = klass;
	}

	@Override
	public T getNewObject() {
		try {
			T  t = klass.newInstance();;
			return t;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Class<T> getObjectType() {
		return klass;
	}

}
