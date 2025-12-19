package cli;

import parser.BinaryParser;

public class Main {

    public static void main(String[] args) {

        if (args.length < 4) {
            printUsage();
            return;
        }

        String command = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
        String key = args[3];

        try {
            switch (command.toLowerCase()) {
                case "encode":
                    BinaryParser.encode(inputFile, outputFile, key);
                    System.out.println("✔ File encoded successfully.");
                    break;

                case "decode":
                    BinaryParser.decode(inputFile, outputFile, key);
                    System.out.println("✔ File decoded successfully.");
                    break;

                default:
                    System.err.println("❌ Unknown command: " + command);
                    printUsage();
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  encode <inputFile> <outputFile> <key>");
        System.out.println("  decode <inputFile> <outputFile> <key>");
        System.out.println();
        System.out.println("Example:");
        System.out.println("  java cli.Main encode input.txt output.encx myKey");
    }
}
