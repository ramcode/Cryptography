class Util
{
  public static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

  public static byte[] short2byte(short[] paramArrayOfShort)
  {
    int i = paramArrayOfShort.length;
    byte[] arrayOfByte = new byte[i * 2];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfShort[(j++)];
      arrayOfByte[(k++)] = (byte)(m >>> 8 & 0xFF);
      arrayOfByte[(k++)] = (byte)(m & 0xFF);
    }
    return arrayOfByte;
  }

  public static short[] byte2short(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    short[] arrayOfShort = new short[i / 2];
    int j = 0; for (int k = 0; k < i / 2; ) {
      arrayOfShort[(k++)] = (short)((paramArrayOfByte[(j++)] & 0xFF) << 8 | paramArrayOfByte[(j++)] & 0xFF);
    }

    return arrayOfShort;
  }

  public static byte[] int2byte(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    byte[] arrayOfByte = new byte[i * 4];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfInt[(j++)];
      arrayOfByte[(k++)] = (byte)(m >>> 24 & 0xFF);
      arrayOfByte[(k++)] = (byte)(m >>> 16 & 0xFF);
      arrayOfByte[(k++)] = (byte)(m >>> 8 & 0xFF);
      arrayOfByte[(k++)] = (byte)(m & 0xFF);
    }
    return arrayOfByte;
  }

  public static int[] byte2int(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    int[] arrayOfInt = new int[i / 4];
    int j = 0; for (int k = 0; k < i / 4; ) {
      arrayOfInt[(k++)] = ((paramArrayOfByte[(j++)] & 0xFF) << 24 | (paramArrayOfByte[(j++)] & 0xFF) << 16 | (paramArrayOfByte[(j++)] & 0xFF) << 8 | paramArrayOfByte[(j++)] & 0xFF);
    }

    return arrayOfInt;
  }

  public static String toHEX(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    char[] arrayOfChar = new char[i * 3];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfByte[(j++)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 4 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m & 0xF)];
      arrayOfChar[(k++)] = ' ';
    }
    return new String(arrayOfChar);
  }

  public static String toHEX(short[] paramArrayOfShort)
  {
    int i = paramArrayOfShort.length;
    char[] arrayOfChar = new char[i * 5];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfShort[(j++)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 12 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 8 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 4 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m & 0xF)];
      arrayOfChar[(k++)] = ' ';
    }
    return new String(arrayOfChar);
  }

  public static String toHEX(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    char[] arrayOfChar = new char[i * 10];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfInt[(j++)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 28 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 24 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 20 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 16 & 0xF)];
      arrayOfChar[(k++)] = ' ';
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 12 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 8 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 4 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m & 0xF)];
      arrayOfChar[(k++)] = ' ';
    }
    return new String(arrayOfChar);
  }

  public static String toHEX1(byte paramByte)
  {
    char[] arrayOfChar = new char[2];
    int i = 0;
    arrayOfChar[(i++)] = HEX_DIGITS[(paramByte >>> 4 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramByte & 0xF)];
    return new String(arrayOfChar);
  }

  public static String toHEX1(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    char[] arrayOfChar = new char[i * 2];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfByte[(j++)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 4 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m & 0xF)];
    }
    return new String(arrayOfChar);
  }

  public static String toHEX1(short[] paramArrayOfShort)
  {
    int i = paramArrayOfShort.length;
    char[] arrayOfChar = new char[i * 4];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfShort[(j++)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 12 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 8 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 4 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m & 0xF)];
    }
    return new String(arrayOfChar);
  }

  public static String toHEX1(int paramInt)
  {
    char[] arrayOfChar = new char[8];
    int i = 0;
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 28 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 24 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 20 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 16 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 12 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 8 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt >>> 4 & 0xF)];
    arrayOfChar[(i++)] = HEX_DIGITS[(paramInt & 0xF)];
    return new String(arrayOfChar);
  }

  public static String toHEX1(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    char[] arrayOfChar = new char[i * 8];
    int j = 0; for (int k = 0; j < i; ) {
      int m = paramArrayOfInt[(j++)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 28 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 24 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 20 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 16 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 12 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 8 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m >>> 4 & 0xF)];
      arrayOfChar[(k++)] = HEX_DIGITS[(m & 0xF)];
    }
    return new String(arrayOfChar);
  }

  public static byte[] hex2byte(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[(i + 1) / 2];

    int j = 0; int k = 0;
    if (i % 2 == 1) {
      arrayOfByte[(k++)] = (byte)hexDigit(paramString.charAt(j++));
    }
    while (j < i) {
      arrayOfByte[(k++)] = (byte)(hexDigit(paramString.charAt(j++)) << 4 | hexDigit(paramString.charAt(j++)));
    }

    return arrayOfByte;
  }

  public static boolean isHex(String paramString)
  {
    int i = paramString.length();
    int j = 0;

    while (j < i) {
      int k = paramString.charAt(j++);
      if (((k < 48) || (k > 57)) && ((k < 65) || (k > 70)) && ((k < 97) || (k > 102)))
        return false;
    }
    return true;
  }

  public static int hexDigit(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9'))
      return paramChar - '0';
    if ((paramChar >= 'A') && (paramChar <= 'F'))
      return paramChar - 'A' + 10;
    if ((paramChar >= 'a') && (paramChar <= 'f')) {
      return paramChar - 'a' + 10;
    }
    return 0;
  }
}