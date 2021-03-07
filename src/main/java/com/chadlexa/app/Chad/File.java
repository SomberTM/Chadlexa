package com.chadlexa.app.Chad;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class File {
    
    static String keyValueSeperator = "=";
    static String extension = "chad";

    public java.io.File source;
    public Map<String, String> content = new HashMap<String, String>();

    public File(java.io.File file) {
        this.source = file;
    }

    public File(String fileName) {
        this.source = new java.io.File(fileName);
    }

    public Map<String, String> parse() throws UnsupportedFileExtensionException, FileNotFoundException { return File.parse(this.source); }
    public void load() { try { this.content = this.parse(); } catch(Exception e) {} }
    public String get(String key) {
        this.load();
        return this.content.get(key);
    }
    public void set(String key, String value) { File.set(this.source, key, value); }


    @SuppressWarnings("serial")
    static class UnsupportedFileExtensionException extends Exception {
        public UnsupportedFileExtensionException(String extension) {
            super("Extension \"" + extension + "\" is not supported for parsing");
        }
    }

    public static void readLines(java.io.File file, BiConsumer<String, Integer> lineConsumer) throws FileNotFoundException {
        Scanner reader = new Scanner(file);
        int ln = 0;
        while (reader.hasNextLine())
            lineConsumer.accept(reader.nextLine(), ln++);
        reader.close();
    }

    public static StringBuffer contentsOf(java.io.File file) throws FileNotFoundException {
        StringBuffer buffer = new StringBuffer();
        readLines(file, line -> buffer.append(line + System.lineSeparator()));
        return buffer;
    }

    public static void readLines(java.io.File file, Consumer<String> lineConsumer) throws FileNotFoundException {
        readLines(file, (line, ln) -> lineConsumer.accept(line));
    }

    public static Map<String, String> parse(java.io.File file) throws UnsupportedFileExtensionException, FileNotFoundException {
        String[] name = file.getName().split("[.]");

        String ext;
        if (!(ext = name[name.length-1]).equals(extension))
            throw new UnsupportedFileExtensionException(ext);

        HashMap<String, String> parsed = new HashMap<>();

        readLines(file, line -> {
            String[] keyv = line.split(keyValueSeperator);
            if (keyv.length == 2)
                parsed.put(keyv[0], keyv[1]);   
        });

        return parsed;
    }

    @SuppressWarnings("serial")
    private static Map<String, String> cleaners = new HashMap<String, String>() {{
        put("\\(", "/lp");
        put("\\)", "/rp");
        put("\\[", "/lb");
        put("\\]", "/rb");
    }};

    private static String removeBackslash(String in) {
        return in.replaceAll("\\\\", "");
    }

    private static String clean(String in) {
        String curr = in;
        for (Map.Entry<String, String> entry : cleaners.entrySet())
            curr = curr.replaceAll(entry.getKey(), entry.getValue());
        return curr;
    }

    private static String undoClean(String in) {
        String curr = in;
        for (Map.Entry<String, String> entry : cleaners.entrySet())
            curr = curr.replaceAll(entry.getValue(), removeBackslash(entry.getKey()));
        return curr;
    }

    public static void set(java.io.File file, String key, String value) {
        Map<String, String> existing = new HashMap<>();

        try {
            existing = parse(file);

            StringBuffer buffer = contentsOf(file);
            String contents = clean(buffer.toString());

            String format = String.format("%s=%s", key, value);
            String existsFormat = clean(String.format("%s=%s", key, existing.get(key)));

            if (!existsFormat.equals(format)) {
                if (existing.size() > 0 && existing.containsKey(key))
                    contents = contents.replaceAll(existsFormat, format);
                else
                    contents += format + System.lineSeparator();

                FileWriter writer = new FileWriter(file, false);
                writer.write(undoClean(contents));
                writer.close();
            }
        } catch(Exception e) { e.printStackTrace(); System.exit(0); }
    }

}

// StringBuffer cBuffer = new StringBuffer();

            // readLines(file, line -> cBuffer.append(line + System.lineSeparator()));

            // String contents = cBuffer.toString();
            // String format = String.format("%s=%s", key, value);

            // if (existing.size() > 0 && existing.containsKey(key)) {
            //     writer.write(contents.replaceAll(String.format("%s=%s", key, existing.get(key)), format));
            // } else
            //     writer.write(format + System.lineSeparator());

            // writer.append(contents);
            // writer.flush();