/*
 * Copyright 2023 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gepardec.wor.xmi;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.xml.Assertions.xml;

class JavaxToJakartaXmiTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipeFromResources("com.gepardec.java.migrate.jakarta.Xmi");
    }

    @DocumentExample
    @Test
    void testJakartaXmi() {
        rewriteRun(
          spec -> spec.expectedCyclesThatMakeChanges(1),
          //language=xml
          xml(
            """
            <?xml version="1.0" encoding="UTF-8" ?>
              <xmi:XMI xmlns:xmi="http://www.omg.org/spec/XMI/20110701" xmlns:uml="http://www.omg.org/spec/UML/20110701" xmlns:Default="http://www.sparxsystems.com/profiles/Default/1.0">
                  <xmi:Documentation exporter="Enterprise Architect XMI Add-In" exporterVersion="3.4" owner="111" timestamp="2022-12-16T07:15:46.610+01:00" />
                  <Default:JAVA.package xmi:id="_LBNJ2FrXLDWolg6RWTnGLA" base_Class="_pZpO1HCvyEuhEbdfZWaDGA" JAVA.package="javax.activation" />
                  <Default:JAVA.package xmi:id="_R0hqCdebzjWBz7Ec81Zlow" base_Class="_gu0Mrjkmc06ul0HoX13b9g" JAVA.package="at.gepardec.common.api.types" />
                  <ownedAttribute xmi:type="uml:Property" xmi:id="_Pn9Eu-RptEaenxP0orItTQ" name="javax.transaction.global.timeout" visibility="private">
                  </ownedAttribute>
              </xmi:XMI>
            """,
            """
            <?xml version="1.0" encoding="UTF-8" ?>
              <xmi:XMI xmlns:xmi="http://www.omg.org/spec/XMI/20110701" xmlns:uml="http://www.omg.org/spec/UML/20110701" xmlns:Default="http://www.sparxsystems.com/profiles/Default/1.0">
                  <xmi:Documentation exporter="Enterprise Architect XMI Add-In" exporterVersion="3.4" owner="111" timestamp="2022-12-16T07:15:46.610+01:00" />
                  <Default:JAVA.package xmi:id="_LBNJ2FrXLDWolg6RWTnGLA" base_Class="_pZpO1HCvyEuhEbdfZWaDGA" JAVA.package="jakarta.activation" />
                  <Default:JAVA.package xmi:id="_R0hqCdebzjWBz7Ec81Zlow" base_Class="_gu0Mrjkmc06ul0HoX13b9g" JAVA.package="at.gepardec.common.api.types" />
                  <ownedAttribute xmi:type="uml:Property" xmi:id="_Pn9Eu-RptEaenxP0orItTQ" name="jakarta.transaction.global.timeout" visibility="private">
                  </ownedAttribute>
              </xmi:XMI>
            """,
            sourceSpecs -> sourceSpecs.path("myapp.xmi")
          )
        );
    }
}
