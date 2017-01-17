/*
 * Copyright 2017 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package views.helpers

import forms.validation.testutils.DataMap
import org.scalatest.Matchers._
import play.api.data.Forms._
import play.api.data.{Field, Form}
import play.api.i18n.Messages.Implicits._
import util.UnitTestTrait
import forms.validation.utils.MappingUtil._

class RadioHelperSpec extends UnitTestTrait {

  private def radioHelper(field: Field, legend: String, options: Seq[String])
  = views.html.helpers.radioHelper(field, legend, options)(applicationMessages)

  case class TestData(radio: String)

  val radioName = "radio"
  val testForm = Form(
    mapping(
      radioName -> oText.toText.verifying(DataMap.alwaysFail)
    )(TestData.apply)(TestData.unapply)
  )

  val testLegend = "my test legend text"
  val testOptions = Seq("Yes", "No")

  "RadioHelper" should {
    "populate the relevent content in the correct positions" in {
      val testField = testForm(radioName)
      val doc = radioHelper(testField, testLegend, testOptions).doc
      doc.getElementsByTag("div").hasClass("form-group") shouldBe true
      doc.getElementsByTag("div").hasClass("form-field") shouldBe true
      doc.getElementsByTag("legend").text() shouldBe testLegend
      val inputs = doc.getElementsByTag("input")
      inputs.size() shouldBe 2
      inputs.get(0).attr("value") shouldBe "Yes"
      inputs.get(0).attr("type") shouldBe "radio"
      inputs.get(1).attr("value") shouldBe "No"
      inputs.get(1).attr("type") shouldBe "radio"

      val lablesForInputs = doc.getElementsByTag("label")
      lablesForInputs.size() shouldBe 2
      lablesForInputs.get(0).text() shouldBe "Yes"
      lablesForInputs.get(1).text() shouldBe "No"
    }

    "if the form is populated, then select the correct radio button" in {
      val testField = testForm.fill(TestData("No"))(radioName)
      val doc = radioHelper(testField, testLegend, testOptions).doc

      val inputs = doc.getElementsByTag("input")
      inputs.size() shouldBe 2
      inputs.get(0).attr("value") shouldBe "Yes"
      inputs.get(0).attr("checked") shouldBe ""
      inputs.get(1).attr("value") shouldBe "No"
      inputs.get(1).attr("checked") shouldBe "checked"
    }

    "when there is error on the field, the errors needs to be displayed, but not otherwise" in {
      val testField = testForm(radioName)
      val doc = radioHelper(testField, testLegend, testOptions).doc
      doc.getElementsByTag("div").hasClass("form-field--error") shouldBe false
      doc.getElementsByClass("error-notification").isEmpty shouldBe true

      val errorField = testForm.bind(DataMap.EmptyMap)(radioName)
      val errDoc = radioHelper(errorField, testLegend, testOptions).doc
      errDoc.getElementsByTag("div").hasClass("form-field--error") shouldBe true
      errDoc.getElementsByClass("error-notification").isEmpty shouldBe false
    }
  }

}