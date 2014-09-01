
<form>
  <h4>Veuillez séléctionner l'arrêt à ajouter à vos favoris.</h4>
  <br/>
  <div class="list">

    <label class="item item-input item-select">
      <div class="input-label">
        Ligne
      </div>
      <select ng-model="selectedLine" ng-change="lineChange()" id="selectLine">
					<option value="woot">Choisissez</option>
          <optgroup label="Mettis">
						<option value="M998">A</option>
						<option value="M999">B</option>
					</optgroup>
					<optgroup label="Lignes">
						<option value="L2">1</option>
						<option value="L1">2</option>
						<option value="L4">3</option>
						<option value="L5">4</option>
						<option value="L3">5</option>
					</optgroup>
					<optgroup label="Citeis">
						<option value="C12">11</option>
						<option value="C130">12</option>
						<option value="C9">13</option>
						<option value="C82">14</option>
						<option value="C7">15</option>
						<option value="C11">16</option>
						<option value="C131">17</option>
					</optgroup>
          <optgroup label="Navettes">
						<option value="N60">83</option>
						<option value="N97">84</option>
						<option value="N123">86</option>
						<option value="N41">88</option>
						<option value="N99">89</option>
						<option value="N40">90</option>
						<option value="N126">91</option>
						<option value="N127">92</option>
						<option value="N129">93</option>
					</optgroup>
          <optgroup label="Proxis">
						<option value="P32">101</option>
						<option value="P55">102</option>
						<option value="P48">103</option>
						<option value="P80">105</option>
						<option value="P43">106</option>
						<option value="P62">107</option>
						<option value="P45">108</option>
						<option value="P46">109</option>
						<option value="P81">110</option>
						<option value="P24">111</option>
						<option value="P93">112</option>
						<option value="P65">113</option>
					</optgroup>
				</select>
    </label>

    <label class="item item-input item-select">
      <div class="input-label">
        Direction
      </div>
      <select ng-model="selectedHead" ng-change="headChange()" disabled="disabled" id="selectHead">
          <option value="woot">Choisissez</option>
          <option value="{{head.value}}" ng-repeat="head in heads">{{head.name}}</option>
        </select>
    </label>

    <label class="item item-input item-select">
      <div class="input-label">
        Arrêt
      </div>
      <select ng-model="selectedStop" ng-change="stopChange()" disabled="disabled" id="selectStop">
          <option value="woot">Choisissez</option>
          <option value="{{stop.value}}" ng-repeat="stop in stops">{{stop.name}}</option>
        </select>
    </label>

  </div>
</form>

<button class="button button-block button-positive"
  style="width: 95%; margin: 0 auto; margin-top: 2em" ng-click="add()" ng-disabled="cantAdd()">Ajouter aux favoris</button>
