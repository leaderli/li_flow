package com.leaderli.li.flow.listener;

import com.leaderli.li.flow.editor.model.ModelRole;

public interface NotifyListener<T> {





	default boolean canNotifyForModel(Object model) {

		return true;
	}

	default int typeAndRole() {
		return ModelRole.ADAPTER_FLAG_ALL;
	}


	default void notifyChanged(T oldVal, T newVal) {
		notifyChanged();
	}

	void notifyChanged();

}

