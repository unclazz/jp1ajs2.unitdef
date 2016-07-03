package org.unclazz.jp1ajs2.unitdef.query2;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import static org.unclazz.jp1ajs2.unitdef.query2.QueryUtils.*;

public final class TypedValueIterableQuery<T,U,V> 
extends AbstractItrableQuery<T, V>
implements Query<T, Iterable<V>> {
	private final Query<T, Iterable<U>> baseQuery;
	private final List<Predicate<V>> preds;
	private final Query<U, V> transformer;
	
	TypedValueIterableQuery(final Query<T, Iterable<U>> baseQuery, 
			final List<Predicate<V>> preds, final Query<U, V> transformer) {
		assertNotNull(baseQuery, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.baseQuery = baseQuery;
		this.preds = preds;
		this.transformer = transformer;
	}
	TypedValueIterableQuery(final Query<T, Iterable<U>> baseQuery, final Query<U, V> transformer) {
		this(baseQuery, Collections.<Predicate<V>>emptyList(), transformer);
	}
	
	@Override
	public Iterable<V> queryFrom(T t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(baseQuery.queryFrom(t), new YieldCallable<U,V>(){
			@Override
			public Yield<V> yield(final U item, final int index) {
				final V typed = transformer.queryFrom(item);
				if (typed == null) {
					return Yield.yieldVoid();
				}
				for (final Predicate<V> pred : preds) {
					if (!pred.test(typed)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(typed);
			}
		});
	}
	
	public TypedValueIterableQuery<T,U,V> and(final Predicate<V> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<V>> newPreds = new LinkedList<Predicate<V>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new TypedValueIterableQuery<T,U,V>(this.baseQuery, newPreds, transformer);
	}
}
