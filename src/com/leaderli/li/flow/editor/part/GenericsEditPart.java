package com.leaderli.li.flow.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.leaderli.li.flow.adapter.Notify;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.util.ModelUtil;

public abstract class GenericsEditPart<T extends NodeNotify> extends AbstractGraphicalEditPart {




	public GenericsEditPart() {
		super();
	}

	@Override
	public void setModel(Object model) {
		super.setModel(model);
		initAfterSetModel();
	}

	protected void initAfterSetModel() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public T getModel() {
		return (T) super.getModel();
	}

	private List<Notify> notifys = new ArrayList<Notify>();


	protected void addNotify(Notify notify) {
		this.notifys.add(notify);
	}
//	@Override
//	public void notifyChanged() {
//
//	}

//	@Override
//	public boolean isAdapterForType(Object adapter) {
//		return adapter == getModel();
//	}
	/**
	 * 元素绘制时触发
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			this.notifys.forEach(getModel()::addNotify);
		}
		super.activate();
	}

	/**
	 * 元素销毁时触发
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			this.notifys.forEach(getModel()::removeNotify);
		}
		super.deactivate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {

		if (adapter == IProject.class) {
			return getProject();
		}
		return super.getAdapter(adapter);
	}


	@Override
	public void addEditPartListener(EditPartListener listener) {
		super.addEditPartListener(listener);
	}

	public IProject getProject() {
		return ModelUtil.getFlowEditor(getModel()).getProject();
	}

}
