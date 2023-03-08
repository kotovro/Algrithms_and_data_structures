package ru.cs.vsu.voronetskiy_k_v.Task02;

/*
public class WriteReadFromFile {
   /*
    public static String[] readStringArrayFromFile(String fName) throws IOException {

        Scanner sc;
        sc = new Scanner(new FileReader(fName));
        ArrayList<String> tmp = new ArrayList<>();
        while (sc.hasNextLine()) {
            tmp.add(sc.nextLine());
        }
        return tmp.toArray(new String[0]);
    }

    public static void writeStringToFile(String fName, String array) throws IOException {
        FileWriter fw = new FileWriter(fName);
        fw.write(array);
        fw.close();
    }


    public static int[][] readIntArrayFromFile(String fName) throws IOException {

        String[] res = readStringArrayFromFile(fName);
        int spaceCount = res[0].replaceAll("[^ ]", "").length();
        int[][] retArr = new int[res.length][spaceCount + 1];
        int i = 0;
        int j = 0;
        for (String s : res) {
            for (String el : s.split(" ")) {
                retArr[i][j] = Integer.parseInt(el);
                j++;
            }
            j = 0;
            i++;
        }
        return retArr;
    }
}


 */