package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.parameter.AnteroposteriorRelationship;
import org.unclazz.jp1ajs2.unitdef.parameter.CommandLine;
import org.unclazz.jp1ajs2.unitdef.parameter.DeleteOption;
import org.unclazz.jp1ajs2.unitdef.parameter.ElapsedTime;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDate;
import org.unclazz.jp1ajs2.unitdef.parameter.EndDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.EndStatusJudgementType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionCycle;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionTimedOutStatus;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;
import org.unclazz.jp1ajs2.unitdef.parameter.ExitCodeThreshold;
import org.unclazz.jp1ajs2.unitdef.parameter.FileWatchCondition;
import org.unclazz.jp1ajs2.unitdef.parameter.FixedDuration;
import org.unclazz.jp1ajs2.unitdef.parameter.LinkedRuleNumber;
import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;
import org.unclazz.jp1ajs2.unitdef.parameter.MapSize;
import org.unclazz.jp1ajs2.unitdef.parameter.ResultJudgmentType;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitCount;
import org.unclazz.jp1ajs2.unitdef.parameter.RunConditionWatchLimitTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDate;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateAdjustment;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensation;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDateCompensationDeadline;
import org.unclazz.jp1ajs2.unitdef.parameter.StartDelayTime;
import org.unclazz.jp1ajs2.unitdef.parameter.StartTime;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.parameter.WriteOption;
import org.unclazz.jp1ajs2.unitdef.query.SingleParameterQuery.ValueIterableQuery;
import org.unclazz.jp1ajs2.unitdef.query.SingleParameterQuery.ValueOneQuery;
import static org.unclazz.jp1ajs2.unitdef.query.JointQuery.*;
import static org.unclazz.jp1ajs2.unitdef.query.ParameterQueries.*;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.UnsignedIntegral;

public interface SingleParameterQuery extends Query<Parameter, Parameter>,
ParameterConditionalModifier<SingleParameterQuery> {
	@Override
	WhenValueAtNClause<SingleParameterQuery> whenValueAt(int i);
	@Override
	WhenValueCountNClause<SingleParameterQuery> whenValueCount(int c);
	
	ValueIterableQuery values();
	ValueOneQuery valueAt(int i);
	
	Query<Parameter, AnteroposteriorRelationship> ar();
	Query<Parameter, StartDateAdjustment> cftd();
	Query<Parameter, CharSequence> cm();
	Query<Parameter, ExecutionCycle> cy();
	Query<Parameter, EndDate> ed();
	Query<Parameter, EndStatusJudgementType> ej();
	Query<Parameter, UnsignedIntegral> ejc();
	Query<Parameter, Element> el();
	Query<Parameter, ElapsedTime> etm();
	Query<Parameter, ExecutionTimedOutStatus> ets();
	Query<Parameter, ExecutionUserType> eu();
	Query<Parameter, EndDelayTime> ey();
	Query<Parameter, FixedDuration> fd();
	Query<Parameter, FileWatchCondition> flwc();
	Query<Parameter, ResultJudgmentType> jd();
	Query<Parameter, LinkedRuleNumber> ln();
	Query<Parameter, MailAddress> mladr();
	Query<Parameter, CommandLine> sc();
	Query<Parameter, StartDate> sd();
	Query<Parameter, WriteOption> sea();
	Query<Parameter, StartDateCompensation> sh();
	Query<Parameter, StartDateCompensationDeadline> shd();
	Query<Parameter, WriteOption> soa();
	Query<Parameter, StartTime> st();
	Query<Parameter, StartDelayTime> sy();
	Query<Parameter, MapSize> sz();
	Query<Parameter, CommandLine> te();
	Query<Parameter, ExitCodeThreshold> tho();
	Query<Parameter, ElapsedTime> tmitv();
	Query<Parameter, DeleteOption> top1();
	Query<Parameter, DeleteOption> top2();
	Query<Parameter, DeleteOption> top3();
	Query<Parameter, DeleteOption> top4();
	Query<Parameter, UnitType> ty();
	Query<Parameter, RunConditionWatchLimitCount> wc();
	Query<Parameter, RunConditionWatchLimitTime> wt();
	Query<Parameter, ExitCodeThreshold> wth();
	
	public static interface ValueIterableQuery 
	extends IterableQuery<Parameter, ParameterValue> {
		IterableQuery<Parameter, Integer> asInteger();
		IterableQuery<Parameter, Integer> asInteger(int defaultValue);
		IterableQuery<Parameter, String> asString();
		IterableQuery<Parameter, String> asQuotedString();
		IterableQuery<Parameter, String> asQuotedString(boolean forceQuote);
		IterableQuery<Parameter, Boolean> asBoolean(String... trueValues);
		IterableQuery<Parameter, Tuple> asTuple();
	}
	public static interface ValueOneQuery
	extends OneQuery<Parameter, ParameterValue>{
		OneQuery<Parameter, Integer> asInteger();
		OneQuery<Parameter, Integer> asInteger(int defaultValue);
		OneQuery<Parameter, String> asString();
		OneQuery<Parameter, String> asEscapedString();
		OneQuery<Parameter, String> asQuotedString();
		OneQuery<Parameter, String> asQuotedString(boolean forceQuote);
		OneQuery<Parameter, Boolean> asBoolean(String... trueValues);
		OneQuery<Parameter, Tuple> asTuple();
	}
}


