/**
 * The BSD License
 *
 * Copyright (c) 2010, 2011 RIPE NCC
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   - Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   - Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *   - Neither the name of the RIPE NCC nor the names of its contributors may be
 *     used to endorse or promote products derived from this software without
 *     specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.ripe.rpki.validator
package views

import scalaz._
import Scalaz._
import scala.xml._
import net.ripe.rpki.validator.rtr._
import models._
import scalaz.NonEmptyList
import net.ripe.rpki.validator.lib.Validation.ErrorMessage

class WhitelistView(whitelist: Whitelist, params: Map[String, String] = Map.empty, errors: Seq[ErrorMessage] = Seq.empty) extends View with ViewHelpers {
  private val fieldNameToText = Map("asn" -> "Origin", "prefix" -> "Prefix", "maxPrefixLength" -> "Maximum prefix length")

  def tab = Tabs.WhitelistTab
  def title = Text("Whitelist")
  def body = {
    <p>By adding a whitelisted announcement the validator will ensure that all routers receive this announcement, irrespective of the actual validated ROAs.</p>
    <h2>Add entry</h2>
    <div>{ renderErrors(errors, fieldNameToText) }</div>
    <div class="well">
      <form method="POST" class="form-stacked">
        <fieldset>
          <div>
            <div class="span4"><label for="announcement-asn">Origin</label></div>
            <div class="span4"><label for="announcement-prefix">Prefix</label></div>
            <div class="span4"><label for="announcement-maxprefixlen">Maximum prefix length</label></div>
            <div class="span4"></div>
          </div>
          <div class="span4">
            <input id="announcement-asn" type="text" name="asn" value={ params.getOrElse("asn", "") } placeholder="ASN (required)"/>
          </div>
          <div class="span4">
            <input id="announcement-prefix" type="text" name="prefix" value={ params.getOrElse("prefix", "") } placeholder="IPv4 or IPv6 prefix (required)"/>
          </div>
          <div class="span4">
            <input id="announcement-maxprefixlen" type="text" name="maxPrefixLength" value={ params.getOrElse("maxPrefixLength", "") } placeholder="Number (optional)"/>
          </div>
          <div class="span2">
            <input type="submit" class="btn primary" value="Add"/>
          </div>
        </fieldset>
      </form>
    </div>
    <div>
      <h2>Current entries</h2>{
        if (whitelist.entries.isEmpty)
          <div class="alert-message block-message"><p>No whitelist entries defined.</p></div>
        else {
          <table id="whitelist-table" class="zebra-striped" style="display: none;">
            <thead>
              <tr>
                <th>Origin</th><th>Prefix</th><th>Maximum Prefix Length</th><th>&nbsp;</th>
              </tr>
            </thead>
            <tbody>{
              for (entry <- whitelist.entries) yield {
                <tr>
                  <td>{ entry.asn.getValue() }</td>
                  <td>{ entry.prefix }</td>
                  <td>{ entry.maxPrefixLength.getOrElse("") }</td>
                  <td>
                    <form method="POST" action="/whitelist" style="padding:0;margin:0;">
                      <input type="hidden" name="_method" value="DELETE"/>
                      <input type="hidden" name="asn" value={ entry.asn.toString }/>
                      <input type="hidden" name="prefix" value={ entry.prefix.toString }/>
                      <input type="hidden" name="maxPrefixLength" value={ entry.maxPrefixLength.map(_.toString).getOrElse("") }/>
                      <input type="submit" class="btn" value="delete"/>
                    </form>
                  </td>
                </tr>
              }
            }</tbody>
          </table>
          <script><!--
$(document).ready(function() {
  $('#whitelist-table').dataTable({
      "sPaginationType": "full_numbers",
      "aoColumns": [
        { "sType": "numeric" },
        null,
        { "sType": "numeric" },
        { "bSortable": false }
      ]
    }).show();
});
// --></script>
        }
      }
    </div>
  }
}
