import java.io.PrintStream;
import java.util.Arrays;

class AES
{
  public int traceLevel = 0;

  public String traceInfo = "";
  public static final int ROUNDS = 14;
  public static final int BLOCK_SIZE = 16;
  public static final int KEY_LENGTH = 32;
  int numRounds;
  byte[][] Ke;
  byte[][] Kd;
  static final byte[] S = { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 
    125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, 
    -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, 
    -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, 
    -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, 
    -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, 
    -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, 
    -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 
    78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, 
    -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 
    105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, 
    -103, 45, 15, -80, 84, -69, 22 };

  static final byte[] Si = { 82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, 
    -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 
    76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, 
    -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, 
    -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 
    69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, 
    -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 
    28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, 
    -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 
    89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 
    59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 
    105, 20, 99, 85, 33, 12, 125 };

  static final byte[] rcon = { 0, 1, 2, 4, 8, 16, 32, 64, -128, 27, 54, 108, -40, -85, 77, -102, 47, 94, -68, 99, -58, 
    -105, 53, 106, -44, -77, 125, -6, -17, -59, -111 };
  public static final int COL_SIZE = 4;
  public static final int NUM_COLS = 4;
  public static final int ROOT = 283;
  static final int[] row_shift = { 0, 1, 2, 3 };

  static final int[] alog = new int[256];

  static final int[] log = new int[256];

  static
  {
    alog[0] = 1;
    for (int i = 1; i < 256; i++) {
      int j = alog[(i - 1)] << 1 ^ alog[(i - 1)];
      if ((j & 0x100) != 0)
        j ^= 283;
      alog[i] = j;
    }
    for (int i = 1; i < 255; i++)
      log[alog[i]] = i;
  }

  public static int getRounds(int paramInt)
  {
    switch (paramInt) {
    case 16:
      return 10;
    case 24:
      return 12;
    }
    return 14;
  }

  static final int mul(int paramInt1, int paramInt2) {
    return (paramInt1 != 0) && (paramInt2 != 0) ? alog[((log[(paramInt1 & 0xFF)] + log[(paramInt2 & 0xFF)]) % 255)] : 
      0;
  }

