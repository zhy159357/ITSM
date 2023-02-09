package com.ruoyi.activiti.web.esb.utils;

import java.io.*;

public class ByteArray {
    private DataInputStream dataInput;

    public ByteArray(byte[] bytes) {
        this.dataInput = new DataInputStream(new ByteArrayInputStream(bytes));
    }

    public int available() throws IOException {
        return this.dataInput.available();
    }

    public final boolean readBoolean() throws IOException {
        return this.dataInput.readBoolean();
    }

    public final byte readByte() throws IOException {
        return this.dataInput.readByte();
    }

    public final int readUnsignedByte() throws IOException {
        return this.dataInput.readUnsignedByte();
    }

    public final int readUnsignedShort() throws IOException {
        int ch1 = this.dataInput.read();
        int ch2 = this.dataInput.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch2 << 8) + (ch1 << 0);
    }

    public int read() throws IOException {
        return this.dataInput.read();
    }

    public final short readShort() throws IOException {
        return this.dataInput.readShort();
    }

    public final long readLong() throws IOException {
        return this.dataInput.readLong();
    }

    public final char readChar() throws IOException {
        return this.dataInput.readChar();
    }

    public final int readInt() throws IOException {
        return this.dataInput.readInt();
    }

    public final int readUnsignedInt() throws IOException {
        int ch1 = this.dataInput.read();
        int ch2 = this.dataInput.read();
        int ch3 = this.dataInput.read();
        int ch4 = this.dataInput.read();

        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        int result = (ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0);
        return result;
    }

    public final float readFloat() throws IOException {
        return this.dataInput.readFloat();
    }

    public final double readDouble() throws IOException {
        return this.dataInput.readDouble();
    }

    public final String readUTF() throws IOException {
        byte ch1 = this.dataInput.readByte();
        byte ch2 = this.dataInput.readByte();

        DataInputStream lengthDataInput = new DataInputStream(new ByteArrayInputStream(new byte[]{ch2, ch1}));

        String result = readUTFBytes(lengthDataInput.readUnsignedShort());

        return result;
    }

    public final String readUTFBytes(int length)
            throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        for (int i = 0; i < length; ++i) {
            byte b = this.dataInput.readByte();
            if ((length > 31) && (i == 0)) {
                continue;
            }
            out.write(b);
        }
        String result = new String(out.toByteArray(), "UTF-8").trim();
        return result;
    }
}