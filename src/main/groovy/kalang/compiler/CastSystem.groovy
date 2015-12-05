package kalang.compiler

import jast.ast.CastExpr
import jast.ast.ClassExpr
import jast.ast.ExprNode
import jast.ast.InvocationExpr

import java.util.List;

import org.apache.commons.collections4.BidiMap
import org.apache.commons.collections4.bidimap.TreeBidiMap

@groovy.transform.TypeChecked
class CastSystem {
	
	private static final String FLOAT_CLASS = "java.lang.Float";
	private static final String DOUBLE_CLASS = "java.lang.Double";

	private static final String INT_CLASS = "java.lang.Integer";
	private static final String LONG_CLASS = "java.lang.Long";

	private static final String BOOLEAN_CLASS = "java.lang.Boolean";

	private static final String CHAR_CLASS = "java.lang.Character";

	private static final String STRING_CLASS = "java.lang.String";

	private static final String NULL_CLASS = "java.lang.NullObject";

	private static final String ROOT_CLASS = "java.lang.Object";
			
	private BidiMap primitive2class;
	
	private List numberClass = [INT_CLASS,LONG_CLASS,FLOAT_CLASS,DOUBLE_CLASS]
	
	private List numberPrimitive = ["int","long","float","double"]

	AstLoader astLoader
	
	CastSystem(AstLoader astLoader){
		def m = primitive2class = new TreeBidiMap();
		m."int" = INT_CLASS
		m."long" = LONG_CLASS
		m."float" = FLOAT_CLASS
		m."double" = DOUBLE_CLASS
		m."char" = CHAR_CLASS
		m."boolean" = BOOLEAN_CLASS
		this.astLoader = astLoader
	}
	
	boolean isPrimitiveType(String type){
		primitive2class.containsKey(type)
	}
	
	String classifyType(String from){
		return primitive2class.get(from) ?: from
	}

	boolean hasPrimitiveType(String type){
		this.primitive2class.containsValue(type)
	}
	
	String getPrimitiveType(String type){
		primitive2class.getKey(type)
	}
	
	String[] getPrimitiveTypes(){
		return primitive2class.values().toArray()
	}
	
	ExprNode classExprToPrimitiveExpr(ExprNode expr,String clsType){
		String fromPri = this.getPrimitiveType(clsType)
		def ivc = new InvocationExpr();
		ivc.target = expr;
		ivc.methodName = "${fromPri}Value"
		return ivc
	}
	
	ExprNode primitiveExprToClassExpr(ExprNode expr,String primitiveType){
		String classType = this.primitive2class.get(primitiveType)
		if(!classType) return expr
		ClassExpr ce = new ClassExpr(classType)
		InvocationExpr ie = new InvocationExpr(ce,"valueOf",[expr]);
		return ie
	}
	
	/**
	 * auto cast type
	 * @param expr
	 * @param fromType
	 * @param toType
	 * @return
	 */
	ExprNode cast(ExprNode expr,String fromType,String toType){
		if(fromType==toType) return expr;
		if(isNumber(fromType)){
			if(isNumber(toType)){
				return numberToNumber(expr,fromType,toType)
			}else if(toType==ROOT_CLASS){
				if(isPrimitiveType(fromType)){
					return this.primitiveExprToClassExpr(expr,fromType)
				}else return expr;
			}
			return null;
		}else{
			if(isSubclass(fromType,toType)) return expr;
			return null;
		}
	}		
	public ExprNode numberToNumber(ExprNode expr,String fromType,String toType){
		String priFrom = this.getPrimitiveType(fromType) ?: fromType
		String priTo = this.getPrimitiveType(toType) ?: toType
		if(!this.isPrimitiveType(fromType)){//from class type
			expr = this.classExprToPrimitiveExpr(expr,fromType)
		}
		if(priFrom!=priTo){
			expr = primitiveCast(expr,priFrom,priTo);
		}
		if(toType==priTo) return expr;
		return this.primitiveExprToClassExpr(expr,priTo)
	}
	
	private ExprNode primitiveCast(ExprNode expr,String fromType,String toType){
		if(castable(fromType,toType)){
			return new CastExpr(fromType,expr)
		}
		return null
	}
	
	boolean castable(String from,String to){
		if(to==this.ROOT_CLASS) return true
		to = classifyType(to)
		from = classifyType(from)
		if(from==to) return true
		HashMap<String,List> baseMap = [:]
		baseMap.put(INT_CLASS , [LONG_CLASS, FLOAT_CLASS, DOUBLE_CLASS])
		baseMap.put(LONG_CLASS  , [FLOAT_CLASS, DOUBLE_CLASS])
		baseMap.put(FLOAT_CLASS  , [DOUBLE_CLASS])
		baseMap.put(DOUBLE_CLASS  , [])
		if(baseMap.containsKey(from)){
			return baseMap.get(from).contains(to)
		}
		if(isSubclass(from,to)) return true
		return false;
	}

	boolean castable(List<String> from,List<String> to){
		if(from.size()!=to.size())  return false
		for(int i=0;i<from.size();i++){
			def f = from.get(i)
			def t = to.get(i)
			if(!castable(f,t)){
				return false
			}
		}
		return true
	}
	
	boolean isNumberPrimitive(String type){
		return this.numberPrimitive.contains(type)
	}

	boolean isNumberClass(String type){
		return this.numberClass.contains(type)
	}
	
	boolean isNumber(String type){
		return this.isNumberPrimitive(type)||this.isNumberClass(type)
	}

	boolean isBoolean(String type){
		return type == BOOLEAN_CLASS || type == "boolean"
	}

	String getHigherType(String type1,String type2){
		if(type1==DOUBLE_CLASS || type2==DOUBLE_CLASS) return DOUBLE_CLASS
		if(type1==FLOAT_CLASS || type2 ==FLOAT_CLASS) return FLOAT_CLASS
		if(type1==LONG_CLASS || type2==LONG_CLASS) return LONG_CLASS
		return INT_CLASS
	}
	
	boolean isSubclass(String type,String subclassType){
		if(isPrimitiveType(type)) return false
		def fromAst = astLoader.loadAst(type)
		while(fromAst){
			def parent = fromAst.parentName
			if(!parent) return false;
			if(parent==subclassType) return true;
			fromAst = astLoader.loadAst(parent)
		}
		return false;
	}
	
}
