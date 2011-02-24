/*
 * Copyright 2010 the original author or authors.
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
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for ConsecutiveStringConcatenationRule
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 329 $ - $Date: 2010-04-29 04:20:25 +0200 (Thu, 29 Apr 2010) $
 */
class ConsecutiveStringConcatenationRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'ConsecutiveStringConcatenation'
    }

    void testSuccessScenario() {
        final SOURCE = '''
            def f = 'Hello' +           // OK because of line break
                        'World'
            def h = 'Hello' + null      // OK because not a string
            def i = 'Hello' + method()  // OK because not a string
            def j = 'Hello' - "$World"  // OK because not +
        '''
        assertNoViolations(SOURCE)
    }

    void testSimpleCase() {
        final SOURCE = '''
            def a = 'Hello' + 'World'   // should be 'HelloWorld'
        '''
        assertSingleViolation(SOURCE, 2, "'Hello' + 'World'", "String concatenation can be joined into the literal 'HelloWorld")
    }

    void testStringAndNumber() {
        final SOURCE = '''
            def g = 'Hello' + 5         // should be 'Hello5'
        '''
        assertSingleViolation(SOURCE, 2, "'Hello' + 5", "String concatenation can be joined into the literal 'Hello5'")
    }

    void testGStringAndString() {
        final SOURCE = '''
            def b = "$Hello" + 'World'  // should be "${Hello}World"
        '''
        assertSingleViolation(SOURCE, 2, """"\$Hello" + 'World'""", 'String concatenation can be joined into a single literal')
    }

    void testStringAndGString() {
        final SOURCE = '''
            def c = 'Hello' + "$World"  // should be "Hello${World}"
        '''
        assertSingleViolation(SOURCE, 2, """ 'Hello' + \"\$World\"""", 'String concatenation can be joined into a single literal')
    }

    void testStringAndMultilineString() {
        final SOURCE = """
            def d = 'Hello' + '''
                        world   // should be joined
                      '''
        """
        assertSingleViolation(SOURCE, 2, "'Hello' +", "String concatenation can be joined into the literal 'Hello\\n                        world   // should be joined\\n                      '")
    }

    void testMultilineStringAndString() {
        final SOURCE = """
            def e = '''Hello
                  ''' + 'world'   // should be joined
        """
        assertSingleViolation(SOURCE, 3, "+ 'world'", "String concatenation can be joined into the literal 'Hello\\n                  world'")
    }

    protected Rule createRule() {
        new ConsecutiveStringConcatenationRule()
    }
}