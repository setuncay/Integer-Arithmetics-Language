package programmingtest.interpreter.service;

import io.vavr.control.Either;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.springframework.util.ResourceUtils;
import programmingtest.interpreter.domain.CompilationError;
import programmingtest.interpreter.domain.Output;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArithmeticExecutorTest {
    final ArithmeticExecutor executor = new ArithmeticExecutor();
    @Test(expected = IllegalArgumentException.class)
    public void throwsErrorWhenGivenSourceFilePathIsInvalid() {
        // Given
        final String invalidFilePath = "thisSourceFileDoesnotExist.xml";

        // When
        executor.run(invalidFilePath);
    }

    @Test
    public void failsToExecuteASourceWithSyntaxErrors() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:syntax_errors.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isLeft());
        assertEquals(result.getLeft().get(0).getLineNumber(), 2);
        assertEquals(result.getLeft().get(0).getCharAt(), 18);
        assertEquals(result.getLeft().get(0).getDetailMessage(), "mismatched input 'cardinality' expecting {'/>', 'value=\"'}");
        assertEquals(result.getLeft().get(0).getSourceName(), "syntax_errors.xml");
    }

    @Test
    public void canExecuteValidSourceFiles1() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:source1.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isRight());
        final String outputStr = Strings.join(result.right().get().stream().map(output -> output.toString()).collect(Collectors.toList()), ',');
        assertEquals("11", outputStr);
    }

    @Test
    public void canExecuteValidSourceFiles2() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:source2.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isRight());
        final String outputStr = Strings.join(result.right().get().stream().map(output -> output.toString()).collect(Collectors.toList()), ',');
        assertEquals("14", outputStr);
    }

    @Test
    public void canExecuteValidComplexSourceFiles() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:complex.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isRight());
        final String outputStr = Strings.join(result.right().get().stream().map(output -> output.toString()).collect(Collectors.toList()), ',');
        assertEquals("18,23,29,36,44,53,63,null,null,14", outputStr);
    }

    @Test
    public void displayErrorForDuplicateVariableDefinitions() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:duplicate_variable_definitions.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isLeft());
        assertEquals(result.getLeft().get(0).getLineNumber(), 3);
        assertEquals(result.getLeft().get(0).getCharAt(), 4);
        assertEquals(result.getLeft().get(0).getDetailMessage(), "Duplicate Variable Definition a");
        assertEquals(result.getLeft().get(0).getSourceName(), "duplicate_variable_definitions.xml");
    }

    @Test
    public void displayErrorWhenAddOperand2AttributeReferencesAnUnDefinedVariable() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:add_statement_references_undefined_variable.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isLeft());
        assertEquals(result.getLeft().get(0).getLineNumber(), 4);
        assertEquals(result.getLeft().get(0).getCharAt(), 4);
        assertEquals(result.getLeft().get(0).getDetailMessage(), "n2 references an undefined variable");
        assertEquals(result.getLeft().get(0).getSourceName(), "add_statement_references_undefined_variable.xml");
    }

    @Test
    public void displayErrorWhenAddOperand1AttributeReferencesAnUnDefinedVariable() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:add_statement_references_undefined_variable_2.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isLeft());
        assertEquals(result.getLeft().get(0).getLineNumber(), 4);
        assertEquals(result.getLeft().get(0).getCharAt(), 4);
        assertEquals(result.getLeft().get(0).getDetailMessage(), "n1 references an undefined variable");
        assertEquals(result.getLeft().get(0).getSourceName(), "add_statement_references_undefined_variable_2.xml");
    }

    @Test
    public void displayErrorWhenAddToAttributeReferencesAnUnDefinedVariable() throws FileNotFoundException {
        // Given
        File sourceFileWithSyntaxErrors = ResourceUtils.getFile("classpath:add_statement_references_undefined_variable_3.xml");
        // When
        final Either<List<CompilationError>, List<Output>> result = executor.run(sourceFileWithSyntaxErrors);
        // Then
        assertTrue(result.isLeft());
        assertEquals(result.getLeft().get(0).getLineNumber(), 4);
        assertEquals(result.getLeft().get(0).getCharAt(), 4);
        assertEquals(result.getLeft().get(0).getDetailMessage(), "to references an undefined variable");
        assertEquals(result.getLeft().get(0).getSourceName(), "add_statement_references_undefined_variable_3.xml");
    }
}
