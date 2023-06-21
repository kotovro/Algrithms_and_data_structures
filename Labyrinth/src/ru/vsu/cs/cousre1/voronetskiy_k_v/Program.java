package ru.vsu.cs.cousre1.voronetskiy_k_v;

import javax.swing.*;
import java.util.Locale;

/**
 * Класс с методом main
 */
public class Program {

    /**
     * Основная функция программы
     */
    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.ROOT);
        ru.vsu.cs.cousre1.voronetskiy_k_v.utils.SwingUtils.setLookAndFeelByName("Windows");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ru.vsu.cs.cousre1.voronetskiy_k_v.utils.SwingUtils.setDefaultFont("Microsoft Sans Serif", 18);

        java.awt.EventQueue.invokeLater(() -> new MainForm().setVisible(true));
    }
}
