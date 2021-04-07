package com.leaderli.li.flow.editor.policy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;

import com.leaderli.li.flow.editor.part.GenericsEditPart;

public class GenericsAbstractEditPolicy<T extends GenericsEditPart<?>> extends AbstractEditPolicy {

	@SuppressWarnings("unchecked")
	public T getGenericsHost() {
		return (T) getHost();
	}
	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void setHost(EditPart host) {
		checkInstall(host);
		super.setHost(host);
	}
	
	private void checkInstall(EditPart host) {
		Class<?> klass = this.getClass();
		while (true) {
			Type superclass = klass.getGenericSuperclass();
			if (superclass != null && superclass instanceof Class) {
				klass = (Class<?>) superclass;
				continue;
			}
			if (superclass instanceof ParameterizedType) {
				Assert.isTrue(((Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0]).isAssignableFrom(host.getClass()), "installEditPolicy at error editpart");
			}
			break;
		}
		
	}


}
