package com.leaderli.li.flow.util;

import org.junit.Test;

public class ResourcesUtilTest {

	@Test
	public void test() {
		assert "flow".equals(ResourcesUtil.getSimpleName("flow"));
		assert "flow".equals(ResourcesUtil.getSimpleName("b.flow"));
		assert !"flow".equals(ResourcesUtil.getSimpleName("flow.b"));
	}

}
