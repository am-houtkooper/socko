//
// Copyright 2013 Vibul Imtarnasan, David Bolton and Socko contributors.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
package org.mashupbots.socko.rest.test1

import org.mashupbots.socko.rest.RestQuery
import org.mashupbots.socko.rest.RestPath
import org.mashupbots.socko.rest.RestDelete
import org.mashupbots.socko.rest.RestGet
import org.mashupbots.socko.rest.RestPost
import org.mashupbots.socko.rest.RestPut
import org.mashupbots.socko.rest.RestRequest
import org.mashupbots.socko.rest.RestRequestContext
import org.mashupbots.socko.rest.RestResponse
import org.mashupbots.socko.rest.RestResponseContext
import akka.actor.ActorRef
import akka.actor.ActorSystem
import org.mashupbots.socko.rest.RestDispatcher

@RestGet(path = "/pets")
case class GetPetsRequest(context: RestRequestContext) extends RestRequest
case class GetPetsResponse(context: RestResponseContext) extends RestResponse
class GetPetsDispatcher extends RestDispatcher {
  def getActor(actorSystem: ActorSystem, request: RestRequest): ActorRef = null
}

@RestPost(
  path = "/dogs1",
  responseClass = "FunnyNameDogResponse",
  dispatcherClass = "GetPetsDispatcher")
case class PostDogs1Request(context: RestRequestContext) extends RestRequest

@RestPut(
  path = "/dogs2",
  responseClass = "org.mashupbots.socko.rest.test1.FunnyNameDogResponse",
  dispatcherClass = "org.mashupbots.socko.rest.test1.GetPetsDispatcher",
  errorResponses = Array("400=username not found", "401=yet another error"))
case class PutDogs2Request(context: RestRequestContext) extends RestRequest

case class FunnyNameDogResponse(context: RestResponseContext) extends RestResponse

@RestDelete(path = "/pets/{id}")
case class DeletePetsRequest(context: RestRequestContext, @RestPath() id: String) extends RestRequest
case class DeletePetsResponse(context: RestResponseContext, message: String) extends RestResponse
class DeletePetsDispatcher extends RestDispatcher {
  def getActor(actorSystem: ActorSystem, request: RestRequest): ActorRef = null
}

// Error because there is no corresponding response class
@RestGet(path = "/noresponse", dispatcherClass = "GetPetsProcessorLocator")
case class NoResponseRequest(context: RestRequestContext) extends RestRequest

// Error because this does not have a @RestGet
case class NoAnnotationRequest(context: RestRequestContext) extends RestRequest

// Error because parameter binding not annotated
@RestDelete(path = "/pets/{id}")
case class NoParameterAnnotationRequest(context: RestRequestContext, id: String) extends RestRequest
case class NoParameterAnnotationResponse(context: RestResponseContext) extends RestResponse
class NoParameterAnnotationDispatcher extends RestDispatcher {
  def getActor(actorSystem: ActorSystem, request: RestRequest): ActorRef = null
}

// Error because parameter binding annotated more than one
@RestDelete(path = "/pets/{id}")
case class MultiParameterAnnotationRequest(context: RestRequestContext, @RestPath()@RestQuery() id: String) extends RestRequest
case class MultiParameterAnnotationResponse(context: RestResponseContext) extends RestResponse
class MultiParameterAnnotationDispatcher extends RestDispatcher {
  def getActor(actorSystem: ActorSystem, request: RestRequest): ActorRef = null
}

// Error because there is no corresponding response class
@RestGet(path = "/noresponse", dispatcherClass = "GetPetsProcessorLocator")
case class NotARequest(context: RestRequestContext)

// Error no dispathcer
@RestGet(path = "/pets")
case class GetNoDispatcherRequest(context: RestRequestContext) extends RestRequest
case class GetNoDispatcherResponse(context: RestResponseContext) extends RestResponse

// Error bad dispathcer because the dispatcher has parameters in the constructor
@RestGet(path = "/pets")
case class GetBadDispatcherRequest(context: RestRequestContext) extends RestRequest
case class GetBadDispatcherResponse(context: RestResponseContext) extends RestResponse
class GetBadDispatcherDispatcher(param1: String) extends RestDispatcher {
  def getActor(actorSystem: ActorSystem, request: RestRequest): ActorRef = null
}

// Ignored because not a RestRequest
case class NotARestClass()
