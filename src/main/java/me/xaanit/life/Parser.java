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

    private static final String VARIABLE = "var\\s+(.+)\\s+=\\s+(.+)";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile(VARIABLE);


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
                } else {
                    throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! You can call methods! Var: " + typeS, lineNumber);
                }
            } else {
                throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! You can call methods! Var: " + typeS, lineNumber);
            }

            UserVariable temp = new UserVariable(type, type == ParameterType.STRING ? typeS.replace("\"", "") : typeS, name);
            variables.put(name, temp);
        } else if (line.startsWith("if")) {//if
        } else if (line.startsWith("for")) {
            int startFrom = -1;
            int goTo = -1;
            try {
                String temp = line.substring(0, line.indexOf(")") == -1 ? -1 : line.indexOf(")") + 1);
                if (!temp.matches(FOR)) throw new ParseException("For loop not correct! Line: " + temp, lineNumber);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                //   throw new ParseException("For loop failed! " + ex.getMessage());
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
                // throw new ParseException("Missing an end )!");
            }
        } else if (index != -1) {
            String method = line.substring(0, index);
            Method m1 = null;
            for (Class clazz : classes) {
                for (Method me : clazz.getMethods()) {
                    if (me.getName().equals(method) && me.isAnnotationPresent(LifeExecutable.class)) {
                        m1 = me;
                    }
                }
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
                List<Object> list = toObjectList(getVariables(method, line, lineNumber));
                final Object[] arr = list.toArray(new Object[list.size()]);
                return m1.invoke(null, arr);
            } else {
                throw new ParseException("Method not valid! Line: " + line, lineNumber);
            }
        } else {
            throw new ParseException("Line not valid! Line: " + line, lineNumber);
        }
        System.out.println("Idk how you got here.");
        return null; // I have no idea if it can get here. I'll be surprised if it does.
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
            if (!line.isEmpty())
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
     * Gets the variables from a line.
     *
     * @param name The name of the method.
     * @param line The line to look at
     * @return The list of variables found.
     */
    private List<Variable> getVariables(String name, String line, int lineNumber) {
        List<Variable> list = new ArrayList<>();
        if (!line.matches(EMPTY_METHOD)) {
            line = line.substring(name.length()).replace("(", "").replace(")", "");
            String[] variables = line.split(",");
            for (String var : variables) {
                var = var.trim();
                var = trimLeadingSpaces(var);
                if (var.matches(STRING)) {
                    list.add(new Variable(ParameterType.STRING, var.replace("\"", "")));
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
                } else {
                    throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! Var: " + var, lineNumber);
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

}
