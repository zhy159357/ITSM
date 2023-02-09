package com.ruoyi.activiti.web.esb.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayWriter
{
    private DataOutputStream dataOut = null;
    private OutputStream output;

    public ByteArrayWriter(OutputStream output)
    {
        this.dataOut = new DataOutputStream(output);
        this.output = output;
    }

    public void writeUTF(String value) throws IOException {
        this.dataOut.write(value.getBytes().length + 1);
        this.dataOut.writeUTF(value);
    }

    public void writeInt(int value) throws IOException {
        this.dataOut.writeInt(value);
    }

    public void close() throws IOException {
        this.output.close();
    }
}
