package usertools.jp1ajs2.unitdef.ext;

import usertools.jp1ajs2.unitdef.util.ValueResolver;

public enum WriteOption {
	NEW, ADD;

	public static final ValueResolver<WriteOption> VALUE_RESOLVER = new ValueResolver<WriteOption>() {
		@Override
		public WriteOption resolve(String rawValue) {
			return valueOf(rawValue.toUpperCase());
		}
	};
}
