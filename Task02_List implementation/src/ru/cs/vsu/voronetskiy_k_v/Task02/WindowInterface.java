package ru.cs.vsu.voronetskiy_k_v.Task02;

import java.util.Locale;




public class WindowInterface {
    public static int run(String inFile, String outFile) {
        Locale.setDefault(Locale.ROOT);
        try {
            FrameForTask02 form = new FrameForTask02(inFile, outFile);
            java.awt.EventQueue.invokeLater(() -> form.setVisible(true));
        } catch (Exception e){
            return -1;
        }
        return 0;
    }
}