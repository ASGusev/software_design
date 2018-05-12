package ru.spbau.sd.cli.interpreter.commands;

import org.apache.commons.cli.*;
import ru.spbau.sd.cli.interpreter.ExecutionResult;
import ru.spbau.sd.cli.interpreter.io.InputStream;
import ru.spbau.sd.cli.interpreter.io.OutputStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Searches for a given pattern in one or more files and prints lines containing
 * matches. If no files are provided, the input stream is used.
 *
 * Arguments: the first argument is the pattern to search. It may be a plain string
 * of a regular expression. Other arguments are paths to files to search in.
 *
 * Supported options:
 *
 * -i - ignore case
 *
 * -w - search for complete words
 *
 * -A n - show n lines after each matched line
 */
public class CmdGrep implements Command {
    private static final String KEY_IGNORE_CASE = "i";
    private static final String KEY_WORDS = "w";
    private static final String KEY_AGGREGATE = "A";
    private static final String MSG_MISSING_PATTERN = "Pattern is missing.";
    private static final String MSG_INCORRECT_A_VALUE = "Incorrect A value.";

    private void grepStream(Stream<String> lines, Pattern pattern,
                            boolean ignoreCase, int aggregateLines,
                            OutputStream outputStream) {
        int[] aggregateRest = { 0 };
        lines.forEach((line) ->
                {
                    String testLine = ignoreCase ? line.toLowerCase() : line;
                    Matcher matcher = pattern.matcher(testLine);
                    if (matcher.find()) {
                        outputStream.write(line + '\n');
                        aggregateRest[0] = aggregateLines;
                    } else if (aggregateRest[0] > 0) {
                        outputStream.write(line + '\n');
                        aggregateRest[0]--;
                    }
                }
        );
    }

    private void grepFile(String filename, Pattern pattern, boolean ignoreCase,
                          int aggregateLines, OutputStream outputStream) {
        try {
            Path filepath = Paths.get(filename);
            Stream<String> lines = Files.lines(filepath);
            grepStream(lines, pattern, ignoreCase, aggregateLines, outputStream);
        } catch (IOException e) {
            outputStream.write(e.getMessage());
        }
    }

    private void grepText(String text, Pattern pattern, boolean ignoreCase,
                          int aggregateLines, OutputStream outputStream) {
        Stream<String> lines = Arrays.stream(text.split("\n"));
        grepStream(lines, pattern, ignoreCase, aggregateLines, outputStream);
    }

    @Override
    public ExecutionResult run(List<String> arguments, InputStream inputStream, OutputStream outputStream) {
        Options options = new Options();
        Option opt_ignore_case = Option.builder(KEY_IGNORE_CASE).build();
        Option opt_words = Option.builder(KEY_WORDS).build();
        Option opt_aggregate = Option.builder(KEY_AGGREGATE).hasArg().build();
        options.addOption(opt_ignore_case);
        options.addOption(opt_words);
        options.addOption(opt_aggregate);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        boolean ignoreCase;
        boolean words;
        int aggregateLines = -1;
        String searchPattern;
        List<String> filenames;
        try {
            commandLine = parser.parse(options, arguments.toArray(new String[0]));
            ignoreCase = commandLine.hasOption(KEY_IGNORE_CASE);
            words = commandLine.hasOption(KEY_WORDS);

            if (commandLine.hasOption(KEY_AGGREGATE)) {
                aggregateLines = Integer.parseInt(
                        commandLine.getOptionValue(KEY_AGGREGATE));
            }

            List<String> args = commandLine.getArgList();
            if (args.isEmpty()) {
                outputStream.write(MSG_MISSING_PATTERN);
                return ExecutionResult.Error;
            } else {
                searchPattern = args.get(0);
                filenames = args.subList(1, args.size());
            }
        } catch (ParseException e) {
            outputStream.write(e.getMessage());
            return ExecutionResult.Error;
        } catch (NumberFormatException e) {
            outputStream.write(MSG_INCORRECT_A_VALUE);
            return ExecutionResult.Error;
        }

        if (words)  {
            searchPattern = "\\b" + searchPattern + "\\b";
        }
        if (ignoreCase) {
            searchPattern = searchPattern.toLowerCase();
        }
        Pattern pattern = Pattern.compile(searchPattern);

        if (filenames.isEmpty()) {
            grepText(inputStream.read(), pattern, ignoreCase, aggregateLines,
                    outputStream);
        } else {
            for (String filename: filenames) {
                grepFile(filename, pattern, ignoreCase, aggregateLines, outputStream);
            }
        }
        return ExecutionResult.OK;
    }
}
