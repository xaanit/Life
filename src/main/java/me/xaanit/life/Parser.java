package me.xaanit.life;

import me.xaanit.life.annotations.LifeExecutable;
import me.xaanit.life.exceptions.IncompatibleFileExtension;
import me.xaanit.life.exceptions.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

public class Parser {

    private Set<Object> instances = new HashSet<>();
    private Map<Class, Object> classes = new HashMap<>();

    private static final String EMPTY_METHOD = ".+\\(\\)";
    private static final String STRING = "\".+\"";
    private static final String CHAR = "'.'";
    private static final String BOOLEAN = "false|true|Flase|True";
    private static final String INT = "-?[0-9]+";
    private static final String DOUBLE = "-?[0-9]+\\.[0-9]+";
    private static final String FLOAT = "-?[0-9]+\\.?[0-9]?[fF]";
    private static final String LONG = "-?[0-9]+[Ll]";


    public Parser() {
    }

    public Parser register(Object instance) {
        this.instances.add(instance);
        this.classes.put(instance.getClass(), instance);
        return this;
    }

    public Parser register(Object... clazz) {
        this.instances.addAll(Arrays.asList(clazz));
        for (Object o : clazz) {
            this.classes.put(o.getClass(), o);
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
            } else {
                String method = line.substring(0, index);
                Method m1 = null;
                for (Object o : instances) {
                    Class clazz = o.getClass();
                    for (Method me : clazz.getMethods()) {
                        if (me.getName().equals(method) && (me.isAnnotationPresent(LifeExecutable.class)) || me.getClass().isAnnotationPresent(LifeExecutable.class)) {
                            m1 = me;
                        }
                    }
                }
                if (m1 != null) {
                        List<Object> list = toObjectList(getVariables(method, line));
                        final Object[] arr = list.toArray(new Object[list.size()]);
                        m1.invoke(Modifier.isStatic(m1.getModifiers()) ? null : classes.get(m1.getClass()), arr);
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
        line = line.replace(name, "").replace("(", "").replace(")", "");
        if (!line.matches(EMPTY_METHOD)) {
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

    private boolean validate(Method m) {
        for (Parameter p : m.getParameters())
            if (!equalsAny(p.getClass(),
                    String.class,
                    double.class,
                    int.class,
                    float.class,
                    byte.class,
                    char.class,
                    boolean.class)) return false;
        return true;
    }

    private boolean equalsAny(Class clazz, Class... toCheckAgainst) {
        for (Class c : toCheckAgainst)
            if (c == clazz) return true;

        return false;
    }
}
