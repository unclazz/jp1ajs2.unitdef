package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Tuple;

final class QuotedParameterValue extends DefaultParameterValue {
	private final CharSequence seq;
	QuotedParameterValue(final CharSequence seq) {
		this.seq = seq;
	}
	@Override
	public CharSequence getRawCharSequence() {
		return seq;
	}
	@Override
	public Tuple getTuple() {
		return Tuple.EMPTY_TUPLE;
	}
	@Override
	public ParameterValueType getType() {
		return ParameterValueType.QUOTED;
	}
}