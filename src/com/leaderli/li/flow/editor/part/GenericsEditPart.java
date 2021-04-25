package com.leaderli.li.flow.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.leaderli.li.flow.editor.BaseGraphicalEditorWithFlyoutPalette;
import com.leaderli.li.flow.editor.model.NodeNotify;
import com.leaderli.li.flow.listener.NotifyListener;

public abstract class GenericsEditPart<T extends NodeNotify, F extends BaseGraphicalEditorWithFlyoutPalette> extends AbstractGraphicalEditPart implements FlowEditorProvider<F> {


	protected F editor;



	@Override
	public F getEditor() {
		return this.editor;
	}

	@Override
	public void setEditor(F flowEditor) {
		this.editor = flowEditor;
	}

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

	/**
	 * 
	 */
	private List<NotifyListener<?>> editPartNotifyListeners = new ArrayList<>();


	protected void addNotifyListener(NotifyListener<?> notify) {
		this.editPartNotifyListeners.add(notify);
	}
	/**
	 * 元素绘制时触发
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			this.editPartNotifyListeners.forEach(getModel()::addNotify);
		}
		super.activate();
	}

	/**
	 * 元素销毁时触发
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			this.editPartNotifyListeners.forEach(getModel()::removeNotify);
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



	public IProject getProject() {
		return this.editor.getProject();
	}

}
