import algorithms.Algorithm;
import algorithms.AlgorithmHolder;
import algorithms.ECB;
import analysis.Analysis;
import ciphers.Magma;
import utils.Conversions;
import windows.MainWindow;
import ciphers.CipherHolder;
import ciphers.Kuznyechik;

import javax.swing.*;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Locale;

public class App {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("ru", "RU"));
        CipherHolder[] ciphers = new CipherHolder[] {new CipherHolder(Kuznyechik.class), new CipherHolder(Magma.class)};
        MainWindow window = new MainWindow("Литорея", ciphers);
        SwingUtilities.invokeLater(() -> window.display(800, 600));

//        String message = "The Java platform strongly emphasizes security, including language safety, cryptography, public key infrastructure, authentication, secure communication, and access control. The JCA is a major piece of the platform, and contains a \"provider\" architecture and a set of APIs for digital signatures, message digests (hashes), certificates and certificate validation, encryption (symmetric/asymmetric block/stream ciphers), key generation and management, and secure random number generation, to name a few.";
//        String fullKey = "88 99 AA BB CC DD EE FF 00 11 22 33 44 55 66 77 FE DC BA 98 76 54 32 10 01 23 45 67 89 AB CD EF";
//
//
//        String oneStarKey = "88 99 AA BB CC DD EE FF 00 11 * 33 44 55 66 77 FE DC BA 98 76 54 32 10 01 23 45 67 89 AB CD EF";
//        AlgorithmHolder holder = new AlgorithmHolder(ECB.class, Kuznyechik.class);
//        Algorithm algorithmFull = holder.instantiate(Conversions.hex(fullKey));
//        byte[] encryptedMessage = algorithmFull.encryptString(message);
//
//        Analysis analysis = new Analysis(holder, oneStarKey);
//        byte[] recognizedKey = analysis.analyze(encryptedMessage);
//
//        Algorithm algorithmRecognized = holder.instantiate(recognizedKey);
//        String recognizedMessage = algorithmRecognized.decryptString(encryptedMessage);
//        System.out.println(message);
//        System.out.println(recognizedMessage);
    }
}
