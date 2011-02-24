/*
 * Copyright 2011 the original author or authors.
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
 * Tests for GroovyLangImmutableRule
 *
 * @author Hamlet D'Arcy
 * @version $Revision: 329 $ - $Date: 2010-04-29 04:20:25 +0200 (Thu, 29 Apr 2010) $
 */
class GroovyLangImmutableRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'GroovyLangImmutable'
    }

    void testSuccessScenario1() {
        final SOURCE = '''
              @groovy.transform.Immutable
              class Person { }
        '''
        assertNoViolations(SOURCE)
    }

    void testSuccessScenario2() {
        final SOURCE = '''
              import groovy.transform.Immutable
              @Immutable
              class Person { }
        '''
        assertNoViolations(SOURCE)
    }

    void testSuccessScenario3() {
        final SOURCE = '''
              import groovy.transform.*
              @Immutable
              class Person { }
        '''
        assertNoViolations(SOURCE)
    }

    void testSuccessScenario4() {
        final SOURCE = '''
              import groovy.transform.Immutable as Imtl
              @Imtl
              class Person { }
        '''
        assertNoViolations(SOURCE)
    }

    void testDefaultImport() {
        final SOURCE = '''
              @Immutable
              class Person { }
        '''
        assertSingleViolation(SOURCE, 2, '@Immutable', 'groovy.lang.Immutable is deprecated in favor of groovy.transform.Immutable')
    }

    void testFullyQualified() {
        final SOURCE = '''
          @groovy.lang.Immutable
          class Person { }
        '''
        assertSingleViolation(SOURCE, 2, '@groovy.lang.Immutable', 'groovy.lang.Immutable is deprecated in favor of groovy.transform.Immutable')
    }

    void testImportAlias() {
        final SOURCE = '''
              import groovy.lang.Immutable as Imtl
              @Imtl
              class Person { }
        '''
        assertSingleViolation(SOURCE, 3, '@Imtl', 'groovy.lang.Immutable is deprecated in favor of groovy.transform.Immutable')
    }

    protected Rule createRule() {
        new GroovyLangImmutableRule()
    }
}