package com.leaderli.li.flow.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * 
 * 使用包装类，简化关于property的set和get操作，使其直接与model的属性的set和get方法做映射
 */
public abstract class ModelPropertyDescriptor<T, R> implements IPropertyDescriptor {

	private IPropertyDescriptor propertyDescriptor;
	/**
	 * 使用int值来标记唯一的属性项
	 */
	private int id;
	protected T model;


//	protected boolean updating = false;

	public ModelPropertyDescriptor(IPropertyDescriptor propertyDescriptor, T model) {
		this.propertyDescriptor = propertyDescriptor;
		this.model = model;
	}

	public void setProperty(R value) {

	}

	@SuppressWarnings("unchecked")
	public void setPropertyObject(Object value) {
		this.setProperty((R) value);
	}

	public abstract R getProperty();
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		return this.propertyDescriptor.createPropertyEditor(parent);
	}

	@Override
	public String getCategory() {
		return this.propertyDescriptor.getCategory();
	}

	@Override
	public String getDescription() {
		return this.propertyDescriptor.getDescription();
	}

	@Override
	public String getDisplayName() {
		return this.propertyDescriptor.getDisplayName();
	}

	@Override
	public String[] getFilterFlags() {
		return this.propertyDescriptor.getFilterFlags();
	}

	@Override
	public Object getHelpContextIds() {
		return this.propertyDescriptor.getHelpContextIds();
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public Object getId() {
		return this.id;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		return this.propertyDescriptor.getLabelProvider();
	}

	@Override
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		return this.propertyDescriptor.isCompatibleWith(anotherProperty);
	}


}
