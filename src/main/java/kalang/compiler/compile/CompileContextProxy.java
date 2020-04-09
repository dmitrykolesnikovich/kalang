
package kalang.compiler.compile;
import kalang.compiler.antlr.KalangLexer;
import kalang.compiler.antlr.KalangParser;
import org.antlr.v4.runtime.CommonTokenStream;
/**
 *
 * @author Kason Yang
 */
public class CompileContextProxy implements CompileContext{
    
    private CompileContext config;

    public CompileContextProxy(CompileContext config) {
        this.config = config;
    }

    @Override
    public KalangLexer createLexer(CompilationUnit compilationUnit, String source) {
        return config.createLexer(compilationUnit, source);
    }

    @Override
    public CommonTokenStream createTokenStream(CompilationUnit compilationUnit, KalangLexer lexer) {
        return config.createTokenStream(compilationUnit, lexer);
    }

    @Override
    public KalangParser createParser(CompilationUnit compilationUnit, CommonTokenStream tokenStream) {
        return config.createParser(compilationUnit, tokenStream);
    }

    @Override
    public AstBuilder createAstBuilder(CompilationUnit compilationUnit, KalangParser parser) {
        return config.createAstBuilder(compilationUnit, parser);
    }

    @Override
    public CodeGenerator createCodeGenerator(CompilationUnit compilationUnit) {
        return config.createCodeGenerator(compilationUnit);
    }

    @Override
    public SemanticAnalyzer createSemanticAnalyzer(CompilationUnit compilationUnit) {
        return config.createSemanticAnalyzer(compilationUnit);
    }

    @Override
    public AstLoader getAstLoader() {
        return config.getAstLoader();
    }

    @Override
    public SourceLoader getSourceLoader() {
        return config.getSourceLoader();
    }

    @Override
    public DiagnosisHandler getDiagnosisHandler() {
        return config.getDiagnosisHandler();
    }

    @Override
    public void stopCompile(int stopPhase) {
        config.stopCompile(stopPhase);
    }

    @Override
    public int getCompilingPhase() {
        return config.getCompilingPhase();
    }

    @Override
    public Configuration getConfiguration() {
        return config.getConfiguration();
    }

    
}
