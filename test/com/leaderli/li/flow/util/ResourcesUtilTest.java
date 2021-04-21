package com.leaderli.li.flow.util;

import org.junit.Test;

public class ResourcesUtilTest {

	@Test
	public void test() {
		assert "flow".equals(ResourcesUtil.getPackageSimpleName("flow"));
		assert "flow".equals(ResourcesUtil.getPackageSimpleName("b.flow"));
		assert !"flow".equals(ResourcesUtil.getPackageSimpleName("flow.b"));
	}

}
