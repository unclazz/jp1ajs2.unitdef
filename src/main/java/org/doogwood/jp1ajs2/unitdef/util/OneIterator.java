package org.doogwood.jp1ajs2.unitdef.util;

import java.util.Iterator;

/**
 * ただ1つの要素を返すだけのイテレータ実装.
 * 
 * @param <E> {@link #next()}が返すと仮定される要素の型
 */
public final class OneIterator<E> implements Iterator<E> {
	private final E e;
	private boolean first = true;
	public OneIterator(E e) {
		this.e = e;
	}
	
	@Override
	public boolean hasNext() {
		return first;
	}
	
	@Override
	public E next() {
		if (first) {
			first = false;
			return e;
		} else {
			return null;
		}
	}

	@Override
	public void remove() {
		// Do nothing.
	}
}