package com.orientechnologies.orient.etl.transformer;

import com.orientechnologies.orient.etl.OETLBaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class OLogTransformerTest extends OETLBaseTest {

  private PrintStream sysOut;

  @Before
  public void redirectSysOutToByteBuff() {

    sysOut = System.out;
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output, true));

  }

  @After
  public void redirecByteBuffToSysout() {
    System.setOut(sysOut);
  }

  @Test
  public void testPrefix() throws Exception {
    ByteArrayOutputStream output = getByteArrayOutputStream();
    String cfgJson = "{source: { content: { value: 'id,text\n1,Hello\n2,Bye'} }, "
        + "extractor : { csv: {} }, "
        + "transformers : [{ log : {prefix:'-> '}}], "
        + "loader : { test: {} } }";
    process(cfgJson);
    String[] stringList = output.toString().split(System.getProperty("line.separator"));
    assertEquals("[1:log] INFO -> {id:1,text:Hello}", stringList[2]);
    assertEquals("[2:log] INFO -> {id:2,text:Bye}", stringList[3]);
  }

  @Test
  public void testPostfix() throws Exception {
    ByteArrayOutputStream output = getByteArrayOutputStream();
    String cfgJson = "{source: { content: { value: 'id,text\n1,Hello\n2,Bye'} }, "
        + "extractor : { csv : {} }, "
        + "transformers : [{ log : {postfix:'-> '}}], "
        + "loader : { test: {} } }";
    process(cfgJson);
    String[] stringList = output.toString().split(System.getProperty("line.separator"));

    assertEquals("[1:log] INFO {id:1,text:Hello}-> ", stringList[2]);
    assertEquals("[2:log] INFO {id:2,text:Bye}-> ", stringList[3]);
  }

  private ByteArrayOutputStream getByteArrayOutputStream() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output, true));
    return output;
  }

}