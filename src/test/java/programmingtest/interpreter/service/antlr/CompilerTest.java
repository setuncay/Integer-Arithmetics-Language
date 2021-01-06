package programmingtest.interpreter.service.antlr;


import io.vavr.control.Either;
import lang.IntegerArithmeticParser;
import org.junit.Test;
import programmingtest.interpreter.domain.CompilationError;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompilerTest {
    private final String sourceName = "test1.xml";

    @Test
    public void shouldParseValidPrograms() {
        //Given
        final String source = "<program>" +
               "<var name=\"a\" value=\"5\" />" +
                "<var name=\"b\" value=\"6\" />" +
                 "<var name=\"c\" />" +
                "<var name=\"d\" />" +
                "<add n1=\"a\" n2=\"b\" to=\"c\" />" +
                " <print  n=\"b\" />" +
                "</program>";
        // When
        final Compiler compiler = new Compiler();
        // Then
        assertTrue(compiler.compile(source, sourceName).isRight());
    }

    @Test
    public void emptyProgramSyntacticallyValid() {
        // Given
        final String source = "<program>" +
                "</program>";
        // When
        final Compiler compiler = new Compiler();
        // Then
        assertTrue(compiler.compile(source, sourceName).isRight());
    }

    @Test
    public void shouldReportCompilationError() {
        // Given
        final String source = "<program>" +
                "<program>";
        // When
        final Compiler compiler = new Compiler();
        // Then
        final Either<List<CompilationError>, IntegerArithmeticParser.ProgramContext> result = compiler.compile(source, sourceName);
        assertTrue(result.isLeft());
        assertEquals(result.getLeft().size(), 1);
        final CompilationError error = result.getLeft().get(0);
        assertEquals(error.getLineNumber(), 1);
        assertEquals(error.getCharAt(), 9);
        assertEquals(error.getDetailMessage(), "mismatched input '<program>' expecting {'</program>', '<var', '<add', '<print'}");
        assertEquals(error.getSourceName(), "test1.xml");
    }
}
