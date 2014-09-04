<div id="topLine">
  <div class="row" style="text-align: center">
    <div class="col col-20 boxLine" style="background-color: {{current.color}}">{{current.line}}</div>
    <div class="col col-33 boxHead">{{current.head}}</div>
    <div class="col col-50 boxStop">{{current.stop}}</div>
  </div>
  <div class="row">
    <div class="col boxInfo"><i class="fa fa-clock-o" style="font-size: 3em"/></div>
    <div class="col boxInfo" style="font-weight: 3em" ng-repeat="f in next" ng-class="{hurry: f.hurry}">{{f.min}}" @ {{f.label}}<span ng-if="!$last">&nbsp;</span></div>
  </div>
</div>

<div class="row" ng-repeat="tt in timeTableBy5" style="margin-top: -5px">
  <div class="col col-20" ng-repeat="t in tt"
    style="background-color: #ecf0f1; color: #2c3e50; text-align:center">
      {{t}}
  </div>
</div>


<div id="map" style="height: 400px; width: 95%; margin: 0 auto"></div>
