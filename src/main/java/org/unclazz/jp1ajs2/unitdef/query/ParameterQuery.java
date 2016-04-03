package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Parameter;

/**
 * ユニット定義パラメータから値を取得するためのクエリ.
 *
 * @param <R> クエリにより取得される値の型
 */
public interface ParameterQuery<R> {
	/**
	 * ユニット定義パラメータから値を取得して返す.
	 * @param p ユニット定義パラメータ
	 * @return 取得された値
	 */
	R queryFrom(Parameter p);
}