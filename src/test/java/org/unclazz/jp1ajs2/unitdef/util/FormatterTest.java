package org.unclazz.jp1ajs2.unitdef.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.TestUtils;
import org.unclazz.jp1ajs2.unitdef.util.Formatter;

public class FormatterTest {
	
	@Test
	public void formatはユニット定義文字列を返す() {
		final Formatter f = Formatter.DEFAULT;
		assertThat(f.format(TestUtils.minimalUnitDef1()).toString(), is(TestUtils.minimalUnitDefString1));
		assertThat(f.format(TestUtils.minimalUnitDef2()).toString(), is(TestUtils.minimalUnitDefString2));
		assertThat(f.format(TestUtils.jobnetUnitDef1()).toString(), is(TestUtils.jobnetUnitDefString1));
		assertThat(f.format(TestUtils.nestedUnitDef1()).toString(), is(TestUtils.nestedUnitDefString1));
	}

}
