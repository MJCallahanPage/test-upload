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

package services

import javax.inject.{Inject, Singleton}

import audit.Logging
import connectors.EnrolmentConnector
import connectors.models.Enrolment.Enrolled
import play.api.mvc.Result
import uk.gov.hmrc.play.http.HeaderCarrier
import uk.gov.hmrc.play.frontend.auth.connectors.AuthConnector

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EnrolmentService @Inject()(val authConnector: AuthConnector,
                                 val enrolmentConnector: EnrolmentConnector,
                                 logging: Logging) {

  def checkEnrolment(f: Enrolled => Future[Result])(implicit hc: HeaderCarrier): Future[Result] = {
    logging.debug(s"Checking enrolment")
    for {
      authority <- authConnector.currentAuthority
      enrolment <- enrolmentConnector.getIncomeTaxSAEnrolment(authority.fold("")(_.uri))
      result <- f(enrolment.isEnrolled)
    } yield result
  }
}
