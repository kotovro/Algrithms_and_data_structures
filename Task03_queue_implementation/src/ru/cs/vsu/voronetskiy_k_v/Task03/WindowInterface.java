package ru.cs.vsu.voronetskiy_k_v.Task03;

import java.util.Locale;




public class WindowInterface {
    public static int run(String inFile, String outFile) {
        Locale.setDefault(Locale.ROOT);
        try {
            FrameForTask03 form = new FrameForTask03(inFile, outFile);
            java.awt.EventQueue.invokeLater(() -> form.setVisible(true));
        } catch (Exception e){
            return -1;
        }
        return 0;
    }
}