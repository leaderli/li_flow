package com.leaderli.li.flow.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

public class GenericsPropertySource<T> implements IPropertySource {

	List<ModelPropertyDescriptor<T, ?>> descriptors = new ArrayList<>();


	public void addModelPropertyDescriptor(ModelPropertyDescriptor<T, ?> descriptor) {
		// 根据插入list的位置重置其id
		descriptor.setId(descriptors.size());
		descriptors.add(descriptor);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] arr = new IPropertyDescriptor[descriptors.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = descriptors.get(i);
		}
		return arr;
	}

	public ModelPropertyDescriptor<T, ?> getModelPropertyDescriptor(Object id) {
		return descriptors.get((int) id);
	}
	@Override
	public Object getPropertyValue(Object id) {
		return getModelPropertyDescriptor(id).getProperty();
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		getModelPropertyDescriptor(id).setPropertyObject(value);
	}
	@Override
	public boolean isPropertySet(Object id) {
		Object value = getPropertyValue(id);
		if (value == null || value.equals("")) {
			return false;
		}
		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {

		throw new UnsupportedOperationException("resetPropertyValue not currently supported");
	}



	@Override
	public Object getEditableValue() {
		return this;
	}

}