  public static void trace_static() {
    System.out.print("AES Static Tables\n");
    System.out.print("S[] = \n");

    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++)
        System.out.print(Util.toHEX1(S[(i * 16 + j)]) + ", ");
      System.out.println();
    }
    System.out.print("Si[] = \n");
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++)
        System.out.print(Util.toHEX1(Si[(i * 16 + j)]) + ", ");
      System.out.println();
    }
    System.out.print("rcon[] = \n");
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 6; j++)
        System.out.print(Util.toHEX1(rcon[(i * 6 + j)]) + ", ");
      System.out.println();
    }
    System.out.print("log[] = \n");
    for (int i = 0; i < 32; i++) {
      for (int j = 0; j < 8; j++)
        System.out.print(Util.toHEX1(log[(i * 8 + j)]) + ", ");
      System.out.println();
    }
    System.out.print("alog[] = \n");
    for (int i = 0; i < 32; i++) {
      for (int j = 0; j < 8; j++)
        System.out.print(Util.toHEX1(alog[(i * 8 + j)]) + ", ");
      System.out.println();
    }
  }

  public byte[] encrypt(byte[] paramArrayOfByte) {
    byte[] arrayOfByte1 = new byte[16];
    byte[] arrayOfByte2 = new byte[16];

    this.traceInfo = "";
    if (this.traceLevel > 0) {
      this.traceInfo = ("encryptAES(" + Util.toHEX1(paramArrayOfByte) + ")");
    }
    if (paramArrayOfByte == null)
      throw new IllegalArgumentException("Empty plaintext");
    if (paramArrayOfByte.length != 16) {
      throw new IllegalArgumentException("Incorrect plaintext length");
    }

    byte[] arrayOfByte3 = this.Ke[0];
    for (int i = 0; i < 16; i++)
      arrayOfByte1[i] = (byte)(paramArrayOfByte[i] ^ arrayOfByte3[i]);
    this.traceInfo = 
      (this.traceInfo + "\n  R0 (Key = " + Util.toHEX1(arrayOfByte3) + " ,After AddRoundkey = " + 
      Util.toHEX1(arrayOfByte1) + ")" + "\n");

    for (int n = 1; n < this.numRounds; n++) {
      arrayOfByte3 = this.Ke[n];
      if (this.traceLevel > 1) {
        this.traceInfo = (this.traceInfo + "\n  R" + n + " (Key = " + Util.toHEX1(arrayOfByte3));
      }
      for (int i = 0; i < 16; i++)
        arrayOfByte2[i] = S[(arrayOfByte1[i] & 0xFF)];
      this.traceInfo = (this.traceInfo + " ,After SubBytes= " + Util.toHEX1(arrayOfByte2));
      if (this.traceLevel > 2) {
        this.traceInfo = (this.traceInfo + "\n\tSB = " + Util.toHEX1(arrayOfByte2));
      }

      for (int i = 0; i < 16; i++) {
        int k = i % 4;
        int j = (i + row_shift[k] * 4) % 16;
        arrayOfByte1[i] = arrayOfByte2[j];
      }
      this.traceInfo = (this.traceInfo + " ,After Shift rows= " + Util.toHEX1(arrayOfByte1));
      if (this.traceLevel > 2) {
        this.traceInfo = (this.traceInfo + "\n\tSR = " + Util.toHEX1(arrayOfByte1));
      }

      for (int m = 0; m < 4; m++) {
        int i = m * 4;
        arrayOfByte2[i] = 
          (byte)(mul(2, arrayOfByte1[i]) ^ mul(3, arrayOfByte1[(i + 1)]) ^ 
          arrayOfByte1[(i + 2)] ^ arrayOfByte1[(i + 3)]);
        arrayOfByte2[(i + 1)] = 
          (byte)(arrayOfByte1[i] ^ mul(2, arrayOfByte1[(i + 1)]) ^ 
          mul(3, arrayOfByte1[(i + 2)]) ^ arrayOfByte1[(i + 3)]);
        arrayOfByte2[(i + 2)] = 
          (byte)(arrayOfByte1[i] ^ arrayOfByte1[(i + 1)] ^ mul(2, arrayOfByte1[(i + 2)]) ^ 
          mul(3, arrayOfByte1[(i + 3)]));
        arrayOfByte2[(i + 3)] = 
          (byte)(mul(3, arrayOfByte1[i]) ^ arrayOfByte1[(i + 1)] ^ arrayOfByte1[(i + 2)] ^ 
          mul(2, arrayOfByte1[(i + 3)]));
      }
      this.traceInfo = (this.traceInfo + " ,After Mix columns= " + Util.toHEX1(arrayOfByte2));
      if (this.traceLevel > 2) {
        this.traceInfo = (this.traceInfo + "\n\tMC = " + Util.toHEX1(arrayOfByte2));
      }

      for (int i = 0; i < 16; i++)
        arrayOfByte1[i] = (byte)(arrayOfByte2[i] ^ arrayOfByte3[i]);
      this.traceInfo = (this.traceInfo + " ,After AddRoundKey= " + Util.toHEX1(arrayOfByte1) + "\n");
      if (this.traceLevel > 2)
        this.traceInfo += "\n\tAK";
      if (this.traceLevel > 1)
      {
        continue;
      }
    }

    arrayOfByte3 = this.Ke[this.numRounds];
    if (this.traceLevel > 1) {
      this.traceInfo = (this.traceInfo + "\n  R" + this.numRounds + " (Key = " + Util.toHEX1(arrayOfByte3));
    }
    for (int i = 0; i < 16; i++)
      arrayOfByte1[i] = S[(arrayOfByte1[i] & 0xFF)];
    this.traceInfo = (this.traceInfo + " ,After SubBytes= " + Util.toHEX1(arrayOfByte1));
    if (this.traceLevel > 2) {
      this.traceInfo = (this.traceInfo + "\n\tSB = " + Util.toHEX1(arrayOfByte1));
    }
    for (int i = 0; i < 16; i++) {
      int k = i % 4;
      int j = (i + row_shift[k] * 4) % 16;
      arrayOfByte2[i] = arrayOfByte1[j];
    }
    this.traceInfo = (this.traceInfo + " ,After Shift rows= " + Util.toHEX1(arrayOfByte1));
    if (this.traceLevel > 2) {
      this.traceInfo = (this.traceInfo + "\n\tSR = " + Util.toHEX1(arrayOfByte1));
    }
    for (int i = 0; i < 16; i++)
      arrayOfByte1[i] = (byte)(arrayOfByte2[i] ^ arrayOfByte3[i]);
    this.traceInfo = (this.traceInfo + " ,After AddRoundKey= " + Util.toHEX1(arrayOfByte1) + "\n");

    return arrayOfByte1;
  }

  public byte[] decrypt(byte[] paramArrayOfByte) {
    byte[] arrayOfByte1 = new byte[16];
    byte[] arrayOfByte2 = new byte[16];

    this.traceInfo = "";
    if (this.traceLevel > 0) {
      this.traceInfo = ("decryptAES(" + Util.toHEX1(paramArrayOfByte) + ")");
    }
    if (paramArrayOfByte == null)
      throw new IllegalArgumentException("Empty ciphertext");
    if (paramArrayOfByte.length != 16) {
      throw new IllegalArgumentException("Incorrect ciphertext length");
    }

    byte[] arrayOfByte3 = this.Kd[0];
    for (int i = 0; i < 16; i++)
      arrayOfByte1[i] = (byte)(paramArrayOfByte[i] ^ arrayOfByte3[i]);
    if (this.traceLevel > 2)
      this.traceInfo = 
        (this.traceInfo + "\n  R0 (Key = " + Util.toHEX1(arrayOfByte3) + ")\n\t AK = " + 
        Util.toHEX1(arrayOfByte1));
    else if (this.traceLevel > 1) {
      this.traceInfo = 
        (this.traceInfo + "\n  R0 (Key = " + Util.toHEX1(arrayOfByte3) + ")\t = " + 
        Util.toHEX1(arrayOfByte1));
    }

    for (int n = 1; n < this.numRounds; n++) {
      arrayOfByte3 = this.Kd[n];
      if (this.traceLevel > 1) {
        this.traceInfo = (this.traceInfo + "\n  R" + n + " (Key = " + Util.toHEX1(arrayOfByte3) + ")\t");
      }
      for (int i = 0; i < 16; i++) {
        int k = i % 4;

        int j = (i + 16 - row_shift[k] * 4) % 16;
        arrayOfByte2[i] = arrayOfByte1[j];
      }
      if (this.traceLevel > 2) {
        this.traceInfo = (this.traceInfo + "\n\tISR = " + Util.toHEX1(arrayOfByte2));
      }
      for (int i = 0; i < 16; i++)
        arrayOfByte1[i] = Si[(arrayOfByte2[i] & 0xFF)];
      if (this.traceLevel > 2) {
        this.traceInfo = (this.traceInfo + "\n\tISB = " + Util.toHEX1(arrayOfByte1));
      }
      for (int i = 0; i < 16; i++)
        arrayOfByte2[i] = (byte)(arrayOfByte1[i] ^ arrayOfByte3[i]);
      if (this.traceLevel > 2) {
        this.traceInfo = (this.traceInfo + "\n\t AK = " + Util.toHEX1(arrayOfByte2));
      }
      for (int m = 0; m < 4; m++) {
        int i = m * 4;
        arrayOfByte1[i] = 
          (byte)(mul(14, arrayOfByte2[i]) ^ mul(11, arrayOfByte2[(i + 1)]) ^ 
          mul(13, arrayOfByte2[(i + 2)]) ^ mul(9, arrayOfByte2[(i + 3)]));
        arrayOfByte1[(i + 1)] = 
          (byte)(mul(9, arrayOfByte2[i]) ^ mul(14, arrayOfByte2[(i + 1)]) ^ 
          mul(11, arrayOfByte2[(i + 2)]) ^ mul(13, arrayOfByte2[(i + 3)]));
        arrayOfByte1[(i + 2)] = 
          (byte)(mul(13, arrayOfByte2[i]) ^ mul(9, arrayOfByte2[(i + 1)]) ^ 
          mul(14, arrayOfByte2[(i + 2)]) ^ mul(11, arrayOfByte2[(i + 3)]));
        arrayOfByte1[(i + 3)] = 
          (byte)(mul(11, arrayOfByte2[i]) ^ mul(13, arrayOfByte2[(i + 1)]) ^ 
          mul(9, arrayOfByte2[(i + 2)]) ^ mul(14, arrayOfByte2[(i + 3)]));
      }
      if (this.traceLevel > 2)
        this.traceInfo += "\n\tIMC";
      if (this.traceLevel <= 1)
        continue;
      this.traceInfo = (this.traceInfo + " = " + Util.toHEX1(arrayOfByte1));
    }

    arrayOfByte3 = this.Kd[this.numRounds];
    if (this.traceLevel > 1) {
      this.traceInfo = 
        (this.traceInfo + "\n  R" + this.numRounds + " (Key = " + Util.toHEX1(arrayOfByte3) + 
        ")\t");
    }
    for (int i = 0; i < 16; i++) {
      int k = i % 4;

      int j = (i + 16 - row_shift[k] * 4) % 16;
      arrayOfByte2[i] = arrayOfByte1[j];
    }
    if (this.traceLevel > 2) {
      this.traceInfo = (this.traceInfo + "\n\tISR = " + Util.toHEX1(arrayOfByte1));
    }
    for (int i = 0; i < 16; i++)
      arrayOfByte2[i] = Si[(arrayOfByte2[i] & 0xFF)];
    if (this.traceLevel > 2) {
      this.traceInfo = (this.traceInfo + "\n\tISB = " + Util.toHEX1(arrayOfByte1));
    }
    for (int i = 0; i < 16; i++)
      arrayOfByte1[i] = (byte)(arrayOfByte2[i] ^ arrayOfByte3[i]);
    if (this.traceLevel > 2)
      this.traceInfo += "\n\t AK";
    if (this.traceLevel > 1)
      this.traceInfo = (this.traceInfo + " = " + Util.toHEX1(arrayOfByte1) + "\n");
    if (this.traceLevel > 0)
      this.traceInfo = (this.traceInfo + " = " + Util.toHEX1(arrayOfByte1) + "\n");
    return arrayOfByte1;
  }

  public void setKey(byte[] paramArrayOfByte) {
    int i = paramArrayOfByte.length;
    int j = i / 4;

    this.traceInfo = "";
    if (this.traceLevel > 0) {
      this.traceInfo = ("setKey(" + Util.toHEX1(paramArrayOfByte) + ")\n");
    }
    if (paramArrayOfByte == null)
      throw new IllegalArgumentException("Empty key");
    if ((paramArrayOfByte.length != 16) && (paramArrayOfByte.length != 24) && (paramArrayOfByte.length != 32)) {
      throw new IllegalArgumentException("Incorrect key length");
    }

    this.numRounds = getRounds(i);
    int i1 = (this.numRounds + 1) * 4;

    byte[] arrayOfByte1 = new byte[i1];
    byte[] arrayOfByte2 = new byte[i1];
    byte[] arrayOfByte3 = new byte[i1];
    byte[] arrayOfByte4 = new byte[i1];

    this.Ke = new byte[this.numRounds + 1][16];
    this.Kd = new byte[this.numRounds + 1][16];

    int k = 0;
    for (int m = 0; k < j; k++) {
      arrayOfByte1[k] = paramArrayOfByte[(m++)];
      arrayOfByte2[k] = paramArrayOfByte[(m++)];
      arrayOfByte3[k] = paramArrayOfByte[(m++)];
      arrayOfByte4[k] = paramArrayOfByte[(m++)];
    }

    for (k = j; k < i1; k++) {
      int i2 = arrayOfByte1[(k - 1)];
      int i3 = arrayOfByte2[(k - 1)];
      int i4 = arrayOfByte3[(k - 1)];
      int i5 = arrayOfByte4[(k - 1)];
      if (k % j == 0) {
        int i6 = i2;
        i2 = (byte)(S[(i3 & 0xFF)] ^ rcon[(k / j)]);
        i3 = S[(i4 & 0xFF)];
        i4 = S[(i5 & 0xFF)];
        i5 = S[(i6 & 0xFF)];
      } else if ((j > 6) && (k % j == 4)) {
        i2 = S[(i2 & 0xFF)];
        i3 = S[(i3 & 0xFF)];
        i4 = S[(i4 & 0xFF)];
        i5 = S[(i5 & 0xFF)];
      }

      arrayOfByte1[k] = (byte)(arrayOfByte1[(k - j)] ^ i2);
      arrayOfByte2[k] = (byte)(arrayOfByte2[(k - j)] ^ i3);
      arrayOfByte3[k] = (byte)(arrayOfByte3[(k - j)] ^ i4);
      arrayOfByte4[k] = (byte)(arrayOfByte4[(k - j)] ^ i5);
    }

    int n = 0;
    for (k = 0; n < this.numRounds + 1; n++) {
      for (int m = 0; m < 4; m++) {
        this.Ke[n][(4 * m)] = arrayOfByte1[k];
        this.Ke[n][(4 * m + 1)] = arrayOfByte2[k];
        this.Ke[n][(4 * m + 2)] = arrayOfByte3[k];
        this.Ke[n][(4 * m + 3)] = arrayOfByte4[k];
        this.Kd[(this.numRounds - n)][(4 * m)] = arrayOfByte1[k];
        this.Kd[(this.numRounds - n)][(4 * m + 1)] = arrayOfByte2[k];
        this.Kd[(this.numRounds - n)][(4 * m + 2)] = arrayOfByte3[k];
        this.Kd[(this.numRounds - n)][(4 * m + 3)] = arrayOfByte4[k];
        k++;
      }

    }

    if (this.traceLevel > 3) {
      this.traceInfo += "  Encrypt Round keys:\n";
      for (n = 0; n < this.numRounds + 1; n++)
        this.traceInfo = (this.traceInfo + "  R" + n + "\t = " + Util.toHEX1(this.Ke[n]) + "\n");
      this.traceInfo += "  Decrypt Round keys:\n";
      for (n = 0; n < this.numRounds + 1; n++)
        this.traceInfo = (this.traceInfo + "  R" + n + "\t = " + Util.toHEX1(this.Kd[n]) + "\n");
    }
  }

  public static void self_test(String paramString1, String paramString2, String paramString3, int paramInt) {
    byte[] arrayOfByte1 = Util.hex2byte(paramString1);
    byte[] arrayOfByte2 = Util.hex2byte(paramString2);
    byte[] arrayOfByte3 = Util.hex2byte(paramString3);

    AES localAES = new AES();
    localAES.traceLevel = paramInt;
    localAES.setKey(arrayOfByte1);
    System.out.print(localAES.traceInfo);

    byte[] arrayOfByte4 = localAES.encrypt(arrayOfByte2);
    System.out.print(localAES.traceInfo);
    if (Arrays.equals(arrayOfByte4, arrayOfByte3))
      System.out.print("Test OK\n");
    else {
      System.out.print("Test Failed. Result was " + Util.toHEX(arrayOfByte4) + "\n");
    }
    arrayOfByte4 = localAES.decrypt(arrayOfByte3);
    System.out.print(localAES.traceInfo);
    if (Arrays.equals(arrayOfByte4, arrayOfByte2))
      System.out.print("Test OK\n");
    else
      System.out.print("Test Failed. Result was " + Util.toHEX(arrayOfByte4) + "\n");
    System.out.println();
  }

  public static void main(String[] paramArrayOfString) {
    int i = 2;

    switch (paramArrayOfString.length) {
    case 0:
      break;
    case 1:
      i = Integer.parseInt(paramArrayOfString[0]);
      break;
    case 3:
      self_test(paramArrayOfString[0], paramArrayOfString[1], paramArrayOfString[2], i);
      System.exit(0);
      break;
    case 4:
      i = Integer.parseInt(paramArrayOfString[3]);
      if (i > 4)
        trace_static();
      self_test(paramArrayOfString[0], paramArrayOfString[1], paramArrayOfString[2], i);
      System.exit(0);
      break;
    case 2:
    default:
      System.out.println("Usage: AES [lev | key plain cipher {lev}]\n");
      System.exit(0);
    }

    if (i > 4) {
      trace_static();
    }
    self_test("000102030405060708090a0b0c0d0e0f", "00112233445566778899aabbccddeeff", 
      "69c4e0d86a7b0430d8cdb78070b4c55a", i);

    self_test("000102030405060708090a0b0c0d0e0f1011121314151617", "00112233445566778899aabbccddeeff", 
      "dda97ca4864cdfe06eaf70a0ec0d7191", i);

    self_test("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", 
      "00112233445566778899aabbccddeeff", "8ea2b7ca516745bfeafc49904b496089", i);
  }
}