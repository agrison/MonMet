<div ng-show="noFavorites()" style="text-align: center; margin-top: 7em">
  <div id="noFavorites">
    <i class="fa fa-bus" style="color: gray; font-size: 8em; text-shadow: 0px 0px 10px gray;" id="bus"></i><br/><br/><br/>
    Vous n'avez pas de favoris, vous pouvez en ajouter en cliquant sur le bouton ci-dessous.
  </div>
</div>

<div ng-hide="noFavorites()">
  <div id="favoritesContainer">
    <ul class="list">
      <li class="item" ng-repeat="fav in favorites" style="background-color: #ecf0f1;">
        <div class="deleteButton deleteButtonHolder">
          <button id="toggleDelete" class="button button-icon icon ion-minus-circled"
                  style="color: white; margin-right: 40px"
                  ng-click="deleteFavorite(fav)"></button>
        </div>
        <div ng-click="viewFavorite(fav)" style="cursor: pointer">
        <div class="stop" style="color: {{fav.color}}">{{fav.stop}}</div>
        <div class="lineAndDirection" style="color: {{fav.color}}">{{fav.line}} <i class="ion-arrow-right-c"></i> {{fav.head}}</div>
        <div style="float: right; margin-top: 0.5em; font-style: italic" ng-show="fav.next" class="nextRides">
          <div class="col" style="display: inline; margin-right: 7px"><i class="fa fa-clock-o"></i></div>
          <div class="col" style="display: inline" ng-repeat="f in fav.next" ng-class="{hurry: f.hurry}">{{f.min}}" @ {{f.label}}<span ng-if="!$last">&nbsp;</span></div>
        </div>
        <div style="float: right; margin-top: 0.5em; font-style: italic" ng-hide="fav.next">
          Plus de passage avant demain.
        </div>
        </div>
      </li>
    </ul>
  </div>
</div>

<button class="button button-block button-positive"
  style="width: 95%; margin: 0 auto; margin-top: 2em; font-weight: bold" ng-click="toAdd()">Ajouter un favori</button>
