package programmingtest.interpreter.service;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.vavr.control.Either;
import lang.IntegerArithmeticParser;
import programmingtest.interpreter.domain.AddStatement;
import programmingtest.interpreter.domain.CompilationError;
import programmingtest.interpreter.domain.InterpretationError;
import programmingtest.interpreter.domain.Output;
import programmingtest.interpreter.domain.PrintStatement;
import programmingtest.interpreter.domain.Program;
import programmingtest.interpreter.domain.Variable;
import programmingtest.interpreter.service.antlr.Compiler;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class ArithmeticExecutor {

    public Either<List<CompilationError>, List<Output>> run(String programFilePath) {
        final File programFile = new File(programFilePath);
        return this.run(programFile);
    }

    public Either<List<CompilationError>, List<Output>> run(File programFile) {
        verifySourceFile(programFile);
        final String source = readFileContents(programFile);
        Compiler compiler = new Compiler();
        final String programName = programFile.getName();
        final Either<List<CompilationError>, IntegerArithmeticParser.ProgramContext> result
                = compiler.compile(source, programName);

        if (result.isRight()) {
            return interpret(result.get(), programName);
        } else {
            return Either.left(result.getLeft());
        }
    }

    public Either<List<CompilationError>, List<Output>> interpret(IntegerArithmeticParser.ProgramContext programContext, String programName) {
        List<IntegerArithmeticParser.StatementContext> statements = programContext.statement();
        if (statements == null) {
            return Either.right(Collections.emptyList());
        }

        final List<Variable> variableStatements = statements
                .stream()
                .filter(statementContext -> statementContext.varStatement() != null)
                .map(IntegerArithmeticParser.StatementContext::varStatement)
                .map(this::toVar)
                .collect(Collectors.toList());

        final List<CompilationError> variableErrors = validateVarStatements(variableStatements, programName);
        if (!variableErrors.isEmpty()) {
            return Either.left(variableErrors);
        }

        final List<AddStatement> addStatementStatements = statements
                .stream()
                .filter(statementContext -> statementContext.addStatement() != null)
                .map(IntegerArithmeticParser.StatementContext::addStatement)
                .map(addStatementContext -> this.toAdd(addStatementContext, variableStatements))
                .collect(Collectors.toList());

        final List<CompilationError> addErrors = validateAddStatements(addStatementStatements, programName);
        if (!addErrors.isEmpty()) {
            return Either.left(addErrors);
        }

        final List<PrintStatement> printStatementStatements = statements
                .stream()
                .filter(statementContext -> statementContext.printStatement() != null)
                .map(IntegerArithmeticParser.StatementContext::printStatement)
                .map(printStatementContext -> this.toPrint(printStatementContext, variableStatements))
                .collect(Collectors.toList());

        final List<CompilationError> printErrors = validatePrintStatements(printStatementStatements, programName);
        if (!printErrors.isEmpty()) {
            return Either.left(printErrors);
        }

        return Either.right(
                ProgramEvaluator
                        .evaluate(new Program(variableStatements, addStatementStatements, printStatementStatements))
        );
    }


    private List<CompilationError> validatePrintStatements(List<PrintStatement> printStatementStatements, String programName) {
        final List<CompilationError> errors = Lists.newArrayList();
        for (PrintStatement printStatement : printStatementStatements) {
            if (printStatement.getVariable() == null) {
                errors.add(InterpretationError.fromToken(printStatement.getToken(), "to references an undefined variable", programName));
            }
        }
        return errors;
    }

    private List<CompilationError> validateAddStatements(List<AddStatement> addStatementStatements, String programName) {
        final List<CompilationError> errors = Lists.newArrayList();
        addStatementStatements.forEach(addStatement -> {
            addStatement.getOperand1().forEach(var -> {
                if (var == null) {
                    errors.add(InterpretationError.fromToken(addStatement.getToken(), "n1 references an undefined variable", programName));
                }
            });

            addStatement.getOperand2().forEach(var -> {
                if (var == null) {
                    errors.add(InterpretationError.fromToken(addStatement.getToken(), "n2 references an undefined variable", programName));
                }
            });

            if (addStatement.getResult() == null) {
                errors.add(InterpretationError.fromToken(addStatement.getToken(), "to references an undefined variable", programName));
            }

        });
        return errors;
    }


    private List<CompilationError> validateVarStatements(List<Variable> variableStatements, String programName) {
        final Set<Variable> duplicateVariables = findDuplicates(variableStatements, Variable::getName);
        return duplicateVariables.stream().map(var ->
                InterpretationError.fromToken(
                        var.getToken(),
                        "Duplicate Variable Definition " + var.getName(),
                        programName))
                .collect(Collectors.toList());

    }

    private Variable toVar(IntegerArithmeticParser.VarStatementContext varStatementContext) {
        final String variableName = varStatementContext.nameAttribute().ID().getText();
        if (varStatementContext.valueAttribute() != null) {
            final int value = Integer.parseInt(varStatementContext.valueAttribute().INTLIT().getText());
            return new Variable(variableName, value, varStatementContext.start);
        } else {
            return new Variable((variableName), varStatementContext.start);
        }
    }

    private AddStatement toAdd(IntegerArithmeticParser.AddStatementContext addStatementContext, List<Variable> variableStatements) {
        final Either<Integer, Variable> operand1 = addStatementContext.n1Attribute().ID() == null ?
                Either.left(Integer.parseInt(addStatementContext.n1Attribute().INTLIT().getText())) :
                Either.right(variableStatements.stream().filter(var -> var.getName().equals(addStatementContext.n1Attribute().ID().getText())).findFirst().orElse(null));

        final Either<Integer, Variable> operand2 = addStatementContext.n2Attribute().ID() == null ?
                Either.left(Integer.parseInt(addStatementContext.n2Attribute().INTLIT().getText())) :
                Either.right(variableStatements.stream().filter(var -> var.getName().equals(addStatementContext.n2Attribute().ID().getText())).findFirst().orElse(null));

        final Variable result = variableStatements.stream().filter(var -> var.getName().equals(addStatementContext.toAttribute().ID().getText())).findFirst().orElse(null);

        return new AddStatement(operand1, operand2, result, addStatementContext.start);
    }

    private PrintStatement toPrint(IntegerArithmeticParser.PrintStatementContext printStatementContext, List<Variable> variableStatements) {
        final Variable result = variableStatements.stream().filter(var -> var.getName().equals(printStatementContext.nAttribute().ID().getText())).findFirst().orElse(null);
        return new PrintStatement(result, printStatementContext.start);
    }

    private void verifySourceFile(File programFile) {
        if (!programFile.exists()) {
            throw new IllegalArgumentException("Provided Source File "
                    + programFile.getAbsolutePath() + " does not exist!");
        }
    }

    private String readFileContents(File programFile) {
        try {
            return Files
                    .asByteSource(programFile)
                    .asCharSource(Charsets.UTF_8)
                    .read();

        } catch (Exception ex) {
            throw new IllegalArgumentException("Error in reading file"
                    + programFile.getAbsolutePath());
        }
    }

    public static <T, R> Set<T> findDuplicates(
            Collection<? extends T> collection,
            Function<? super T, ? extends R> criteria) {
        Set<R> uniques = new HashSet<>();
        return collection
                .stream()
                .filter(e -> {
                    final R criteriaValue = criteria.apply(e);
                    return !uniques.add(criteriaValue);
                })
                .collect(toSet());
    }
}
