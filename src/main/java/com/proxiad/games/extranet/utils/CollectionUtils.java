package com.proxiad.games.extranet.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollectionUtils {

	public static <T> T random(Collection<T> coll) {
		int num = (int) (Math.random() * coll.size());
		for(T t: coll) if (--num < 0) return t;
		throw new AssertionError();
	}

	public static <T> List<T> random(Collection<T> coll, Integer number) {
		return IntStream.range(0, number)
				.mapToObj(idx -> random(coll))
				.collect(Collectors.toList());
	}

}
