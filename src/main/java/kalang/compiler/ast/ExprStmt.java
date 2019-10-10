/*

*/
package kalang.compiler.ast;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
public class ExprStmt extends Statement{
    
    protected ExprNode expr;
    
    public ExprStmt(ExprNode expr){
        this.expr = expr;
        offset = expr.offset;
    }
    
    @Override
    public List<AstNode> getChildren(){
        List<AstNode> ls = new LinkedList();
        addChild(ls, getExpr());
        return ls;
    }
    
    /**
     * @return the expr
     */
    public ExprNode getExpr() {
        return expr;
    }

    /**
     * @param expr the expr to set
     */
    public void setExpr(ExprNode expr) {
        Objects.requireNonNull(expr);
        this.expr = expr;
    }

    @Override
    public String toString() {
        return Objects.toString(expr);
    }
}