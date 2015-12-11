package com.pardus.kdictionary;
class WordBlock extends DataEntry {
	long position;
	/** 单词 */
	String word;
	/** 翻译 */
	String translate;
	int nextPosition = -1;

	public WordBlock(long position) {
		this.position = position;
	}

	@Override
	public byte[] encode() {
		reset(MODE.WRITE);
		writeString(word);
		writeString(translate);
		writeInt(nextPosition);
		return getData();
	}

	@Override
	public void decode() {
		reset(MODE.READ);
		word = readString();
		translate = readString();
		nextPosition = readInt();
	}
}