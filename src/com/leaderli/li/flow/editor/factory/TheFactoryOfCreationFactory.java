package com.leaderli.li.flow.editor.factory;

import java.util.function.Consumer;

public class TheFactoryOfCreationFactory {

	public static <T> GenericsCreationFactory<T> getGenericsCreationFactoryWithConsumer(Class<T> klass,
			Consumer<T> consumer) {

		return new GenericsCreationFactory<T>(klass) {

			@Override
			public T getNewObject() {
				T t = super.getNewObject();
				if (consumer != null) {
					consumer.accept(t);
				}
				return t;
			}
		};
	}
}
