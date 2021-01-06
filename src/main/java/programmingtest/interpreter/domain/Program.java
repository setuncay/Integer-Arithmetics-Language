package programmingtest.interpreter.domain;

import java.util.List;

/**
 * A simple POJO representation of an Integer Arithmetics Language. Example:
 * <program>
 *   <var name="a" value="5" />
 *   <var name="b" value="6" />
 *   <var name="c" />
 *   <var name="d" />
 *   <add n1="a" n2="b" to="c" />
 *   <add n1="c" n2="3" to="d" />
 *   <print n="d" />
 * </program>
 */
public class Program {
    private final List<Variable> variables;
    private final List<AddStatement> addStatementStatements;
    private final List<PrintStatement> printStatementStatements;

    public Program(List<Variable> variables, List<AddStatement> addStatementStatements, List<PrintStatement> printStatementStatements) {
        this.variables = variables;
        this.addStatementStatements = addStatementStatements;
        this.printStatementStatements = printStatementStatements;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<AddStatement> getAddStatements() {
        return addStatementStatements;
    }

    public List<PrintStatement> getPrintStatements() {
        return printStatementStatements;
    }
}