final class DefaultValueOneQuery implements ValueOneQuery {
	private final SingleParameterQuery baseQuery;
	private final int i;
	DefaultValueOneQuery(final SingleParameterQuery baseQuery, final int i) {
		this.baseQuery = baseQuery;
		this.i = i;
	}
	@Override
	public Query<Parameter, ParameterValue> cached() {
		return CachedQuery.wrap(this);
	}
	@Override
	public ParameterValue queryFrom(Parameter t) {
		return baseQuery.queryFrom(t).getValues().get(i);
	}
	@Override
	public OneQuery<Parameter, Integer> asInteger() {
		return join(this, new CastInteger());
	}
	@Override
	public OneQuery<Parameter, Integer> asInteger(int defaultValue) {
		return join(this, new CastInteger(defaultValue));
	}
	@Override
	public OneQuery<Parameter, String> asString() {
		return join(this, new CastString());
	}
	@Override
	public OneQuery<Parameter, String> asEscapedString() {
		return join(this, new CastEscapedString());
	}
	@Override
	public OneQuery<Parameter, String> asQuotedString() {
		return join(this, new CastQuotedString(false));
	}
	@Override
	public OneQuery<Parameter, String> asQuotedString(boolean forceQuote) {
		return join(this, new CastQuotedString(forceQuote));
	}
	@Override
	public OneQuery<Parameter, Boolean> asBoolean(String... trueValues) {
		return join(this, new CastBoolean(trueValues));
	}
	@Override
	public OneQuery<Parameter, Tuple> asTuple() {
		return join(this, new CastTuple());
	}
}

