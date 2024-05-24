package com.gepardec.wor.lord.queues;

import com.gepardec.wor.lord.queue.QueueRecipe;
import com.gepardec.wor.lord.util.ParserUtil;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

public class BinaryQueueCallToWebTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec
          .recipe(new QueueRecipe())
          .parser(ParserUtil.createParserWithRuntimeClasspath());
    }

    @DocumentExample
    @Test
    public void whenMultipleCallsDifferentTypes_thenChangeToTernaryAndAddToConfigClassOnce() {
        rewriteRun(
          //language=java
          java(
            """
              package com.gepardec.wor.lord;
            
              import com.gepardec.wor.lord.stubs.Laqaumv4Dto;
              import com.gepardec.wor.lord.stubs.Laqaumv4Record;
              import com.gepardec.wor.lord.stubs.QueueHelper;
              import com.gepardec.wor.lord.stubs.SystemErrorException;
              import com.gepardec.wor.lord.stubs.TmMagnaxMessageFormatter;
              import com.gepardec.wor.lord.stubs.XplFormatException;
              
              public class Test {
                  public static final String SERVICE_NAME = "LAAAUMV4";
                  public void test(Laqaumv4Dto request) {
                      byte[] data;
                      String user = "eLeAUOnl";
                      QueueHelper queueHelper = getQueueHelper();
              
                      Laqaumv4Record reqRecord = new Laqaumv4Record();
                      reqRecord.setDto(request);
              
                      TmMagnaxMessageFormatter formatter = new TmMagnaxMessageFormatter();
                      try {
                          reqRecord.format(formatter);
                          data = formatter.getBytes(SERVICE_NAME, user, false);
              
                      } catch (XplFormatException e) {
                          throw new SystemErrorException(
                            "Fehler beim Formatieren der Request-Message des Services "
                              + SERVICE_NAME, e);
                      }
              
                      queueHelper.send("", data);
                  }
    
                  public QueueHelper getQueueHelper() {
                      return new QueueHelper();
                  }
              }
              """,
            """
              package com.gepardec.wor.lord;

              import com.gepardec.wor.lord.stubs.QueueHelper;
              import at.sozvers.stp.lgkk.a02.laaaumv4.Laqaumv4;
              import at.sozvers.stp.lgkk.a02.laaaumv4.ObjectFactory;
              import at.sozvers.stp.lgkk.a02.laaaumv4.ExecuteService;
              import com.gepardec.wor.lord.stubs.XmlRequestWrapper;

              public class Test {
                  public static final ObjectFactory objectFactory = new ObjectFactory();
                  public static final String SERVICE_NAME = "LAAAUMV4";

                  public void test(Laqaumv4 request) {
                      byte[] data;
                      String user = "eLeAUOnl";
                      QueueHelper queueHelper = getQueueHelper();

                      ExecuteService serviceRequest = objectFactory.createExecuteService();
                      serviceRequest.setArg0(request);
                      XmlRequestWrapper<ExecuteService> xmlRequestWrapper = new XmlRequestWrapper<>(SERVICE_NAME, serviceRequest);
                      data = marshallDto(xmlRequestWrapper);

                      queueHelper.send("", data);
                  }

                  public QueueHelper getQueueHelper() {
                      return new QueueHelper();
                  }
              }
              """
          )
        );
    }



}
