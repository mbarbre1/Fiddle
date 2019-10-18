package com.barbre.fiddle.io.image.z; // this file belongs to JavaZine's zfileio package

import java.io.DataInputStream;
import java.io.IOException;

/*
 ZFileInput 1.0   97/2/27
 Copyright (c) 1997, Marcel Schoen and Andre Pinheiro

 Code by Marcel Schoen (Marcel.Schoen@village.ch)
 Documentation/Arrangements by Andre Pinheiro (l41325@alfa.ist.utl.pt)

 All rights reserved.

 Published by JavaZine - Your Java webzine
 Links to JavaZine's websites
   - http://camoes.rnl.ist.utl.pt/~adlp/JavaZine/Links/JavaZine.html

 Permission to use, copy, modify, and distribute this software
 and its documentation for NON-COMMERCIAL or COMMERCIAL purposes
 and without fee is hereby granted provided that the copyright
 and "published by" messages above appear in all copies.
 We will not be held responsible for any unwanted effects due to
 the usage of this software or any derivative.
 No warrantees for usability for any specific application are
 given or implied.
*/

public class ZFileInput {
	private byte[] buffer = new byte[4];
	private DataInputStream data = null;
	private boolean intelSwitch = false;

	public final void setIntelSwitch(boolean value) {
		intelSwitch = value;
	}

	public final void use(DataInputStream data) throws IOException {
		this.data = data;
	}

	public final void close() throws IOException {
		data.close();
	}

	public final int available() throws IOException {
		return data.available();
	}

	public final void skipBytes(int n) throws IOException {
		data.skip(n);
	}

	public final int read16Bit() throws IOException {
		int n = 0;

		data.read(buffer, 0, 2);
		n = ((buffer[0] & 255) << 8) + (buffer[1] & 255);

		if (intelSwitch)
			return (int) switchEndianOrder((long) n);
		else
			return n;
	}

	public final long read32Bit() throws IOException {
		long n = 0;
		int b0 = 0, b1 = 0, b2 = 0, b3 = 0;

		data.read(buffer, 0, 4);

		b0 = (buffer[0] & 255);
		b1 = (buffer[1] & 255);
		b2 = (buffer[2] & 255);
		b3 = (buffer[3] & 255);

		if (intelSwitch) {
			n = (b3 << 24) + (b2 << 16);
			n += (b1 << 8) + b0;
		} else {
			n = (b0 << 24) + (b1 << 16);
			n += (b2 << 8) + b3;
		}

		return n;
	}

	public final int readByte() throws IOException {
		int n = 0;

		data.read(buffer, 0, 1);
		n = buffer[0] & 255;

		return n;
	}

	public final byte[] readBytes(int n, ZProgressStatus client) throws IOException {
		byte[] bytes = new byte[n];
		int num = n, bt = 0, off = 0, act = 1, percent = 0;

		//System.out.println("--- num: " + num);

		while (num > 0) {
			bt = data.available();
			if (bt > num)
				bt = num;

			//System.out.println("--- off: " + off + "  bt: " + bt);

			act = data.read(bytes, off, bt);
			if (act == -1) {
				num = 0;
				Thread.yield();
			}

			off += act;
			percent = (off * 100) / n;
			client.imageLoaded(percent, null, " ");
			num -= act;
		}

		return bytes;
	}

	public final byte[] readBytes(int n) throws IOException {
		byte[] bytes = new byte[n];
		int num = n, bt = 0, off = 0, act = 1;

		while (num > 0) {
			bt = data.available();
			if (bt > n)
				bt = n;

			act = data.read(bytes, off, bt);
			if (act < bt) {
				num = 0;
				Thread.yield();
			}

			off += act;
			num -= act;
		}

		return bytes;
	}

	public final String readString(int n) throws IOException {
		char[] chars = new char[n];

		for (int zl = 0; zl < n; zl++)
			chars[zl] = (char) data.read();

		return String.copyValueOf(chars);
	}

	// provides a switch for the Intel stuff
	private final long switchEndianOrder(long value) {
		long value2 = (value & 0xFFFFFFFF), n1 = value2 & 0xFF, n2 = (value2 >> 8) & 0xFF, n3 = (value2 >> 16) & 0xFF, n4 = (value2 >> 24) & 0xFF;

		return (n3 << 24) + (n4 << 16) + (n1 << 8) + n2;
	}

}