final class DefaultValueIterableQuery 
extends IterableQuerySupport<Parameter, ParameterValue>
implements ValueIterableQuery {
	
	private final List<Predicate<ParameterValue>> preds;
	private final SingleParameterQuery baseQuery;
	
	DefaultValueIterableQuery(final SingleParameterQuery baseQuery) {
		this.baseQuery = baseQuery;
		this.preds = Collections.emptyList();
	}
	DefaultValueIterableQuery(final SingleParameterQuery baseQuery,
			final List<Predicate<ParameterValue>> preds) {
		this.baseQuery = baseQuery;
		this.preds = preds;
	}

	@Override
	public Query<Parameter, Iterable<ParameterValue>> and(Predicate<ParameterValue> pred) {
		final LinkedList<Predicate<ParameterValue>> newList = new LinkedList<Predicate<ParameterValue>>();
		newList.addAll(preds);
		newList.addLast(pred);
		return new DefaultValueIterableQuery(baseQuery, newList);
	}
	@Override
	public Iterable<ParameterValue> queryFrom(Parameter t) {
		return LazyIterable.forEach(baseQuery.queryFrom(t).getValues(),
			new YieldCallable<ParameterValue,ParameterValue>(){
			@Override
			public Yield<ParameterValue> yield(ParameterValue item, int index) {
				for (final Predicate<ParameterValue> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	@Override
	public IterableQuery<Parameter, Integer> asInteger() {
		return query(new CastInteger());
	}
	@Override
	public IterableQuery<Parameter, Integer> asInteger(int defaultValue) {
		return query(new CastInteger(defaultValue));
	}
	@Override
	public IterableQuery<Parameter, String> asString() {
		return query(new CastString());
	}
	@Override
	public IterableQuery<Parameter, String> asQuotedString() {
		return query(new CastQuotedString(false));
	}
	@Override
	public IterableQuery<Parameter, String> asQuotedString(boolean forceQuote) {
		return query(new CastQuotedString(forceQuote));
	}
	@Override
	public IterableQuery<Parameter, Boolean> asBoolean(String... trueValues) {
		return query(new CastBoolean(trueValues));
	}
	@Override
	public IterableQuery<Parameter, Tuple> asTuple() {
		return query(new CastTuple());
	}
}

final class DefaultSingleParameterQuery implements SingleParameterQuery{
	private final class QueryFactory implements ModifierFactory<SingleParameterQuery> {
		private final WhenThenList whenThenList;
		QueryFactory(WhenThenList n) {
			this.whenThenList = n;
		}
		@Override
		public SingleParameterQuery apply(WhenThenList whenThenList) {
			return new DefaultSingleParameterQuery(this.whenThenList == null ?
					whenThenList : this.whenThenList.concat(whenThenList));
		}
	}
	
	private final WhenThenList whenThenList;
	
	DefaultSingleParameterQuery(WhenThenList whenThenList) {
		this.whenThenList = whenThenList;
	}
	DefaultSingleParameterQuery() {
		this.whenThenList = null;
	}

	@Override
	public Parameter queryFrom(Parameter t) {
		return whenThenList == null ? t : whenThenList.apply(t);
	}

	@Override
	public WhenValueAtNClause<SingleParameterQuery> whenValueAt(int i) {
		return new DefaultWhenValueAtNClause<SingleParameterQuery>
			(new QueryFactory(whenThenList), i);
	}

	@Override
	public WhenValueCountNClause<SingleParameterQuery> whenValueCount(int c) {
		return new DefaultWhenValueCountNClause<SingleParameterQuery>
				(new QueryFactory(whenThenList), c);
	}

	@Override
	public ValueIterableQuery values() {
		return new DefaultValueIterableQuery(this);
	}

	@Override
	public ValueOneQuery valueAt(int i) {
		return new DefaultValueOneQuery(this, i);
	}

	@Override
	public Query<Parameter, AnteroposteriorRelationship> ar() {
		return join(this, AR);
	}

	@Override
	public Query<Parameter, StartDateAdjustment> cftd() {
		return join(this, CFTD);
	}

	@Override
	public Query<Parameter, CharSequence> cm() {
		return join(this, CM);
	}

	@Override
	public Query<Parameter, ExecutionCycle> cy() {
		return join(this, CY);
	}

	@Override
	public Query<Parameter, EndDate> ed() {
		return join(this, ED);
	}

	@Override
	public Query<Parameter, EndStatusJudgementType> ej() {
		return join(this, EJ);
	}

	@Override
	public Query<Parameter, UnsignedIntegral> ejc() {
		return join(this, EJC);
	}

	@Override
	public Query<Parameter, Element> el() {
		return join(this, EL);
	}

	@Override
	public Query<Parameter, ElapsedTime> etm() {
		return join(this, ETM);
	}

	@Override
	public Query<Parameter, ExecutionTimedOutStatus> ets() {
		return join(this, ETS);
	}

	@Override
	public Query<Parameter, ExecutionUserType> eu() {
		return join(this, EU);
	}

	@Override
	public Query<Parameter, EndDelayTime> ey() {
		return join(this, EY);
	}

	@Override
	public Query<Parameter, FixedDuration> fd() {
		return join(this, FD);
	}

	@Override
	public Query<Parameter, FileWatchCondition> flwc() {
		return join(this, FLWC);
	}

	@Override
	public Query<Parameter, ResultJudgmentType> jd() {
		return join(this, JD);
	}

	@Override
	public Query<Parameter, LinkedRuleNumber> ln() {
		return join(this, LN);
	}

	@Override
	public Query<Parameter, MailAddress> mladr() {
		return join(this, MLADR);
	}

	@Override
	public Query<Parameter, CommandLine> sc() {
		return join(this, SC);
	}

	@Override
	public Query<Parameter, StartDate> sd() {
		return join(this, SD);
	}

	@Override
	public Query<Parameter, WriteOption> sea() {
		return join(this, SEA);
	}

	@Override
	public Query<Parameter, StartDateCompensation> sh() {
		return join(this, SH);
	}

	@Override
	public Query<Parameter, StartDateCompensationDeadline> shd() {
		return join(this, SHD);
	}

	@Override
	public Query<Parameter, WriteOption> soa() {
		return join(this, SOA);
	}

	@Override
	public Query<Parameter, StartTime> st() {
		return join(this, ST);
	}

	@Override
	public Query<Parameter, StartDelayTime> sy() {
		return join(this, SY);
	}

	@Override
	public Query<Parameter, MapSize> sz() {
		return join(this, SZ);
	}

	@Override
	public Query<Parameter, CommandLine> te() {
		return join(this, TE);
	}

	@Override
	public Query<Parameter, ExitCodeThreshold> tho() {
		return join(this, THO);
	}

	@Override
	public Query<Parameter, ElapsedTime> tmitv() {
		return join(this, TMITV);
	}

	@Override
	public Query<Parameter, DeleteOption> top1() {
		return join(this, TOP1);
	}

	@Override
	public Query<Parameter, DeleteOption> top2() {
		return join(this, TOP2);
	}

	@Override
	public Query<Parameter, DeleteOption> top3() {
		return join(this, TOP3);
	}

	@Override
	public Query<Parameter, DeleteOption> top4() {
		return join(this, TOP4);
	}

	@Override
	public Query<Parameter, UnitType> ty() {
		return join(this, TY);
	}

	@Override
	public Query<Parameter, RunConditionWatchLimitCount> wc() {
		return join(this, WC);
	}

	@Override
	public Query<Parameter, RunConditionWatchLimitTime> wt() {
		return join(this, WT);
	}

	@Override
	public Query<Parameter, ExitCodeThreshold> wth() {
		return join(this, WTH);
	}
}

