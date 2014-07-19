package usertools.jp1ajs2.unitdef.util;

import usertools.jp1ajs2.unitdef.core.Param;
import usertools.jp1ajs2.unitdef.core.Unit;

/**
 * JP1/AJS2のユニット定義を文字列化するオブジェクト.
 */
public class Formatter {
	/**
	 * JP1/AJS2のユニット定義を文字列化する際のオプション.
	 */
	public static class FormatOptions {
		private String lineSeparator = "\r\n";
		private int tabWidth = 4;
		/**
		 * 行区切り文字列を設定する.
		 * @param s 行区切り文字列
		 */
		public void setLineSeparator(String s) {
			this.lineSeparator = s;
		}
		/**
		 * タブ幅を設定する.
		 * @param i タブ幅
		 */
		public void setTabWidth(int i) {
			this.tabWidth = i;
		}
		/**
		 * 行区切り文字列を取得する.
		 * @return 行区切り文字列
		 */
		public String getLineSeparator() {
			return lineSeparator;
		}
		/**
		 * タブ幅を取得する.
		 * @return タブ幅
		 */
		public int getTabWidth() {
			return tabWidth;
		}
	}
	
	private final String lineSeparator;
	private final int tabWidth;
	
	/**
	 * デフォルトのフォーマット・オプションでフォーマッタを初期化する.
	 */
	public Formatter() {
		this(new FormatOptions());
	}
	
	/**
	 * カスタマイズしたフォーマット・オプションでフォーマッタを初期化する.
	 * @param options
	 */
	public Formatter(final FormatOptions options) {
		lineSeparator = options.getLineSeparator();
		tabWidth = options.getTabWidth();
	}
	
	/**
	 * ユニット定義情報オブジェクトをフォーマットする.
	 * @param unit ユニット
	 * @return フォーマットしたユニット定義
	 */
	public String format(final Unit unit) {
		// ヘルパー関数を呼び出してフォーマットを実行
		return formatUnit(new StringBuilder(), 0, unit).toString();
	}

	/**
	 * 指定されたインデントの深さに基づき空白文字を追加する.
	 * @param builder フォーマット中の文字列
	 * @param depth インデントの深さ
	 * @return フォーマット中の文字列（インデント追加済み）
	 */
	protected StringBuilder appendSpaces(final StringBuilder builder, final int depth) {
		// ユニット定義の“深さ” x タブ幅 ぶんだけホワイトスペースを追加
		for (int i = 0; i < depth * tabWidth; i ++) {
			builder.append(' ');
		}
		return builder;
	}
	/**
	 * ユニット定義をフォーマットする.
	 * @param builder フォーマット中の文字列
	 * @param depth インデントの深さ
	 * @param unit ユニット
	 * @return フォーマット中の文字列（ユニット情報追記済み）
	 */
	protected StringBuilder formatUnit(final StringBuilder builder, final int depth, final Unit unit) {
		// 行頭のインデント
		appendSpaces(builder, depth);
		// ユニット定義の開始
		builder.append("unit=").append(unit.getName());
		// 許可モードほかの属性をカンマ区切りで列挙
		if (unit.getPermissionMode().isSome()) {
			builder.append(",").append(unit.getPermissionMode().get());
		}
		if (unit.getOwnerName().isSome()) {
			builder.append(",").append(unit.getOwnerName().get());
		}
		if (unit.getResourceGroupName().isSome()) {
			builder.append(",").append(unit.getResourceGroupName().get());
		}
		// ユニット定義属性の終了
		builder.append(";").append(lineSeparator);
		// 行頭のインデント
		appendSpaces(builder, depth);
		// パラメータ群・サブユニット群のまえに波括弧
		builder.append("{").append(lineSeparator);
		// パラメータ群の列挙
		for (final Param p : unit.getParams()) {
			// ヘルパー関数で個々のパラメータをフォーマット
			formatParam(builder, depth + 1, p);
		}
		// サブユニット群の列挙
		for (final Unit u : unit.getSubUnits()) {
			// 再帰呼び出しによりサブユニットをフォーマット
			formatUnit(builder, depth + 1, u);
		}
		// 行頭のインデント
		appendSpaces(builder, depth);
		// パラメータ群・サブユニット群のあとに波括弧
		builder.append("}").append(lineSeparator);
		// ユニット定義の終わり
		return builder;
	}
	
	/**
	 * パラメータをフォーマットする.
	 * @param builder フォーマット中の文字列
	 * @param depth インデントの深さ
	 * @param param パラメータ
	 * @return フォーマット中の文字列（パラメータ情報追記済み）
	 */
	protected StringBuilder formatParam(final StringBuilder builder, final int depth, final Param param) {
		// 行頭のインデント
		appendSpaces(builder, depth);
		// パラメータ名
		builder.append(param.getName());
		// パラメータ値
		for (int i = 0; i < param.getValues().size(); i ++) {
			// 先頭の要素のまえには"="を、後続の要素のまえには","をそれぞれ挿入
			builder.append(i == 0 ? "=" : ",").append(param.getValues().get(i).toString());
		}
		// 行末処理
		builder.append(";").append(lineSeparator);
		// パラメータ宣言の終了
		return builder;
	}	
}