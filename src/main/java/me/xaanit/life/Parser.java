package me.xaanit.life;

import me.xaanit.life.annotations.LifeExecutable;
import me.xaanit.life.exceptions.IncompatibleFileExtension;
import me.xaanit.life.exceptions.LifeException;
import me.xaanit.life.exceptions.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Parser {

    private Set<Object> instances = new HashSet<>();
    private Map<String, Object> classes = new HashMap<>();

    private static final String EMPTY_METHOD = ".+\\(\\)";
    private static final String STRING = "\".+\"";
    private static final String CHAR = "'.'";
    private static final String BOOLEAN = "false|true|False|True";
    private static final String INT = "-?[0-9]+";
    private static final String DOUBLE = "-?[0-9]+\\.[0-9]+";
    private static final String FLOAT = "-?[0-9]+\\.?[0-9]?[fF]";
    private static final String LONG = "-?[0-9]+[Ll]";


    public Parser() {
    }

    public Parser register(Object instance) {
        if (instance == null)
            throw new LifeException("Instance must be the instance of a class!");
        this.instances.add(instance);
        this.classes.put(instance.getClass().getName(), instance);
        return this;
    }

    public Parser register(Object... clazz) {
        this.instances.addAll(Arrays.asList(clazz));
        for (Object o : clazz) {
            this.classes.put(o.getClass().getName(), o);
        }
        return this;
    }

    private void execute(List<String> lines, String[] args) throws InvocationTargetException, IllegalAccessException {
        for (String line : lines) {
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    line = line.replace("args[" + i + "]", args[i]);
                }
            }
            int index = line.indexOf('(');
            if (line.startsWith("var")) {
                // do variable stuff
            } else if (line.startsWith("if")) {//if
            } else if (line.startsWith("for")) {// for
            } else if (index != -1) {
                String method = line.substring(0, index);
                Method m1 = null;
                for (Object o : instances) {
                    Class clazz = o.getClass();
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
                        throw new ParseException("Your method must return a primitive (excluding short/byte), a String, or it must be void!");
                    List<Object> list = toObjectList(getVariables(method, line));
                    final Object[] arr = list.toArray(new Object[list.size()]);
                    for (String str : classes.keySet()) {
                        System.out.printf("%s || %s", str, classes.get(str).getClass().getName());
                        System.out.println();
                        System.out.printf(m1.getClass().getName());
                        System.out.println();
                        System.out.printf(classes.containsKey(m1.getClass().getName()) + "");
                        System.out.println();
                        System.out.printf(str.equals(m1.getClass().getName()) + "");
                    }

                    //m1.invoke(Modifier.isStatic(m1.getModifiers()) ? null : classes.get(m1.getClass().getName()), arr); // For now.
                }
            }
        }
    }


    private List<Object> toObjectList(List<Variable> vars) {
        List<Object> list = new ArrayList<>();
        for (Variable var : vars)
            list.add(var.convert());
        return list;
    }


    private List<Variable> getVariables(String name, String line) {
        List<Variable> list = new ArrayList<>();
        if (!line.matches(EMPTY_METHOD)) {
            line = line.replace(name, "").replace("(", "").replace(")", "");
            String[] variables = line.split(",");
            for (String var : variables) {
                var = var.trim();
                var = trimLeadingSpaces(var);
                if (var.matches(STRING)) {
                    list.add(new Variable(ParameterType.STRING, var.replace("\"", "")));
                    continue;
                } else if (var.matches(CHAR)) {
                    list.add(new Variable(ParameterType.CHAR, var));
                    continue;
                } else if (var.matches(INT)) {
                    list.add(new Variable(ParameterType.INT, var));
                    continue;
                } else if (var.matches(DOUBLE)) {
                    list.add(new Variable(ParameterType.DOUBLE, var));
                    continue;
                } else if (var.matches(BOOLEAN)) {
                    list.add(new Variable(ParameterType.BOOLEAN, var));
                    continue;
                } else if (var.matches(FLOAT)) {
                    list.add(new Variable(ParameterType.FLOAT, var));
                    continue;
                } else if (var.matches(LONG)) {
                    list.add(new Variable(ParameterType.LONG, var));
                    continue;
                } else {
                    throw new ParseException("Variables can only be primitives (excluding byte and short) or Strings! Floats must be suffixed with f or F, longs with l or L, chars surrounded by single quotes, and Strings surrounded by double! Var: " + var);
                }
            }
        }
        return list;
    }

    private String trimLeadingSpaces(String line) {
        if (line.isEmpty()) return "";
        while (line.charAt(0) == ' ') line = line.substring(1);
        return line;
    }


    public void execute(File file, String[] args) throws IOException, InvocationTargetException, IllegalAccessException {
        if (!file.getName().endsWith("life"))
            throw new IncompatibleFileExtension("You must pass a .life file!");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        List<String> res = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) res.add(line);
        br.close();
        fr.close();
        execute(res, args);
    }


    private boolean equalsAny(Class clazz, Class... toCheckAgainst) {
        for (Class c : toCheckAgainst)
            if (c == clazz) return true;

        return false;
    }
}
