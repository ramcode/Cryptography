import java.io.PrintStream;
import java.util.Random;

public class GenAES
{
  private AES myAES;
  private static final String usage = "Usage:\nGenAES [n]\n\t- generate 1 [n] random AES triples\n";

  public static void main(String[] paramArrayOfString)
  {
    byte[] arrayOfByte1 = new byte[16];
    byte[] arrayOfByte2 = new byte[16];

    int i = 1;

    if (paramArrayOfString.length > 0) {
      i = Integer.parseInt(paramArrayOfString[0]);
    }

    Random localRandom = new Random(System.currentTimeMillis());

    System.out.println("# Random AES (key,plain,cipher) triples");

    for (int j = 0; j < i; j++)
    {
      localRandom.nextBytes(arrayOfByte1);
      localRandom.nextBytes(arrayOfByte2);

      AES localAES = new AES();
      localAES.setKey(arrayOfByte1);
      byte[] arrayOfByte3 = localAES.encrypt(arrayOfByte2);

      System.out.println(Util.toHEX1(arrayOfByte1) + " " + Util.toHEX1(arrayOfByte2) + " " + Util.toHEX1(arrayOfByte3));
    }
  }
}