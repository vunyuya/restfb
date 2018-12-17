/**
 * Copyright (c) 2010-2018 Mark Allen, Norbert Bartels.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.restfb.types;

import static org.junit.Assert.*;

import org.junit.Test;

import com.restfb.AbstractJsonMapperTests;
import com.restfb.types.webhook.WebhookEntry;
import com.restfb.types.webhook.WebhookObject;
import com.restfb.types.webhook.messaging.MessageItem;
import com.restfb.types.webhook.messaging.MessagingItem;
import com.restfb.types.webhook.messaging.NlpResult;
import com.restfb.types.webhook.messaging.nlp.*;

public class WebhookMessagingNlpTest extends AbstractJsonMapperTests {

  @Test
  public void messagingMessageWithNlpField_noNlpContent() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-7"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertNotNull(nlp.getEntities());
    assertEquals(0, nlp.getEntities().size());
  }

  @Test
  public void messagingMessageWithNlpField_singleEntityIntend() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-6"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertNotNull(nlp.getEntities());
    assertEquals(1, nlp.getEntities().size());
    NlpCustomWitAi intend = nlp.getEntities().get(0).as(NlpCustomWitAi.class);
    NlpCustomWitAi intendByClass = nlp.getEntities(NlpCustomWitAi.class).get(0);
    assertEquals(intend, intendByClass);
    assertEquals("value", intend.getType());
    assertEquals(0.91431927422157D, intend.getConfidence().doubleValue(), 0.01);
    assertEquals("weather", intend.getValue());
    assertEquals("intend", nlp.getEntities(NlpCustomWitAi.class).get(0).getWitAiKey());
  }

  @Test
  public void messagingMessageWithNlpField_twoEntitiesReminderDate() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-5"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(2, nlp.getEntities().size());
    assertEquals(1, nlp.getEntities(NlpDatetime.class).size());
    assertEquals(1, nlp.getEntities(NlpReminder.class).size());
    NlpDatetime datetime = nlp.getEntities(NlpDatetime.class).get(0);
    assertEquals("day", datetime.getGrain());
    assertEquals("value", datetime.getType());
    assertEquals("2017-08-02T00:00:00.000+02:00", datetime.getValue());
    assertEquals(0.99557711676036D, datetime.getConfidence().doubleValue(), 0.01);
  }

  @Test
  public void messagingMessageWithNlpField_singleEntityReminder() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-3"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpReminder reminder = nlp.getEntities().get(0).as(NlpReminder.class);
    assertEquals(true, reminder.getSuggested());
    assertEquals("value", reminder.getType());
    assertEquals("hallo!", reminder.getValue());
    assertEquals(0.95058024208635D, reminder.getConfidence().doubleValue(), 0.01);
  }

  @Test
  public void messagingMessageWithNlpField_singleEntityGreetings() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-2"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpGreetings greetings = nlp.getEntities().get(0).as(NlpGreetings.class);
    assertEquals("true", greetings.getValue());
    assertEquals(0.99982774257166D, greetings.getConfidence().doubleValue(), 0.01);
  }

  @Test
  public void messagingMessageWithNlpField_unknownEntity() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-8"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    assertEquals("foobar", nlp.getEntities(NlpCustomWitAi.class).get(0).getWitAiKey());
  }

  @Test
  public void messagingMessageWithNlpField_amountOfMoney() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-money"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpAmountOfMoney amountOfMoney = nlp.getEntities().get(0).as(NlpAmountOfMoney.class);

    assertEquals("4.56", amountOfMoney.getValue());
    assertEquals(1D, amountOfMoney.getConfidence(), 0.01);
    assertEquals("value", amountOfMoney.getType());
    assertEquals("EUR", amountOfMoney.getUnit());
  }

  @Test
  public void messagingMessageWithNlpField_phoneNumber() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-phone"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpPhoneNumber phoneNumber = nlp.getEntities().get(0).as(NlpPhoneNumber.class);

    assertEquals("12345654321", phoneNumber.getValue());
    assertEquals(0.960765D, phoneNumber.getConfidence(), 0.01);
  }

  @Test
  public void messagingMessageWithNlpField_email() {
    WebhookObject webhookObject =
        createJsonMapper().toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-email"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpEmail email = nlp.getEntities().get(0).as(NlpEmail.class);

    assertEquals("test@emaple.org", email.getValue());
    assertEquals(0.960765D, email.getConfidence(), 0.01);
  }

  @Test
  public void messagingMessageWithNlpField_distance() {
    WebhookObject webhookObject = createJsonMapper()
      .toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-distance"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpDistance distance = nlp.getEntities().get(0).as(NlpDistance.class);

    assertEquals("20", distance.getValue());
    assertEquals(0.960765D, distance.getConfidence(), 0.01);
    assertEquals("value", distance.getType());
    assertEquals("kilometre", distance.getUnit());
  }

  @Test
  public void messagingMessageWithNlpField_duration() {
    WebhookObject webhookObject = createJsonMapper()
      .toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-duration"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(6, nlp.getEntities().size());

    for (int i = 0; i < nlp.getEntities().size(); i++) {
      NlpDuration duration = nlp.getEntities().get(i).as(NlpDuration.class);

      assertEquals(String.valueOf(i + 1), duration.getValue());
      String unit = duration.getUnit();

      int multiplier = 1;
      if ("second".equals(unit)) {
        assertEquals(String.valueOf(i + 1), duration.getSecond());
        multiplier = 1;
      }
      if ("minute".equals(unit)) {
        assertEquals(String.valueOf(i + 1), duration.getMinute());
        multiplier = 60;
      }
      if ("hour".equals(unit)) {
        assertEquals(String.valueOf(i + 1), duration.getHour());
        multiplier = 60 * 60;
      }
      if ("day".equals(unit)) {
        assertEquals(String.valueOf(i + 1), duration.getDay());
        multiplier = 60 * 60 * 24;
      }
      if ("month".equals(unit)) {
        assertEquals(String.valueOf(i + 1), duration.getMonth());
        multiplier = 60 * 60 * 24 * 30;
      }
      if ("year".equals(unit)) {
        assertEquals(String.valueOf(i + 1), duration.getYear());
        multiplier = 60 * 60 * 24 * 365;
      }
      assertNotNull(duration.getNormalized());
      assertEquals("second", duration.getNormalized().getUnit());
      assertEquals(String.valueOf((i + 1) * multiplier), duration.getNormalized().getValue());
    }
  }

  @Test
  public void messagingMessageWithNlpField_quantity() {
    WebhookObject webhookObject = createJsonMapper()
      .toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-quantity"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpQuantity quantity = nlp.getEntities().get(0).as(NlpQuantity.class);

    assertEquals("3000", quantity.getValue());
    assertEquals(1.0D, quantity.getConfidence(), 0.01);
    assertEquals("value", quantity.getType());
    assertEquals("gram", quantity.getUnit());
  }

  @Test
  public void messagingMessageWithNlpField_temperature() {
    WebhookObject webhookObject = createJsonMapper()
      .toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-temperature"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpTemperature temperature = nlp.getEntities().get(0).as(NlpTemperature.class);

    assertEquals("26", temperature.getValue());
    assertEquals(0.96164833333333D, temperature.getConfidence(), 0.01);
    assertEquals("value", temperature.getType());
    assertEquals("celsius", temperature.getUnit());
  }

  @Test
  public void messagingMessageWithNlpField_volume() {
    WebhookObject webhookObject = createJsonMapper()
      .toJavaObject(jsonFromClasspath("webhooks/messaging-message-nlp-volume"), WebhookObject.class);
    assertNotNull(webhookObject);
    NlpResult nlp = getNlpResultFromWebhookObject(webhookObject);
    assertEquals(1, nlp.getEntities().size());
    NlpVolume volume = nlp.getEntities().get(0).as(NlpVolume.class);

    assertEquals("3", volume.getValue());
    assertEquals(1D, volume.getConfidence(), 0.01);
    assertEquals("value", volume.getType());
    assertEquals("litre", volume.getUnit());
  }

  private NlpResult getNlpResultFromWebhookObject(WebhookObject webhookObject) {
    WebhookEntry entry = webhookObject.getEntryList().get(0);
    assertFalse(entry.getMessaging().isEmpty());
    MessagingItem messagingItem = entry.getMessaging().get(0);
    assertTrue(messagingItem.isMessage());
    MessageItem messageItem = messagingItem.getMessage();
    assertNotNull(messageItem.getNlp());
    assertTrue(messageItem.hasNlp());
    return messageItem.getNlp();
  }

}
