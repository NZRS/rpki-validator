/**
 * The BSD License
 *
 * Copyright (c) 2010-2012 RIPE NCC
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

import scala.xml._
import org.joda.time._
import config.ReleaseInfo
import lib.{ UserPreferences, NewVersionDetails }

object Layouts {
  def none(view: View): NodeSeq = {
    <html lang="en">
      <head>
        <meta charset="utf-8"/>
      </head>
      <body>{ view.body }</body>
    </html>
  }

  def standard(view: View, newVersionDetails: Option[NewVersionDetails], userPreferences: UserPreferences): NodeSeq = {
    <html lang="en">
      <head>
        <meta charset="utf-8"/>
        <meta name="description" content="Publicly available RPKI validator provided by NZRS to ensure routers can perform prefix origin verification"/>
        <meta name="keywords" content="RPKI validator,NZRS,RIR,Prefix origin verification,Resouce Public Key Infrastructure,RFC 6810"/>
        <meta name="author" content="NZRS"/>

        <title>NZRS RPKI Validator - { view.title }</title>
        <link rel="shortcut icon" href="/favicon.ico"/>
        <link rel="stylesheet" href="/stylesheets/bootstrap/1.3.0/bootstrap.css"/>
        <link rel="stylesheet" href="/stylesheets/application.css"/>
        <script src="/javascript/datatables/1.8.2/jquery.js"/>
        <script src="/javascript/datatables/1.8.2/jquery.dataTables.min.js"/>
        <script src="/javascript/bootstrap/1.3.0/bootstrap-alerts.js"/>
        <script src="/javascript/bootstrap/1.3.0/bootstrap-twipsy.js"/>
        <script src="/javascript/bootstrap/1.3.0/bootstrap-popover.js"/>
        <script src="/javascript/bootstrap/1.4.0/bootstrap-dropdown.js"/>
        <script type="text/javascript"><!--
            $(document).ready(function(){
                $("#feedbackButton").hover(function(){
                        $(this).css("left","0px");
                }, function(){
                        $(this).css("left","-5px");
                });
            $(".dropdown").dropdown();
             });
        // --></script>
        <!-- Google Analytics -->
        <script type="text/javascript">
          (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o), m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','//www.google-analytics.com/analytics.js','ga');

          ga('create', 'UA-31742468-13', 'auto');
          ga('send', 'pageview');
        </script>
      </head>
      <body>
        <div class="">
          <div class="container nzrs-header">
            <div class="row" style="position: relative;">
              <div class="span4 nzrs-header-logo">
                <a href="https://internetnz.net.nz/" title="Internet NZ" target="_blank">
                  <img src="/images/InternetNZ.svg" alt="Internet NZ" width="171" height="70"/>
                </a>
              </div>

              <div class="span4 nzrs-header-heading">
                <a class="h1 brand" href="/">RPKI Validator</a>
              </div>
            </div>
          </div>
          <div class="fill nzrs-navbar">
            <div class="container">
              <ul class="nav">
                {
                  for (tab <- Tabs.visibleTabs) yield {
                    <li class={ if (tab == view.tab) "active" else "" }><a href={ tab.url }>{ tab.text }</a></li>
                  }
                }
              </ul>
            </div>
          </div>
        </div>
        <div class="container">
          {
            val newVersionNotify = newVersionDetails match {
              case Some(versionDetails) => {
                  <p>New version { versionDetails.version } available <a href={ versionDetails.url.toString }>here</a>.</p>
              }
              case None => NodeSeq.Empty
            }

            newVersionNotify match {
              case NodeSeq.Empty => NodeSeq.Empty
              case messages =>
              <div class="alert-message block-message"  data-alert="alert">
                  <a class="close" href="#">×</a>
                  { messages }
              </div>
            }

          }
          <div class="page-header">
            <h1>{ view.title }</h1>
          </div>
          { view.body }
        </div>
        <footer class="nzrs-footer">
          <div class="copyright">
            With thanks to <a href="http://www.ripe.net/lir-services/resource-management/certification/tools-and-resources">RIPE NCC</a> who are the authors and copyright owners of this tool
          </div>
        </footer>
        <div id="feedbackButton">
          <a href="mailto:rpki@internetnz.net.nz?subject=RPKI%20Validator%20Feedback"><img src="/images/feedback.png" width="41" height="111" alt="Feedback"/></a>
        </div>
      </body>
    </html>
  }
}
