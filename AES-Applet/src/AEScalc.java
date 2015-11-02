import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintStream;

public class AEScalc extends Applet
  implements ActionListener, ItemListener
{
  private AES myAES;
  private Label lTitle;
  private Label lData;
  private Label lKey;
  private Label lResult;
  private Label lTrace;
  private TextField tData;
  private TextField tKey;
  private TextField tResult;
  private Button bEncrypt;
  private Button bDecrypt;
  private Button bAbout;
  private Button bQuit;
  private CheckboxGroup cbTrace;
  private Checkbox cTrace0;
  private Checkbox cTrace1;
  private Checkbox cTrace2;
  private Checkbox cTrace3;
  private Checkbox cTrace4;
  private Checkbox cTrace5;
  private TextArea taTrace;
  private int traceLev = 2;

  boolean application = false;
  private static final String about = "AES Calculator Applet v2.0 Feb 2005.\n\nThe AES Calculator applet is used to encrypt or decrypt test data values\nusing AES block cipher.  It takes a 128-bit (32 hex digit) data value and a\n128/192/256-bit (32/48/64 hex digit) key.  It can optionally provide a trace\nof the calculations performed, with varying degrees of detail.\n\nAEScalc was written and is Copyright 2005 by Lawrie Brown.\nPermission is granted to copy, distribute, and use this applet\nprovided the author is acknowledged and this copyright notice remains intact.\n\nSee http://www.unsw.adfa.edu.au/~lpb/ for authors website.\n";
  private static final String usage = "Usage:\nAEScalc [-tlevel]\n\t- run AES calculator as GUI applet (with specified trace-level)\nAEScalc [-e|-d] [-tlevel] hexkey hexdata\n\t- AES en/decrypt data using key (both in hex)\n\t- with trace details at specified level (0-5)\n";

  public void init()
  {
    setLayout(new BorderLayout());

    setBackground(Color.white);

    Font localFont1 = new Font("Times", 1, 24);
    this.lTitle = new Label("AES Block Cipher Calculator");
    this.lTitle.setAlignment(1);
    this.lTitle.setFont(localFont1);
    this.lTitle.setForeground(Color.blue);
    add("North", this.lTitle);

    Panel localPanel1 = new Panel();
    GridBagLayout localGridBagLayout = new GridBagLayout();
    localPanel1.setLayout(localGridBagLayout);
    GridBagConstraints localGridBagConstraints = new GridBagConstraints();
    localGridBagConstraints.anchor = 17;
    Font localFont2 = new Font("Courier", 1, 14);

    this.lData = new Label("Input Data (in hex)");
    this.lData.setAlignment(0);
    this.lData.setFont(localFont2);
    this.lData.setForeground(Color.blue);
    localGridBagConstraints.gridwidth = -1;
    localGridBagLayout.setConstraints(this.lData, localGridBagConstraints);
    localPanel1.add(this.lData);

    this.tData = new TextField(32);
    this.tData.setText("");
    localGridBagConstraints.gridwidth = 0;
    localGridBagLayout.setConstraints(this.tData, localGridBagConstraints);
    localPanel1.add(this.tData);

    this.lKey = new Label("AES Key (in hex)");
    this.lKey.setAlignment(0);
    this.lKey.setFont(localFont2);
    this.lKey.setForeground(Color.blue);
    localGridBagConstraints.gridwidth = 1;
    localGridBagLayout.setConstraints(this.lKey, localGridBagConstraints);
    localPanel1.add(this.lKey);

    this.tKey = new TextField(64);
    this.tKey.setText("");
    localGridBagConstraints.gridwidth = 0;
    localGridBagLayout.setConstraints(this.tKey, localGridBagConstraints);
    localPanel1.add(this.tKey);

    this.lResult = new Label("Result displayed here.");
    this.lResult.setAlignment(0);
    this.lResult.setFont(localFont2);
    this.lResult.setForeground(Color.blue);
    localGridBagConstraints.gridwidth = 1;
    localGridBagLayout.setConstraints(this.lResult, localGridBagConstraints);
    localPanel1.add(this.lResult);

    this.tResult = new TextField(32);
    this.tResult.setEditable(false);
    localGridBagConstraints.gridwidth = 0;
    localGridBagLayout.setConstraints(this.tResult, localGridBagConstraints);
    localPanel1.add(this.tResult);

    Panel localPanel2 = new Panel();
    this.bEncrypt = new Button("Encrypt");
    this.bEncrypt.setForeground(Color.blue);
    localPanel2.add(this.bEncrypt);
    this.bEncrypt.addActionListener(this);
    this.bDecrypt = new Button("Decrypt");
    this.bDecrypt.setForeground(Color.blue);
    localPanel2.add(this.bDecrypt);
    this.bDecrypt.addActionListener(this);
    this.bAbout = new Button("About");
    this.bAbout.setForeground(Color.blue);
    localPanel2.add(this.bAbout);
    this.bAbout.addActionListener(this);
    this.bQuit = new Button("Quit");
    this.bQuit.setForeground(Color.blue);
    if (this.application) {
      localPanel2.add(this.bQuit);
      this.bQuit.addActionListener(this);
    }
    localGridBagConstraints.anchor = 10;
    localGridBagConstraints.gridwidth = 0;
    localGridBagLayout.setConstraints(localPanel2, localGridBagConstraints);
    localPanel1.add(localPanel2);

    add("Center", localPanel1);

    Panel localPanel3 = new Panel();
    localPanel3.setLayout(new BorderLayout());
    this.lTrace = new Label("Trace of AES Calculations or Errors");
    this.lTrace.setAlignment(1);
    this.lTrace.setFont(localFont2);
    this.lTrace.setForeground(Color.blue);
    localPanel3.add("North", this.lTrace);

    this.cbTrace = new CheckboxGroup();
    Panel localPanel4 = new Panel();
    Label localLabel = new Label("Trace Level: ");
    localLabel.setFont(localFont2);
    localLabel.setForeground(Color.blue);
    localPanel4.add(localLabel);
    this.cTrace0 = new Checkbox("0: none", this.cbTrace, this.traceLev == 0);
    localPanel4.add(this.cTrace0);
    this.cTrace0.addItemListener(this);
    this.cTrace1 = new Checkbox("1: calls", this.cbTrace, this.traceLev == 1);
    if (this.traceLev >= 1) localPanel4.add(this.cTrace1);
    this.cTrace1.addItemListener(this);
    this.cTrace2 = new Checkbox("2: +rounds", this.cbTrace, this.traceLev == 2);
    if (this.traceLev >= 2) localPanel4.add(this.cTrace2);
    this.cTrace2.addItemListener(this);
    this.cTrace3 = new Checkbox("3: +steps", this.cbTrace, this.traceLev == 3);
    if (this.traceLev >= 3) localPanel4.add(this.cTrace3);
    this.cTrace3.addItemListener(this);
    this.cTrace4 = new Checkbox("4: +subkeys", this.cbTrace, this.traceLev == 4);
    if (this.traceLev >= 4) localPanel4.add(this.cTrace4);
    this.cTrace4.addItemListener(this);
    this.cTrace5 = new Checkbox("5: +static", this.cbTrace, this.traceLev >= 5);
    if (this.traceLev >= 5) localPanel4.add(this.cTrace5);
    this.cTrace5.addItemListener(this);
    localPanel3.add("Center", localPanel4);

    Font localFont3 = new Font("Courier", 0, 12);
    this.taTrace = new TextArea("AES Calculator Applet v2.0 Feb 2005.\n\nThe AES Calculator applet is used to encrypt or decrypt test data values\nusing AES block cipher.  It takes a 128-bit (32 hex digit) data value and a\n128/192/256-bit (32/48/64 hex digit) key.  It can optionally provide a trace\nof the calculations performed, with varying degrees of detail.\n\nAEScalc was written and is Copyright 2005 by Lawrie Brown.\nPermission is granted to copy, distribute, and use this applet\nprovided the author is acknowledged and this copyright notice remains intact.\n\nSee http://www.unsw.adfa.edu.au/~lpb/ for authors website.\n", 20, 80, 1);
    this.taTrace.setEditable(false);
    this.taTrace.setFont(localFont3);
    this.taTrace.setBackground(Color.white);
    localPanel3.add("South", this.taTrace);

    add("South", localPanel3);

    this.myAES = new AES();

    this.myAES.traceLevel = this.traceLev;
  }

  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str3 = "";
    Object localObject = paramActionEvent.getSource();

    if (localObject == this.bEncrypt)
    {
      String str2 = this.tKey.getText();
      int i = str2.length();
      if (((i != 32) && (i != 48) && (i != 64)) || (!Util.isHex(str2)))
      {
        this.lResult.setForeground(Color.red);
        this.lResult.setText("Error in Key!!!");
        this.taTrace.setForeground(Color.red);
        this.taTrace.setText("AES key [" + str2 + "] must be 32 or 48 or 64 hex digits long.");

        this.tResult.setText("");
        return;
      }
      byte[] arrayOfByte2 = Util.hex2byte(str2);

      String str1 = this.tData.getText();
      if ((str1.length() != 32) || (!Util.isHex(str1)))
      {
        this.lResult.setForeground(Color.red);
        this.lResult.setText("Error in Data!!!");
        this.taTrace.setForeground(Color.red);
        this.taTrace.setText("AES data [" + str1 + "] must be strictly " + 32 + " hex digits long.");

        this.tResult.setText("");
        return;
      }
      byte[] arrayOfByte1 = Util.hex2byte(str1);

      this.myAES.setKey(arrayOfByte2);
      if (this.traceLev > 0) str3 = str3 + this.myAES.traceInfo;

      byte[] arrayOfByte3 = this.myAES.encrypt(arrayOfByte1);
      if (this.traceLev > 0) str3 = str3 + this.myAES.traceInfo;

      this.lResult.setForeground(Color.blue);
      this.lResult.setText("Encrypted value is:");
      this.tResult.setText(Util.toHEX1(arrayOfByte3));
      this.taTrace.setForeground(Color.black);
      this.taTrace.setText(str3);
    }
    else if (localObject == this.bDecrypt)
    {
      String str2 = this.tKey.getText();
      int i = str2.length();
      if (((i != 32) && (i != 48) && (i != 64)) || (!Util.isHex(str2)))
      {
        this.lResult.setForeground(Color.red);
        this.lResult.setText("Error in Key!!!");
        this.taTrace.setForeground(Color.red);
        this.taTrace.setText("AES key [" + str2 + "] must be 32 or 48 or 64 hex digits long.");

        this.tResult.setText("");
        return;
      }
      byte[] arrayOfByte2 = Util.hex2byte(str2);

      String str1 = this.tData.getText();
      if ((str1.length() != 32) || (!Util.isHex(str1)))
      {
        this.lResult.setForeground(Color.red);
        this.lResult.setText("Error in Data!!!");
        this.taTrace.setForeground(Color.red);
        this.taTrace.setText("AES data [" + str1 + "] must be strictly " + 32 + " hex digits long.");

        this.tResult.setText("");
        return;
      }
      byte[] arrayOfByte1 = Util.hex2byte(str1);

      this.myAES.setKey(arrayOfByte2);
      if (this.traceLev > 0) str3 = str3 + this.myAES.traceInfo;

      byte[] arrayOfByte3 = this.myAES.decrypt(arrayOfByte1);
      if (this.traceLev > 0) str3 = str3 + this.myAES.traceInfo;

      this.lResult.setForeground(Color.blue);
      this.lResult.setText("Decrypted value is:");
      this.tResult.setText(Util.toHEX1(arrayOfByte3));
      this.taTrace.setForeground(Color.black);
      this.taTrace.setText(str3);
    }
    else if (localObject == this.bAbout) {
      this.taTrace.setForeground(Color.black);
      this.taTrace.setText("AES Calculator Applet v2.0 Feb 2005.\n\nThe AES Calculator applet is used to encrypt or decrypt test data values\nusing AES block cipher.  It takes a 128-bit (32 hex digit) data value and a\n128/192/256-bit (32/48/64 hex digit) key.  It can optionally provide a trace\nof the calculations performed, with varying degrees of detail.\n\nAEScalc was written and is Copyright 2005 by Lawrie Brown.\nPermission is granted to copy, distribute, and use this applet\nprovided the author is acknowledged and this copyright notice remains intact.\n\nSee http://www.unsw.adfa.edu.au/~lpb/ for authors website.\n");
    } else if ((localObject == this.bQuit) && (this.application)) {
      System.exit(0);
    }
  }

  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    Object localObject = paramItemEvent.getSource();

    if (localObject == this.cTrace0) this.traceLev = 0;
    else if (localObject == this.cTrace1) this.traceLev = 1;
    else if (localObject == this.cTrace2) this.traceLev = 2;
    else if (localObject == this.cTrace3) this.traceLev = 3;
    else if (localObject == this.cTrace4) this.traceLev = 4;
    else if (localObject == this.cTrace5) this.traceLev = 5;

    this.myAES.traceLevel = this.traceLev;

    this.taTrace.setForeground(Color.black);
    this.taTrace.setText("AES trace level set to " + this.traceLev);
  }

  public static void main(String[] paramArrayOfString)
  {
    int i = 1;
    int j = -1;
    int k = 0;
    int m = paramArrayOfString.length;

    while ((m > 0) && (paramArrayOfString[k].startsWith("-"))) {
      if (paramArrayOfString[k].equals("-e")) {
        i = 1;
      } else if (paramArrayOfString[k].equals("-d")) {
        i = 0;
      } else if (paramArrayOfString[k].startsWith("-t")) {
        j = Integer.parseInt(paramArrayOfString[k].substring(2));
      } else {
        System.err.println("Unknown flag: " + paramArrayOfString[k] + "\n" + "Usage:\nAEScalc [-tlevel]\n\t- run AES calculator as GUI applet (with specified trace-level)\nAEScalc [-e|-d] [-tlevel] hexkey hexdata\n\t- AES en/decrypt data using key (both in hex)\n\t- with trace details at specified level (0-5)\n" + "\n" + "AES Calculator Applet v2.0 Feb 2005.\n\nThe AES Calculator applet is used to encrypt or decrypt test data values\nusing AES block cipher.  It takes a 128-bit (32 hex digit) data value and a\n128/192/256-bit (32/48/64 hex digit) key.  It can optionally provide a trace\nof the calculations performed, with varying degrees of detail.\n\nAEScalc was written and is Copyright 2005 by Lawrie Brown.\nPermission is granted to copy, distribute, and use this applet\nprovided the author is acknowledged and this copyright notice remains intact.\n\nSee http://www.unsw.adfa.edu.au/~lpb/ for authors website.\n");
        System.exit(1);
      }
      k++;
      m--;
    }

    if (m == 0)
    {
      System.setProperty("apple.laf.useScreenMenuBar", "true");

      Frame localFrame = new Frame("AES Block Cipher Calculator");

      Object localObject = new AEScalc();
      ((AEScalc)localObject).application = true;
      if (j > 0) ((AEScalc)localObject).traceLev = j;

      ((AEScalc)localObject).init();
      localFrame.add("Center", (Component)localObject);
      localFrame.setSize(800, 1000);
      localFrame.pack();
      localFrame.show();
      ((AEScalc)localObject).start();
    }
    else if (m < 2) {
      System.err.println("Usage:\nAEScalc [-tlevel]\n\t- run AES calculator as GUI applet (with specified trace-level)\nAEScalc [-e|-d] [-tlevel] hexkey hexdata\n\t- AES en/decrypt data using key (both in hex)\n\t- with trace details at specified level (0-5)\n");
      System.exit(1);
    }
    else
    {
      String str2 = paramArrayOfString[(k++)];
      int n = str2.length();
      if (((n != 32) && (n != 48) && (n != 64)) || (!Util.isHex(str2)))
      {
        System.err.println("AES key [" + str2 + "] must be 32 or 48 or 64 hex digits long." + "Usage:\nAEScalc [-tlevel]\n\t- run AES calculator as GUI applet (with specified trace-level)\nAEScalc [-e|-d] [-tlevel] hexkey hexdata\n\t- AES en/decrypt data using key (both in hex)\n\t- with trace details at specified level (0-5)\n");

        System.exit(1);
      }
      byte[] arrayOfByte2 = Util.hex2byte(str2);

      String str1 = paramArrayOfString[(k++)];
      if ((str1.length() != 32) || (!Util.isHex(str1)))
      {
        System.err.println("AES data [" + str1 + "] must be strictly " + 32 + " hex digits long.\n" + "Usage:\nAEScalc [-tlevel]\n\t- run AES calculator as GUI applet (with specified trace-level)\nAEScalc [-e|-d] [-tlevel] hexkey hexdata\n\t- AES en/decrypt data using key (both in hex)\n\t- with trace details at specified level (0-5)\n");

        System.exit(1);
      }
      byte[] arrayOfByte1 = Util.hex2byte(str1);

      Object localObject = new AES();

      if (j > 0) ((AES)localObject).traceLevel = j;

      if (j > 4) AES.trace_static();

      ((AES)localObject).setKey(arrayOfByte2);
      if (j > 0) System.out.print(((AES)localObject).traceInfo);

      if (i != 0) {
        byte[] arrayOfByte3 = ((AES)localObject).encrypt(arrayOfByte1);
        if (j > 0) System.out.print(((AES)localObject).traceInfo);
        System.out.println(Util.toHEX1(arrayOfByte3));
      } else {
        byte[] arrayOfByte3 = ((AES)localObject).decrypt(arrayOfByte1);
        if (j > 0) System.out.print(((AES)localObject).traceInfo);
        System.out.println(Util.toHEX1(arrayOfByte3));
      }
    }
  }
}