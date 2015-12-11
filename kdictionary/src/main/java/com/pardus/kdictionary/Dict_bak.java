package com.pardus.kdictionary;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class Dict_bak {
	FileChannel fileChannel;

	static int indexFor(int h, int length) {
		// assert Integer.bitCount(length) == 1 :
		// "length must be a non-zero power of 2";
		return h & length - 1;
	}

//	public static void main(String[] args) throws IOException {
//		Dict dict = new Dict();
//		dict.put("aa", "bb");
//		dict.put("aa1", "bb1");
//		dict.put("aa2", "bb2");
//		dict.put("aa3", "bb3");
//		dict.put("aa4", "bb4");
//		System.out.println(dict.get("aa").translate);
//		System.out.println(dict.get("aa1").translate);
//		System.out.println(dict.get("aa2").translate);
//		System.out.println(dict.get("aa3").translate);
//		System.out.println(dict.get("aa4").translate);
//		dict.close();
//	}

	public Dict_bak(Context context) {
		try {
			File file = context.getExternalCacheDir() == null ? context.getCacheDir(): context.getExternalCacheDir();
			String path = file.getAbsolutePath();
			Log.e("liang",context.getExternalCacheDir()+"");
			fileChannel = new RandomAccessFile(path+ File.separator+"dic.dat", "rwd").getChannel();
			capacity = 4;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	transient int size;

	transient int capacity;
	/** 数据块大小,单位字节 */
//	private static final byte block_size = 42;
	private static final byte block_size = 120;

	final int hash(Object k) {
		int h = 0;

		h ^= k.hashCode();

		// This function ensures that hashCodes that differ only by
		// constant multiples at each bit position have a bounded
		// number of collisions (approximately 8 at default load factor).
		h ^= h >>> 20 ^ h >>> 12;
		return h ^ h >>> 7 ^ h >>> 4;
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
			b.nextPosition = (int) fileChannel.size();
			writeWordBlock(b);
			b = new WordBlock(fileChannel.size());
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
		buffer.force();
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
}
