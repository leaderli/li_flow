package com.leaderli.li.flow.editor.model;

public interface ModelRole {

	int HIGN_BINARY = 1 << 8;
	int FLOW_TYPE = HIGN_BINARY << 0;
	int GOTO_TYPE = HIGN_BINARY << 1;
	int DIAGRAM_TYPE = HIGN_BINARY << 2;
	int CONNECTION_TYPE = HIGN_BINARY << 3;

	int NAME_ROLE = 1 << 0;
	int POINT_ROLE = 1 << 1;
	int CHILD_ROLE = 1 << 2;
	int CONNECTION_SOURCE_ROLE = 1 << 3;
	int CONNECTION_TARGET_ROLE = 1 << 4;
	int FLOW_NAME_ROLE = 1 << 5;
	int JAVA_CODE_ROLE = 1 << 6;

	int ALL_ROLE = Integer.MAX_VALUE >> 23;
	int ALL_TYPE = Integer.MAX_VALUE >> 23 << 8;




	int ADAPTER_FLAG_ALL = 0;

	/**
	 * 
	 * flag = 0 时，则恒为true
	 */
	static boolean isTypeRole(int adapterFlag, int notifyTypeRole) {
		return (adapterFlag & notifyTypeRole) == adapterFlag;
	}
}
