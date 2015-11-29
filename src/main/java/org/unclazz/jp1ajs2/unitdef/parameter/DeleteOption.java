package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * 削除オプション.
 */
public enum DeleteOption {
	/**
	 * ファイルを保存する.
	 */
	SAVE("sav"),
	/**
	 * ファイルを削除する.
	 */
	DELETE("del");

	private final String code;
	
	private DeleteOption(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}