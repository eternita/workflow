/**
 * Copyright (c) 2013-2016, Neuro4j
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.neuro4j.workflow;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class UUIDMgr
{
    private static UUIDMgr instance;

    private Random random;

    private byte[] ip;

    private long processID;

    private short counter;

    private short startCounter;

    private long lastTime;


    static
    {
        instance = new UUIDMgr();
    }


    private UUIDMgr()
    {
        random = new Random();

        processID = System.nanoTime();

        counter = (short) (random.nextInt() & 0xffff);
        startCounter = counter;

        lastTime = 0L;
        try
        {
            InetAddress host = InetAddress.getLocalHost();
            ip = host.getAddress();
        } catch (UnknownHostException ue)
        {
            ip = new byte[] { (byte) (random.nextInt() & 0xff),
                    (byte) (random.nextInt() & 0xff),
                    (byte) (random.nextInt() & 0xff),
                    (byte) (random.nextInt() & 0xff) };
        }

    }

    public static UUIDMgr getInstance()
    {
        return instance;
    }

    public String createUUIDString()
    {
        return createUUID(0);
    }

    private String createUUID(int aTypeCode)
    {
        byte[] array = new byte[18];

        byte hiCode = (byte) ((aTypeCode >> 8) & 0xff);

        byte loCode = (byte) (aTypeCode & 0xff);

        array[8] = hiCode;

        array[9] = loCode;

        array[2] = ip[0];
        array[3] = ip[1];
        array[4] = ip[2];
        array[5] = ip[3];

        array[14] = (byte) ((processID >> 8) & 0xff);

        array[15] = (byte) (processID & 0xff);

        long time = 0L;

        short localCounter = 0;

        int rand = 0;

        synchronized (this)
        {
            time = System.currentTimeMillis();
            if (time == lastTime)
            {
                counter++;
                if (counter == startCounter)
                {
                    do
                    {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ie) {
                        }

                        time = System.currentTimeMillis();

                    } while (lastTime == time);

                    lastTime = time;
                }
            }
            else
            {
                lastTime = time;
                counter = (short) (random.nextInt() & 0xffff);
                startCounter = counter;
            }

            localCounter = counter;
            rand = random.nextInt();

            array[10] = (byte) ((time >> 40) & 0xff);
            array[11] = (byte) ((time >> 32) & 0xff);
            array[16] = (byte) ((time >> 24) & 0xff);
            array[17] = (byte) ((time >> 16) & 0xff);
            array[13] = (byte) ((time >> 8) & 0xff);
            array[12] = (byte) (time & 0xff);

            array[6] = (byte) ((localCounter >> 8) & 0xff);
            array[7] = (byte) (localCounter & 0xff);

            array[0] = (byte) ((rand >> 8) & 0xff);
            array[1] = (byte) (rand & 0xff);
        }

        return encodeUUID(array);
    }


    private String encodeUUID(byte[] array)
    {
        return encode(array);
    }

    public final static char[] ENCRYPTION_TABLE =
    {
            // 0 1 2 3 4 5 6 7
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // 0
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // 1
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // 2
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // 3
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // 4
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // 5
            'w', 'x', 'y', 'z', '0', '1', '2', '3', // 6
            '4', '5', '6', '7', '8', '9', '0', '_' // 7
    };

    protected final static byte[] dec_table =
    {
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    };

    private static String encode(byte[] data)
    {
        return new String(encodeAsByteArray(data));
    }

    private static byte[] encodeAsByteArray(byte[] data)
    {
        int i = 0, j = 0;
        int len = data.length;
        int delta = len % 3;
        int outlen = ((len + 2) / 3) * 4 + (len == 0 ? 2 : 0);
        byte[] output = new byte[outlen];
        byte a, b, c;

        for (int count = len / 3; count > 0; count--)
        {
            a = data[i++];
            b = data[i++];
            c = data[i++];
            output[j++] = (byte) (ENCRYPTION_TABLE[(a >>> 2) & 0x3F]);
            output[j++] = (byte) (ENCRYPTION_TABLE[((a << 4) & 0x30) + ((b >>> 4) & 0x0F)]);
            output[j++] = (byte) (ENCRYPTION_TABLE[((b << 2) & 0x3C) + ((c >>> 6) & 0x03)]);
            output[j++] = (byte) (ENCRYPTION_TABLE[c & 0x3F]);
        }

        if (delta == 1)
        {
            a = data[i++];
            output[j++] = (byte) (ENCRYPTION_TABLE[(a >>> 2) & 0x3F]);
            output[j++] = (byte) (ENCRYPTION_TABLE[((a << 4) & 0x30)]);
            output[j++] = (byte) '=';
            output[j++] = (byte) '=';
        }
        else if (delta == 2)
        {
            a = data[i++];
            b = data[i++];
            output[j++] = (byte) (ENCRYPTION_TABLE[(a >>> 2) & 0x3F]);
            output[j++] = (byte) (ENCRYPTION_TABLE[((a << 4) & 0x30) + ((b >>> 4) & 0x0F)]);
            output[j++] = (byte) (ENCRYPTION_TABLE[((b << 2) & 0x3C)]);
            output[j++] = (byte) '=';
        }

        if (j != outlen)
        {
            throw new InternalError("Bug: incorrect length calculated for base64 output");
        }

        return output;
    }

}
