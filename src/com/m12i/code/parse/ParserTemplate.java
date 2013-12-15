package com.m12i.code.parse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * {@link Parser}インターフェースに対しいくつかの補助メソッドを追加した抽象クラス.
 * このクラスはパーサを実装するにあたり頻繁に使用されると思われる各種処理──
 * 文字チェック、空白やコメントのスキップ、引用符で囲われた文字列の読み取りなど──を提供するクラスです。
 * 実装構造上このクラスとこのクラスの実装クラスのすべてのメソッドは同期化されません。
 * @param <T> パースした結果得られるオブジェクトの型
 */
public abstract class ParserTemplate<T> implements Parser<T>, Parsable {
	private Parsable code = null;
	public final T parse(final Parsable p) throws ParseException {
		code = p;
		return parseMain();
	}
	public T parse(final String s) throws ParseException {
		InputStreamBasedParsable p = null;
		try {
			p = new InputStreamBasedParsable(s);
			return parse(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (UnexpectedException e) {
			throw ParseException.unexpectedError(p, e);
		} finally {
			if(p != null) {
				p.close();
			}
		}
	}
	public T parse(final InputStream s) throws IOException, ParseException {
		InputStreamBasedParsable p = null;
		try {
			p = new InputStreamBasedParsable(s);
			return parse(p);
		} catch (UnexpectedException e) {
			throw ParseException.unexpectedError(p, e);
		} finally {
			if(p != null) {
				p.close();
			}
		}
	}
	public T parse(final InputStream s, final String charset) throws IOException, ParseException {
		InputStreamBasedParsable p = null;
		try {
			p = new InputStreamBasedParsable(s, charset);
			return parse(p);
		} catch (UnexpectedException e) {
			throw ParseException.unexpectedError(p, e);
		} finally {
			if(p != null) {
				p.close();
			}
		}
	}
	/**
	 * 対象コードをパースして返す.
	 * このメソッドは{@link #parse(Parsable)}から呼び出されます。
	 * {@link ParserTemplate<T>}を具象クラスとして実装する場合、このメソッドがパース処理のエントリー・ポイントとなります。
	 * {@link #parse(Parsable)}に引数として渡された{@link Parsable}インスタンスには、
	 * {@link #code()}メソッドを通じてアクセスできます。
	 * @return 読み取り結果
	 * @throws ParseException 構文エラーが発生した場合、もしくは、読み取り中に予期せぬエラーが発生した場合
	 */
	protected abstract T parseMain() throws ParseException;
	/**
	 * パース対象を返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * @return パース対象
	 */
	protected Parsable code() {
		return code;
	}
	/**
	 * シングルクオテーション（一重引用符）で囲われた文字列で使用されるエスケープシーケンス・プレフィックスを返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文がシングルクオテーション文字列やその内部でのエスケープシーケンス使用をサポートしていない場合、
	 * このメソッドは{@literal '\u0000'}（ヌル文字）を返すようにします。
	 * @return エスケープシーケンス・プレフィックス
	 */
	protected abstract char escapePrefixInSingleQuotes();
	/**
	 * ダブルクオテーション（二重引用符）で囲われた文字列で使用されるエスケープシーケンス・プレフィックスを返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文がダブルクオテーション文字列やその内部でのエスケープシーケンス使用をサポートしていない場合、
	 * このメソッドは{@literal '\u0000'}（ヌル文字）を返すようにします。
	 * @return エスケープシーケンス・プレフィックス
	 */
	protected abstract char escapePrefixInDoubleQuotes();
	/**
	 * 行コメント開始文字列を返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文が行コメントをサポートしていない場合、空文字列もしくは{@link null}を返すようにします。
	 * @return 行コメント開始文字列
	 */
	protected abstract String lineCommentStart();
	/**
	 * ブロック・コメント開始文字列を返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * パース対象コードの構文がブロック・コメントをサポートしていない場合、空文字列もしくは{@link null}を返すようにします。
	 * @return ブロック・コメント開始文字列
	 */
	protected abstract String blockCommentStart();
	/**
	 * ブロック・コメント終了文字列を返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * @return ブロック・コメント終了文字列
	 */
	protected abstract String blockCommentEnd();
	/**
	 * 空白文字列とともにコメントもスキップするかどうかを返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * 具象クラスを定義する場合このメソッドを実装する必要があります。
	 * @return 空白文字列とともにコメントもスキップするかどうか（{@literal true}：する、{@literal false}：しない）
	 */
	protected abstract boolean skipCommentWithSpace();
	/**
	 * 現在文字が空白文字であるかどうかを判定して返す.
	 * このメソッドは{@link ParserTemplate<T>}のテンプレートメソッドから呼び出されます。
	 * デフォルトの実装では、半角スペース（コード32）と同じかそれより小さいコードの文字の場合、空白文字と見做します。
	 * 具象クラスでこの挙動を変えたい場合は、オーバーライドをしてください。
	 * @return 現在文字が空白文字であるかどうか（{@literal true}：である、{@literal false}：でない）
	 */
	protected boolean currentIsSpace() {
		return current() <= ' ';
	}
	/**
	 * 引数で指定された文字列をスキップする.
	 * 引数で指定された文字列を構成する文字を順に取り出して、パース対象の現在文字以降の文字列と比較します。
	 * 双方が一致している間は{@code #next()}でスキップを続けます。
	 * もし1文字でも一致しない文字があった場合、このメソッドは例外をスローします。
	 * @param word スキップ文字列
	 * @throws ParseException スキップ文字列がパース対象の現在文字以降と一致しない場合
	 */
	public void skipWord(String word) throws ParseException {
		for(int i = 0; i < word.length(); i ++) {
			currentMustBe(word.charAt(i));
			next();
		}
	}
	/**
	 * 引数で指定された文字が現れる前までの文字列を読み取って返す.
	 * 読み取りが完了したとき、読み取り位置は引数で指定された文字のある場所を指します。
	 * @param c 対象文字
	 * @return 読み取り結果の文字列
	 */
	public String parseUntil(char c) {
		final StringBuilder sb = new StringBuilder();
		while(! hasReachedEof() && currentIsNot(c)) {
			sb.append(current());
			next();
		}
		return sb.toString();
	}
	/**
	 * アルファベット（A-Za-z）のみで構成された文字列を読み取って返す.
	 * 読み取りが完了したとき、読み取り位置は非アルファベット文字のある場所を指します。
	 * @return 読み取り結果の文字列
	 */
	public String parseAlphabet() {
		final StringBuilder sb = new StringBuilder();
		while(! hasReachedEof() 
				&& ('a' <= current() && current() <= 'z'
						|| 'A' <= current() && current() <= 'Z')) {
			sb.append(current());
			next();
		}
		return sb.toString();
	}
	/**
	 * アルファベットと数字（A-Za-z0−9）のみで構成された文字列を読み取って返す.
	 * 読み取りが完了したとき、読み取り位置は非アルファベットかつ非数字の文字のある場所を指します。
	 * @return 読み取り結果の文字列
	 */
	public String parseAlphanum() {
		final StringBuilder sb = new StringBuilder();
		while(! hasReachedEof() 
				&& ('a' <= current() && current() <= 'z'
						|| 'A' <= current() && current() <= 'Z'
						|| '0' <= current() && current() <= '9')) {
			sb.append(current());
			next();
		}
		return sb.toString();
	}
	/**
	 * ダブルクオテーション（二重引用符）もしくはシングルクオテーション（一重引用符）で囲われた文字列を読み取って返す.
	 * それぞれの引用符で囲われた文字列において使用可能なエスケープシーケンス・プレフィックスは、
	 * {@link #escapePrefixInSingleQuotes()}や{@link #escapePrefixInSingleQuotes()}で定義されます。
	 * これらのメソッドが{@literal '\u0000'}（ヌル文字）を返す場合、
	 * 引用符で囲われた文字列内でエスケープシーケンスの使用はサポートされない状態となります。
	 * @return 引用符で囲われていた文字列（前後の引用符は除去されたもの）
	 * @throws ParseException 構文エラーが発生した場合
	 */
	public String parseQuotedString() throws ParseException {
		// 現在文字が二重引用符もしくは一重引用符でない場合は構文エラー
		if(currentIsNotAnyOf('"', '\'')){
			throw ParseException.syntaxError(code());
		}
		
		final StringBuilder sb = new StringBuilder();
		final char quote = current();
		final char escapePrefix = quote == '"' ? escapePrefixInDoubleQuotes() : escapePrefixInSingleQuotes();
		final boolean escapeEnabled = quote != '\u0000';
		next();
		
		// EOFに到達するまで繰り返す
		while (! hasReachedEof()) {
			// 現在文字が引用符である場合は読み取りは終了
			if(currentIs(quote)) {
				// 引用符の次の位置に読み取り位置を進める
				next();
				// ここまで読み取ってきた結果を返す
				return sb.toString();
			}
			// エスケープシーケンス使用がサポートされており、現在文字がエスケープシーケンス・プレフィックスである場合
			if (escapeEnabled && currentIs(escapePrefix)) {
				// 読み取り位置を進めた上で現在文字を取得
				sb.append(next());
			}
			// それ以外の場合
			else {
				// 現在文字を取得
				sb.append(current());
			}
			// 次に進む
			next();
		}
		
		// 終了側の引用符が登場しなかった場合は構文エラー
		throw ParseException.syntaxError(code());
	}
	/**
	 * 読み取り位置を次の行の先頭に移動してその行（文字列）を返す.
	 * @return 読み取り位置移動後の行（文字列）
	 */
	public String nextLine() {
		final int l = lineNo();
		while(! hasReachedEof() && l == lineNo()) {
			next();
		}
		return line();
	}
	/**
	 * 空白文字からなる文字列をスキップする.
	 * 現在位置の文字が空白文字と見做される文字でない場合、何もせず即座に処理を終了します。
	 * 現在位置の文字が空白文字と見做される文字である場合、現在位置が空白文字と見做されない文字となるまで、読み取り位置を前進させます。
	 * このメソッドが空白文字と見做すのは、半角スペース（コード32）と同じかそれより小さいコードの文字の場合です。
	 * このメソッドが処理を終えた時、現在文字は空白文字と見做されないいずれかの文字です。
	 * {@link #skipCommentWithSpace()}が{@literal true}を返す場合、このメソッドは空白文字列とともにコメントもスキップします。
	 * @throws ParseException 構文エラーが発生した場合
	 */
	public void skipSpace() throws ParseException {
		// EOFに到達するまで繰り返す
		while (!hasReachedEof()) {

			// 半角スペース（コード32）と同じかそれより小さいコードの文字の場合
			if (current() <= ' ') {
				// ホワイトスペースとみなしてスキップ
				next();
				
			} 
			// 空白文字とともにコメントスキップする設定が有効であり、かつ現在文字および続く文字列がコメント開始文字列である場合
			else if (skipCommentWithSpace() 
					&& (remainingCodeStartsWith(lineCommentStart()) 
							|| remainingCodeStartsWith(blockCommentStart()))) {

				// コメント開始とみなして行コメントのスキップ
				skipComment();
			} 
			// それ以外の文字の場合
			else {
				break;
			}
		}
	}
	/**
	 * コメント文字列をスキップする.
	 * このメソッドは行コメントおよびブロック・コメントをスキップします。
	 * 行コメントの開始文字列は{@link #lineCommentStart()}で、
	 * ブロック・コメントの開始文字列は{@link #blockCommentStart()}、終了文字列は{@link #blockCommentEnd()}で定義します。
	 * @throws ParseException 構文エラーが発生した場合
	 */
	public void skipComment() throws ParseException {
		// 現在文字および続く文字列が行コメント開始文字列である場合
		if(remainingCodeStartsWith(lineCommentStart())) {
			nextLine();
		}
		// 現在文字および続く文字列がブロックコメント開始文字列である場合
		else if(remainingCodeStartsWith(blockCommentStart())) {
			// コメント開始文字列をスキップ
			skipWord(blockCommentStart());
			
			// EOFに到達するまで繰り返す。
			while (!hasReachedEof()) {

				// 現在文字および続く文字列がコメント終了文字列である場合
				if (remainingCodeStartsWith(blockCommentEnd())) {
					// コメント終了文字列をスキップ。
					skipWord(blockCommentEnd());
					// コメントは終了、内側ループを抜ける。
					return;
					
				}
				// それ以外の場合、
				else {
					// 現在位置を前進させる。
					next();
				}
			}
			
			throw ParseException.syntaxError(code());
		}
	}
	/**
	 * 現在文字が引数で指定された文字と一致するか検証した上でその現在文字を返す.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致しない場合
	 */
	public char currentMustBe(char c) throws ParseException {
		if(currentIsNot(c)) {
			throw ParseException.arg1ExpectedButFoundArg2(code(), c, current());
		}
		return c;
	}
	/**
	 * 読み取り位置を1つ前進させたあと現在文字が引数で指定された文字と一致するか検証した上でその現在文字を返す.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致しない場合
	 */
	public char nextMustBe(char c) throws ParseException {
		if(nextIsNot(c)) {
			throw ParseException.arg1ExpectedButFoundArg2(code(), c, current());
		}
		return c;
	}
	/**
	 * {@link #currentMustBe(char)}の反対.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致した場合
	 */
	public char currentMustNotBe(char c) throws ParseException {
		if(currentIs(c)) {
			throw ParseException.arg1ExpectedButFoundArg2(code(), c, current());
		}
		return current();
	}
	/**
	 * {@link #nextMustBe(char)}の反対.
	 * @param c 対象文字
	 * @return 現在文字
	 * @throws ParseException 指定された文字と現在文字が一致した場合
	 */
	public char nextMustNotBe(char c) throws ParseException {
		if(nextIs(c)) {
			throw ParseException.arg1ExpectedButFoundArg2(code(), c, current());
		}
		return current();
	}
	/**
	 * 現在文字と引数で指定された文字が一致するかどうかを判定する.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致する、{@literal false}：一致しない）
	 */
	public boolean currentIs(char c) {
		return current() == c;
	}
	/**
	 * 読み取り位置を1つ前進させたあと現在文字と引数で指定された文字が一致するかどうかを判定する.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致する、{@literal false}：一致しない）
	 */
	public boolean nextIs(char c) {
		next();
		return current() == c;
	}
	/**
	 * {@link #currentIs(char)}の反対.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致しない、{@literal false}：一致する）
	 */
	public boolean currentIsNot(char c) {
		return ! currentIs(c);
	}
	/**
	 * {@link #nextIs(char)}の反対.
	 * @param c 対象文字
	 * @return 判定結果（{@literal true}：一致しない、{@literal false}：一致する）
	 */
	public boolean nextIsNot(char c) {
		next();
		return ! currentIs(c);
	}
	/**
	 * 現在文字の後方に引数で指定された文字列が続くかどうかを判定する.
	 * このメソッドは「先読み」判定を行うものであり、処理中に読み取り位置を前進させません。
	 * 現在文字の後方に続く文字列を単にチェックして結果を返すだけです。
	 * @param s 対象文字列
	 * @return 判定結果（{@literal true}：指定された文字列が続く、{@literal false}：続かない）
	 */
	public boolean currentIsFollowedBy(String s) {
		return (s == null || s.length() == 0 || line() == null) ? false : line().substring(columnNo()).startsWith(s);
	}
	/**
	 * {@link #currentIsFollowedBy(String)}の反対.
	 * @param s 対象文字列
	 * @return 判定結果（{@literal true}：指定された文字列が続かない、{@literal false}：続く）
	 */
	public boolean currentIsNotFollowedBy(String s) {
		return (s == null || s.length() == 0 || line() == null) ? true : !(line().substring(columnNo()).startsWith(s));
	}
	/**
	 * 現在文字も含めパース対象コードの残りの部分が引数で指定された文字列で始まるかどうかを判定する.
	 * {@link #currentIsFollowedBy(String)}同様にこのメソッドも読み取り位置を前進させずに判定処理を行います。
	 * @param s 対象文字列
	 * @return 判定結果（{@literal true}：指定された文字列で始まる、{@literal false}：始まらない）
	 */
	public boolean remainingCodeStartsWith(String s) {
		return (s == null || s.length() == 0) ? false : (currentIs(s.charAt(0)) && (s.length() == 1 || currentIsFollowedBy(s.substring(1))));
	}
	/**
	 * 現在文字が引数で指定された文字のうちのいずれかと一致するかどうか判定する.
	 * @param cs 対象文字
	 * @return 判定結果（{@literal true}：いずれかに一致する、{@literal false}：いずれにも一致しない）
	 */
	public boolean currentIsAnyOf(char... cs) {
		for(final char c : cs) {
			if(currentIs(c)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * {@link #currentIsAnyOf(char...)}の反対.
	 * @param cs 対象文字
	 * @return 判定結果（{@literal true}：いずれにも一致しない、{@literal false}：いずれかに一致する）
	 */
	public boolean currentIsNotAnyOf(char... cs) {
		return ! currentIsAnyOf(cs);
	}
	/**
	 * 文字コード上（バイト表現上）、現在文字が引数で指定された2つの文字の間のいずれかの文字であるかどうか判定する.
	 * 判定ロジックは右のようになる： {@code (c0 <= current() && current() <= c1)}
	 * @param c0 対象文字
	 * @param c1 対象文字
	 * @return 判定結果（{@literal true}：いずれかに一致する、{@literal false}：いずれにも一致しない）
	 */
	public boolean currentIsBetween(char c0, char c1) {
		return (c0 <= c1) ? (c0 <= current() && current() <= c1) : (c1 <= current() && current() <= c0);
	}
	/**
	 * {@link #currentIsBetween(char, char)}の反対.
	 * @param c0 対象文字
	 * @param c1 対象文字
	 * @return 判定結果（{@literal true}：いずれにも一致しない、{@literal false}：いずれかに一致する）
	 */
	public boolean currentIsNotBetween(char c0, char c1) {
		return ! currentIsBetween(c0, c1);
	}
	
	@Override
	public char current() {
		return code().current();
	}
	@Override
	public char next() {
		return code().next();
	}
	@Override
	public int lineNo() {
		return code().lineNo();
	}
	@Override
	public int columnNo() {
		return code().columnNo();
	}
	@Override
	public String line() {
		return code().line();
	}
	@Override
	public boolean hasReachedEof() {
		return code().hasReachedEof();
	}
	
	public static class InputStreamBasedParsable implements Parsable {

		private final BufferedReader reader;

		private String line = null;

		private char current = '\u0000';

		private int position = -1;

		private int lineNo = 0;

		private boolean hasReachedEof = false;

		public InputStreamBasedParsable(final InputStream s, final String charset)
				throws IOException {
			this.reader = new BufferedReader(new InputStreamReader(s, charset));
			next();
		}

		public InputStreamBasedParsable(final InputStream s)
				throws IOException {
			this(s, "utf-8");
		}
		
		public InputStreamBasedParsable(final String s) throws IOException {
			this(new ByteArrayInputStream(s.getBytes()), "utf-8");
		}

		@Override
		public char current() {
			return current;
		}

		@Override
		public int columnNo() {
			return position + 1;
		}

		@Override
		public String line() {
			return line;
		}

		@Override
		public int lineNo() {
			return lineNo;
		}

		@Override
		public boolean hasReachedEof() {
			return hasReachedEof;
		}

		@Override
		public char next() {
			if (!hasReachedEof) {
				while (line == null || line.length() - 1 == position) {
					try {
						line = reader.readLine();
						lineNo += 1;
						position = -1;

						if (line == null) {
							hasReachedEof = true;
							current = '\u0000';
							return current;
						}
					} catch (IOException ex) {
						throw new UnexpectedException(ex);
					}
				}
				position += 1;
				current = line.charAt(position);
			}
			return current;
		}

		protected void close() {
			try {
				reader.close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}
}
