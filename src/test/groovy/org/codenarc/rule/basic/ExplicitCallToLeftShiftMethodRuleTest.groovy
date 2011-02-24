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
 * Tests for ExplicitCallToLeftShiftMethodRule
 *
 * @author Hamlet D'Arcy
 * @version $Revision$ - $Date$
 */
class ExplicitCallToLeftShiftMethodRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'ExplicitCallToLeftShiftMethod'
    }

    void testSuccessScenario() {
        rule.ignoreThisReference = true
        final SOURCE = '''
        	a << b
            a.leftShift()
            a.leftShift(a, b)
            leftShift(a)
        '''
        assertNoViolations(SOURCE)
    }

    void testViolation() {
        final SOURCE = '''
            a.leftShift(b)
        '''
        assertSingleViolation(SOURCE, 2, 'a.leftShift(b)', 'Explicit call to a.leftShift(b) method can be rewritten as a << (b)')
    }

    protected Rule createRule() {
        new ExplicitCallToLeftShiftMethodRule()
    }
}