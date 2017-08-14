package me.xaanit.life;

public class UserVariable extends Variable {
    private final String name;

    protected UserVariable(ParameterType type, String info, String name) {
       super(type, info);
       this.name = name;
    }

    public String getName() {
        return name;
    }
}
