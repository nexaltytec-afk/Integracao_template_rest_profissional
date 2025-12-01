package br.com.nexalty.template_rest_profissional.utils;

public class Base64Helper {
    private final static char[] map = {
            '1', 'a', 'g', 'M', 'n', 'T', 'U', '-',
            'A', '2', 'h', 'L', 'o', 'S', 's', '+',
            'B', 'b', '3', 'K', 'p', 'R', 'V', 'w',
            'C', 'c', 'i', '4', 'q', 'Q', 'v', 'W',
            'D', 'd', 'j', 'J', '5', 'P', 'x', 'Z',
            'E', 'e', 'k', 'I', 'r', '6', 'X', 'z',
            'F', '0', 'l', 'H', 't', 'O', '7', 'y',
            '9', 'f', 'm', 'G', 'u', 'N', 'Y', '8'
    };


    public final static String encode(byte[] ba) {
        int sz = ba.length;
        StringBuffer sb = new StringBuffer();
        int i;
        for (i = 0; i < (sz -2); i += 3) {
            sb.append(map[(ba[i] >>> 2) & 0x3F]);
            sb.append(map[((ba[i] << 4) & 0x30) + ((ba[i+1] >>> 4) & 0xf)]);
            sb.append(map[((ba[i+1] << 2) & 0x3c) + ((ba[i+2] >>> 6) & 0x3)]);
            sb.append(map[ba[i+2] & 0X3F]);
        }
        switch (sz - i) {
            case 0:
                break;
            case 1:
                sb.append(map[(ba[i] >>> 2) & 0x3F]);
                sb.append(map[((ba[i] << 4) & 0x30)]);
                sb.append('=');
                sb.append('=');
                break;
            case 2:
                sb.append(map[(ba[i] >>> 2) & 0x3F]);
                sb.append(map[((ba[i] << 4) & 0x30) + ((ba[i+1] >>> 4) & 0xf)]);
                sb.append(map[((ba[i+1] << 2) & 0x3c)]);
                sb.append('=');
                break;
        }
        return sb.toString();
    }

    public final static byte[] decode(String s)
            throws IllegalArgumentException {
        int nb;
        byte[] bo = new byte[s.length()];
        int boi = 0;
        byte[] b = s.getBytes();
        byte b1 = (byte) -1, b2 = (byte) -1, b3 = (byte) -1, b4 = (byte) -1;
        for (int i = 0; i < s.length(); i += 4) {
            for (int j = 0; j < 64; ++j) {
                if (b[i] == map[j])
                    b1 = (byte) j;
                if (b[i + 1] == map[j])
                    b2 = (byte) j;
                if (b[i + 2] == map[j])
                    b3 = (byte) j;
                if (b[i + 3] == map[j])
                    b4 = (byte) j;
            }
            //if ((b1 <0) || (b2 < 0) || (b3 < 0) || (b4 < 0))
            if ((b1 < 0) || (b2 < 0))
                throw new IllegalArgumentException("decode invalid character ");
            nb = 3;
            if (b[i + 3] == '=')
                nb = 2;
            if (b[i + 2] == '=')
                nb = 1;
            switch (nb) {
                case 1:
                    bo[boi++] = (byte) ((b1 << 2) | (b2 >>> 4));
                    break;
                case 2:
                    bo[boi++] = (byte) ((b1 << 2) | (b2 >>> 4));
                    bo[boi++] = (byte) ((b2 << 4) | (b3 >>> 2));
                    break;
                case 3:
                    bo[boi++] = (byte) ((b1 << 2) | (b2 >>> 4));
                    bo[boi++] = (byte) ((b2 << 4) | (b3 >>> 2));
                    bo[boi++] = (byte) ((b3 << 6) | b4);
                    break;
            }
        }
        byte[] br = new byte[boi];
        for (int i = 0; i < boi; ++i)
            br[i] = bo[i];
        return br;
    }
}
