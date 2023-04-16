package src.ru.cs.vsu.voronetskiy_k_v.Task04;

import src.ru.cs.vsu.voronetskiy_k_v.Task04.utils.ArrayUtils;

public class ConsoleApp {
    public static void printHelp(String name) {
        System.out.printf("Usage: %s [options] [parameters]%n", name);
        System.out.printf("or: %s [options] [parameters] input_file_name%n", name);
        System.out.printf("or: %s [options] input_file_name output_file_name%n", name);
        System.out.println("Options:");
        System.out.println("-w or --window : run in window mode");
        System.out.println("-h or --help : print this help and exit");
        System.out.println("Parameters:");
        System.out.println("-i input_file_name or --input-file=input_file_name");
        System.out.println("-o output_file_name or --output-file=output_file_name");
        System.out.println("-f=index_from  or --from=index_from");
        System.out.println("-t=index_to  or --to=index_to");
        System.out.println();
        System.out.println("input_file_name is a file with input array, output_file_name is a file with result array");
    }

    public static void main(String[] args) {
        InputArgs ia = new InputArgs(args);
        if ((ia.inFile == null || ia.outFile == null || ia.inFile.length() < 1 || ia.outFile.length() < 1) && !ia.isWindow && !ia.isHelp)  {
            System.err.println("Insufficient arguments.");
            ia.isHelp = true;
        }
        if (ia.isHelp) {
            printHelp(args[0]);
            return;
        }
        if (ia.isWindow) {
            if (WindowInterface.run(ia.inFile, ia.outFile) != 0) {
                System.err.println("Unexpected error");
            }
        } else {
            int[] result = RunSolution.runSolution(ia.inFile, ia.outFile, ia.from, ia.to);
            System.out.println(ArrayUtils.toString(result));
            if (result.length < 1) {
                System.err.println("File IO error.");
            }
        }
    }
}
