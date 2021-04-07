package com.leaderli.li.flow.adapter;

public interface Notify {





	default boolean canNotifyForModel(Object model) {
		return true;
	}

	default boolean canNotifyForTypeAndRole(int typeRole) {
		return true;
	}


	void notifyChanged(int typeRole, String oldVal, String newVal);

}

