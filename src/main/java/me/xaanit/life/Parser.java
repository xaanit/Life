package me.xaanit.life;

import me.xaanit.life.annotations.LifeExecutable;
import me.xaanit.life.exceptions.IncompatibleFileExtension;
import me.xaanit.life.exceptions.LifeException;
import me.xaanit.life.exceptions.ParseException;
import me.xaanit.life.stdlib.ParserAddition;
import me.xaanit.life.stdlib.ParserEquality;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class Parser {

    private Set<Class> classes = new HashSet<>();
    private static final Logger logger = Logger.getLogger("Life");
    private Map<String, UserVariable> variables = new HashMap<>();

    private static final String EMPTY_METHOD = ".+\\(\\)";
    private static final String STRING = "\".+\"";
    private static final String CHAR = "'.'";
    private static final String BOOLEAN = "false|true|False|True";
    private static final String INT = "-?[0-9]+";
    private static final String DOUBLE = "-?[0-9]+\\.[0-9]+";
    private static final String FLOAT = "-?[0-9]+\\.?[0-9]?[fF]";
    private static final String LONG = "-?[0-9]+[Ll]";

    private static final String FOR = "for\\([0-9]+,\\s+[0-9]+\\)";

    private static final String VARIABLE = "var\\s+([a-zA-Z]+)\\s+=\\s+(.+)";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile(VARIABLE);

    private static final String EDITING_VARIABLE = "(%s)\\s+=\\s+(.+)";

    private static final String IF_OR_WHILE_METHOD = "%s\\(!?((.+?)\\((.*)\\))\\)";
    private static final String IF_OR_WHILE_VARIABLE = "%s\\(!?(.+)\\)";

    private int maxWhileRepetitions = -1;
    private int maxForRepetitions = -1;

    private static ExecutorService parserService = Executors.newFixedThreadPool(
            ForkJoinPool.getCommonPoolParallelism(), r -> {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("LifeParser");
                return thread;
            });


    public Parser() {
        this(true, null);
    }

    public Parser(boolean registerStandardLibrary) {
        this(registerStandardLibrary, null);
    }

    public Parser(boolean registerStandardLibrary, Class[] notToRegister) {
        if (registerStandardLibrary) registerStandardLib(notToRegister);

    }


    private final void registerStandardLib(Class[] clazz) {
        final Class[] classes = {ParserEquality.class, ParserAddition.class};
        for (Class c : classes) {
            boolean register = true;
            if (clazz != null)
                register = !equalsAny(c, clazz);
            if (register)
                register(c);
        }
        logger.log(Level.INFO, "[Life] Standard lib init'd.");

    }

    /**
     * Registers a class for methods to run through Life.
     *
     * @param clazz An instance of the class (i.e <code>Parser.class</code>
     * @return Itself
     */
    public Parser register(Class clazz) {
        if (clazz == null)
            throw new LifeException("Must enter a valid class!");
        this.classes.add(clazz);
        return this;
    }

    /**
     * Registers a number of classes for methods to run through Life.
     *
     * @param clazz A list of the classes you want to register (i.e <code>Parser.class, Object.class</code>
     * @return Itself
     */
    public Parser register(Class... clazz) {
        this.classes.addAll(Arrays.asList(clazz));
        return this;
    }

    /**
     * Executes the given code.
     *
     * @param line The line to execute
     * @param args The args.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object execute(String line, String[] args, int lineNumber) throws InvocationTargetException, IllegalAccessException {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                line = line.replace("args[" + i + "]", args[i]);
            }
        }

        line = line.replaceAll("\\/\\/.+", "").replaceAll("\\/\\/", "").replaceAll("\\/\\*.+\\*\\/", ""); // Comments

        for (UserVariable v : variables.values()) {
            String temp;
            if (v.getType() == ParameterType.STRING)
                temp = "\"" + v.getInfo() + "\"";
            else
                temp = v.getInfo();
            line = line.replace("{" + v.getName() + "}", temp);
        }
        int index = line.indexOf('(');
        if (line.startsWith("var")) {
            if (!line.matches(VARIABLE))
                throw new ParseException("Variable needs to be formatted as: var name = info\nYou formatted it as: " + line, lineNumber);
            Matcher m = VARIABLE_PATTERN.matcher(line);
            m.find();
            ParameterType type;
            String name = m.group(1);
            if (variables.containsKey(name))
                throw new ParseException("Variable " + name + " already exists!", lineNumber);
            String typeS = m.group(2);
            UserVariable temp = getUserVariable(typeS, lineNumber, name, args);
            variables.put(name, temp);
        } else if (isEditingVariable(line, lineNumber)) {
            String name = trimLeadingSpaces(line);
            name = name.substring(0, name.indexOf(" "));
            String toEditRegex = String.format(EDITING_VARIABLE, name);
            Pattern p = Pattern.compile(toEditRegex);
            Matcher m = p.matcher(line);
            m.find();
            name = m.group(1);
            String typeS = m.group(2);
            UserVariable temp = getUserVariable(typeS, lineNumber, name, args);
            variables.put(name, temp);
        } else if (line.startsWith("if")) {//if
            int indexOfIf = line.indexOf("<");
            if (indexOfIf == -1) throw new ParseException("If statement invalid!", lineNumber);
            String find = trimLeadingSpaces(line.substring(0, indexOfIf).trim());
            boolean cont;
            boolean shouldBeFalse = find.charAt(3) == '!';
            if (find.matches(String.format(IF_OR_WHILE_METHOD, "if"))) {
                Pattern p = Pattern.compile(String.format(IF_OR_WHILE_METHOD, "if"));
                Matcher m = p.matcher(find);
                m.find();
                Object o = execute(m.group(1), args, lineNumber);
                if (!(o instanceof Boolean))
                    throw new ParseException("Methods in if statements can only be booleans!", lineNumber);
                boolean b = (boolean) o;
                cont = shouldBeFalse ? !b : b;
            } else if (find.matches(String.format(IF_OR_WHILE_VARIABLE, "if"))) {
                Pattern p = Pattern.compile(String.format(IF_OR_WHILE_VARIABLE, "if"));
                Matcher m = p.matcher(find);
                m.find();
                String var = m.group(1);
                boolean b = var.matches(BOOLEAN) && var.equalsIgnoreCase("true");
                cont = shouldBeFalse ? !b : b;
            } else {
                throw new ParseException("Invalid token in if statement!", lineNumber);
            }

            if (cont) {
                int foundStarts = 0;
                int foundEnds = 0;
                int firstFoundIndex = -2;
                int lastFoundIndex = -2;
                int i = 0;
                while (true) {
                    try {
                        char a = i == 0 ? ' ' : line.charAt(i - 1);
                        char b = line.charAt(i);
                        char c = i == line.length() - 1 ? ' ' : line.charAt(i + 1);

                        if (b == '<' && (a != '<') && (c != '<')) {
                            foundStarts++;
                            if (firstFoundIndex == -2)
                                firstFoundIndex = i;
                        } else if (b == '>' && (a != '>') && (c != '>')) {
                            foundEnds++;
                            lastFoundIndex = i;
                        }
                        i++;
                    } catch (IndexOutOfBoundsException ex) {
                        break;
                    }
                }

                if (foundStarts != foundEnds)
                    throw new ParseException("Found a mismatched amount of <'s and >'s! Found " + foundStarts + " <'s and " + foundEnds + " >'s. Line: " + line, lineNumber);

                String exe = trimLeadingSpaces(line.substring(firstFoundIndex + 1, lastFoundIndex).trim());
                execute(exe, args, lineNumber);
            } else {
                return new FailedIf();
            }
        } else if (line.startsWith("while")) { // while
            int indexOfWhile = line.indexOf("<<<");
            if (indexOfWhile == -1) throw new ParseException("While loop invalid!", lineNumber);
            String find = trimLeadingSpaces(line.substring(0, indexOfWhile).trim());
            boolean cont;
            boolean shouldBeFalse = find.charAt(3) == '!';
            String ifStatement;
            if (find.matches(String.format(IF_OR_WHILE_METHOD, "while"))) {
                Pattern p = Pattern.compile(String.format(IF_OR_WHILE_METHOD, "while"));
                Matcher m = p.matcher(find);
                m.find();
                String temp = m.group(1);
                Object o = execute(temp, args, lineNumber);
                if (!(o instanceof Boolean))
                    throw new ParseException("Methods in if statements can only be booleans!", lineNumber);
                boolean b = (boolean) o;
                cont = shouldBeFalse ? !b : b;
                ifStatement = temp;
            } else if (find.matches(String.format(IF_OR_WHILE_VARIABLE, "while"))) {
                Pattern p = Pattern.compile(String.format(IF_OR_WHILE_VARIABLE, "while"));
                Matcher m = p.matcher(find);
                m.find();
                String var = m.group(1);
                boolean b = var.matches(BOOLEAN) && var.equalsIgnoreCase("true");
                cont = shouldBeFalse ? !b : b;
                ifStatement = var;
            } else {
                throw new ParseException("Invalid token in while loop!", lineNumber);
            }

            if (cont) {
                int[] arr = getBracketInfo(line, "<<<", ">>>");
                int foundStarts = arr[0];
                int foundEnds = arr[1];
                int firstFoundIndex = arr[2];
                int lastFoundIndex = arr[3];
                if (foundStarts != foundEnds)
                    throw new ParseException("Found a mismatched amount of <<<'s and >>>'s! Found " + foundStarts + " <<<'s and " + foundEnds + " >>>'s.", lineNumber);

                String exe = trimLeadingSpaces(line.substring(firstFoundIndex + 3, lastFoundIndex).trim());
                exe = "if(" + (shouldBeFalse ? "!" : "") + ifStatement + ") < " + exe + " > ";
                int i = 0;
                if (maxWhileRepetitions != 0) {
                    while (!(execute(exe, args, lineNumber) instanceof FailedIf)) {
                        if (i == maxWhileRepetitions)
                            break;
                        i++;
                    }
                }
            }
        } else if (line.startsWith("for")) {
            int startFrom;
            int goTo;
            try {
                String temp = line.substring(0, !line.contains(")") ? -1 : line.indexOf(")") + 1);
                if (!temp.matches(FOR)) throw new ParseException("For loop not correct! Line: " + temp, lineNumber);
                String[] split = temp.replaceAll("[for\\(\\)\\s]", "").split(",");
                startFrom = Integer.parseInt(split[0]);
                goTo = Integer.parseInt(split[1]);
            } catch (NumberFormatException ex) {
                throw new ParseException("For loop failed! " + ex.getMessage(), lineNumber);
            } catch (IndexOutOfBoundsException ex) {
                throw new ParseException("Missing an end )!", lineNumber);
            }

            if (startFrom < goTo && maxForRepetitions != 0) {
                if (goTo - startFrom > maxForRepetitions && maxForRepetitions != -1)
                    throw new ParseException("The max for repetitions for this is " + maxForRepetitions + ", yours would run " + (goTo - startFrom) + " times.", lineNumber);
                int foundStarts = 0;
                int foundEnds = 0;
                int firstFoundIndex = line.indexOf("<<");
                if (firstFoundIndex == -1) throw new ParseException("Could not find a starting <<!", lineNumber);
                int i = 0;
                while (true) {
                    try {
                        String temp = line.substring(i, i + 2);
                        if (temp.equals("<<"))
                            foundStarts++;
                        else if (temp.equals(">>"))
                            foundEnds++;
                        i++;
                    } catch (IndexOutOfBoundsException ex) {
                        break;
                    }
                }
                int lastFoundIndex = line.lastIndexOf(">>");
                if (foundStarts != foundEnds)
                    throw new ParseException("Found a mismatched amount of <<'s and >>'s! Found " + foundStarts + " <<'s and " + foundEnds + " >>'s.", lineNumber);
                String exe = trimLeadingSpaces(line.substring(firstFoundIndex + 2, lastFoundIndex).trim());
                for (int goFrom = startFrom; goFrom < goTo; goFrom++)
                    execute(exe, args, lineNumber);
            }
        } else if (index != -1) {
            String method = line.substring(0, index);
            Method m1 = null;
            List<Variable> variables = getVariables(method, line, args, lineNumber);
            for (Class clazz : classes) {
                for (Method me : clazz.getMethods()) {
                    if (me.getName().equals(method) && me.isAnnotationPresent(LifeExecutable.class)) {
                        /*          List<String> foundUserVars = new ArrayList<>();
                         List<String> foundDevVars = new ArrayList<>();

                         for (Variable v : variables) foundUserVars.add(v.getType().toString().toLowerCase());

                         for (Parameter p : me.getParameters())
                         foundDevVars.add(p.getType().getName().toLowerCase().contains("string") ? "string" : p.getType().getName().toLowerCase());

                         System.out.println(foundUserVars);
                         System.out.println(foundDevVars);

                         if (foundUserVars.size() != foundDevVars.size()) {
                         System.out.println(String.format("SIZE MISMATCH, USER %s DEV %s", foundUserVars.size(), foundDevVars.size()));
                         continue;
                         }
                         for (int i = 0; i < foundUserVars.size(); i++)
                         if (!foundUserVars.get(i).equals(foundDevVars.get(i))) {
                         System.out.println("DO NOT MATCH. FOUND " + foundUserVars.get(i) + " IN USER VARS AND " + foundDevVars.get(i) + " AT DEV VARS AT POS " + i);
                         continue;
                         }
                         System.out.println("FOUND BOTH OF THE SAME.");
                         */
                        m1 = me;
                        break;
                    }
                }
                if (m1 != null)
                    break;
            }
            if (m1 != null) {
                if (!equalsAny(m1.getReturnType(),
                        String.class,
                        double.class,
                        int.class,
                        float.class,
                        char.class,
                        boolean.class,
                        void.class))
                    throw new ParseException("Your method must return a primitive (excluding short/byte), a String, or it must be void!", lineNumber);
                List<Object> list = toObjectList(variables);
                final Object[] arr = list.toArray(new Object[list.size()]);
                return m1.invoke(null, arr);
            } else {
                throw new ParseException("Method not valid! Line: " + line, lineNumber);
            }
        } else {
            throw new ParseException("Line not valid! Line: " + line, lineNumber);
        }
        return null; // I have no idea if it can get here. I'll be surprised if it does.
    }

    private String turnWhileIntoIf(String line, String condition, boolean includeNot) {
        String res = "if(" + (includeNot ? "!" : "") + condition + ") < " + line + " > ";
        System.out.println("TURNED WHILE INTO IF: " + res);
        return res;
    }

    /**
     * Executes the given code, one line at a time.
     *
     * @param lines The lines to execute
     * @param args  The args.
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */

    private void execute(List<String> lines, String[] args) throws InvocationTargetException, IllegalAccessException, LifeException {
        int i = 0;
        for (String line : lines) {
            i++;
            if (!line.isEmpty() && !line.startsWith("//"))
                execute(line, args, i);
        }
    }


    /**
     * Makes a list of variables a list of Objects (for method calling).
     *
     * @param vars The variables.
     * @return The variables, but converted to their Objects.
     */
    private List<Object> toObjectList(List<Variable> vars) {
        List<Object> list = new ArrayList<>();
        for (Variable var : vars)
            list.add(var.convert());
        return list;
    }

    /**
     * Gets the user variable.
     *
     * @param typeS      The info to check
     * @param lineNumber The line number, for exceptions
     * @return The user variable based on the info found.
     */
    private UserVariable getUserVariable(String typeS, int lineNumber, String name, String[] args) throws IllegalAccessException, InvocationTargetException {
        ParameterType type;
        if (typeS.matches(STRING)) {
            type = ParameterType.STRING;

        } else if (typeS.matches(CHAR)) {
            type = ParameterType.CHAR;

        } else if (typeS.matches(INT)) {
            type = ParameterType.INT;

        } else if (typeS.matches(DOUBLE)) {
            type = ParameterType.DOUBLE;

        } else if (typeS.matches(BOOLEAN)) {
            type = ParameterType.BOOLEAN;

        } else if (typeS.matches(FLOAT)) {
            type = ParameterType.FLOAT;

        } else if (typeS.matches(LONG)) {
            type = ParameterType.LONG;

        } else if (typeS.indexOf("(") != -1) {
            if (typeS.indexOf(")") == -1)
                throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! You can call methods! Var: " + typeS, lineNumber);


            Object called = execute(typeS, args, lineNumber);

            if (called instanceof String) {
                type = ParameterType.STRING;
                typeS = "\"" + called.toString() + "\"";
            } else if (called instanceof Double) {
                type = ParameterType.DOUBLE;
                typeS = called.toString();
            } else if (called instanceof Float) {
                type = ParameterType.FLOAT;
                typeS = called.toString() + "F";
            } else if (called instanceof Character) {
                type = ParameterType.CHAR;
                typeS = "'" + called.toString() + "'";
            } else if (called instanceof Integer) {
                type = ParameterType.INT;
                typeS = called.toString();
            } else if (called instanceof Long) {
                type = ParameterType.LONG;
                typeS = called.toString() + "L";
            } else if (called instanceof Boolean) {
                type = ParameterType.BOOLEAN;
                typeS = called.toString().toLowerCase();
            } else if (called == null) {
                throw new ParseException("Void methods can not be used in variables!", lineNumber);
            } else {
                throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! You can call methods! Var: " + typeS, lineNumber);
            }
        } else {
            throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! You can call methods! Var: " + typeS, lineNumber);
        }
        return new UserVariable(type, type == ParameterType.STRING ? typeS.replace("\"", "") : typeS, name);
    }

    /**
     * Checks to see if the line is the start of a variable manipulation
     *
     * @param line       The line to check
     * @param lineNumber The line number, for exceptions
     * @return {@code true} if it is; otherwise {@code false}
     */
    private boolean isEditingVariable(String line, int lineNumber) {
        if (line.indexOf("=") == -1 || line.startsWith("if") || line.startsWith("for"))
            return false;
        line = trimLeadingSpaces(line);
        try {
            line = line.substring(0, line.indexOf(" "));
        } catch (IndexOutOfBoundsException ex) {
            throw new ParseException("Invalid line: " + line, lineNumber);
        }
        for (String key : variables.keySet())
            if (line.equals(key))
                return true;
        return false;
    }


    /**
     * Gets the variables from a line.
     *
     * @param name The name of the method.
     * @param line The line to look at
     * @return The list of variables found.
     */
    private List<Variable> getVariables(String name, String line, String[] args, int lineNumber) throws InvocationTargetException, IllegalAccessException {
        final String METHOD = ".+\\(.*\\)";
        List<Variable> list = new ArrayList<>();
        if (!line.matches(EMPTY_METHOD)) {
            line = line.substring(name.length()).replace("(", "").replace(")", "");
            String[] variables = line.split(",");
            for (String var : variables) {
                var = var.trim();
                var = trimLeadingSpaces(var);
                Object o = null;
                if (var.matches(METHOD)) {
                    o = execute(var, args, lineNumber);
                }
                if (var.matches(STRING)) {
                    list.add(new Variable(ParameterType.STRING, var));
                } else if (var.matches(CHAR)) {
                    list.add(new Variable(ParameterType.CHAR, var));
                } else if (var.matches(INT)) {
                    list.add(new Variable(ParameterType.INT, var));
                } else if (var.matches(DOUBLE)) {
                    list.add(new Variable(ParameterType.DOUBLE, var));
                } else if (var.matches(BOOLEAN)) {
                    list.add(new Variable(ParameterType.BOOLEAN, var));
                } else if (var.matches(FLOAT)) {
                    list.add(new Variable(ParameterType.FLOAT, var));
                } else if (var.matches(LONG)) {
                    list.add(new Variable(ParameterType.LONG, var));
                } else if (o != null && o instanceof String) {
                    list.add(new Variable(ParameterType.STRING, o.toString()));
                } else if (o != null && o instanceof Character) {
                    list.add(new Variable(ParameterType.CHAR, o.toString()));
                } else if (o != null && o instanceof Integer) {
                    list.add(new Variable(ParameterType.INT, o.toString()));
                } else if (o != null && o instanceof Double) {
                    list.add(new Variable(ParameterType.DOUBLE, o.toString()));
                } else if (o != null && o instanceof Boolean) {
                    list.add(new Variable(ParameterType.BOOLEAN, o.toString()));
                } else if (o != null && o instanceof Float) {
                    list.add(new Variable(ParameterType.FLOAT, o.toString()));
                } else if (o != null && o instanceof Long) {
                    list.add(new Variable(ParameterType.LONG, o.toString()));
                }
            }
        }
        return list;
    }

    /**
     * Similar to {@link String#trim()} but for spaces before the line.
     *
     * @param line The line to reverse-trim
     * @return The trimmed line.
     */
    private String trimLeadingSpaces(String line) {
        if (line.isEmpty()) return "";
        while (line.charAt(0) == ' ') line = line.substring(1);
        return line;
    }


    /**
     * Executes the life file with the specified args.
     *
     * @param file The file
     * @param args The args to provide to the user.
     * @throws IOException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws LifeException             If anything goes wrong.
     */
    public void execute(File file, String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
        if (!file.getName().endsWith("life"))
            throw new IncompatibleFileExtension("You must pass a .life file!");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].replace("\"", "");
            args[i] = !args[i].matches(CHAR)
                    && !args[i].matches(INT)
                    && !args[i].matches(DOUBLE)
                    && !args[i].matches(LONG)
                    && !args[i].matches(FLOAT)
                    && !args[i].matches(BOOLEAN)
                    ? "\"" + args[i] + "\""
                    : args[i];
        }
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        List<String> res = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) res.add(line);
        br.close();
        fr.close();
        this.variables.clear();
        execute(res, args);
    }


    /**
     * If a class (unknown) equals any of the following classes.
     *
     * @param clazz          The class you want to check
     * @param toCheckAgainst the classes you want to check against
     * @return {@code true} if it matches ANY; otherwise {@code false}.
     */
    private boolean equalsAny(Class clazz, Class... toCheckAgainst) {
        for (Class c : toCheckAgainst)
            if (c == clazz) return true;

        return false;
    }

    private String format(String str, String... strs) {
        int i = 0;
        while (str.contains("{}")) {
            str = str.replaceFirst("\\{\\}", strs[i]);
            i++;
        }
        return str;
    }

    public int getMaxWhileRepetitions() {
        return maxWhileRepetitions;
    }

    /**
     * Sets the max amount of times a while loop can run. -1 for infinite. 0 for never.
     *
     * @param maxWhileRepetitions The max amount of times a while loop can execute before exiting.
     */
    public void setMaxWhileRepetitions(int maxWhileRepetitions) {
        if (maxWhileRepetitions < 0)
            maxWhileRepetitions = -1;
        this.maxWhileRepetitions = maxWhileRepetitions;
    }

    public int getMaxForRepetitions() {
        return maxForRepetitions;
    }

    /**
     * Sets the max amount of times a for loop can run. -1 for infinite. 0 for never. If the number of times the for loop will run exceeds this, an error will be thrown.
     *
     * @param maxForRepetitions The max amount of times a for loop can run.
     */
    public void setMaxForRepetitions(int maxForRepetitions) {
        if (maxForRepetitions < 0)
            maxForRepetitions = -1;
        this.maxForRepetitions = maxForRepetitions;
    }


    private int[] getBracketInfo(String line, String toLookFor, String backwards) {
        int foundStarts = 0;
        int foundEnds = 0;
        int firstFoundIndex = -2;
        int lastFoundIndex = -2;
        int i = 0;
        int length = toLookFor.length();
        while (true) {
            try {
                int temp1 = foundStarts;
                int temp2 = foundEnds;
                char a = i <= length ? ' ' : line.charAt(i - length);
                String b = line.substring(i, i + length);
                char c = i == line.length() - length ? ' ' : line.charAt(i + length);

                if (b.equalsIgnoreCase(toLookFor) && (a != '<') && (c != '<')) {
                    foundStarts++;
                    if (firstFoundIndex == -2)
                        firstFoundIndex = i;
                } else if (b.equalsIgnoreCase(backwards) && (a != '>') && (c != '>')) {
                    foundEnds++;
                    lastFoundIndex = i;
                }
                i++;
            } catch (IndexOutOfBoundsException ex) {
                break;
            }
        }
        return new int[]{foundStarts, foundEnds, firstFoundIndex, lastFoundIndex};
    }


    public static void async(Runnable r) {
        parserService.execute(r);
    }


    private class FailedIf {
    }
}