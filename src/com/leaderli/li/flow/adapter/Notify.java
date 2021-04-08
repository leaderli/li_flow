package com.leaderli.li.flow.adapter;

import com.leaderli.li.flow.editor.model.ModelRole;

public interface Notify {





	default boolean canNotifyForModel(Object model) {
		return true;
	}

	default int typeAndRole() {
		return ModelRole.ADAPTER_FLAG_ALL;
	}


	void notifyChanged(int typeRole, String oldVal, String newVal);

}

