
package kalang.compiler.ast;
import kalang.compiler.core.Type;
import kalang.compiler.core.Types;
/**
 *
 * @author Kason Yang 
 */
public class CompareBinaryExpr extends BinaryExpr{

    public CompareBinaryExpr(ExprNode expr1, ExprNode expr2, String operation) {
        super(expr1, expr2, operation);
    }

    @Override
    public Type getType() {
        return Types.BOOLEAN_TYPE;
    }
    
    

}
