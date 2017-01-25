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

package controllers

import config.{FrontendAppConfig, FrontendAuthConnector}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, AnyContent, Request}
import play.twirl.api.Html

import scala.concurrent.Future

object EligibleController extends EligibleController {
  override lazy val applicationConfig = FrontendAppConfig
  override lazy val authConnector = FrontendAuthConnector
  override lazy val postSignInRedirectUrl = FrontendAppConfig.ggSignInContinueUrl
}

trait EligibleController extends BaseController {

  def view()(implicit request: Request[_]): Html =
    views.html.eligible(
      postAction = controllers.routes.EligibleController.submitEligible(),
      backUrl = backUrl
    )

  val showEligible: Action[AnyContent] = Authorised.async { implicit user =>
    implicit request =>
      Future.successful(Ok(view()))
  }

  val submitEligible: Action[AnyContent] = Authorised.async { implicit user =>
    implicit request =>
      Future.successful(Redirect(controllers.routes.ContactEmailController.showContactEmail()))
  }

  lazy val backUrl: String = controllers.property.routes.PropertyIncomeController.showPropertyIncome().url

}
