package com.pardus.kdictionary;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class Dict {
	FileChannel fileChannel;

	static int indexFor(int h, int length) {
		return h & length - 1;
	}

	public static void main(String[] args) throws IOException {
		long start1 = System.currentTimeMillis();
		int exceptNum = 10000;
		Dict dict = new Dict(exceptNum);
		while (exceptNum-- > 0) {
			dict.put("aa" + exceptNum, "bb" + exceptNum);
		}
		dict.force();
		System.out.println(System.currentTimeMillis() - start1);
		long start = System.currentTimeMillis();
		exceptNum = 10000;
		while (exceptNum-- > 0) {
			String a = dict.get("aa" + exceptNum).translate;
			// System.out.println(dict.get("aa" + capacity).translate);
		}
		System.out.println(System.currentTimeMillis() - start);
		dict.close();
	}

	public Dict(int capacity) {
		try {
			fileChannel = new RandomAccessFile("G:\\Smart\\Test\\dic.dat", "rwd").getChannel();
			this.capacity = 1;
			capacity = (int) (capacity / 0.75);
			while ((this.capacity <<= 1) < capacity) {
				;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	transient int size;

	transient int capacity;
	/** 数据块大小,单位字节 */
	private static final byte block_size = 42;

	transient int hashSeed = 0;

	final int hash(Object k) {
		int h = k.hashCode();
		h += ~(h << 9);
		h ^= h >>> 14;
		h += h << 4;
		h ^= h >>> 10;
		return h;
	}

	public WordBlock get(String key) throws IOException {
		int hash = hash(key);
		int i = indexFor(hash, capacity);
		int position = i * block_size;
		WordBlock b = readWordBlock(position);
		for (; b.nextPosition > 0; b = readWordBlock(b.nextPosition)) {
			Object k;
			if (hash(b.word) == hash && ((k = b.word) == key || key.equals(k))) {
				return b;
			}
		}
		if (b.nextPosition == -1) {
			Object k;
			if (hash(b.word) == hash && ((k = b.word) == key || key.equals(k))) {
				return b;
			}
		}
		return null;
	}

	public void put(String key, String value) throws IOException {
		int hash = hash(key);
		int i = indexFor(hash, capacity);
		int position = i * block_size;
		WordBlock b = readWordBlock(position);
		for (; b.nextPosition > 0; b = readWordBlock(b.nextPosition)) {
			Object k;
			if (hash(b.word) == hash && ((k = b.word) == key || key.equals(k))) {
				b.translate = value;
				writeWordBlock(b);
				return;
			}
		}
		if (b.nextPosition == 0) {
			b = new WordBlock(b.position);
		} else {
			int nextPosition = 0;
			if (fileChannel.size() > capacity * block_size) {
				nextPosition = (int) fileChannel.size();
			} else {
				nextPosition = capacity * block_size;
			}
			System.out.println(key + "  " + b.word);
			b.nextPosition = nextPosition;
			writeWordBlock(b);
			b = new WordBlock(nextPosition);
		}
		b.word = key;
		b.translate = value;
		b.nextPosition = -1;
		writeWordBlock(b);
	}

	private WordBlock readWordBlock(int position) throws IOException {
		MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, position, block_size).load();
		WordBlock block = new WordBlock(position);
		byte[] data = new byte[block_size];
		buffer.get(data);
		block.setData(data);
		block.decode();
		return block;
	}

	private void writeWordBlock(WordBlock block) throws IOException {
		MappedByteBuffer buffer = fileChannel.map(MapMode.READ_WRITE, block.position, block_size);
		byte[] data = block.encode();
		if (data.length > block_size) {
			throw new RuntimeException();
		}
		buffer.put(data);
		// buffer.force();
	}

	public void close() {
		if (fileChannel != null) {
			try {
				fileChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void force() throws IOException {
		fileChannel.force(true);
	}
}
