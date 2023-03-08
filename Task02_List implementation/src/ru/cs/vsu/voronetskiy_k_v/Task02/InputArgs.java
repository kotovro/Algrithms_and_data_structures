package ru.cs.vsu.voronetskiy_k_v.Task02;


public class InputArgs {
    public String inFile = null;
    public String outFile = null;

    public boolean isWindow = false;
    public boolean isHelp = false;
    public InputArgs(String[] args) {
        boolean isInFile = false;
        boolean isOutFile = false;
        for (String param : args) {
            if (!isOutFile && !isInFile && param.indexOf('-') != 0) {
                if (inFile == null) {
                    inFile = param;
                } else if (outFile == null) {
                    outFile = param;
                }
            }
            if (isInFile) {
                if (param.indexOf('-') != 0) {
                    inFile = param;
                }
                isInFile = false;
            }
            if (isOutFile) {
                if (param.indexOf('-') != 0) {
                    outFile = param;
                }
                isOutFile = false;
            }
            if (param.indexOf("--input-file") == 0) {
                inFile = param.split("=")[1];
            } else if (param.indexOf("--output-file") == 0) {
                outFile = param.split("=")[1];
            } else if (param.equals("-i")) {
                isInFile = true;
            } else if (param.equals("-o")) {
                isOutFile = true;
            } else if (param.equals("--window") || param.equals("-w")) {
                isWindow = true;
            } else if (param.equals("--help") || param.equals("-h")) {
                isHelp = true;
            }
        }
        //inFile = "test_input02.txt";
        //isWindow = true;
        //outFile = "out.txt";
    }
}


