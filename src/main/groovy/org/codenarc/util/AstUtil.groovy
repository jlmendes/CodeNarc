/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.util

import org.codehaus.groovy.ast.stmt.Statement
import org.codehaus.groovy.ast.stmt.ExpressionStatement
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codehaus.groovy.ast.expr.VariableExpression
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.expr.MapExpression
import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode

/**
 * Contains static utility methods related to Groovy AST.
 *
 * @author Chris Mair
 * @version $Revision$ - $Date$
 */
class AstUtil {

    /**
     * Return true if the Statement is a block
     * @param statement - the Statement to check
     * @return true if the Statement is a block
     */
    static boolean isBlock(Statement statement) {
        return statement instanceof BlockStatement
    }

    /**
     * Return true if the Statement is a block and it is empty (contains no "meaningful" statements).
     * This implementation also addresses some "weirdness" around some statement types (specifically finally)
     * where the BlockStatement answered false to isEmpty() even if it was.
     * @param statement - the Statement to check
     * @return true if the BlockStatement is empty
     */
    static boolean isEmptyBlock(Statement statement) {
        return statement instanceof BlockStatement &&
            (statement.empty ||
            (statement.statements.size() == 1 && statement.statements[0].empty))
    }

    /**
     * Return true only if the Statement represents a method call for the specified method object (receiver),
     * method name, and with the specified number of arguments.
     * @param stmt - the AST Statement
     * @param methodObject - the name of the method object (receiver)
     * @param methodName - the name of the method being called
     * @param numArguments - the number of arguments passed into the method
     * @return true only if the Statement is a method call matching the specified criteria
     */
    public static boolean isMethodCall(Statement stmt, String methodObject, String methodName, int numArguments) {
        def match = false
        if (stmt instanceof ExpressionStatement) {
            def expression = stmt.expression
            if (expression instanceof MethodCallExpression) {
                match = isMethodCall(expression, methodObject, methodName, numArguments)
            }
        }
        return match
    }

    /**
     * Return true only if the MethodCallExpression represents a method call for the specified method object (receiver),
     * method name, and with the specified number of arguments.
     * @param methodCall - the AST MethodCallExpression
     * @param methodObject - the name of the method object (receiver)
     * @param methodName - the name of the method being called
     * @param numArguments - the number of arguments passed into the method
     * @return true only if the method call matches the specified criteria
     */
    public static boolean isMethodCall(MethodCallExpression methodCall, String methodObject, String methodName, int numArguments) {
        def match = isMethodCall(methodCall, methodObject, methodName)
        def argumentsExpression = methodCall.arguments
        def arguments = (argumentsExpression instanceof MapExpression) ?
            argumentsExpression.mapEntryExpressions : argumentsExpression.expressions
        match = match && arguments.size() == numArguments
        return match
    }

    /**
     * Return true only if the MethodCallExpression represents a method call for the specified method
     * object (receiver) and method name.
     * @param methodCall - the AST MethodCallExpression
     * @param methodObject - the name of the method object (receiver)
     * @param methodName - the name of the method being called
     * @return true only if the method call matches the specified criteria
     */
    public static boolean isMethodCall(MethodCallExpression methodCall, String methodObject, String methodName) {
        def match = false
        def objectExpression = methodCall.objectExpression
        if (objectExpression instanceof VariableExpression) {
            def objectName = objectExpression.name
            match = (objectName == methodObject)
        }
        def method = methodCall.method
        def value = method.properties['value']
        match = match && (value == methodName)
        return match
    }

    /**
     * Return the AnnotationNode for the named annotation, or else null.
     * Supports Groovy 1.5 and Groovy 1.6.
     * @param node - the AnnotatedNode
     * @param name - the name of the annotation
     * @return the AnnotationNode or else null 
     */
    public static AnnotationNode getAnnotation(AnnotatedNode node, String name) {
        def annotations = node.annotations
        return annotations instanceof Map ?
            annotations[name] :                                         // Groovy 1.5
            annotations.find { annot -> annot.classNode.name == name }  // Groovy 1.6
    }

    /**
     * Private constructor. All methods are static.
     */
    private AstUtil() { }
}