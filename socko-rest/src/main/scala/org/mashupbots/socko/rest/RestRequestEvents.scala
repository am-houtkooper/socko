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
package org.mashupbots.socko.rest

import org.mashupbots.socko.infrastructure.LocalCache
import org.mashupbots.socko.events.HttpRequestEvent
import org.mashupbots.socko.events.SockoEvent

/**
 * Cache of SockoEvents so REST processor actors can access it for custom
 * request deseralization and custom response seralization.
 *
 * It is NOT passes as part of [[org.mashupbots.socko.rest.RestRequest]] because
 * we want [[org.mashupbots.socko.rest.RestRequest]] to be fully immutable.
 */
object RestRequestEvents {

  private val cache: LocalCache = new LocalCache(1000000, 16)

  /**
   * Places the [[org.mashupbots.socko.rest.SockoEvent]] in the cache for 10 seconds.
   * The event is key'ed by the context ID.
   *
   * @param context [[org.mashupbots.socko.rest.RestRequestContext]] associated with the event
   * @param evt The event associated with the context
   */
  def put(context: RestRequestContext, evt: SockoEvent) = {
    cache.set(context.id.toString, evt, 10000)
  }

  /**
   * Places the [[org.mashupbots.socko.rest.SockoEvent]] in the cache for 10 seconds.
   * The event is key'ed by the context ID.
   *
   * @param context [[org.mashupbots.socko.rest.RestRequestContext]] associated with the event
   * @returns The event associated with the context, `None` if not found
   */
  def get(context: RestRequestContext): Option[SockoEvent] = {
    cache.get(context.id.toString).asInstanceOf[Option[SockoEvent]]
  }

  /**
   * Removes the specified [[org.mashupbots.socko.rest.SockoEvent]] from the cache
   *
   * @param context [[org.mashupbots.socko.rest.RestRequestContext]] associated with the event
   */
  def remove(context: RestRequestContext) = {
    cache.remove(context.id.toString)
  }
}