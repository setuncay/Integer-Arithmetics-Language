package programmingtest.interpreter.domain;

import com.google.common.base.Objects;
import org.antlr.v4.runtime.Token;


/**
 * A simple POJO to encapsulate:
 * <var name="a" value="5"/>
 * <var name="b" />
 */
public class Variable {
    private String name;
    private Integer value;
    private final Token token;

    public Variable(String name, Integer value, Token token) {
        this.name = name;
        this.value = value;
        this.token = token;
    }

    public Variable(String name, Token token) {
        this(name, null, token);
    }

    public String getName() {
        return name;
    }

    public Token getToken() {
        return token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equal(name, variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
