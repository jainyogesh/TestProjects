package org.yjain.reflection;

public class BaseDao extends BaseClass<String, Long> {

	@Override
	public String retrieve(Long arg) {
		return arg.toString();
	}

}
